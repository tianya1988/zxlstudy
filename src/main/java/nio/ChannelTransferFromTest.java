package nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Created by jason on 17-12-5.
 * 可以实现文件内容复制的功能
 */
public class ChannelTransferFromTest {
    public static void main(String[] args) throws IOException {
        RandomAccessFile fromFile = new RandomAccessFile("/home/jason/Desktop/p1-es-template.json", "rw");
        FileChannel      fromChannel = fromFile.getChannel();

        RandomAccessFile toFile = new RandomAccessFile("/home/jason/Desktop/test2", "rw");
        FileChannel toChannel = toFile.getChannel();

        long position = 0;
        long count = fromChannel.size();

        long l = toChannel.transferFrom(fromChannel, position, count);
    }
}
