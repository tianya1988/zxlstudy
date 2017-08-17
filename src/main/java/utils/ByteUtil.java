package utils;

/**
 * Created by h on 17-2-17.
 */
public class ByteUtil {

    public static byte[] bigEndian(int value) {
        return new byte[]{
            (byte) (value >> 24 & 255), (byte) (value >> 16 & 255), (byte) (value >> 8 & 255), (byte) (value & 255)
        };
    }

    public static byte[] littleEndian(int value) {
        return new byte[]{
            (byte) (value & 255), (byte) (value >> 8 & 255), (byte) (value >> 16 & 255), (byte) (value >> 24 & 255)};
    }

    public static int littleEndianToInt(byte[] src, int offset) {
        return src[offset] & 255
                | (src[offset + 1] & 255) << 8
                | (src[offset + 2] & 255) << 16
                | (src[offset + 3] & 255) << 24;
    }

    public static int bigEndianToInt(byte[] src, int offset) {
        return (src[offset] & 255) << 24
                | (src[offset + 1] & 255) << 16
                | (src[offset + 2] & 255) << 8
                | src[offset + 3] & 255;
    }
}
