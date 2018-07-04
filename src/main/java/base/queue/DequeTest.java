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


        ArrayDeque<Integer> integerArrayDeque = new ArrayDeque<Integer>();
        integerArrayDeque.add(1);
        integerArrayDeque.add(2);
        System.out.println(integerArrayDeque.getLast());
        System.out.println(integerArrayDeque.peekFirst());
//        System.out.println(integerArrayDeque.peekFirst());
//        System.out.println(integerArrayDeque.pollFirst());
//        System.out.println(integerArrayDeque.pollFirst());


    }
}
