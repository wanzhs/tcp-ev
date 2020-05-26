package org.ga.ev.zw.utils;

import org.ga.ev.zw.domain.enums.ZwCmd;
import org.ga.ev.zw.service.ICmdService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 指令处理器工厂
 *
 * @author wanzhongsu
 * @date 2020/5/25 17:07
 */
public class CmdFactory {
    private static Map<ZwCmd, ICmdService> map = new ConcurrentHashMap<>();

    public static void addService(ZwCmd cmd, ICmdService service) {
        map.put(cmd, service);
    }

    public static ICmdService getService(ZwCmd cmd) {
        return map.get(cmd);
    }
}
