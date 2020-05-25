package org.ga.ev.hhd.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.ga.ev.hhd.domain.enums.HhdCmd;

/**
 * 海汇德帧数据
 *
 * @author wanzhongsu
 * @date 2020/5/25 15:38
 */
@Data
@Accessors(chain = true)
public class HhdFrame {
    /**
     * 海汇德命令
     */
    private HhdCmd cmd;
    /**
     * 包序号
     */
    private int seq;
    /**
     * 数据域
     */
    private byte[] data;
}
