package curl;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;

/**
 * Created by jason on 19-7-16.
 */
public class ThreatBookCurl2 {


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

    public static void main(String[] args) {

        try {


            FileOutputStream outputStream = new FileOutputStream(new File("/home/jason/Desktop/black-domain.txt"));


            FileInputStream fileInputStream = new FileInputStream(new File("/home/jason/Desktop/domain.txt"));
            //按行返回
            List<String> lines = null;

            lines = IOUtils.readLines(fileInputStream);


            int i = 1;

            for (String domain : lines) {

                String[] cmds = {"curl", "https://x.threatbook.cn/nodev4/domain/" + domain, "-H", "authority: x.threatbook.cn", "-H", "cache-control: max-age=0", "-H", "upgrade-insecure-requests: 1", "-H", "user-agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36", "-H", "accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3", "-H", "accept-encoding: gzip, deflate, br", "-H", "accept-language: zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7", "-H", "cookie: gr_user_id=9b0c736d-606c-415b-a01b-736d717bda11; grwng_uid=21deaa8f-a6a3-4c2b-9716-251ba290e8b5; Hm_lvt_abc9391835d9cb24629fcba67b0a85b5=1563245075; Hm_lvt_7fe1ffa0ec1869ee64eeb064aa1eeb72=1563242687,1563245064,1563246299; Hm_lpvt_7fe1ffa0ec1869ee64eeb064aa1eeb72=1563248328; islogin=undefined; rememberme=9857e073f54844d648e6967f3f867d5b130f50de|liaowuhen821@126.com|1563256462810; gr_session_id_93c2f45e22af239e=2161b12a-ca4b-4560-9b0c-4546d0413835; gr_session_id_93c2f45e22af239e_2161b12a-ca4b-4560-9b0c-4546d0413835=true", "--compressed"};
                String content = execCurl(cmds);

                //wb-tag wb-tag-2
                //wb-tag wb-tag-3
                System.out.println("...." + i + "...." + domain + "...." + content);
                if (content.contains("wb-tag wb-tag-2") || content.contains("wb-tag wb-tag-3") || content.contains("wb-tag wb-tag-1")) {
                    IOUtils.write(domain, outputStream);
                    IOUtils.write("\r\n", outputStream);
                }
                i++;
                try {
                    Thread.currentThread().sleep(20000l);//太频繁，不让访问，需要输入验证码进行验证
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            IOUtils.closeQuietly(fileInputStream);
            IOUtils.closeQuietly(outputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
