package curl;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;

/**
 * Created by jason on 19-7-16.
 */
public class ThreatBookCurl {


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

//                String[] cmds = {"curl", "https://x.threatbook.cn/nodev4/tb-intelligence/" + domain, "-H", "accept-encoding: gzip, deflate, br", "-H", "accept-language: zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7", "-H", "user-agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36", "-H", "accept: application/json", "-H", "referer: https://x.threatbook.cn/nodev4/domain/p.zer2.com", "-H", "authority: x.threatbook.cn", "-H", "cookie: gr_user_id=9b0c736d-606c-415b-a01b-736d717bda11; grwng_uid=21deaa8f-a6a3-4c2b-9716-251ba290e8b5; Hm_lvt_abc9391835d9cb24629fcba67b0a85b5=1563245075; Hm_lvt_7fe1ffa0ec1869ee64eeb064aa1eeb72=1563242687,1563245064,1563246299; Hm_lpvt_7fe1ffa0ec1869ee64eeb064aa1eeb72=1563248328; islogin=undefined; rememberme=9857e073f54844d648e6967f3f867d5b130f50de|liaowuhen821@126.com|1563256462810; gr_session_id_93c2f45e22af239e=2161b12a-ca4b-4560-9b0c-4546d0413835; gr_session_id_93c2f45e22af239e_2161b12a-ca4b-4560-9b0c-4546d0413835=true", "--compressed"};
//                String[] cmds = {"curl", "https://x.threatbook.cn/nodev4/tb-intelligence/" + domain, "-H", "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:67.0) Gecko/20100101 Firefox/67.0", "-H", "Accept: application/json", "-H", "Accept-Language: en-US,en;q=0.5", "--compressed", "-H", "Referer: https://x.threatbook.cn/nodev4/domain/v.y6h.net", "-H", "Connection: keep-alive", "-H", "Cookie: VB_LANG=zh; Hm_lvt_7fe1ffa0ec1869ee64eeb064aa1eeb72=1563266407,1563266471,1563266519; Hm_lpvt_7fe1ffa0ec1869ee64eeb064aa1eeb72=1563266519; gr_user_id=b3d5ad4c-cb4b-4708-b4b1-b5e68703746c; gr_session_id_93c2f45e22af239e=f1bec6fd-4e7d-4fa9-8fec-f3dbe4f92f32; gr_session_id_93c2f45e22af239e_f1bec6fd-4e7d-4fa9-8fec-f3dbe4f92f32=true; 889930cec4eb8153_gr_session_id_96b76bda-085d-4f2b-8b6e-f25fd28449b3=true; 889930cec4eb8153_gr_session_id=96b76bda-085d-4f2b-8b6e-f25fd28449b3; grwng_uid=9388f275-676c-41c1-afde-4e7dc1dabde0; rememberme=b19ede9f4d70c49c528476be678d9afe4c0121f7|473775450@qq.com|1563266518873; gr_cs1_f1bec6fd-4e7d-4fa9-8fec-f3dbe4f92f32=username%3A473775450%40qq.com", "-H", "TE: Trailers"};
                String[] cmds = {"curl", "https://x.threatbook.cn/nodev4/tb-intelligence/" + domain, "-H", "User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:68.0) Gecko/20100101 Firefox/68.0", "-H", "Accept: application/json", "-H", "Accept-Language: en-US,en;q=0.5", "--compressed", "-H", "Referer: https://x.threatbook.cn/nodev4/domain/adnetwork33.redirectme.net", "-H", "Connection: keep-alive", "-H", "Cookie: Hm_lvt_7fe1ffa0ec1869ee64eeb064aa1eeb72=1563266407,1563266471,1563266519,1563325465; gr_user_id=b3d5ad4c-cb4b-4708-b4b1-b5e68703746c; grwng_uid=9388f275-676c-41c1-afde-4e7dc1dabde0; rememberme=b19ede9f4d70c49c528476be678d9afe4c0121f7|473775450@qq.com|1563266518873; VB_LANG=zh; gr_session_id_93c2f45e22af239e=8f05c67c-56bf-4d30-8b86-201cf57ec3f1; gr_session_id_93c2f45e22af239e_8f05c67c-56bf-4d30-8b86-201cf57ec3f1=true; gr_cs1_8f05c67c-56bf-4d30-8b86-201cf57ec3f1=username%3A473775450%40qq.com; Hm_lpvt_7fe1ffa0ec1869ee64eeb064aa1eeb72=1563325465", "-H", "Cache-Control: max-age=0", "-H", "TE: Trailers"};
                String content = execCurl(cmds);
                //wb-tag wb-tag-2
                //wb-tag wb-tag-3
                System.out.println("...." + i + "...." + domain + "...." + content);
                if (content.contains("有效") && (!content.contains("白名单"))) {
                    System.out.println(domain);
                    IOUtils.write(domain, outputStream);
                    IOUtils.write("\r\n", outputStream);
                }
                i++;
                try {
                    Thread.currentThread().sleep(5000l);
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
