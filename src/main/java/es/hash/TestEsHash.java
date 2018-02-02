package es.hash;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by jason on 18-1-17.
 */
public class TestEsHash {
    public static void main(String[] args) {
//        int hash = Murmur3HashFunction.hash("953871046805135400" + "");
//        System.out.println("hash=" + hash);
//        System.out.println(Math.floorMod(hash, 128));
//        System.out.println(Math.floorMod(hash, 3));


//        System.out.println(Math.floorMod(20, 3));
//        System.out.println(Math.floorMod(-40, 3));


        HashMap<String, String> hashMap = new HashMap<String, String>();

        int i = 100000;
        int j = i + 500000;

        while (i < j) {
            i++;
            int hash = Murmur3HashFunction.hash(i + "");
            String partition1 = Math.floorMod(hash, 30) + "";
            String partition2 = Math.floorMod(hash, 128) + "";
            System.out.println("        " + i + "          " + Math.floorMod(hash, 96) + "          " + Math.floorMod(hash, 24));


            if (hashMap.containsKey(partition1)) {
                String old = hashMap.get(partition1);
                if (!old.contains(partition2)) {
                    hashMap.put(partition1, old + "," + partition2);
                }
            } else {
                hashMap.put(partition1, partition2);
            }

        }

        Set<Map.Entry<String, String>> entries = hashMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println(entry.getKey() + "     :      " + entry.getValue());
        }


        //953445648234307584 5 11  ***
        //953445648234307585 6 4
        //953445648234307586 5 5   ***
        //953445648234307587 8 6
        //953445648234307588 2 0
        //953445648234307589 2 2
        //953445648234307590 5 5
        //953445648234307591 3 9
        //953445648234307592 5 1   ***


    }
}
