package utils;

/**
 * Created by jason on 20-5-14.
 */
public class StringDecode {
    public static void main(String[] args) {
        String str = "www.baidu.com/sfda/asdfa/asdfa/asdf";
        byte[] bytes = str.getBytes();
        System.out.println(str.getBytes());

        for (byte aByte : bytes) {
            System.out.println(aByte);
        }

        String s = new String(bytes);
        System.out.println(s);
    }
}
