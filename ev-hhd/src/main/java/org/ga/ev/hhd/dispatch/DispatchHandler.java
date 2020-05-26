package org.ga.ev.hhd.dispatch;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.ga.ev.hhd.domain.HhdAttributeKey;
import org.ga.ev.hhd.domain.HhdFrame;
import org.ga.ev.hhd.domain.enums.HhdCmd;
import org.ga.ev.hhd.service.ICmdService;
import org.ga.ev.hhd.utils.AuthedChannel;
import org.ga.ev.hhd.utils.ChannelThreadLocal;
import org.ga.ev.hhd.utils.CmdFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 海汇德命令处理分发器
 *
 * @author wanzhongsu
 * @date 2020/5/25 15:38
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class DispatchHandler extends SimpleChannelInboundHandler<HhdFrame> {
    @Resource
    private AuthedChannel authedChannel;

    @Resource
    private ChannelThreadLocal threadLocal;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HhdFrame msg) throws Exception {
        threadLocal.set(ctx.channel());
        HhdCmd cmd = msg.getCmd();
        ICmdService service = CmdFactory.getService(cmd);
        if (!ObjectUtil.isEmpty(service)) {
            service.run(msg);
        } else {
            log.info(StrUtil.format("海汇德命令{}不存在相应的实现", cmd));
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)) {
                Channel channel = ctx.channel();
                if (channel.hasAttr(HhdAttributeKey.CTRL_CODE)) {
                    log.info(StrUtil.format("已认证的连接,集控器{}连接超时,服务端主动关闭连接"
                            , channel.attr(HhdAttributeKey.CTRL_CODE).get()));
                } else {
                    log.info(StrUtil.format("未认证的连接,连接超时"));
                }
                channel.close();
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        if (channel.hasAttr(HhdAttributeKey.CTRL_CODE)) {
            log.info(StrUtil.format("已认证的连接关闭,集控器{}", channel.attr(HhdAttributeKey.CTRL_CODE).get()));
            authedChannel.removeChannel(channel.attr(HhdAttributeKey.CTRL_CODE).get());
        } else {
            log.info(StrUtil.format("未认证的连接关闭"));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        if (channel.hasAttr(HhdAttributeKey.CTRL_CODE)) {
            log.info(StrUtil.format("netty解析异常,集控器{}", channel.attr(HhdAttributeKey.CTRL_CODE).get()));
        }
    }
}
