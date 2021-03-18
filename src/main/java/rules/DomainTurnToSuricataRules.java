package rules;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;

/**
 * Created by jason on 21-2-5.
 */
public class DomainTurnToSuricataRules {

    public static void main(String[] args) {
//        scsc-selfdefine.rules
        //alert dns any any -> any any (msg:"域名黑名单"; dns.query; content:"www.ms08067.com"; nocase; endswith; priority:2; sid:96000003; rev:1; metadata:matchType ti-scsc-domain, created_at 2020_06_29, updated_at 2021_03_15;)

        String part1 = "alert dns any any -> any any (msg:\"域名黑名单\"; dns.query; content:\"";
        String part2 = "\"; nocase; endswith; priority:2; sid:";
        String part3 = "; rev:1; metadata:matchType ti-scsc-domain, created_at 2021_03_15, updated_at 2021_03_15;)";

        long sid = 96500001;

        FileInputStream fileInputStream = null;
        FileInputStream fileInputStream2 = null;

        FileOutputStream outputStream = null;

        try {
            // http_uri
            fileInputStream = new FileInputStream(new File("/home/jason/Desktop/rule-replace/black-domain.txt"));

            outputStream = new FileOutputStream(new File("/home/jason/Desktop/rule-replace/scsc-bw-jt.rules"));

            sid = handleRules(part1, part2, part3, sid, fileInputStream, outputStream);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        IOUtils.closeQuietly(fileInputStream);
        IOUtils.closeQuietly(fileInputStream2);
        IOUtils.closeQuietly(outputStream);
    }

    private static long handleRules(String part1, String part2, String part3, long sid, FileInputStream fileInputStream, FileOutputStream outputStream) throws IOException {
        List<String> lines = IOUtils.readLines(fileInputStream);
        for (String line : lines) {
            System.out.println(line);
            String newRule = part1 + line + part2 + (sid++) + part3;

            IOUtils.write(newRule, outputStream);
            IOUtils.write("\r\n", outputStream);

        }
        return sid;
    }
}
