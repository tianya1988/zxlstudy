package utils;

import snowflake.Snowflake;

/**
 * Created by jason on 19-7-11.
 */
public class IDUtils {
    public static void main(String[] args) {
        System.out.println(Snowflake.getId());

        /**
         * 本地测试必须修改hosts,因为会找ip对应的mac地址，而127.0.0.1无mac地址。
         * 172.16.128.188 localhost
         * 172.16.128.188 jason
         */

        //1149175400519774208
        //1149175504366546944
        //1149175933020221440
        //6554961545561260056

    }
}
