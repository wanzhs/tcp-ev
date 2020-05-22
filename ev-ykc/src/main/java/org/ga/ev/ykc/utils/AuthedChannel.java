package org.ga.ev.ykc.utils;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 认证通道管理
 *
 * @author wanzhongsu
 * @date 2020/5/22 12:26
 */
public class AuthedChannel {
    private Map<String, Channel> map = new ConcurrentHashMap<>();

    public void addChannel(String ctrlAddress, Channel channel) {
        map.put(ctrlAddress, channel);
    }

    public Channel getChannel(String ctrlAddress, Channel channel) {
        return map.get(ctrlAddress);
    }

    public Channel removeChannel(String ctrlAddress) {
        return map.remove(ctrlAddress);
    }
}
