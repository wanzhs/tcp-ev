package org.ga.ev.hhd.service;

import org.ga.ev.hhd.domain.HhdFrame;

/**
 * 命令实现抽象接口
 *
 * @author wanzhongsu
 * @date 2020/5/25 15:58
 */
public interface ICmdService {
    /**
     * 海汇德接口运行方法
     *
     * @param frame 帧数据
     * @author wanzhongsu
     * @date 2020/5/25 15:59
     */
    void run(HhdFrame frame);
}
