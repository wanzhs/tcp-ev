package org.ga.ev.ykc.dispatch;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.ga.ev.ykc.domain.YkcFrame;
import org.ga.ev.ykc.domain.enums.YkcCmd;
import org.ga.ev.ykc.service.ICmdService;
import org.ga.ev.ykc.utils.ChannelThreadLocal;
import org.ga.ev.ykc.utils.CmdFactory;
import org.ga.ev.ykc.utils.YkcAttributeKey;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 云快充帧数据适配器
 *
 * @author wanzhongsu
 * @date 2020/5/21 16:43
 */
@ChannelHandler.Sharable
@Slf4j
@Component
public class DispatchHandler extends SimpleChannelInboundHandler<YkcFrame> {
    @Resource
    private ChannelThreadLocal threadLocal;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, YkcFrame o) throws Exception {
        Channel channel = channelHandlerContext.channel();
        threadLocal.setChannel(channel);
        YkcCmd ykcCmd = o.getCmd();
        ICmdService cmdService = CmdFactory.getService(ykcCmd);
        if (!ObjectUtil.isEmpty(cmdService)) {
            cmdService.run(o);
        } else {
            log.info(StrUtil.format("命令{}的适配器不存在", ykcCmd));
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                Channel channel = ctx.channel();
                log.info(StrUtil.format("已认证的连接超时,集控器{},服务端主动关闭", channel.attr(YkcAttributeKey.CTRL_ADDRESS).get()));
                channel.close();
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        if (channel.hasAttr(YkcAttributeKey.CTRL_ADDRESS)) {
            log.info(StrUtil.format("已认证的连接关闭,集控器{}", channel.attr(YkcAttributeKey.CTRL_ADDRESS).get()));
        } else {
            log.info("未认证的连接关闭");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        if (channel.hasAttr(YkcAttributeKey.CTRL_ADDRESS)) {
            log.info(StrUtil.format("netty解析异常{}", ExceptionUtil.getMessage(cause)));
        }
    }
}
