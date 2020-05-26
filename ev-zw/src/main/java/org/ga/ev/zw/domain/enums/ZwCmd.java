package org.ga.ev.zw.domain.enums;

/**
 * 智网命令枚举
 *
 * @author wanzhongsu
 * @date 2020/5/25 17:03
 */
public enum ZwCmd {
    X01(0x01);
    private int value;

    ZwCmd(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static ZwCmd getInstance(int value) {
        switch (value) {
            case 0x01:
                return X01;
            default:
                return null;
        }
    }
}
