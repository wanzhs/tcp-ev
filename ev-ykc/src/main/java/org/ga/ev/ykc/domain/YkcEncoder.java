package org.ga.ev.ykc.domain;

import cn.hutool.core.util.ObjectUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.springframework.stereotype.Component;

/**
 * 云快充数据编码器
 *
 * @author wanzhongsu
 * @date 2020/5/21 16:47
 */
@Component
public class YkcEncoder extends MessageToByteEncoder<YkcFrame> {
    private final int START_MARK = 0x68;

    private static int seq = -1;

    public static int getSeq() {
        seq++;
        seq = ((seq + 128) % 256) - 128;
        return seq;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, YkcFrame ykcFrame, ByteBuf byteBuf) throws Exception {
        //写起始标志
        ByteBuf out = Unpooled.buffer();
        out.writeByte(START_MARK);
        byte[] msg = ykcFrame.getData();
        int msgLength = 0;
        if (!ObjectUtil.isEmpty(msg) && msg.length > 0) {
            //有数据
            msgLength = msg.length;
        }
        //写数据长度
        out.writeByte(msgLength + 4);
        //写序列化域
        out.writeShort(ykcFrame.getSeq());
        //写加密标志
        out.writeByte(ykcFrame.isScript() ? 0x01 : 0x00);
        //写帧类型标志
        out.writeByte(ykcFrame.getCmd().getValue());
        //写消息体
        if (msgLength > 0) {
            out.writeBytes(ykcFrame.getData());
        }
        //写帧校验域
        byte[] lengthData = new byte[msgLength + 4];
        out.getBytes(out.readerIndex() + 2, lengthData);
        byte[] checkCode = CrcUtils.getCheckCode(lengthData);
        out.writeBytes(checkCode);
        byteBuf.writeBytes(out);
    }

    public static void main(String[] args) {
        byte abc = (byte) 0x1f;
        System.out.print((abc + 256) % 256);
    }
}
