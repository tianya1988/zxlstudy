package base.queue;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;

/**
 * Created by jason on 18-4-17.
 */
public class DequeTest {
    public static void main(String[] args) {
        ArrayDeque<ByteBuffer> byteBuffers = new ArrayDeque<ByteBuffer>();

        System.out.println(byteBuffers.isEmpty());
        System.out.println(byteBuffers.pollFirst());
    }
}
