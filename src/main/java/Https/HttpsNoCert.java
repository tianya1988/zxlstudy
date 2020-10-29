package Https;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.MapUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by jason on 20-10-28.
 */
public class HttpsNoCert {
    public static void test(String[] args) throws IOException {

        URL url = new URL("https://172.16.0.194:13443/api/v1/scans/b50b2b07-18a9-4de0-a849-786ebbc574e2");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        //设置头部信息
        httpURLConnection.setConnectTimeout(5000);
        httpURLConnection.setRequestProperty("Content-type", "application/json;charset=utf8");
        // httpURLConnection.setRequestProperty("accept", "*/*");
        // httpURLConnection.setRequestProperty("connection", "Keep-Alive");
        // httpURLConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
        httpURLConnection.setRequestProperty("X-Auth", "1986ad8c0a5b3df4d7028d5f3c06e936c8a8e18b0c00342268172e0e6da94271c");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);

        httpURLConnection.setRequestMethod("GET");


        boolean addHttps = url.toString().startsWith("https");
        if (addHttps) {
            SSL addSSL = new SSL();
            addSSL.trustAllHosts((HttpsURLConnection) httpURLConnection);
            ((HttpsURLConnection) httpURLConnection).setHostnameVerifier(addSSL.DO_NOT_VERIFY);
            httpURLConnection.connect();
        } else {
            httpURLConnection.connect();
        }
        if (httpURLConnection.getResponseCode() == 200) {
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            System.out.println(JSONObject.parseObject(stringBuffer.toString()).toJSONString());
        }

    }

    public static void main(String[] args) throws IOException {
        HashMap<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("X-Auth", "1986ad8c0a5b3df4d7028d5f3c06e936c8a8e18b0c00342268172e0e6da94271c");

        HashMap<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("l", "20");

        String url = "https://172.16.0.194:13443/api/v1/scans/b50b2b07-18a9-4de0-a849-786ebbc574e2";
        String content = request(url, headerMap, null, "GET");
        System.out.println(content);

    }

    public static String request(String url, Map<String, String> headerMap, Map<String, String> paramsMap, String method) throws IOException {

        // URL urlObject = new URL("https://172.16.0.194:13443/api/v1/scans/b50b2b07-18a9-4de0-a849-786ebbc574e2");
        URL urlObject = new URL(url);

        HttpURLConnection httpURLConnection = (HttpURLConnection) urlObject.openConnection();

        // 设置头部信息
        httpURLConnection.setConnectTimeout(10000); //10秒连接超时
        httpURLConnection.setReadTimeout(10000);    //10秒读取超时
        httpURLConnection.setRequestProperty("Content-type", "application/json;charset=utf8");
        // httpURLConnection.setRequestProperty("accept", "*/*");
        // httpURLConnection.setRequestProperty("connection", "Keep-Alive");
        // httpURLConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
        // httpURLConnection.setRequestProperty("X-Auth", "1986ad8c0a5b3df4d7028d5f3c06e936c8a8e18b0c00342268172e0e6da94271c");

        if (MapUtils.isNotEmpty(headerMap)) {
            Set<Map.Entry<String, String>> headerEntries = headerMap.entrySet();
            for (Map.Entry<String, String> headerEntry : headerEntries) {
                httpURLConnection.setRequestProperty(headerEntry.getKey(), headerEntry.getValue());
            }
        }

        // 指示应用程序要将数据写入URL连接,其值默认为false（是否传参）
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);

        httpURLConnection.setRequestMethod(method);//GET/POST

        boolean addHttps = urlObject.toString().startsWith("https");
        if (addHttps) {
            SSL addSSL = new SSL();
            addSSL.trustAllHosts((HttpsURLConnection) httpURLConnection);
            ((HttpsURLConnection) httpURLConnection).setHostnameVerifier(addSSL.DO_NOT_VERIFY);
            httpURLConnection.connect();
        } else {
            httpURLConnection.connect();
        }

        // TODO 传递参数,待验证
        if (MapUtils.isNotEmpty(paramsMap)) {
            System.out.println(JSONObject.toJSONString(paramsMap));
            OutputStream out = httpURLConnection.getOutputStream();
            out.write(JSONObject.toJSONString(paramsMap).getBytes());
            out.flush(); //清空缓冲区,发送数据
            out.close();
        }

        if (httpURLConnection.getResponseCode() == 200) {
            InputStream inputStream = httpURLConnection.getInputStream();// 读
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            return JSONObject.parseObject(stringBuffer.toString()).toJSONString();
        } else {
            return "请求异常！";
        }
    }
}
