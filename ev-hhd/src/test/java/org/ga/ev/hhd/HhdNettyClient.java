package org.ga.ev.hhd;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.ga.ev.hhd.domain.HhdEncoder;
import org.ga.ev.hhd.domain.HhdFrame;
import org.ga.ev.hhd.domain.enums.HhdCmd;

/**
 * 海汇德客户端模拟
 *
 * @author wanzhongsu
 * @date 2020/5/25 16:51
 */
public class HhdNettyClient {
    private String ip = "127.0.0.1";
    private int port = 9001;
    private boolean stop = false;

    private void run() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new HhdEncoder());
            }
        });
        try {
            Channel channel = bootstrap.connect(ip, port).sync().channel();
            int i = 0;
            while (++i < 2) {
                HhdFrame hhdFrame = new HhdFrame();
                hhdFrame.setCmd(HhdCmd.X0B)
                        .setData("hello world".getBytes())
                        .setSeq(HhdEncoder.getSeq());
                channel.writeAndFlush(hhdFrame);
                System.out.println("已发送" + i);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new HhdNettyClient().run();
    }
}
