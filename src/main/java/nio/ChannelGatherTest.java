package nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by jason on 17-12-5.
 */
public class ChannelGatherTest {
    public static void main(String[] args) throws IOException {
        ByteBuffer header = ByteBuffer.allocate(128);
        ByteBuffer body = ByteBuffer.allocate(1024);
        header.put("what is your name ? ".getBytes());
        body.put("my name is lili".getBytes());

        ByteBuffer[] bufferArray = {header, body};

        // write data into channel
        RandomAccessFile aFile = new RandomAccessFile("/home/jason/Desktop/test", "rw");
        FileChannel inChannel = aFile.getChannel();
        inChannel.write(bufferArray);
        System.out.println(inChannel);
    }
}
