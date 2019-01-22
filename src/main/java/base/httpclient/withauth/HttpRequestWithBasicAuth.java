package base.httpclient.withauth;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * Created by jason on 19-1-22.
 */
public class HttpRequestWithBasicAuth {

    public static void main(String args[]) {

        String url = "http://11.11.110.1:8097/api/data-flow-monitor/total-storage";

        RestTemplateBuilder builder = new RestTemplateBuilder();
        RestTemplate restTemplate = builder
                .basicAuthorization("admin", "secret+3sCnpc")
                .build();

        String content = restTemplate.getForObject(url, String.class);
        System.out.println(content);

    }

}
