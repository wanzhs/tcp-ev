package org.ga.ev.hhd.utils;

import io.netty.channel.Channel;
import io.netty.util.concurrent.FastThreadLocal;
import org.springframework.stereotype.Service;

/**
 * 通道threadLocal
 *
 * @author wanzhongsu
 * @date 2020/5/26 9:56
 */
@Service
public class ChannelThreadLocal {
    private FastThreadLocal<Channel> threadLocal = new FastThreadLocal<>();

    public void set(Channel channel) {
        threadLocal.set(channel);
    }

    public Channel get() {
        return threadLocal.get();
    }
}
