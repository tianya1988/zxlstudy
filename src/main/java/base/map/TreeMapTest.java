package base.map;

import java.util.Set;
import java.util.TreeMap;

/**
 * Created by jason on 17-5-24.
 */
public class TreeMapTest {

    public static void main(String[] args) {
        //结论： 按key排序并放置元素，toArray之后，可得到有序数组

        /*
        TreeMap<Integer, String> treeMap = new TreeMap<Integer, String>();
        treeMap.put(4, "key1");
        treeMap.put(3, "key2");
        treeMap.put(7, "key3");
        treeMap.put(5, "key4");
        treeMap.put(8, "key5");
        treeMap.put(9, "key6");

        Object[] objects = treeMap.values().toArray();
        for (Object o:objects) {
            System.out.println(o.toString());
        }

        Set<Integer> integers = treeMap.keySet();
        for (int i : integers) {
            System.out.println(i);
        }

输出：
        key2
        key1
        key4
        key3
        key5
        key6
        3
        4
        5
        7
        8
        9
        */


        TreeMap<String, Integer> treeMap = new TreeMap<String, Integer>();
        treeMap.put("key1", 4);
        treeMap.put("key2", 3);
        treeMap.put("key3", 7);
        treeMap.put("key4", 5);
        treeMap.put("key5", 8);
        treeMap.put("key6", 9);

        Object[] objects = treeMap.values().toArray();
        for (Object o : objects) {
            System.out.println(o.toString());
        }

        Set<String> integers = treeMap.keySet();
        for (String i : integers) {
            System.out.println(i);
        }

        /**
         *
         4
         3
         7
         5
         8
         9
         key1
         key2
         key3
         key4
         key5
         key6

         */

    }
}
