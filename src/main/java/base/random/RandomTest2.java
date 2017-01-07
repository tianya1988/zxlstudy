package base.random;

import java.util.Random;

/**
 * Created by jason on 16-11-27.
 */
public class RandomTest2 {
    public static void main(String[] args) {
        Random random1 = new Random(25);
        for (int i = 0; i < 10; i++) {
            System.out.println(random1.nextInt(100));
            String s;
        }

    }


}
