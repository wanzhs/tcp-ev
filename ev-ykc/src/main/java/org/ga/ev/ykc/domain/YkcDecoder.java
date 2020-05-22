package org.ga.ev.ykc.domain;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.ga.ev.ykc.domain.enums.YkcCmd;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 云快充解码器
 *
 * @author wanzhongsu
 * @date 2020/5/21 16:48
 */
@Slf4j
@Component
public class YkcDecoder extends ByteToMessageDecoder {
    /**
     * 帧起始标志
     */
    private final byte START_MARK = 0x68;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        Object object = decode(channelHandlerContext, byteBuf);
        if (object != null) {
            list.add(object);
        }
    }

    private Object decode(ChannelHandlerContext context, ByteBuf in) {
        if (in.getByte(in.readerIndex()) != START_MARK) {
            in.skipBytes(1);
        }
        int startIndex = findStartMark(in);
        if (startIndex > -1) {
            int length = in.getByte(in.readerIndex() + 1) & 0xfff;
            if (in.writerIndex() - in.readerIndex() >= length + 4) {
                ByteBuf frameData = in.readSlice(length + 4);
                byte[] frameDataBy = new byte[length + 4];
                frameData.getBytes(frameData.readerIndex(), frameDataBy);
                log.info(StrUtil.format("收到帧数据，原始帧数据{}", HexUtil.encodeHexStr(frameDataBy)));
                frameData.skipBytes(2);
                //序号域
                int seq = frameData.readShort();
                //加密标志
                byte script = frameData.readByte();
                //帧类型标志
                byte cmd = frameData.readByte();
                //消息体
                byte[] msg = null;
                if (length > 4) {
                    msg = new byte[length - 4];
                    frameData.readBytes(msg);
                }
                //帧校验域
                byte[] checkBy = new byte[2];
                frameData.readBytes(checkBy);
                //校验数据
                byte[] checkCode = CrcUtils.getCheckCode(ArrayUtil.sub(frameDataBy, 2, frameDataBy.length - 2));
                if (checkBy[0] == checkCode[0] && checkBy[1] == checkCode[1]) {
                    YkcFrame ykcFrame = new YkcFrame()
                            .setSeq(seq).setCmd(YkcCmd.getInstance(cmd)).setData(msg)
                            .setScript(script == 1 ? true : false);
                    log.info("校验通过,数据：{}", JSONUtil.toJsonStr(ykcFrame));
                    return ykcFrame;
                } else {
                    log.info("校验失败,数据{}", HexUtil.encodeHexStr(msg));
                }
            }
        }
        return null;
    }


    private int findStartMark(ByteBuf in) {
        return in.forEachByte(value -> value != START_MARK);
    }
}
