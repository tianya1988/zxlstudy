package nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by jason on 17-12-5.
 */
public class FileChannelClassTest {
    public static void main(String[] args) throws IOException {
        RandomAccessFile aFile = new RandomAccessFile("/home/jason/Desktop/test", "rw");
        aFile.setLength(100);
        FileChannel inChannel = aFile.getChannel();
        System.out.println("size is : " + inChannel.size());
        System.out.println("position is : " + inChannel.position());

        // create buffer with capacity of 200 bytes
        ByteBuffer buf = ByteBuffer.allocate(200);
//        buf.put("sao ga : ".getBytes());

        int bytesRead = inChannel.read(buf); // read from channel into buffer.
        while (bytesRead != -1) {
            System.out.println("bytesRead : " + bytesRead);

            buf.flip(); //make buffer ready for read

            while (buf.hasRemaining()) {
                System.out.println("content : " + (char) buf.get()); // read 1 byte at a time
            }

            buf.clear(); //make buffer ready for writing
            buf.put("ba ga : ".getBytes());
            bytesRead = inChannel.read(buf);
            System.out.println("===========");
        }
        aFile.close();
    }
}
