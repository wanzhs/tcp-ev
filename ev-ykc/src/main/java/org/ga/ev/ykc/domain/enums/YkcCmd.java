package org.ga.ev.ykc.domain.enums;

/**
 * 云充电指令类型
 *
 * @author wanzhongsu
 * @date 2020/5/22 11:56
 */
public enum YkcCmd {
    /**
     * 充电桩登录认证
     */
    X01(0x01),
    /**
     * 登录认证应答
     */
    X02(0x02),
    /**
     * 充电桩心跳包
     */
    X03(0x03),
    /**
     * 心跳包应答
     */
    X04(0x04),
    /**
     *
     */
    X05(0x05),
    /**
     * 计费模型验证请求应答
     */
    X06(0x06),
    /**
     * 充电桩计费模型请求
     */
    X07(0x07),
    /**
     * 计费模型请求应答
     */
    X08(0x08),
    /**
     * 上传实时监测数据
     */
    X11(0x11),
    /**
     * 读取实时监测数据
     */
    X12(0x12),
    /**
     * 离线监测数据
     */
    X13(0x13),
    /**
     * 充电握手
     */
    X15(0x15),
    /**
     * 参数配置
     */
    X17(0x17),
    /**
     * 充电结束
     */
    X19(0x19),
    /**
     * 错误报文
     */
    X1B(0x1B),
    /**
     * 充电阶段BMS中止
     */
    X1D(0x1D),
    /**
     * 充电阶段充电机中止
     */
    X21(0x21),
    /**
     * 充电过程BMS需求、充电机输出
     */
    X23(0x23),
    /**
     * 充电过程BMS信息
     */
    X25(0x25),
    /**
     * 充电桩主动申请启动充电
     */
    X31(0x31),
    /**
     * 运营平台确认启动充电
     */
    X32(0x32),
    /**
     * 远程启机命令回复
     */
    X33(0x33),
    /**
     * 运营平台远程控制启机
     */
    X34(0x34),
    /**
     * 远程停机命令回复
     */
    X35(0x35),
    /**
     * 运营平台远程停机
     */
    X36(0x36),
    /**
     * 交易记录
     */
    X39(0x39),
    /**
     * 交易记录确认
     */
    X40(0x40),
    /**
     * 余额更新应答
     */
    X41(0x41),
    /**
     * 远程账户余额更新
     */
    X42(0x42),
    /**
     * 卡数据同步应答
     */
    X43(0x43),
    /**
     * 离线卡数据同步
     */
    X44(0x44),
    /**
     * 离线卡数据清除应答
     */
    X45(0x45),
    /**
     * 离线卡数据清除
     */
    X46(0x46),
    /**
     * 离线卡数据查询应答
     */
    X47(0x47),
    /**
     * 离线卡数据查询
     */
    X48(0x48),
    /**
     * 充电桩工作参数设置应答
     */
    X51(0x51),
    /**
     * 充电桩工作参数设置
     */
    X52(0x52),
    /**
     * 计费模型应答
     */
    X53(0x53),
    /**
     * 计费模型设置
     */
    X54(0x54),
    /**
     * 对时设置应答
     */
    X55(0x55),
    /**
     * 对时设置
     */
    X56(0x56),
    /**
     * 地锁数据上送（充电桩上送）
     */
    X61(0x61),
    /**
     * 遥控地锁升锁与降锁命令（下行）
     */
    X62(0x62),
    /**
     * 充电桩返回数据（上行）
     */
    X63(0x63),
    /**
     * 0x91	远程重启应答
     */
    X91(0x91),
    /**
     * 远程重启
     */
    X92(0x92),
    /**
     * 远程更新应答
     */
    X93(0x93),
    /**
     * 远程更新
     */
    X94(0x94);

    private int value;

    YkcCmd(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    /**
     * @param value 命令值
     * @return 该命令对应的枚举
     */
    public static YkcCmd getInstance(int value) {
        switch (value) {
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
            case 0x11:
                return X11;
            case 0x12:
                return X12;
            case 0x13:
                return X13;
            case 0x15:
                return X15;
            case 0x17:
                return X17;
            case 0x19:
                return X19;
            case 0x1B:
                return X1B;
            case 0x1D:
                return X1D;
            case 0x21:
                return X21;
            case 0x23:
                return X23;
            case 0x25:
                return X25;
            case 0x31:
                return X31;
            case 0x32:
                return X32;
            case 0x33:
                return X33;
            case 0x34:
                return X34;
            case 0x35:
                return X35;
            case 0x36:
                return X36;
            case 0x39:
                return X39;
            case 0x40:
                return X40;
            case 0x41:
                return X41;
            case 0x42:
                return X42;
            case 0x43:
                return X43;
            case 0x44:
                return X44;
            case 0x45:
                return X45;
            case 0x46:
                return X46;
            case 0x47:
                return X47;
            case 0x48:
                return X48;
            case 0x51:
                return X51;
            case 0x52:
                return X52;
            case 0x53:
                return X53;
            case 0x54:
                return X54;
            case 0x55:
                return X55;
            case 0x56:
                return X56;
            case 0x61:
                return X61;
            case 0x62:
                return X62;
            case 0x63:
                return X63;
            case 0x91:
                return X91;
            case 0x92:
                return X92;
            case 0x93:
                return X93;
            case 0x94:
                return X94;
            default:
                return null;
        }
    }
}
