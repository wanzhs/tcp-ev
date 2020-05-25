package org.ga.ev.hhd;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.ga.ev.hhd.dispatch.DispatchHandler;
import org.ga.ev.hhd.domain.HhdDecoder;
import org.ga.ev.hhd.domain.HhdEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;

/**
 * 海汇德netty端
 *
 * @author wanzhongsu
 * @date 2020/5/25 15:36
 */
@Slf4j
@Component
public class HhdNettyBoot implements CommandLineRunner {
    @Value("${netty.port}")
    private int port;

    @Resource
    private DispatchHandler dispatchHandler;

    @Override
    public void run(String... args) throws Exception {
        ChannelFuture future = null;
        try {
            EventLoopGroup server = new NioEventLoopGroup();
            EventLoopGroup worker = new NioEventLoopGroup(30);
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(server, worker)
                    .localAddress(new InetSocketAddress(port))
                    .channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new IdleStateHandler(90, 0, 0));
                    pipeline.addLast(new HhdDecoder());
                    pipeline.addLast(new HhdEncoder());
                    pipeline.addLast(dispatchHandler);
                }
            }).option(ChannelOption.SO_BACKLOG, 1024).childOption(ChannelOption.SO_KEEPALIVE, true);
            future = bootstrap.bind().syncUninterruptibly();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (future != null) {
                if (future.isSuccess()) {
                    log.info("ok");
                } else {
                    log.info("error");
                }
            }
        }

    }
}
