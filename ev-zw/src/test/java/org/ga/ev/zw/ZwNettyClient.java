package org.ga.ev.zw;

import cn.hutool.json.JSONUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.ga.ev.zw.domain.ZwEncoder;
import org.ga.ev.zw.domain.ZwFrame;
import org.ga.ev.zw.domain.enums.ZwCmd;
import org.ga.ev.zw.domain.enums.ZwRegister;

/**
 * 智网客户端帧数据模拟
 *
 * @author wanzhongsu
 * @date 2020/5/26 11:09
 */
public class ZwNettyClient {
    private String ip = "127.0.0.1";
    private int port = 9000;

    private void run() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new ZwEncoder());
            }
        });
        try {
            Channel channel = bootstrap.connect(ip, port).sync().channel();
            int i = 0;
            while (++i < 2) {
                ZwFrame zwFrame = new ZwFrame();
                zwFrame.setCmd(ZwCmd.X01)
                        .setCtrlAddress("0731000100010001")
                        .setData("hello world".getBytes())
                        .setSeq(ZwEncoder.getSeq())
                        .setOperator("0001")
                        .setRegister(ZwRegister.REGISTERED)
                        .setResponseCode("00FFFF");
                channel.writeAndFlush(zwFrame);
                System.out.println("已发送" + JSONUtil.toJsonStr(zwFrame));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new ZwNettyClient().run();
    }
}
