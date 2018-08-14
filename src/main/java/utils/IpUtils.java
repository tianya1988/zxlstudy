package utils;

import java.math.BigInteger;

public class IpUtils {

    public static long ip2long(String ipv4) {

        String[] splits = ipv4.split("\\.");
        long l = 0;
        l = l + (Long.valueOf(splits[0], 10)) << 24;
        l = l + (Long.valueOf(splits[1], 10) << 16);
        l = l + (Long.valueOf(splits[2], 10) << 8);
        // yunsuanfu youxianji
        l = l + (Long.valueOf(splits[3], 10));
        return l;
    }

    public static String long2ip(long l) {
        String ip = "";
        ip = ip + (l >>> 24);

        ip = ip + "." + ((0x00ffffff & l) >>> 16);
        ip = ip + "." + ((0x0000ffff & l) >>> 8);
        ip = ip + "." + (0x000000ff & l);
        return ip;
    }

    public static BigInteger ipv6toInt(String ipv6) {

        int compressIndex = ipv6.indexOf("::");
        if (compressIndex != -1) {
            String part1s = ipv6.substring(0, compressIndex);
            String part2s = ipv6.substring(compressIndex + 1);
            BigInteger part1 = ipv6toInt(part1s);
            BigInteger part2 = ipv6toInt(part2s);
            int part1hasDot = 0;
            char ch[] = part1s.toCharArray();
            for (char c : ch) {
                if (c == ':') {
                    part1hasDot++;
                }
            }
            // ipv6 has most 7 dot
            return part1.shiftLeft(16 * (7 - part1hasDot)).add(part2);
        }
        String[] str = ipv6.split(":");
        BigInteger big = BigInteger.ZERO;
        for (int i = 0; i < str.length; i++) {
            //::1
            if (str[i].isEmpty()) {
                str[i] = "0";
            }
            big = big.add(BigInteger.valueOf(Long.valueOf(str[i], 16))
                    .shiftLeft(16 * (str.length - i - 1)));
        }
        return big;
    }

    public static String int2ipv6(BigInteger big) {
        String str = "";
        BigInteger ff = BigInteger.valueOf(0xffff);
        for (int i = 0; i < 8; i++) {
            str = big.and(ff).toString(16) + ":" + str;

            big = big.shiftRight(16);
        }
        //the last :
        str = str.substring(0, str.length() - 1);

        return str.replaceFirst("(^|:)(0+(:|$)){2,8}", "::");
    }

    public static long strToLong(final String ip) {
        long result = 0;
        String temp[] = ip.split("\\.");
        if (temp.length == 1) {// ipv6形式的
            return result;
        }
        for (int i = 0; i < temp.length; i++) {
            result += Long.valueOf(temp[i]) << ((3 - i) * 8);
        }
        return result;
    }

    public static long[] ipMaskToLongArray(String ipAndMask) {
        String[] ipArr = ipAndMask.split("/");
        if (ipArr.length != 2) {
            throw new IllegalArgumentException("invalid ipAndMask with: "
                    + ipAndMask);
        }
        int netMask = Integer.valueOf(ipArr[1].trim());
        if (netMask < 0 || netMask > 32) {
            throw new IllegalArgumentException("invalid ipAndMask with: "
                    + ipAndMask);
        }
        long ip = strToLong(ipArr[0]);
        long netIP = ip & (0xFFFFFFFF << (32 - netMask));
        long hostScope = (0xFFFFFFFF >>> netMask);
        return new long[]{netIP, netIP + hostScope};
    }

    public static long getClassBAddress(final String ip) {
        //4294901760l = strToLong("255.255.0.0");
        long result = strToLong(ip);
        return getClassBAddress(result);
    }

    public static long getClassBAddress(final long ip) {
        //4294901760l = strToLong("255.255.0.0");
        return ip & 4294901760l;
    }

    public static void main(String[] args) {
//        long[] longs = ipMaskToLongArray("10.27.122.128/26");
//        System.out.println(longs[0]);
//        System.out.println(long2ip(longs[0]));
//        System.out.println(longs[1]);
//        System.out.println(long2ip(longs[1]));
        System.out.println(long2ip(169573015l));
        System.out.println(ip2long("101.69.121.40"));
    }
}
