package org.ga.ev.zw;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.ga.ev.zw.dispatch.DispatchHandler;
import org.ga.ev.zw.domain.ZwDecoder;
import org.ga.ev.zw.domain.ZwEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;

/**
 * 智网netty启动类
 *
 * @author wanzhongsu
 * @date 2020/5/26 10:13
 */
@Slf4j
@Component
public class ZwNettyBoot implements CommandLineRunner {
    @Value("${netty.port}")
    private int port;
    @Resource
    private DispatchHandler dispatchHandler;
    @Resource
    private ZwEncoder zwEncoder;

    @Override
    public void run(String... args) throws Exception {
        ChannelFuture future = null;
        try {
            ServerBootstrap server = new ServerBootstrap();
            EventLoopGroup boss = new NioEventLoopGroup();
            EventLoopGroup worker = new NioEventLoopGroup(30);
            server.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port));
            server.childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new IdleStateHandler(90, 0, 0));
                    pipeline.addLast(new ZwDecoder())
                            .addLast(new ZwEncoder())
                            .addLast(dispatchHandler);
                }
            }).option(ChannelOption.SO_BACKLOG, 1024).childOption(ChannelOption.SO_KEEPALIVE, true);
            future = server.bind().syncUninterruptibly();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (future == null) {
                log.info("netty boot failed");
            } else {
                log.info("netty boot succeed");
            }
        }
    }
}
