package base.string;

import org.apache.commons.lang.StringUtils;

/**
 * Created by jason on 17-10-26.
 */
public class StringUtilsTest {
    public static void main(String[] args) {
//        System.out.println(StringUtils.isEmpty(""));//true
//        System.out.println(StringUtils.isEmpty(" "));//false
//        System.out.println(StringUtils.isEmpty(null));//true
//        System.out.println(StringUtils.isBlank(" "));//true
//        System.out.println(StringUtils.isBlank(null));//true
//
//        String[] split = StringUtils.split(" 6 \u0000 1 86397 64 01610C726F6F742D73657276657273036E657400056E73746C640C766572697369676E2D67727303636F6D007848696C000007080000038400093A8000015180 ", ' ');
//        System.out.println(split);

//        System.out.println(StringUtils.splitPreserveAllTokens("", '.').length);

//        System.out.println("avro-aqyaudit-xj-to-hdfs.properties".substring(0, "avro-aqyaudit-xj-to-hdfs.properties".lastIndexOf(".properties")));

        try {
            Integer.parseInt("sdf");
        } catch (Exception e){
            Integer.parseInt("23s");
            System.out.println("======");
        }

    }
}
