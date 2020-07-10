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
public class BlackUrl {
    public static void generate() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File("/home/jason/Desktop/suricata/black-url/20200628_url_block_list.rules"));

        FileOutputStream outputStream = new FileOutputStream(new File("/home/jason/Desktop/suricata/black-url/ti-scsc-url-93000000.rules"));

        //按行返回
        List<String> lines = IOUtils.readLines(fileInputStream);
        int sid = 93000000;
        for (String url : lines) {
            // https 不处理
            if (url.startsWith("https")) {
                continue;
            }

            String replacedUrl = url.replace("http://", "");

            if (replacedUrl.endsWith("/")) {
                replacedUrl = replacedUrl.substring(0, replacedUrl.length() - 1);
            }

            /*
            //出现以下字符需要转义
            "     |22|
            ;     |3B|
            :     |3A|
            |     |7C|
             */
            replacedUrl = replacedUrl.replaceAll("\\|", "|7C|");
            replacedUrl = replacedUrl.replaceAll("\"", "|22|");
            replacedUrl = replacedUrl.replaceAll(";", "|3B|");
            replacedUrl = replacedUrl.replaceAll(":", "|3A|");

            int index = replacedUrl.indexOf("/");

            String rule = "";
            if (index < 1) {
                System.out.println(replacedUrl);
                String domain = replacedUrl;
                domain = domain.toLowerCase();

                rule = "alert http any any -> any any (msg:\"intelligence black http url\"; http.host; content:\"" + domain + "\"; priority:255; sid:" + (sid++) + "; rev:1; metadata:matchType ti-scsc-url, created_at 2020_06_29, updated_at 2020_06_29;)";

            } else {
                String domain = replacedUrl.substring(0, index);
                domain = domain.toLowerCase();
                String uri = replacedUrl.substring(index, replacedUrl.length());

                rule = "alert http any any -> any any (msg:\"intelligence black http url\"; http.host; content:\"" + domain + "\"; fast_pattern; http.uri; content:\"" + uri + "\"; endswith; nocase; priority:255; sid:" + (sid++) + "; rev:1; metadata:matchType ti-scsc-url, created_at 2020_06_29, updated_at 2020_06_29;)";
            }

            IOUtils.write(rule, outputStream);
            IOUtils.write("\r\n", outputStream);
        }

        IOUtils.closeQuietly(outputStream);
        IOUtils.closeQuietly(fileInputStream);
    }

    public static void main(String[] args) throws IOException {
        generate();
       /* String url = "http://charttar.net/w_hld/a1/it0/qrt_9n/1_*//*0_/sr?|=";
        String replacedUrl = url.replace("http://", "");
        replacedUrl = replacedUrl.replaceAll("\\|", "|7C|");
        replacedUrl = replacedUrl.replaceAll("\"", "|22|");
        replacedUrl = replacedUrl.replaceAll(";", "|3B|");
        replacedUrl = replacedUrl.replaceAll(":", "|3A|");

        if (replacedUrl.endsWith("/")) {
            replacedUrl = replacedUrl.substring(0, replacedUrl.length() - 1);
        }
        System.out.println(replacedUrl);

        int index = replacedUrl.indexOf("/");
        System.out.println(index);

        String domain = replacedUrl.substring(0, index);
        System.out.println(domain);
        String uri = replacedUrl.substring(index, replacedUrl.length());
        System.out.println(uri);*/
    }

}
