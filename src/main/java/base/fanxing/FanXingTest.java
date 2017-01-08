package base.fanxing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 17-1-8.
 */
public class FanXingTest {

    public static void main(String[] args) {

        List<String> l1 = new ArrayList<String>();
        List<Integer> l2 = new ArrayList<Integer>();
        System.out.println(l1.getClass() == l2.getClass());//true
    }
}
