package cdz.ga.ev.kl;


import cdz.ga.ev.kl.dispatch.DispatchHandler;
import cdz.ga.ev.kl.domain.bean.EvDecoder;
import cdz.ga.ev.kl.domain.bean.EvEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * 电动汽车tcp启动类
 *
 * @author wanzhongsu
 * @date 2020/5/18 14:28
 */
@Slf4j
@Component
public class EvNettyBoot implements CommandLineRunner {
    /**
     * netty启动端口
     */
    @Value("${netty.port}")
    private int nettyPort;

    /**
     * netty 线程配置
     */
    private final EventLoopGroup boosGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup(30);
    private Channel channel;

    @Resource
    private DispatchHandler dispatchHandler;

    @Override
    public void run(String... args) throws Exception {
        ServerBootstrap server = new ServerBootstrap();
        ChannelFuture future = null;
        try {
            server.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(nettyPort));
            final EvEncoder evEncoder = new EvEncoder();
            server.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new IdleStateHandler(90, 0, 0, TimeUnit.SECONDS));
                    pipeline.addLast(new EvDecoder());
                    pipeline.addLast(dispatchHandler);
                    pipeline.addLast(new EvEncoder());
                }
            }).option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            future = server.bind().syncUninterruptibly();
            channel = future.channel();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (future != null && future.isSuccess()) {
                log.info("ok");
            } else {
                log.info("error");
            }
        }
    }
}
