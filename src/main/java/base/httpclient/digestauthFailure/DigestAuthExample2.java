package base.httpclient.digestauthFailure;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DigestAuthExample2 {
    public static void main(String[] args) throws Exception {
        // 图片文件路径
        String imagePath = "D:/项目文件/经验总结/海康/postman以图搜图/刘涛_142330199508200010.jpg";
        // 服务器URL
        String serverUrl = "http://tyjc.scsc.tech:10085/ISAPI/Intelligent/analysisImage/face";

        // 读取图片文件
        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));

        // 创建URL对象
        URL url = new URL(serverUrl);
        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 设置请求方法为POST
        connection.setRequestMethod("POST");
        // 设置允许输出
        connection.setDoOutput(true);
        // 设置允许输入
        connection.setDoInput(true);
        // 设置请求属性Content-Type为multipart/form-data
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
//
//        String login= "username:password";
//        String encoding = Base64.encode(login.getBytes());
//        urlconn.setRequestProperty("Authorization", "Basic "+ encoding);

        // 设置请求属性Authorization为Digest Auth
        connection.setRequestProperty("Authorization", "Digest username=\"admin\",realm=\"ba7f30cc5ca6dfea4700e5b0\", domain=\"/\", qop=\"auth\", nonce=\"dec44af6336b6983:ba7f30cc5ca6dfea4700e5b0:18bee852885:1d3a\", opaque=\"799d5\", algorithm=\"\"MD5\"\", stale=\"FALSE\",uri=\"/ISAPI/Intelligent/analysisImage/face\",nc=\"00000001\",cnonce=\"ze443h12\",response=\"a7dbaa5ba186f52caf6a272e882917f8\"");

        // 写入请求体
        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write("------WebKitFormBoundary7MA4YWxkTrZu0gW".getBytes());
                    outputStream.write("Content-Disposition: form-data; name=\"image\"; filename=\"".getBytes());
            outputStream.write(imagePath.substring(imagePath.lastIndexOf("/") + 1).getBytes());
            outputStream.write(" ".getBytes());
                    outputStream.write("Content-Type: application/octet-stream".getBytes());
                            outputStream.write(imageBytes);
            outputStream.write(" ".getBytes());
                    outputStream.write("------WebKitFormBoundary7MA4YWxkTrZu0gW--".getBytes());
        }

        // 获取响应状态码
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);
        System.out.println("Response Message: " + connection.getResponseMessage());

        // 读取响应内容
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            System.out.println("Response: " + response.toString());
        }
    }
}