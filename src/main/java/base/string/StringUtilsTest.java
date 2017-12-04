package base.string;

import org.apache.commons.lang.StringUtils;

/**
 * Created by jason on 17-10-26.
 */
public class StringUtilsTest {
    public static void main(String[] args) {
        System.out.println(StringUtils.isEmpty(""));//true
        System.out.println(StringUtils.isEmpty(" "));//false
        System.out.println(StringUtils.isEmpty(null));//true
        System.out.println(StringUtils.isBlank(" "));//true
        System.out.println(StringUtils.isBlank(null));//true
    }
}
