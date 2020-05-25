package org.ga.ev.ykc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.BCD;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.ga.ev.ykc.domain.YkcFrame;
import org.ga.ev.ykc.domain.enums.YkcCmd;
import org.ga.ev.ykc.service.ICmdService;
import org.ga.ev.ykc.utils.ChannelThreadLocal;
import org.ga.ev.ykc.utils.CmdFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 登录认证
 *
 * @author wanzhongsu
 * @date 2020/5/25 13:37
 */
@Slf4j
@Service
public class HeartServiceImpl implements ICmdService, InitializingBean {
    @Resource
    private ChannelThreadLocal threadLocal;

    @Override
    public void run(YkcFrame frame) {
        Channel channel = threadLocal.getChannel();
        byte[] data = frame.getData();
        //桩编码
        String ctrlAddress = BCD.bcdToStr(ArrayUtil.sub(data, 0, 7));
        //枪号
        int deviceSub = data[7] & 0xfff;
        //枪状态
        int deviceSubState = data[8] & 0xfff;
        //登录认证逻辑
        log.info(StrUtil.format("收到集控器{}的桩体心跳包,充电枪{}充电枪状态{}",
                ctrlAddress, deviceSub, deviceSubState));

        //回包
        YkcFrame ykcFrame = new YkcFrame();
        BeanUtil.copyProperties(frame, ykcFrame);
        ykcFrame.setCmd(YkcCmd.X04);
        byte[] rsData = ArrayUtil.addAll(BCD.strToBcd(ctrlAddress), new byte[]{(byte) deviceSub},
                new byte[]{0x00});
        ykcFrame.setData(rsData);
        channel.writeAndFlush(ykcFrame);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        CmdFactory.addService(YkcCmd.X03, this);
    }
}
