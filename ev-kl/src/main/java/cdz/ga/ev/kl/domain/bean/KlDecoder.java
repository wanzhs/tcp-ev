package cdz.ga.ev.kl.domain.bean;

import cdz.ga.ev.kl.domain.enums.KlCmd;
import cdz.ga.ev.kl.domain.enums.ExpType;
import cdz.ga.ev.kl.domain.enums.FrameType;
import cdz.ga.ev.kl.domain.utils.TcpUtils;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 电动汽车解码器  解包操作  包检查等等
 *
 * @author wanzhongsu
 * @date 2020/5/18 14:47
 */
@Slf4j
public class KlDecoder extends ByteToMessageDecoder {
    /**
     * 除了数据的其他数据长度
     */
    private final int HEADER_LENGTH = 15;
    /**
     * 数据域长度偏移量
     */
    private final int LENGTH_FIELD_OFFSET = 11;
    /**
     * 帧起始值
     */
    private final byte START_MARK = 0x68;
    /**
     * 结束码
     */
    private final byte END_MARK = 0x16;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object decoded = decode(ctx, in);
        if (decoded != null) {
            out.add(decoded);
        }
    }


    private Object decode(ChannelHandlerContext ctx, ByteBuf in) {
        System.out.println(in.getByte(in.readerIndex()));
        if (in.getByte(in.readerIndex()) != START_MARK) {
            in.skipBytes(1);
            return null;
        }
        int startMarkPos = findFrameStartMark(in.readerIndex(), in);
        if (startMarkPos >= 0) {
            if (findFrameStartMark(startMarkPos + 9, in) >= 0) {
                int endMarkPos = findFrameEndMark(startMarkPos, in);
                if (endMarkPos >= 0) {
                    //完整的一帧数据
                    log.info("收到了完整的一帧数据");
                    int length = in.getShort(in.readerIndex() + LENGTH_FIELD_OFFSET);
                    log.info("数据长度:" + length);
                    ByteBuf frameData = in.readSlice(length + HEADER_LENGTH);
                    byte[] frameDataBy = new byte[length + HEADER_LENGTH];
                    frameData.getBytes(frameData.readerIndex(), frameDataBy);
                    log.info("原始数据：" + HexUtil.encodeHexStr(frameDataBy));
                    byte checkCode = getCheckCodeByBuf(frameData);
                    //帧起始字符
                    frameData.readByte();

                    //终端逻辑地址
                    byte[] ctrlIdBy = new byte[3];
                    frameData.readBytes(ctrlIdBy);
                    int ctrlId = TcpUtils.convert3Bytes2Int(ctrlIdBy);
                    byte[] canIndexBy = new byte[3];
                    frameData.readBytes(canIndexBy);
                    int canIndex = TcpUtils.convert3Bytes2Int(canIndexBy);

                    //主站地址与命令序号
                    byte ms1By = frameData.readByte();
                    //主站地址
                    int ms1 = ms1By & 0x3f;
                    //末帧
                    int fin = ms1By & 0x80;
                    //首帧
                    int fir = ms1By & 0x40;
                    byte ms2By = frameData.readByte();
                    //帧序号
                    byte seq = (byte) (ms2By & 0x1f);
                    // 加密控制 0 不加密 1 DES 2 AES 3 SM4  4 MAC
                    int script = ms2By & 0xe0;

                    //帧起始字符
                    frameData.readByte();

                    //控制码
                    byte cmdBy = frameData.readByte();
                    //功能码
                    int cmdId = cmdBy & 0x3f;
                    //异常标志 0 确认帧 1 否定帧
                    int expType = (cmdBy & 0x40) >> 6;
                    // 传送方向 0 主站发出的命令帧 1 终端发出的应答帧
                    int frameType = (cmdBy & 0x80) >> 7;

                    //数据长度
                    length = frameData.getShort(frameData.readerIndex());
                    frameData.skipBytes(2);
                    byte[] msg = null;
                    if (length > 0) {
                        //数据域
                        msg = new byte[length];
                        frameData.readBytes(msg);
                    }
                    //校验码
                    byte checkCodeBy = frameData.readByte();
                    //结束码
                    frameData.readByte();
                    //检验数据
                    if (checkCodeBy == checkCode) {
                        //校验通过
                        log.info("校验通过");
                        //封装
                        KlCmd klCmd = KlCmd.getInstance(cmdId);
                        KlFrame klFrame = new KlFrame()
                                .setKlCmd(klCmd)
                                .setCanIndex(canIndex)
                                .setSeq(seq)
                                .setCtrlId(ctrlId)
                                .setFrameType(FrameType.getInstance(frameType))
                                .setExpType(ExpType.getInstance(expType))
                                .setMst(ms1).setData(msg);
                        log.info(StrUtil.format("解析成功={}", JSONUtil.toJsonStr(klFrame)));
                        if (ObjectUtil.isEmpty(klCmd)) {
                            log.info(StrUtil.format("未知指令{},不进行适配处理,丢弃", cmdId));
                            return null;
                        }
                        return klFrame;
                    } else {
                        //校验不通过
                        log.info("校验不通过");
                    }
                    return null;
                }
            }
        }
        return null;
    }

    private byte getCheckCodeByBuf(final ByteBuf byteBuf) {
        int value = 0;
        for (int i = byteBuf.readerIndex(); i < byteBuf.readableBytes() - 2; i++) {
            value = value + byteBuf.getByte(i);
        }
        return (byte) (value % 256);
    }

    /**
     * 返回帧起始字符
     *
     * @param startIdx 查找起始位置
     * @param byteBuf  帧数据
     * @return 帧起始位置  找不到时返回 -1
     */
    private int findFrameStartMark(int startIdx, final ByteBuf byteBuf) {
        return byteBuf.forEachByte(startIdx, byteBuf.readableBytes() - startIdx, value -> value != START_MARK);
    }

    /**
     * 返回帧结束字符
     *
     * @param byteBuf 帧数据
     * @return 帧数据结束位置 找不到时返回 -1
     */
    private int findFrameEndMark(int startIdx, final ByteBuf byteBuf) {
        return byteBuf.forEachByte(startIdx, byteBuf.readableBytes() - startIdx, value -> value != END_MARK);
    }
}
