package cdz.ga.ev.kl.service.impl;

import cdz.ga.ev.kl.domain.bean.KlFrame;
import cdz.ga.ev.kl.domain.consts.AuthedChannel;
import cdz.ga.ev.kl.domain.consts.KlAttributeKey;
import cdz.ga.ev.kl.domain.enums.KlCmd;
import cdz.ga.ev.kl.domain.enums.ExpType;
import cdz.ga.ev.kl.domain.enums.FrameType;
import cdz.ga.ev.kl.domain.utils.ChannelThreadLocal;
import cdz.ga.ev.kl.service.CmdFactory;
import cdz.ga.ev.kl.service.ICmdService;
import cn.hutool.core.codec.BCD;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.HexUtil;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 登录处理逻辑
 *
 * @author wanzhongsu
 * @date 2020/5/20 14:52
 */
@Slf4j
@Service
public class LoginServiceImpl implements ICmdService, InitializingBean {
    @Resource
    private ChannelThreadLocal threadLocal;
    @Resource
    private AuthedChannel authedChannel;

    @Override
    public void run(KlFrame frame) {
        Channel channel = threadLocal.get();
        byte[] data = frame.getData();
        log.info(String.format("桩体登录数据:{}", HexUtil.encodeHex(data)));
        String loginPwd = BCD.bcdToStr(ArrayUtil.sub(data, 0, 3));
        String year = BCD.bcdToStr(ArrayUtil.sub(data, 3, 5));
        String month = BCD.bcdToStr(ArrayUtil.sub(data, 5, 6));
        String day = BCD.bcdToStr(ArrayUtil.sub(data, 6, 7));
        String hour = BCD.bcdToStr(ArrayUtil.sub(data, 7, 8));
        String minute = BCD.bcdToStr(ArrayUtil.sub(data, 8, 9));
        String second = BCD.bcdToStr(ArrayUtil.sub(data, 9, 10));
        log.info(String.format("桩体登录密码={},登录时间 年={}月={}日={}时={}分={}秒={}", loginPwd, year, month, day, hour, minute, second));
        //业务逻辑
        replyLogin(channel, true, frame);
    }

    /**
     * @param channel 通道
     * @param passed  是否通过了验证
     */
    private void replyLogin(Channel channel, boolean passed, KlFrame frame) {
        Integer ctrlId = frame.getCtrlId();
        KlFrame klFrame = new KlFrame()
                .setKlCmd(KlCmd.X21)
                .setFrameType(FrameType.REPLY_FRAME)
                .setCanIndex(frame.getCanIndex())
                .setCtrlId(ctrlId)
                .setMst(frame.getMst())
                .setSeq(frame.getSeq());
        if (passed) {
            /**
             * 通过认证，将集控器id加入该管道
             */
            authedChannel.addChannel(frame.getCtrlId(), channel);
            AttributeKey<Integer> attributeKey = KlAttributeKey.CTRL_ID;
            Attribute attribute = channel.attr(attributeKey);
            attribute.set(ctrlId);
            klFrame.setExpType(ExpType.CONFIRM_FRAME);
        } else {
            klFrame.setExpType(ExpType.DENY_FRAME);
        }
        channel.writeAndFlush(klFrame);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        CmdFactory.addService(KlCmd.XA1, this);
    }
}
