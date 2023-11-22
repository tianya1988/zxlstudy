package base.httpclient.digestauthFailure;

import base.httpclient.digestauthOK.HttpRequestUtilsTest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class DigestAuthExample3 {
    public static void main(String[] args) throws Exception {
        // 图片文件路径
        String imagePath = "path/to/your/image.jpg";
        // 服务器URL
        String serverUrl = "http://example.com/upload";
        // 用户名和密码
        String username = "your_username";
        String password = "your_password";

        String wwwAuth = HttpRequestUtilsTest.sendGet(serverUrl, "");       //发起一次授权请求
        System.out.println(wwwAuth);
        if (wwwAuth.startsWith("WWW-Authenticate:")) {
            wwwAuth = wwwAuth.replaceFirst("WWW-Authenticate:", "");
        }





        // 读取图片文件
        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));

        // 创建连接
        URL url = new URL(serverUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "application/octet-stream");

        // 设置Digest Auth认证信息
        String digestAuth = getDigestAuth(username, password);
        connection.setRequestProperty("Authorization", digestAuth);

        // 发送图片数据
        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(imageBytes);
        }

        // 获取响应状态码
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        // 关闭连接
        connection.disconnect();
    }

    private static String getDigestAuth(String username, String password) {
        String nonce = "your_nonce"; // 从服务器获取随机数
        String realm = "your_realm"; // 从服务器获取领域
        String qop = "auth"; // 使用认证方式
        String algorithm = "MD5"; // 使用MD5算法

        // 构建摘要认证信息
        String digestAuthInfo = String.format("Digest username=\"%s\", realm=\"%s\", nonce=\"%s\", uri=\"/upload\", response=\"%s\", qop=%s, nc=00000001, cnonce=\"%s\", algorithm=%s",
                username, realm, nonce, "", qop, "", algorithm);

        // 对摘要认证信息进行Base64编码
        return "Digest " + Base64.getEncoder().encodeToString(digestAuthInfo.getBytes());
    }
}