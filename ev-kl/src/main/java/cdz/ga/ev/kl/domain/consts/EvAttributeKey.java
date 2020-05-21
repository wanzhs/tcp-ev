package cdz.ga.ev.kl.domain.consts;

import io.netty.util.AttributeKey;

/**
 * 电动汽车管道属性
 *
 * @author wanzhongsu
 * @date 2020/5/20 16:54
 */
public class EvAttributeKey {
    /**
     * 集控器id
     */
    public final static AttributeKey<Integer> CTRL_ID = AttributeKey.valueOf("CTRL_ID");
}
