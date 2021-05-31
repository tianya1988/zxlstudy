package base.thread;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class WaitDemo {

    private List synchedList;

    public WaitDemo() {
        // 创建一个同步列表
        synchedList = Collections.synchronizedList(new LinkedList());
    }

    // 删除列表中的元素
    public String removeElement() throws InterruptedException {
        synchronized (synchedList) {

            // 列表为空就等待
            while (synchedList.isEmpty()) {
                System.out.println(Thread.currentThread().getName() + " : List is empty...");
                synchedList.wait();// 停住,直到其他线程调用notify
                System.out.println(Thread.currentThread().getName() + " : Waiting...");
            }
            String element = (String) synchedList.remove(0);

            return element;
        }
    }

    // 添加元素到列表
    public void addElement(String element) {
        System.out.println(Thread.currentThread().getName() + " : Opening...");
        synchronized (synchedList) {

            // 添加一个元素，并通知元素已存在
            synchedList.add(element);
            System.out.println(Thread.currentThread().getName() + " : New Element:'" + element + "'");

            synchedList.notifyAll();
            System.out.println(Thread.currentThread().getName() + " : notifyAll called!");
        }
        System.out.println(Thread.currentThread().getName() + " : Closing...");
    }

    public static void main(String[] args) {
        final WaitDemo demo = new WaitDemo();

        Runnable removeRunable = new Runnable() {

            public void run() {
                try {
                    String item = demo.removeElement();
                    System.out.println(Thread.currentThread().getName() + " : " + item);
                } catch (InterruptedException ix) {
                    System.out.println(Thread.currentThread().getName() + " : Interrupted Exception!");
                } catch (Exception x) {
                    System.out.println(Thread.currentThread().getName() + " : Exception thrown.");
                }
            }
        };

        Runnable addRunable = new Runnable() {

            // 执行添加元素操作，并开始循环
            public void run() {
                demo.addElement("Hello!");
            }
        };

        try {
            Thread threadA1 = new Thread(removeRunable, "Google-A1");
            threadA1.start();

            Thread.sleep(500);

            Thread threadA2 = new Thread(removeRunable, "Runoob-A2");
            threadA2.start();

            Thread.sleep(500);

            Thread threadB = new Thread(addRunable, "Taobao-B");
            threadB.start();

            Thread.sleep(1000);

            threadA1.interrupt();
            threadA2.interrupt();
        } catch (InterruptedException x) {
        }
    }
}