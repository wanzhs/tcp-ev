package cdz.ga.ev.kl.domain.consts;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 管道工厂 管理认证过的管道
 *
 * @author wanzhongsu
 * @date 2020/5/20 16:33
 */
@Component
public class AuthedChannel {
    private ConcurrentMap<Integer, Channel> map = new ConcurrentHashMap<>();

    public void addChannel(Integer ctrlId, Channel channel) {
        map.put(ctrlId, channel);
    }

    public Channel getChannel(Integer ctrlId) {
        return map.get(ctrlId);
    }

    public Channel removeChannel(Integer ctrlId) {
        return map.remove(ctrlId);
    }
}
