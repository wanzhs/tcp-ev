package org.ga.ev.ykc.domain;

import io.netty.buffer.ByteBuf;
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
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, YkcFrame ykcFrame, ByteBuf byteBuf) throws Exception {

    }
}
