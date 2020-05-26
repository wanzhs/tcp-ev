package org.ga.ev.zw.utils;

import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 认证的通道管理
 *
 * @author wanzhongsu
 * @date 2020/5/25 17:07
 */
@Service
public class AuthedChannel {
    private Map<String, Channel> map = new ConcurrentHashMap<>();

    public void addChannel(String ctrlAddress, Channel channel) {
        map.put(ctrlAddress, channel);
    }

    public Channel getChannel(String ctrlAddress) {
        return map.get(ctrlAddress);
    }

    public Channel removeChannel(String ctrlAddress) {
        return map.remove(ctrlAddress);
    }
}
