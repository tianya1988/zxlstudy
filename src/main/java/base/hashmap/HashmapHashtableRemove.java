package base.hashmap;

import java.util.*;

/**
 * Created by jason on 20-7-20.
 */
public class HashmapHashtableRemove {
    public static void main(String[] args) {
        /**
         * HashMap 中的 Iterator 迭代器是 fail-fast 的，而 Hashtable 的 Enumerator 不是 fail-fast 的。

         所以，当其他线程改变了HashMap 的结构，如：增加、删除元素，将会抛出 ConcurrentModificationException 异常，而 Hashtable 则不会。
         * */
        Map<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put("t1", "1");
        hashtable.put("t2", "2");
        hashtable.put("t3", "3");

        Enumeration<Map.Entry<String, String>> iterator1 = (Enumeration<Map.Entry<String, String>>) hashtable.entrySet().iterator();
        hashtable.remove(iterator1.nextElement().getKey());
        while (iterator1.hasMoreElements()) {
            System.out.println(iterator1.nextElement());
        }

        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("h1", "1");
        hashMap.put("h2", "2");
        hashMap.put("h3", "3");

        Iterator<Map.Entry<String, String>> iterator2 = hashMap.entrySet().iterator();
        hashMap.remove(iterator2.next().getKey());
        while (iterator2.hasNext()) {
            System.out.println(iterator2.next());
        }

    }
}
