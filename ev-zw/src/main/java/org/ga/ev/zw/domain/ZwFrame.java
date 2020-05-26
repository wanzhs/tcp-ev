package org.ga.ev.zw.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.ga.ev.zw.domain.enums.ZwCmd;
import org.ga.ev.zw.domain.enums.ZwRegister;

/**
 * 智网帧数据
 *
 * @author wanzhongsu
 * @date 2020/5/25 17:03
 */
@Data
@Accessors(chain = true)
public class ZwFrame {
    /**
     * 命令
     */
    private ZwCmd cmd;
    /**
     * 是否注册
     */
    private ZwRegister register;
    /**
     * 相应码
     */
    private String responseCode;
    /**
     * 运营商
     */
    private String operator;
    /**
     * 集控器地址
     */
    private String ctrlAddress;
    /**
     * 流水号
     */
    private String seq;
    /**
     * 报文详情
     */
    private byte[] data;
}
