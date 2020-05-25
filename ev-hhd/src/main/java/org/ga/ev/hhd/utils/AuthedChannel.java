package org.ga.ev.hhd.utils;

import org.springframework.stereotype.Component;

import java.nio.channels.Channel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 认证的通道管理
 *
 * @author wanzhongsu
 * @date 2020/5/25 15:54
 */
@Component
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
