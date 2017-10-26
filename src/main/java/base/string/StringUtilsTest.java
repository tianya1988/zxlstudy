package base.string;

import org.apache.commons.lang.StringUtils;

/**
 * Created by jason on 17-10-26.
 */
public class StringUtilsTest {
    public static void main(String[] args) {
        System.out.println(StringUtils.isEmpty(""));
        System.out.println(StringUtils.isEmpty(" "));
        System.out.println(StringUtils.isEmpty(null));
        System.out.println(StringUtils.isBlank(" "));
        System.out.println(StringUtils.isBlank(null));

        /*
        true
        false
        true
        true
        true
        */
    }
}
