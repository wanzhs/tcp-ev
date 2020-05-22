package cdz.ga.ev.kl.service;

import cdz.ga.ev.kl.domain.bean.KlFrame;

/**
 * 命令抽象处理接口
 *
 * @author wanzhongsu
 * @date 2020/5/20 14:29
 */
public interface ICmdService {
    /**
     * 实现类调用该方法
     *
     * @param frame 数据
     */
    void run(KlFrame frame);
}
