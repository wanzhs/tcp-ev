package org.ga.ev.ykc.utils;

import org.ga.ev.ykc.domain.enums.YkcCmd;
import org.ga.ev.ykc.service.ICmdService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 命令实现接口管理工厂
 *
 * @author wanzhongsu
 * @date 2020/5/22 12:22
 */
public class CmdFactory {
    private static Map<YkcCmd, ICmdService> map = new ConcurrentHashMap<>();

    public static void addService(YkcCmd cmd, ICmdService service) {
        map.put(cmd, service);
    }

    public static ICmdService getService(YkcCmd cmd) {
        return map.get(cmd);
    }
}
