package test;

/**
 * Created by jason on 20-10-9.
 */
public class RandomTest {
    public static void main(String[] args) {
        int i = 0;
        while (i < 20) {
            i++;
            System.out.println(new java.util.Random().nextInt(5)+1);
        }

    }
}
