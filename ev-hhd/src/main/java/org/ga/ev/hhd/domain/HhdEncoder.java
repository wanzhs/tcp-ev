package org.ga.ev.hhd.domain;

import cn.hutool.core.util.ObjectUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.springframework.stereotype.Component;

/**
 * 海汇德帧数据编码器
 *
 * @author wanzhongsu
 * @date 2020/5/25 15:41
 */
//@ChannelHandler.Sharable
@Component
public class HhdEncoder extends MessageToByteEncoder<HhdFrame> {
    private final short START_MARK = 0X5AA5;
    private final int HEADER_LENGTH = 7;
    private static int seq = 1;

    @Override
    protected void encode(ChannelHandlerContext ctx, HhdFrame msg, ByteBuf out) throws Exception {
        //数据包头
        out.writeShort(START_MARK);
        //包序号
        out.writeInt(msg.getSeq());
        byte[] data = msg.getData();
        int length = 1;
        if (!ObjectUtil.isEmpty(data)) {
            length = length + data.length;
        }
        //数据包长度
        out.writeByte(length);
        //功能编码
        out.writeByte(msg.getCmd().getValue());
        if (length > 1) {
            //数据域
            out.writeBytes(msg.getData());
        }
    }

    /**
     * 获取包序号
     *
     * @return
     */
    public static int getSeq() {
        int rsSeq = seq;
        seq++;
        seq = (seq % 0xFFFFFFFF) + 1;
        return rsSeq;
    }
}
