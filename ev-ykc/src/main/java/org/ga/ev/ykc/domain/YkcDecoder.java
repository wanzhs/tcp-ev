package org.ga.ev.ykc.domain;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 云快充解码器
 *
 * @author wanzhongsu
 * @date 2020/5/21 16:48
 */
@Component
public class YkcDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        Object object = decode(channelHandlerContext, byteBuf);
        if (object != null) {
            list.add(object);
        }
    }

    private Object decode(ChannelHandlerContext context, ByteBuf in) {

        return null;
    }
}
