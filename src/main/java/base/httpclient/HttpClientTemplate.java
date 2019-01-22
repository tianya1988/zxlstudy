package base.httpclient;

import java.io.File;
import java.util.Map;

/**
 * Created by jason on 18-4-8.
 */
public interface HttpClientTemplate {
    HttpResponse get(String url);

    HttpResponse get(String url, Map<String, String> paras);

    HttpResponse getWithBody(String url, String body);

    HttpResponse put(String url, Map<String, String> paras);

    HttpResponse putWithBody(String url, String body);

    HttpResponse post(String url, Map<String, String> paras);

    HttpResponse post(String url, Map<String, String> headerMap, String body);

    HttpResponse postWithBody(String url, String body);

    HttpResponse delete(String url);

    void downloadToFile(String url, File file);

    void downloadToFile(String url, File targetFile, int tryTime);

    HttpResponse request(String url, Map<String, String> paras, String method);

    HttpResponse request(String url, String body, String method);

}
