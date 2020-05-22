package cdz.ga.ev.kl.domain.enums;

/**
 * 命令枚举
 *
 * @author wanzhongsu
 * @date 2020/5/18 16:38
 */
public enum KlCmd {
    /**
     * 读当前数据
     */
    X01(0x01),
    /**
     * 读任务数据
     */
    X02(0x02),
    /**
     * 读充电记录数据
     */
    X03(0x03),
    /**
     * 读编程日志
     */
    X04(0x04),
    /**
     * 实时写对象参数
     */
    X07(0x07),
    /**
     * 写对象参数
     */
    X08(0x08),
    /**
     * 异常告警
     */
    X09(0x09),
    /**
     * 充电启动/停止
     */
    X0A(0x0A),
    /**
     * 充电功率调节
     */
    X0B(0x0B),
    /**
     * 预约充电
     */
    X0C(0x0C),
    /**
     * 遥控控制
     */
    X0D(0x0D),
    /**
     * 远程升级
     */
    X0E(0x0E),
    /**
     * 设置黑/白名单
     */
    X10(0x10),
    /**
     * 远程用户验证
     */
    X11(0x11),
    /**
     * 登录回复
     */
    X21(0x21),
    /**
     * 登录退出回复
     */
    X22(0x22),
    /**
     * 心跳回复
     */
    X24(0x24),
    /**
     * 登录
     */
    XA1(0xA1),
    /**
     * 登录退出
     */
    XA2(0xA2),
    /**
     * 心跳
     */
    XA4(0xA4),
    /**
     * 厂家自定义
     */
    X3F(0x3F);


    /**
     * 命令id
     */
    private Integer cmdId;

    KlCmd(Integer cmdId) {
        this.cmdId = cmdId;
    }

    /**
     * 获取当前命令的命令id
     *
     * @return 命令id
     */
    public byte getCmdId() {
        return (byte) (this.cmdId & 0xff);
    }

    /**
     * 根据桩体指令id获取该指令对应的枚举值
     *
     * @param cmdId 指令id
     * @return 控制命令
     */
    public static KlCmd getInstance(int cmdId) {
        switch (cmdId) {
            case 0x01:
                return X01;
            case 0x02:
                return X02;
            case 0x03:
                return X03;
            case 0x04:
                return X04;
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
            case 0x10:
                return X10;
            case 0x11:
                return X11;
            case 0x21:
                return X21;
            case 0x22:
                return X22;
            case 0x24:
                return X24;
            case 0xA1:
                return XA1;
            case 0xA2:
                return XA2;
            case 0xA4:
                return XA4;
            case 0x3f:
                return X3F;
            default:
                return null;
        }
    }
}
