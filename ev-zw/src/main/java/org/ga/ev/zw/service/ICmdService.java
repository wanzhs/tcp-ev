package org.ga.ev.zw.service;

import org.ga.ev.zw.domain.ZwFrame;

/**
 * 智网命令抽象接口
 *
 * @author wanzhongsu
 * @date 2020/5/25 17:06
 */
public interface ICmdService {
    /**
     * 解析方法
     *
     * @param zwFrame 帧数据
     */
    void run(ZwFrame zwFrame);
}
