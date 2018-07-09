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
        ByteBuffer header = ByteBuffer.allocate(2);
        ByteBuffer body = ByteBuffer.allocate(2);
        header.put("a".getBytes());
        body.put("b".getBytes());

//        ByteBuffer[] bufferArray = {header, body};
        ByteBuffer[] bufferArray = {header};

        // write data into channel
        RandomAccessFile aFile = new RandomAccessFile("/home/jason/Desktop/test", "rw");
        System.out.println("aFile length : " + aFile.length());
        FileChannel inChannel = aFile.getChannel();
        System.out.println("channel size : " + inChannel.size());
        inChannel.position(100);
        inChannel.write(bufferArray);
        System.out.println("channel position : " + inChannel.position());
    }
}
