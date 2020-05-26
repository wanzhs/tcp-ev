package org.ga.ev.zw.dispatch;

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
import org.ga.ev.zw.domain.enums.ZwCmd;
import org.ga.ev.zw.domain.ZwFrame;
import org.ga.ev.zw.service.ICmdService;
import org.ga.ev.zw.utils.AuthedChannel;
import org.ga.ev.zw.utils.ChannelThreadLocal;
import org.ga.ev.zw.utils.CmdFactory;
import org.ga.ev.zw.utils.ZwAttributeKey;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 帧数据分发器
 *
 * @author wanzhongsu
 * @date 2020/5/26 9:54
 */
@Slf4j
@Service
@ChannelHandler.Sharable
public class DispatchHandler extends SimpleChannelInboundHandler<ZwFrame> {
    @Resource
    private AuthedChannel authedChannel;
    @Resource
    private ChannelThreadLocal threadLocal;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ZwFrame msg) throws Exception {
        threadLocal.set(ctx.channel());
        ZwCmd cmd = msg.getCmd();
        ICmdService service = CmdFactory.getService(cmd);
        if (!ObjectUtil.isEmpty(service)) {
            service.run(msg);
        } else {
            log.info(StrUtil.format("智网命令{}不存在相应的解析接口", cmd));
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                Channel channel = ctx.channel();
                if (channel.hasAttr(ZwAttributeKey.CTRL_ADDRESS)) {
                    log.info(StrUtil.format("已认证的连接超时,服务端主动关闭,集控器{}", channel.attr(ZwAttributeKey.CTRL_ADDRESS).get()));
                } else {
                    log.info(StrUtil.format("未认证的连接超时"));
                }
                channel.close();
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("通道激活");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        if (channel.hasAttr(ZwAttributeKey.CTRL_ADDRESS)) {
            String ctrlAddress = channel.attr(ZwAttributeKey.CTRL_ADDRESS).get();
            authedChannel.removeChannel(ctrlAddress);
            log.info(StrUtil.format("已认证的连接关闭,集控器地址{}", ctrlAddress));
        } else {
            log.info(StrUtil.format("未认证的连接关闭"));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        if (channel.hasAttr(ZwAttributeKey.CTRL_ADDRESS)) {
            log.info(StrUtil.format("已认证的连接,netty解析错误{},集控器{}", ExceptionUtil.getMessage(cause),
                    channel.attr(ZwAttributeKey.CTRL_ADDRESS).get()));
        }
    }
}
