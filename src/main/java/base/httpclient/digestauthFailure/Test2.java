package base.httpclient.digestauthFailure;

import java.io.*;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Test2 {
    public static void main(String[] args) throws Exception {
        // 设置目标URL和用户名密码
        String url = "http://tyjc.scsc.tech:10085/ISAPI/Intelligent/analysisImage/face";
        String imagePath = "D:/项目文件/经验总结/海康/postman以图搜图/刘涛_142330199508200010.jpg";
        String username = "admin";
        String password = "qwer1234";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建HttpClient对象
        /*CloseableHttpClient httpClient = HttpClients.createDefault();
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, credentialsProvider);*/

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
            httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
            // 添加Digest Auth认证
            credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));


        // 创建HttpPost对象
        HttpPost httpPost = new HttpPost(url);

        // 设置Digest Auth认证信息
//        String digestAuthHeaderValue = "Digest username=\"" + username + "\", realm=\"example.com\", nonce=\"dcd98b7102dd2f0e8b11d0f600bfb0c093\", uri=\"/ISAPI/Intelligent/analysisImage/face\", response=\"6629fae49393a05397450978507c4ef1\"";
//        httpPost.setHeader("Authorization", digestAuthHeaderValue);

        // 创建MultipartEntityBuilder对象，用于构建包含图片的请求体
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        builder.addBinaryBody("image", new File(imagePath), ContentType.DEFAULT_BINARY, "image.jpg");
        builder.addBinaryBody("image", new File(imagePath), ContentType.IMAGE_JPEG, "image.jpg");
        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);

        // 发送请求并获取响应
        CloseableHttpResponse response = httpClient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity());

        // 输出响应信息
        System.out.println("Status Code: " + statusCode);
        System.out.println("Response Body: " + responseBody);

        // 关闭资源
        response.close();
        httpClient.close();
    }
}
