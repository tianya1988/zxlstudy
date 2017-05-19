package base.threadlocal;

/**
 * Created by jason on 17-3-8.
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[5];
        for (int j = 0; j < 5; j++) {
            threads[j] = new Thread(new Runnable() {
                @Override
                public void run() {
                    //获取当前线程的本地变量，然后累加5次
                    int num = ThreadLocalTest.local.get();
                    for (int i = 0; i < 5; i++) {
                        num++;
                    }
                    //重新设置累加后的本地变量
                    ThreadLocalTest.local.set(num);
                    System.out.println(Thread.currentThread().getName() + " : "+ ThreadLocalTest.local.get());

                }
            }, "Thread-" + j);
        }

        for (Thread thread : threads) {
            thread.start();
        }
    }
}
