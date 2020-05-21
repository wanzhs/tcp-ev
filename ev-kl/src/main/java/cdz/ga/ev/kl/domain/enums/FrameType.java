package cdz.ga.ev.kl.domain.enums;

/**
 * 命令类型 0 主站发出的命令帧 1 终端发出的应答帧
 *
 * @author wanzhongsu
 * @date 2020/5/20 13:46
 */
public enum FrameType {
    /**
     * 命令帧
     */
    ORDERED_FRAME(0),
    /**
     * 应答帧 默认
     */
    REPLY_FRAME(1);

    FrameType(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return this.value;
    }

    public static FrameType getInstance(int value) {
        switch (value) {
            case 0:
                return ORDERED_FRAME;
            case 1:
                return REPLY_FRAME;
        }
        return REPLY_FRAME;
    }
}
