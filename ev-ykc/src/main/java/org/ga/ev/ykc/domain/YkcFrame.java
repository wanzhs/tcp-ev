package org.ga.ev.ykc.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.ga.ev.ykc.domain.enums.YkcCmd;

/**
 * 云快充帧数据
 *
 * @author wanzhongsu
 * @date 2020/5/21 16:47
 */
@Data
@Accessors(chain = true)
public class YkcFrame {
    /**
     * 序列化域
     */
    private int seq;
    /**
     * 是否加密
     */
    private boolean script;
    /**
     * 帧类型标志
     */
    private YkcCmd cmd;
    /**
     * 消息
     */
    private byte[] data;
}
