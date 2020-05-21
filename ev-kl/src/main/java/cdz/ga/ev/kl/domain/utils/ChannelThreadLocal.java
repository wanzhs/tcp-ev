package cdz.ga.ev.kl.domain.utils;

import io.netty.channel.Channel;
import io.netty.util.concurrent.FastThreadLocal;
import org.springframework.stereotype.Component;


/**
 * 存储当前访问的channel
 *
 * @author wanzhongsu
 * @date 2020/5/20 16:05
 */
@Component
public class ChannelThreadLocal {
    private FastThreadLocal<Channel> threadLocal = new FastThreadLocal<>();

    public void put(Channel channel) {
        threadLocal.set(channel);
    }

    public Channel get() {
        return threadLocal.get();
    }
}
