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
        RandomAccessFile aFile = new RandomAccessFile("/home/jason/Desktop/fang-filter", "rw");
        FileChannel inChannel = aFile.getChannel();

        // create buffer with capacity of 48 bytes
        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.put("sao ga ".getBytes());

        int bytesRead = inChannel.read(buf); // read into buffer.
        while (bytesRead != -1) {

            buf.flip(); //make buffer ready for read

            while (buf.hasRemaining()) {
                System.out.print((char) buf.get()); // read 1 byte at a time
            }

            buf.clear(); //make buffer ready for writing
            buf.put("ba ga ".getBytes());
            bytesRead = inChannel.read(buf);
            System.out.println("===========");
        }
        aFile.close();
    }
}
