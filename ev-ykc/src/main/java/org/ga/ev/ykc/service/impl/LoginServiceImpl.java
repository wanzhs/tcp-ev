package org.ga.ev.ykc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.BCD;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import lombok.extern.slf4j.Slf4j;
import org.ga.ev.ykc.domain.YkcFrame;
import org.ga.ev.ykc.domain.enums.YkcCmd;
import org.ga.ev.ykc.service.ICmdService;
import org.ga.ev.ykc.utils.AuthedChannel;
import org.ga.ev.ykc.utils.ChannelThreadLocal;
import org.ga.ev.ykc.utils.CmdFactory;
import org.ga.ev.ykc.utils.YkcAttributeKey;
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
public class LoginServiceImpl implements ICmdService, InitializingBean {
    @Resource
    private AuthedChannel authedChannel;
    @Resource
    private ChannelThreadLocal threadLocal;

    @Override
    public void run(YkcFrame frame) {
        Channel channel = threadLocal.getChannel();
        byte[] data = frame.getData();
        //桩编码
        String ctrlAddress = BCD.bcdToStr(ArrayUtil.sub(data, 0, 7));
        //桩类型
        int ctrlType = data[7] & 0xfff;
        //通信协议版本
        int version = data[9] & 0xfff;
        //登录认证逻辑
        log.info(StrUtil.format("收到集控器{}的桩体登录认证,桩类型{}通信协议版本{}",
                ctrlAddress, ctrlType, version));

        //回包
        authedChannel.addChannel(ctrlAddress, channel);
        Attribute<String> attribute = channel.attr(YkcAttributeKey.CTRL_ADDRESS);
        attribute.set(ctrlAddress);
        YkcFrame ykcFrame = new YkcFrame();
        BeanUtil.copyProperties(frame, ykcFrame);
        ykcFrame.setCmd(YkcCmd.X02);
        byte[] rsData = ArrayUtil.addAll(BCD.strToBcd(ctrlAddress), new byte[]{0x00});
        ykcFrame.setData(rsData);
        channel.writeAndFlush(ykcFrame);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        CmdFactory.addService(YkcCmd.X01, this);
    }
}
