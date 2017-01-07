package base.equals;

/**
 * Created by jason on 16-11-27.
 */
public class StringEqualsTest {
    public static void main(String[] args) {
        String s1 = new String("abc");
        String s2 = new String("abc");
        System.out.println(s1.equals(s2));

        String s3 = "abc";
        String s4 = "abc";
        System.out.println(s3.equals(s4));

    }
}
