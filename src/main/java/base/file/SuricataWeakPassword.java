package base.file;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by jason on 20-10-15.
 */
public class SuricataWeakPassword {

    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File("/home/jason/Desktop/suricata/weak-password/weak-password.txt"));

        FileOutputStream outputStream = new FileOutputStream(new File("/home/jason/Desktop/suricata/weak-password/scsc-weak-pw.rules"));


        List<String> lines = IOUtils.readLines(fileInputStream);


        // alert http any any -> any any (msg:"可能存在弱口令风险";  content:"password"; content:"UGFzc2MwZGUh"; nocase; flowbits:set,userlogin; sid:93999996; rev:2; metadata:alarm_type 弱口令尝试, created_at 2020_10_15, updated_at 2020_10_15;)
        // alert http any any -> any any (msg:"弱口令登录成功";  content:"200"; http_stat_code; flowbits:isset,userlogin; priority:3; sid:93999995; rev:2; metadata:alarm_type 弱口令, created_at 2020_10_15, updated_at 2020_10_15;)

        int sid = 94000001;
        String rulePart1 = "alert http any any -> any any (msg:\"可能存在弱口令风险\";content:\"login\"; http_uri; nocase; content:\"password\"; content:\"";
        String rulePart2 = "\"; nocase; flowbits:set,userlogin; sid:";
        String rulePart3 = "; rev:2; metadata:alarm_type 弱口令尝试, created_at 2020_10_15, updated_at 2020_10_15;)";
        for (String weakPassword : lines) {
            if (weakPassword.contains(";")) {
                continue;
            }
//            if (weakPassword.equals("qwerty")) {
//                System.out.println("aaaaaaaaaaa");
//                break;
//            }

            if (weakPassword.equals("password")) {
                continue;

            }
            String rule = rulePart1 + weakPassword + rulePart2 + sid + rulePart3;

            System.out.println(rule);
            sid++;

            IOUtils.write(rule, outputStream);
            IOUtils.write("\r\n", outputStream);

        }
        // 登录请求，不能仅仅根据响应码为200，则定义为登录成功。大部分网站在登录密码错误的情况下，响应码为200，只是给一个登录失败的响应信息
        // String loginSuccess= "alert http any any -> any any (msg:\"弱口令登录成功\";  content:\"200\"; http_stat_code; flowbits:isset,userlogin; priority:3; sid:94000000; rev:2; metadata:alarm_type 弱口令, created_at 2020_10_15, updated_at 2020_10_15;)";
        // IOUtils.write(loginSuccess, outputStream);


        IOUtils.closeQuietly(fileInputStream);
    }
}
