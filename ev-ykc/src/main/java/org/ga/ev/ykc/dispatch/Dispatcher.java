package org.ga.ev.ykc.dispatch;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

/**
 * 云快充帧数据适配器
 *
 * @author wanzhongsu
 * @date 2020/5/21 16:43
 */
@ChannelHandler.Sharable
@Component
public class Dispatcher extends SimpleChannelInboundHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }
}
