package cdz.ga.ev.kl.domain.utils;

/**
 * tcp编码解码工具类
 *
 * @author wanzhongsu
 * @date 2020/5/19 15:54
 */
public class TcpUtils {
    public static int convert3Bytes2Int(byte[] bytes) {
        int value = bytes[0] * 256 * 256 + bytes[1] * 256 + bytes[2];
        return value;
    }
}
