package base.threadlocal;

/**
 * Created by jason on 17-3-8.
 */
public class ThreadLocalTest {

    //创建一个Integer型的线程本地变量
    public static final ThreadLocal<Integer> local = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

}
