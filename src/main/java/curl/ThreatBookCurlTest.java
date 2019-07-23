package curl;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;

/**
 * Created by jason on 19-7-16.
 */
public class ThreatBookCurlTest {


    public static String execCurl(String[] cmds) {
        ProcessBuilder process = new ProcessBuilder(cmds);
        Process p;
        try {
            p = process.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
//                builder.append(System.getProperty("line.separator"));
            }
            return builder.toString();

        } catch (IOException e) {
            System.out.print("error");
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) throws InterruptedException {


        while (true) {
            String[] cmds = {"curl", "http://news.baidu.com"};
            String content = execCurl(cmds);
            System.out.println(content);
            Thread.sleep(1000l);
        }


    }
}
