package nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by jason on 17-12-5.
 */
public class ChannelScatterTest {
    public static void main(String[] args) throws IOException {
        RandomAccessFile aFile = new RandomAccessFile("/home/jason/Desktop/p1-es-template.json", "rw");
        FileChannel inChannel = aFile.getChannel();
        ByteBuffer header = ByteBuffer.allocate(128);
        ByteBuffer body   = ByteBuffer.allocate(102400);

        ByteBuffer[] bufferArray = { header, body };

        inChannel.read(bufferArray);
        System.out.println(header.toString());
        System.out.println(body.toString());
    }
}
