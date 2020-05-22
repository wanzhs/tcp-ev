package org.ga.ev.ykc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.ga.ev.ykc.domain.YkcEncoder;
import org.ga.ev.ykc.domain.YkcFrame;
import org.ga.ev.ykc.domain.enums.YkcCmd;

/**
 * 云快充客户端模拟
 *
 * @author wanzhongsu
 * @date 2020/5/22 13:32
 */
public class YkcNettyClient {
    private String ip = "127.0.0.1";
    private int port = 9001;

    public void run() {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        bootstrap.group(eventExecutors)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new YkcEncoder());
                    }
                });
        try {
            Channel channel = bootstrap.connect(ip, port).sync().channel();
            int i = 0;
            while (++i < 2) {
                YkcFrame ykcFrame = new YkcFrame();
                ykcFrame.setCmd(YkcCmd.X21)
                        .setData("hello world".getBytes()).setScript(false)
                        .setSeq(YkcEncoder.getSeq());
                channel.writeAndFlush(ykcFrame);
                System.out.println("已发送" + i);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new YkcNettyClient().run();
    }
}
