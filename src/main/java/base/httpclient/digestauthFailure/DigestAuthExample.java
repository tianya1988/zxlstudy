package base.httpclient.digestauthFailure;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class DigestAuthExample {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        String url = "http://tyjc.scsc.tech:10085/ISAPI/Intelligent/analysisImage/face";
        String username = "admin";
        String password = "qwer1234";


        String binaryData = "your-binary-data";

        // 计算摘要
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(password.getBytes(StandardCharsets.UTF_8));
        byte[] hashedPassword = digest.digest();
        String encodedPassword = Base64.getEncoder().encodeToString(hashedPassword);

        // 创建连接
        URL urlObj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Digest username=\"" + username + "\", realm=\"your-realm\", nonce=\"your-nonce\", uri=\"/your-endpoint\", response=\"your-response\", opaque=\"your-opaque\"");
        connection.setRequestProperty("Content-Type", "application/octet-stream");

        // 发送二进制数据
        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(binaryData.getBytes(StandardCharsets.UTF_8));
        }

        // 获取响应
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
