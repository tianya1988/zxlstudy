package base.innerclass;

/**
 * Created by jason on 18-6-20.
 */
public class InheritOuter extends Outer {
    public void test() {
        new Inner("zhangsan").innerTest();
    }
}
