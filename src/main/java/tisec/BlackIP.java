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
public class BlackIP {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File("/home/jason/Desktop/suricata/black-ip/20200628_ip_block_list.rules"));

        FileOutputStream outputStream = new FileOutputStream(new File("/home/jason/Desktop/suricata/black-ip/ti-scsc-ip-91000000.rules"));

        //按行返回
        List<String> lines = IOUtils.readLines(fileInputStream);
        int sid = 91000000;
        for (String strIp : lines) {
            String rule = "alert ip " + strIp + " any <> any any (msg:\"intelligence black ip\"; priority:255; sid:" + (sid++) + "; rev:1; metadata:matchType ti-scsc-ip, created_at 2020_06_29, updated_at 2020_06_29;)";
            IOUtils.write(rule, outputStream);
            IOUtils.write("\r\n", outputStream);
        }

        IOUtils.closeQuietly(outputStream);
        IOUtils.closeQuietly(fileInputStream);
    }
}
