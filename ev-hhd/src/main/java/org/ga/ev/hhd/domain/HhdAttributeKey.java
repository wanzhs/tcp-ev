package org.ga.ev.hhd.domain;

import io.netty.util.AttributeKey;

/**
 * 海汇德属性通道管理器
 *
 * @author wanzhongsu
 * @date 2020/5/25 15:46
 */
public class HhdAttributeKey {
    /**
     * 集控器属性
     */
    public static AttributeKey<String> CTRL_CODE = AttributeKey.valueOf("CTRL_CODE");
}
