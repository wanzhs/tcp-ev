package org.ga.ev.zw.domain;

import cn.hutool.core.codec.BCD;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ObjectUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.ga.ev.zw.domain.enums.ZwRegister;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 智网编码器
 *
 * @author wanzhongsu
 * @date 2020/5/26 10:19
 */
@Component
//@ChannelHandler.Sharable
public class ZwEncoder extends MessageToByteEncoder<ZwFrame> {
    private final byte START_MARK = 0x68;

    @Override
    protected void encode(ChannelHandlerContext ctx, ZwFrame msg, ByteBuf out) throws Exception {
        //启动字符
        out.writeByte(START_MARK);
        //报文长度
        byte[] data = msg.getData();
        int msg_length = 0;
        if (!ObjectUtil.isEmpty(data)) {
            msg_length = data.length;
        }
        out.writeShortLE(msg_length);
        out.writeByte(msg.getRegister() == ZwRegister.UNREGISTERED ? 0x00 : 0x01);
        //响应吗 3
        byte[] responseCode = HexUtil.decodeHex(msg.getResponseCode());
        out.writeBytes(responseCode);
        //运营商
        byte[] operatorBy = BCD.strToBcd(msg.getOperator());
        out.writeBytes(operatorBy);
        //长度设备地址
        byte[] ctrlAddressBy = BCD.strToBcd(msg.getCtrlAddress());
        out.writeBytes(ctrlAddressBy);
        //帧类型
        out.writeByte(msg.getCmd().getValue());
        //流水号
        byte[] seqBy = BCD.strToBcd(msg.getSeq());
        out.writeBytes(seqBy);
        //数据体
        if (msg_length > 0) {
            out.writeBytes(data);
        }
    }

    public static String getSeq() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(new Date());
    }
}
