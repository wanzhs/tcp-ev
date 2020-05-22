package org.ga.ev.ykc.utils;

import io.netty.channel.Channel;
import io.netty.util.concurrent.FastThreadLocal;
import org.springframework.stereotype.Component;

/**
 * 通道管理
 *
 * @author wanzhongsu
 * @date 2020/5/22 12:19
 */
@Component
public class ChannelThreadLocal {
    private FastThreadLocal<Channel> threadLocal = new FastThreadLocal<>();

    public Channel getChannel() {
        return threadLocal.get();
    }

    public void setChannel(Channel channel) {
        threadLocal.set(channel);
    }
}
