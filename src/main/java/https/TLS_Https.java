package https;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;



public class TLS_Https {

    public static void httpsRequest(String urlpath, String body) {
        ResponseBean resBean = new ResponseBean();
        int responseCode = -1;
        String responseMessage = "Exception.";
        HttpsURLConnection conn = null;
        try {
            java.lang.System.setProperty("https.protocols", "TLS1.2");
            URL u = new URL(urlpath);
            conn = (HttpsURLConnection) u.openConnection();

            conn.setHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "text/html;charset=UTF-8");
            // todo 设置下面三个字符串变量
//            CertificateManager.configSSLSocketFactory(conn, Configuration.CLIENTPASSWORD,
//                    Configuration.CLIENTKEYSTOREPATH, Configuration.CLIENTTRUSTOREPATH);
            conn.connect();

            OutputStream out = conn.getOutputStream();
            out.write(body.getBytes());
            out.flush();
            out.close();

            // Sleep 50 mill seconds wait for response.
            Thread.sleep(50);
            responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
                String line = null;
                StringBuilder sb = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                in.close();
                conn.disconnect();
                String responseMsg = sb.toString();
                resBean.httpstatus = responseCode;
                if (null == resBean.result_code) {
                    resBean.result_code = "999";
                }

                if (responseMessage == null) {
                    responseMessage = conn.getResponseMessage();
                }
            } else {
                resBean.httpstatus = responseCode;
                resBean.result_code = "999";
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        resBean.httpstatus = responseCode;
        resBean.result_code = "999";

    }

}