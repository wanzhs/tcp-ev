package cdz.ga.ev.kl.service;

import cdz.ga.ev.kl.domain.enums.Cmd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 指令实现类工厂
 *
 * @author wanzhongsu
 * @date 2020/5/20 14:30
 */
public class CmdFactory {
    private static Map<Cmd, ICmdService> map = new ConcurrentHashMap<>();

    /**
     * 获取指令服务
     *
     * @param cmd 指令
     * @return 服务
     */
    public static ICmdService getService(Cmd cmd) {
        return map.get(cmd);
    }

    /**
     * 添加指令服务
     *
     * @param cmd        指令
     * @param cmdService 服务
     */
    public static void addService(Cmd cmd, ICmdService cmdService) {
        map.put(cmd, cmdService);
    }
}
