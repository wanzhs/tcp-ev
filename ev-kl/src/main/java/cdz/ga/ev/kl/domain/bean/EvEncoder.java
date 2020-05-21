package cdz.ga.ev.kl.domain.bean;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 电动汽车充电协议编码器
 *
 * @author wanzhongsu
 * @date 2020/5/18 14:47
 */
public class EvEncoder extends MessageToByteEncoder<EvFrame> {
    /**
     * 帧起始值
     */
    private final byte START_MARK = 0x68;
    /**
     * 结束码
     */
    private final byte END_MARK = 0x16;

    /**
     * 帧序号 0x01-0x1f 循环递增
     */
    private static int seq = 0;

    public static byte getSeq() {
        seq++;
        seq = seq % 0x1f + 1;
        return (byte) seq;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, EvFrame msg, ByteBuf out) throws Exception {
        ByteBuf byteBuf = Unpooled.buffer();
        //封装帧起始符
        byteBuf.writeByte(START_MARK);
        //封装终端逻辑地址
        int ctrlId = msg.getCtrlId();
        int canIndex = msg.getCanIndex();
        byte[] ctrlIdBy = Convert.intToBytes(ctrlId);
        byte[] canIndexBy = Convert.intToBytes(canIndex);
        byteBuf.writeBytes(ctrlIdBy, 1, 3);
        byteBuf.writeBytes(canIndexBy, 1, 3);
        //封装主站地址
        byte mst = (byte) msg.getMst();
        byte ms1 = (byte) (mst & 0x3f | 0x80 | 0x40);
        byteBuf.writeByte(ms1);
        //封装命令序号
        byte msgSeq = msg.getSeq();
        byte seq = (byte) (msgSeq | 0x00);
        byteBuf.writeByte(seq);
        //封装帧起始符号
        byteBuf.writeByte(START_MARK);
        //封装控制码
        byte cmd = msg.getCmd().getCmdId();
        byte frameType = (byte) (msg.getFrameType().getValue() << 7);
        byte expType = (byte) (msg.getExpType().getValue() << 6);
        byteBuf.writeByte(cmd | frameType | expType);
        //封装数据长度
        if (!ObjectUtil.isEmpty(msg.getData()) && msg.getData().length > 0) {
            byte[] data = msg.getData();
            int length = data.length;
            byte[] lengthBy = Convert.intToBytes(length);
            byteBuf.writeBytes(lengthBy, 2, 2);
            //封装数据域
            byteBuf.writeBytes(data);
        } else {
            byteBuf.writeBytes(new byte[]{0, 0});
        }
        //封装校验码
        byteBuf.writeByte(getCheckCode(byteBuf.array()));
        //封装结束码
        byteBuf.writeByte(END_MARK);
        out.writeBytes(byteBuf);
    }

    /**
     * 获取数据的校验码
     *
     * @param bytes 数据
     * @return byte
     */
    private byte getCheckCode(byte[] bytes) {
        int value = 0;
        for (byte by : bytes) {
            value = value + by;
        }
        return (byte) (value % 256);
    }
}
