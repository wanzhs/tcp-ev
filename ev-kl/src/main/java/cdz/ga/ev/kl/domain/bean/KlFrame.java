package cdz.ga.ev.kl.domain.bean;

import cdz.ga.ev.kl.domain.enums.KlCmd;
import cdz.ga.ev.kl.domain.enums.ExpType;
import cdz.ga.ev.kl.domain.enums.FrameType;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 电动汽车贞数据
 *
 * @author wanzhongsu
 * @date 2020/5/18 15:45
 */
@Data
@Accessors(chain = true)
public class KlFrame {
    /**
     * 终端逻辑地址
     */
    private int ctrlId;
    /**
     * 枪索引
     */
    private int canIndex;
    /**
     * 主站地址与命令序号
     */
    private int mst;
    /**
     * 帧序号
     */
    private byte seq;
    /**
     * 帧类型
     */
    private FrameType frameType;
    /**
     * 异常标志
     */
    private ExpType expType;
    /**
     * 控制码
     */
    private KlCmd klCmd;
    /**
     * 数据域
     */
    private byte[] data;
}
