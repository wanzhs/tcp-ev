package cdz.ga.ev.kl.service.impl;

import cdz.ga.ev.kl.domain.bean.EvFrame;
import cdz.ga.ev.kl.domain.enums.Cmd;
import cdz.ga.ev.kl.domain.enums.ExpType;
import cdz.ga.ev.kl.domain.enums.FrameType;
import cdz.ga.ev.kl.domain.utils.ChannelThreadLocal;
import cdz.ga.ev.kl.service.CmdFactory;
import cdz.ga.ev.kl.service.ICmdService;
import cn.hutool.core.codec.BCD;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 心跳处理逻辑
 *
 * @author wanzhongsu
 * @date 2020/5/20 14:52
 */
@Slf4j
@Service
public class HeartBeatServiceImpl implements ICmdService, InitializingBean {
    @Resource
    private ChannelThreadLocal threadLocal;

    @Override
    public void run(EvFrame frame) {
        Channel channel = threadLocal.get();
        Integer ctrlId = frame.getCtrlId();
        byte[] data = frame.getData();
        String year = BCD.bcdToStr(ArrayUtil.sub(data, 0, 1));
        String month = BCD.bcdToStr(ArrayUtil.sub(data, 1, 2));
        String day = BCD.bcdToStr(ArrayUtil.sub(data, 2, 3));
        String hour = BCD.bcdToStr(ArrayUtil.sub(data, 3, 4));
        String minute = BCD.bcdToStr(ArrayUtil.sub(data, 4, 5));
        String second = BCD.bcdToStr(ArrayUtil.sub(data, 5, 6));
        log.info(StrUtil.format("收到集控器{}心跳命令,时间  年={}月={}日={}时={}分={}秒={}", ctrlId, year, month, day,
                hour, minute, second));
        EvFrame evFrame = new EvFrame();
        evFrame.setCmd(Cmd.X24)
                .setFrameType(FrameType.REPLY_FRAME)
                .setExpType(ExpType.CONFIRM_FRAME);
        channel.writeAndFlush(evFrame);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        CmdFactory.addService(Cmd.XA4, this);
    }
}
