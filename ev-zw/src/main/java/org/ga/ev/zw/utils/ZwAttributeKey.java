package org.ga.ev.zw.utils;

import io.netty.util.AttributeKey;

/**
 * 智网管道属性
 *
 * @author wanzhongsu
 * @date 2020/5/26 10:03
 */
public class ZwAttributeKey {
    /**
     * 集控器属性
     */
    public static AttributeKey<String> CTRL_ADDRESS = AttributeKey.valueOf("CTRL_ADDRESS");
}
