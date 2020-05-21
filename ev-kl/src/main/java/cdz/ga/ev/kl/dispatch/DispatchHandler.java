package cdz.ga.ev.kl.dispatch;

import cdz.ga.ev.kl.domain.bean.EvFrame;
import cdz.ga.ev.kl.domain.consts.AuthedChannel;
import cdz.ga.ev.kl.domain.consts.EvAttributeKey;
import cdz.ga.ev.kl.domain.enums.Cmd;
import cdz.ga.ev.kl.domain.utils.ChannelThreadLocal;
import cdz.ga.ev.kl.service.CmdFactory;
import cdz.ga.ev.kl.service.ICmdService;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 不同帧数据适配器  针对每个不同控制码的不同数据解码操作
 *
 * @author wanzhongsu
 * @date 2020/5/18 14:50
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class DispatchHandler extends SimpleChannelInboundHandler<EvFrame> {
    @Resource
    private ChannelThreadLocal threadLocal;

    @Resource
    private AuthedChannel authedChannel;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, EvFrame msg) throws Exception {
        log.info(StrUtil.format("收到信息：{}", msg));
        Cmd cmd = msg.getCmd();
        ICmdService service = CmdFactory.getService(cmd);
        if (ObjectUtil.isEmpty(service)) {
            log.info(StrUtil.format("指令{}不存在相应的解析处理逻辑", cmd));
        } else {
            try {
                threadLocal.put(ctx.channel());
                service.run(msg);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                Channel channel = ctx.channel();
                if (channel.hasAttr(EvAttributeKey.CTRL_ID)) {
                    Integer ctrlId = channel.attr(EvAttributeKey.CTRL_ID).get();
                    log.info(StrUtil.format("已认证的连接,集控器{}的管道连接超时,服务端主动关闭该管道", ctrlId));
                    channel.close();
                } else {
                    log.info(StrUtil.format("未认证的连接,集控器管道连接超时，"));
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        if (channel.hasAttr(EvAttributeKey.CTRL_ID)) {
            Integer ctrlId = channel.attr(EvAttributeKey.CTRL_ID).get();
            log.info(StrUtil.format("集控器{}的管道离线", ctrlId));
            authedChannel.removeChannel(ctrlId);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        if (channel.hasAttr(EvAttributeKey.CTRL_ID)) {
            Integer ctrlId = channel.attr(EvAttributeKey.CTRL_ID).get();
            log.info(StrUtil.format("已认证的连接,集控器{}的管道netty错误{}", ctrlId, ExceptionUtil.getMessage(cause)));
        } else {
            log.info(StrUtil.format("未认证的连接,集控器管道netty错误{}", ExceptionUtil.getMessage(cause)));
            channel.close();
        }
    }
}
