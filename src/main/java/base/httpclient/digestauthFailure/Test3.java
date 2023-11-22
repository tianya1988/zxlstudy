package base.httpclient.digestauthFailure;


import cn.hutool.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Test3 {


    public static void main(String[] args) throws Exception {
        reqFaceInfo("admin", "qwer1234");
    }

    private static List<JSONObject> reqFaceInfo(String username, String password) {
        List<cn.hutool.json.JSONObject> list = null;
        String url = "http://tyjc.scsc.tech:10085/ISAPI/Intelligent/analysisImage/face";
        String imagePath = "D:/项目文件/经验总结/海康/postman以图搜图/刘涛_142330199508200010.jpg";
        CloseableHttpClient httpClient = null;
        try {
            URI serverURI = new URI(url);

            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
            // 添加Digest Auth认证
            credsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(username, password));

            HttpPost post = new HttpPost(url);
//            post.setHeader("Content-type", "application/octet-stream");
            post.setHeader("Content-type", "image/jpeg");

            post.setHeader("accept", "*/*");
            post.setHeader("Connection", "keep-alive");
            post.setHeader("user-agent", "PostmanRuntime/7.35.0");



            // 通过Binary方式传入照片
            File file = new File(imagePath);
            byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));

//            post.setHeader("Content-length", imageBytes.length+"");不能加

            HttpEntity entity = MultipartEntityBuilder.create().addBinaryBody("file", imageBytes, ContentType.IMAGE_JPEG, file.getName()).build();

            // Cannot retry request with a non-repeatable request entity.
            // HttpEntity entity = MultipartEntityBuilder.create().addBinaryBody("file", new ByteArrayInputStream(Base64.encode(Files.readAllBytes(Paths.get(imagePath)))), ContentType.IMAGE_JPEG, file.getName()).build();
            // HttpEntity entity = MultipartEntityBuilder.create().addBinaryBody("file", new ByteArrayInputStream(Base64.encode(Files.readAllBytes(Paths.get(imagePath)))), ContentType.DEFAULT_BINARY, file.getName())*//*

            post.setEntity(entity);
/*
            File file = new File(imagePath);
            System.out.println(file.getName());
            HttpEntity entity = MultipartEntityBuilder.create()
//                    .addBinaryBody("file", file, ContentType.MULTIPART_FORM_DATA, file.getName())
//                    .addBinaryBody("file", file, ContentType.IMAGE_JPEG, file.getName())
//                    .addBinaryBody("file", file, ContentType.DEFAULT_BINARY, file.getName())


                    *//*.addBinaryBody("file", new ByteArrayInputStream(Base64.encode(Files.readAllBytes(Paths.get(imagePath)))), ContentType.DEFAULT_BINARY, file.getName())*//*
                    .build();
            post.setEntity(entity);*/

            HttpResponse response = httpClient.execute(post);
            String result = EntityUtils.toString(response.getEntity());
            httpClient.close();
            System.out.println("result:" + result);

            //list = parseFaceXml(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

}