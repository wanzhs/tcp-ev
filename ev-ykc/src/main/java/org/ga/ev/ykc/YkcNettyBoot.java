package org.ga.ev.ykc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.ga.ev.ykc.dispatch.Dispatcher;
import org.ga.ev.ykc.domain.YkcDecoder;
import org.ga.ev.ykc.domain.YkcEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;

/**
 * 云快充tcp启动类
 *
 * @author wanzhongsu
 * @date 2020/5/21 16:28
 */
@Component
public class YkcNettyBoot implements CommandLineRunner {
    @Value("${netty.port}")
    private int port;

    @Resource
    private Dispatcher dispatcher;

    private final EventLoopGroup server = new NioEventLoopGroup();
    private final EventLoopGroup worker = new NioEventLoopGroup(30);

    @Override
    public void run(String... args) throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(server, worker)
                .localAddress(new InetSocketAddress(port))
                .channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new IdleStateHandler(90, 0, 0));
                pipeline.addLast(new YkcDecoder());
                pipeline.addLast(dispatcher);
                pipeline.addLast(new YkcEncoder());
            }
        });
    }
}
