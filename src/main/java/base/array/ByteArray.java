package base.array;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by jason on 20-7-20.
 */
public class ByteArray {
    /**
     *   从内存中读取字节数组
     */
    public static void main(String[] args) throws IOException {
        String str1 = "132asd";
        byte[] b = new byte[100]; // byte 元素就是数字
        ByteArrayInputStream in = new ByteArrayInputStream(str1.getBytes());
        in.read(b);
        System.out.println(new String(b));
        in.read(b);
        System.out.println(new String(b));
        System.out.println("========");
    }
}
