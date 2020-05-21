package cdz.ga.ev.kl.domain.enums;

/**
 * 异常标志  0 确认帧 1 否定帧
 *
 * @author wanzhongsu
 * @date 2020/5/20 13:48
 */
public enum ExpType {
    /**
     * 确认帧 默认
     */
    CONFIRM_FRAME(0),
    /**
     * 否定帧
     */
    DENY_FRAME(1);

    ExpType(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return this.value;
    }

    public static ExpType getInstance(int value) {
        switch (value) {
            case 0:
                return CONFIRM_FRAME;
            case 1:
                return DENY_FRAME;
        }
        return CONFIRM_FRAME;
    }
}
