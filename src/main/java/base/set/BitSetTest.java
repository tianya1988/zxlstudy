package base.set;

import java.util.BitSet;

/**
 * Created by jason on 17-5-19.
 */
public class BitSetTest {

    public static void main(String[] args) {
        // BitSet默认大小是64位，两倍扩容
        // BitSet每一位存错true或false，每一位代表一个数值，该数值就是该位在BitSet中的position
        BitSet bs = new BitSet();
        System.out.println(bs.isEmpty() + "--" + bs.size());

        bs.set(0);
        System.out.println(bs.isEmpty() + "--" + bs.size());

        bs.set(1);
        System.out.println(bs.isEmpty() + "--" + bs.size());

        System.out.println("get (65) : " + bs.get(65));

        System.out.println(bs.isEmpty() + "--" + bs.size());

        bs.set(65);
        System.out.println(bs.isEmpty() + "--" + bs.size());

        System.out.println("get (65) : " + bs.get(65));
    }
}
