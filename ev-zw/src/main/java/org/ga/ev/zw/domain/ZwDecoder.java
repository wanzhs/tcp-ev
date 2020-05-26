package org.ga.ev.zw.domain;

import cn.hutool.core.codec.BCD;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.ga.ev.zw.domain.enums.ZwCmd;
import org.ga.ev.zw.domain.enums.ZwRegister;

import java.util.List;

/**
 * 智网解码器  小端模式
 *
 * @author wanzhongsu
 * @date 2020/5/26 10:19
 */
@Slf4j
public class ZwDecoder extends ByteToMessageDecoder {
    private final byte START_MARK = 0x68;
    private final int HEADER_LENGTH = 25;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object obj = decode(ctx, in);
        if (obj != null) {
            out.add(obj);
        }
    }

    private ZwFrame decode(ChannelHandlerContext ctx, ByteBuf in) {
        if (in.getByte(in.readerIndex()) != START_MARK) {
            in.skipBytes(1);
        }
        if (in.readableBytes() >= HEADER_LENGTH) {
            int msgLength = in.getShortLE(in.readerIndex() + 1);
            if (in.readableBytes() >= HEADER_LENGTH + msgLength) {
                ByteBuf frameData = in.readSlice(HEADER_LENGTH + msgLength);
                byte[] bytes = new byte[HEADER_LENGTH + msgLength];
                frameData.getBytes(frameData.readerIndex(), bytes);
                log.info(StrUtil.format("收到完整的一帧数据,原始数据{}", HexUtil.encodeHexStr(bytes)));
                //启动字符 和 报文长度
                frameData.skipBytes(3);
                //是否注册
                ZwRegister registered = frameData.readByte() == 0 ? ZwRegister.UNREGISTERED : ZwRegister.REGISTERED;
                //相应码
                byte[] responseCodeBy = new byte[3];
                frameData.readBytes(responseCodeBy);
                String responseCode = HexUtil.encodeHexStr(responseCodeBy);
                //运营商
                byte[] operatorsBy = new byte[2];
                frameData.readBytes(operatorsBy);
                String operator = BCD.bcdToStr(operatorsBy);
                //集控器地址
                byte[] ctrlAddressBy = new byte[8];
                frameData.readBytes(ctrlAddressBy);
                String ctrlAddress = BCD.bcdToStr(ctrlAddressBy);
                //帧类型
                ZwCmd cmd = ZwCmd.getInstance(frameData.readByte());
                //流水号
                byte[] seqBy = new byte[7];
                frameData.readBytes(seqBy);
                String seq = BCD.bcdToStr(seqBy);
                //消息体
                byte[] msg = null;
                if (msgLength > 0) {
                    msg = new byte[msgLength];
                    frameData.readBytes(msg);
                }
                ZwFrame zwFrame = new ZwFrame().setCmd(cmd)
                        .setCtrlAddress(ctrlAddress).setSeq(seq)
                        .setData(msg).setOperator(operator).setRegister(registered)
                        .setResponseCode(responseCode);
                log.info(StrUtil.format("解析成功帧数据{}", JSONUtil.toJsonStr(zwFrame)));
                return zwFrame;
            }
        }
        return null;
    }
}
