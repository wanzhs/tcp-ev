package org.ga.ev.hhd.domain;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.ga.ev.hhd.domain.enums.HhdCmd;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 海汇德解码器
 *
 * @author wanzhongsu
 * @date 2020/5/25 15:43
 */
@Slf4j
@Component
public class HhdDecoder extends ByteToMessageDecoder {
    private final short START_MARK = 0X5AA5;
    private final int HEADER_LENGTH = 7;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object obj = decode(ctx, in);
        if (obj != null) {
            out.add(obj);
        }
    }

    private HhdFrame decode(ChannelHandlerContext ctx, ByteBuf in) {
        if (in.getShort(in.readerIndex()) != START_MARK) {
            in.skipBytes(2);
        }
        if (in.readableBytes() > HEADER_LENGTH) {
            int length = in.getByte(in.readerIndex() + 6) & 0xfff;
            if (in.readableBytes() >= HEADER_LENGTH + length) {
                ByteBuf frameData = in.readSlice(HEADER_LENGTH + length);
                byte[] bytes = new byte[HEADER_LENGTH + length];
                frameData.getBytes(frameData.readerIndex(), bytes);
                log.info(StrUtil.format("收到完整的一帧原始数据{}", HexUtil.encodeHexStr(bytes)));
                //数据包头
                frameData.skipBytes(2);
                //包序号
                int seq = frameData.readInt();
                //数据包长度
                frameData.skipBytes(1);
                //功能编码
                int cmdInt = frameData.readByte();
                HhdCmd cmd = HhdCmd.getInstance(cmdInt);
                //数据域
                byte[] data = null;
                if (length > 1) {
                    data = new byte[length - 1];
                    frameData.readBytes(data);
                }
                HhdFrame frame = new HhdFrame().setCmd(cmd)
                        .setData(data).setSeq(seq);
                log.info(StrUtil.format("包解析成功{}", JSONUtil.toJsonStr(frame)));
                return frame;
            }
        }
        return null;
    }
}
