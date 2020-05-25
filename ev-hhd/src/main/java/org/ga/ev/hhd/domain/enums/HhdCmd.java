package org.ga.ev.hhd.domain.enums;

/**
 * 海汇德命令
 *
 * @author wanzhongsu
 * @date 2020/5/25 15:57
 */
public enum HhdCmd {
    /**
     * 心跳
     */
    X00(0x00),
    /**
     * 时间同步
     */
    X01(0x01),
    /**
     * 确认数据包
     */
    X02(0x02),
    /**
     * 握手数据包
     */
    X03(0x03),
    /**
     * 充电系统状态
     */
    X04(0x04),
    /**
     * 充电终端状态
     */
    X05(0x05),
    /**
     * 充电系统故障
     */
    X06(0x06),
    /**
     * 充电系统参数设置
     */
    X07(0x07),
    /**
     * 启动/停止充电
     */
    X08(0x08),
    /**
     * 充电请求
     */
    X09(0x09),
    /**
     * 充电开始数据包
     */
    X0A(0x0A),
    /**
     * 充电参数
     */
    X0B(0x0B),
    /**
     * 充电结束
     */
    X0C(0x0C),
    /**
     * 费率数据包
     */
    X0D(0x0D),
    /**
     * 充电记录  统计
     */
    X0E(0x0E),
    /**
     * 充电记录 同步
     */
    X0F(0x0F),
    /**
     * 白名单同步请求
     */
    X10(0x10),
    /**
     * 白名单 操作日志
     */
    X12(0x12),
    /**
     * 黑名单同步请求
     */
    X15(0x15),
    /**
     * 黑名单操作日志
     */
    X16(0x16),
    /**
     * 启动策略下发
     */
    X13(0x13);

    private int value;

    HhdCmd(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static HhdCmd getInstance(int value) {
        switch (value) {
            case 0x00:
                return X00;
            case 0x01:
                return X01;
            case 0x02:
                return X02;
            case 0x03:
                return X03;
            case 0x04:
                return X04;
            case 0x05:
                return X05;
            case 0x06:
                return X06;
            case 0x07:
                return X07;
            case 0x08:
                return X08;
            case 0x09:
                return X09;
            case 0x0A:
                return X0A;
            case 0x0B:
                return X0B;
            case 0x0C:
                return X0C;
            case 0x0D:
                return X0D;
            case 0x0E:
                return X0E;
            case 0x0F:
                return X0F;
            case 0x10:
                return X10;
            case 0x12:
                return X12;
            case 0x13:
                return X13;
            case 0x15:
                return X15;
            case 0x16:
                return X16;
            default:
                return null;
        }
    }
}
