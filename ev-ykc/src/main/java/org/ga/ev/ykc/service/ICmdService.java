package org.ga.ev.ykc.service;

import org.ga.ev.ykc.domain.YkcFrame;

/**
 * 云快充指令处理抽象接口
 *
 * @author wanzhongsu
 * @date 2020/5/21 16:45
 */
public interface ICmdService {
    /**
     * 指令解析方法
     *
     * @param frame 帧数据
     * @author wanzhongsu
     * @date 2020/5/21 16:49
     */
    void run(YkcFrame frame);
}
