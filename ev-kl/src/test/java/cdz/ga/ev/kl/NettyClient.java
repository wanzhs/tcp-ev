package cdz.ga.ev.kl;

import cdz.ga.ev.kl.domain.bean.EvEncoder;
import cdz.ga.ev.kl.domain.bean.EvFrame;
import cdz.ga.ev.kl.domain.enums.Cmd;
import cdz.ga.ev.kl.domain.enums.ExpType;
import cdz.ga.ev.kl.domain.enums.FrameType;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 模拟netty客户端
 *
 * @author wanzhongsu
 * @date 2020/5/20 11:31
 */
public class NettyClient {
    private String ip = "127.0.0.1";
    private int port = 9000;
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
                pipeline.addLast(new EvEncoder());
            }
        });
        try {
            Channel channel = bootstrap.connect(ip, port).sync().channel();
            int i = 0;
            while (++i < 2) {
                EvFrame evFrame = new EvFrame();
                evFrame.setCanIndex(300)
                        .setCtrlId(263)
                        .setSeq(EvEncoder.getSeq())
                        .setFrameType(FrameType.REPLY_FRAME)
                        .setExpType(ExpType.DENY_FRAME)
                        .setCmd(Cmd.X0A)
                        .setMst(29)
                        .setData("HelloWorld".getBytes());
                channel.writeAndFlush(evFrame);
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
        new NettyClient().run();
    }
}
