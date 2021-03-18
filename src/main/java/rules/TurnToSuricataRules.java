package rules;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.List;

/**
 * Created by jason on 21-2-5.
 */
public class TurnToSuricataRules {

    public static void main(String[] args) {
//        scsc-selfdefine.rules
        //alert http any any -> any any (msg:"web扫描";content:"xiaozi"; http_uri; nocase; sid:97700001; rev:1; metadata:alarm_type 恶意扫描, created_at 2021_02_04, updated_at 2021_02_04;)

        String part1 = "alert http any any -> any any (msg:\"";
        String part2 = "\";content:\"";
        String part3 = "\"; http_uri; nocase; sid:";
        String part4 = "; rev:1; metadata:alarm_type ";
        String part5 = ", created_at 2021_02_04, updated_at 2021_02_04;)";

        long sid = 97700001l;

        FileInputStream fileInputStream = null;
        FileInputStream fileInputStream2 = null;

        FileOutputStream outputStream = null;

        try {
            // http_uri
            fileInputStream = new FileInputStream(new File("/home/jason/Desktop/rule-replace/rule-http_uri.json"));

            outputStream = new FileOutputStream(new File("/home/jason/Desktop/rule-replace/scsc-selfdefine.rules"));

            sid = handleRules(part1, part2, part3, part4, part5, sid, fileInputStream, outputStream);


            //================================================
            // http_payload
            part3 = "\"; nocase; sid:";
            fileInputStream2 = new FileInputStream(new File("/home/jason/Desktop/rule-replace/rule-payload.json"));

            sid = handleRules(part1, part2, part3, part4, part5, sid, fileInputStream2, outputStream);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        IOUtils.closeQuietly(fileInputStream);
        IOUtils.closeQuietly(fileInputStream2);
        IOUtils.closeQuietly(outputStream);
    }

    private static long handleRules(String part1, String part2, String part3, String part4, String part5, long sid, FileInputStream fileInputStream, FileOutputStream outputStream) throws IOException {
        List<String> lines = IOUtils.readLines(fileInputStream);
        for (String line : lines) {
            System.out.println(line);
            JSONObject jsonObject = JSONObject.parseObject(line);

            // 逻辑复杂不处理
//            String ruleFurther = jsonObject.getString("ruleFurther");
//            if (StringUtils.isNotEmpty(ruleFurther)) {
//                continue;
//            }

            String msg = jsonObject.getString("ruleDesc");
            String alarmType = jsonObject.getString("ruleType");

            //规则类型向suricata统一
            if ("web扫描".endsWith(alarmType)) {
                alarmType = "恶意扫描";
            }
            if ("Web Shell".endsWith(alarmType)) {
                alarmType = "Webshell";
            }

            if ("Sql注入".endsWith(alarmType)) {
                alarmType = "SQL注入";
            }

            if ("挖矿行为".endsWith(alarmType)) {
                alarmType = "挖矿";
            }
            if ("蠕虫病毒".endsWith(alarmType)) {
                alarmType = "木马病毒";
            }

            //命令执行
            //漏洞利用

            String ruleContext = jsonObject.getString("ruleContext");
            String[] contexts = ruleContext.split("分割");

            for (String context : contexts) {
                String newRule = part1 + msg + part2 + context + part3 + (sid++) + part4 + alarmType + part5;

                IOUtils.write(newRule, outputStream);
                IOUtils.write("\r\n", outputStream);

            }

        }
        return sid;
    }
}
