package base.test;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by jason on 17-1-17.
 */
public class Test {
    public static void main(String[] args) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<String, String>();
        hashMap.put(null, "zhangsan");
        hashtable.put("", "lisi");
        hashtable.put(null, "lisi");

        System.out.println(hashMap.get(null));
        System.out.println(hashtable.get(""));

        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();

    }
}
