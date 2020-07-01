package tisec;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by jason on 20-6-29.
 */
public class BlackDomain {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File("/home/jason/Desktop/suricata/black-domain/20200630_domain_block_list.rules"));

        FileOutputStream outputStream = new FileOutputStream(new File("/home/jason/Desktop/suricata/black-domain/ti-scsc-domain-92000000.rules"));

        //按行返回
        List<String> lines = IOUtils.readLines(fileInputStream);
        int sid = 92000000;
        for (String domain : lines) {
            String rule = "alert dns any any -> any any (msg:\"intelligence black domain\"; dns.query; content:\"" + domain + "\"; nocase; endswith; priority:255; sid:" + (sid++) + "; rev:1; metadata:matchType ti-scsc-domain, created_at 2020_06_29, updated_at 2020_06_29;)";
            IOUtils.write(rule, outputStream);
            IOUtils.write("\r\n", outputStream);
        }

        IOUtils.closeQuietly(outputStream);
        IOUtils.closeQuietly(fileInputStream);
    }
}
