package org.ga.ev.ykc.utils;

import io.netty.util.AttributeKey;

/**
 * 云快充管道属性管理
 *
 * @author wanzhongsu
 * @date 2020/5/22 12:29
 */
public class YkcAttributeKey {
    /**
     * 集控器地址
     */
    public static AttributeKey<String> CTRL_ADDRESS = AttributeKey.valueOf("CTRL_ADDRESS");
}
