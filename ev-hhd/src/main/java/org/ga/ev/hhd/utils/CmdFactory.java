package org.ga.ev.hhd.utils;

import org.ga.ev.hhd.domain.enums.HhdCmd;
import org.ga.ev.hhd.service.ICmdService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 命令工厂
 *
 * @author wanzhongsu
 * @date 2020/5/25 16:00
 */
public class CmdFactory {
    private static Map<HhdCmd, ICmdService> map = new ConcurrentHashMap<>();

    public static void addService(HhdCmd cmd, ICmdService cmdService) {
        map.put(cmd, cmdService);
    }

    public static ICmdService getService(HhdCmd cmd) {
        return map.get(cmd);
    }
}
