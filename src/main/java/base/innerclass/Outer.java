package base.innerclass;

/**
 * Created by jason on 18-6-20.
 */
public class Outer {
    class Inner {
        public Inner(String s) {
            System.out.println(s);
        }

        public void innerTest() {
            System.out.println("inner method!");
        }
    }
}
