package cdz.ga.ev.kl.service;

import cdz.ga.ev.kl.domain.enums.KlCmd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 指令实现类工厂
 *
 * @author wanzhongsu
 * @date 2020/5/20 14:30
 */
public class CmdFactory {
    private static Map<KlCmd, ICmdService> map = new ConcurrentHashMap<>();

    /**
     * 获取指令服务
     *
     * @param klCmd 指令
     * @return 服务
     */
    public static ICmdService getService(KlCmd klCmd) {
        return map.get(klCmd);
    }

    /**
     * 添加指令服务
     *
     * @param klCmd        指令
     * @param cmdService 服务
     */
    public static void addService(KlCmd klCmd, ICmdService cmdService) {
        map.put(klCmd, cmdService);
    }
}
