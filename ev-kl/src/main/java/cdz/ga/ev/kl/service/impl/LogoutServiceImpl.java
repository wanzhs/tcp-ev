package cdz.ga.ev.kl.service.impl;

import cdz.ga.ev.kl.domain.bean.KlFrame;
import cdz.ga.ev.kl.domain.enums.KlCmd;
import cdz.ga.ev.kl.domain.enums.ExpType;
import cdz.ga.ev.kl.domain.enums.FrameType;
import cdz.ga.ev.kl.domain.utils.ChannelThreadLocal;
import cdz.ga.ev.kl.service.CmdFactory;
import cdz.ga.ev.kl.service.ICmdService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 登录退出处理逻辑
 *
 * @author wanzhongsu
 * @date 2020/5/20 14:52
 */
@Slf4j
@Service
public class LogoutServiceImpl implements ICmdService, InitializingBean {
    @Resource
    private ChannelThreadLocal channelThreadLocal;

    @Override
    public void run(KlFrame frame) {
        Channel channel = channelThreadLocal.get();
        Integer ctrlId = frame.getCtrlId();
        log.info(StrUtil.format("集控器{}请求登录退出", ctrlId));
        KlFrame klFrame = new KlFrame();
        BeanUtil.copyProperties(frame, klFrame);
        klFrame.setExpType(ExpType.CONFIRM_FRAME)
                .setKlCmd(KlCmd.X22)
                .setFrameType(FrameType.REPLY_FRAME);
        channel.writeAndFlush(klFrame);
        channel.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        CmdFactory.addService(KlCmd.XA2, this);
    }
}
