package base.httpclient;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Map;

public class HttpClientTemplateImpl implements HttpClientTemplate {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientTemplateImpl.class);

    private HttpClientBuilder httpClientBuilder;

    public HttpClientTemplateImpl() {
        this.httpClientBuilder = HttpClients.custom();
    }

    public HttpClientTemplateImpl(HttpClientBuilder httpClientBuilder) {
        this.httpClientBuilder = httpClientBuilder;
    }

    

    @Override
    public HttpResponse get(String url) {
        return get(url, null);
    }

    @Override
    public HttpResponse get(String url, Map<String, String> paras) {
        return request(url, paras, "GET");
    }

    @Override
    public HttpResponse getWithBody(String url, String body) {
        return request(url, body, "GET");
    }

    @Override
    public HttpResponse put(String url, Map<String, String> paras) {
        return request(url, paras, "PUT");
    }

    @Override
    public HttpResponse putWithBody(final String url, final String data) {
        return request(url, data, "PUT");
    }

    @Override
    public HttpResponse post(String url, Map<String, String> paras) {
        return request(url, paras, "POST");
    }

    @Override
    public HttpResponse post(String url, Map<String, String> headerMap, String body) {
        return request(url, headerMap, body, "POST");
    }

    @Override
    public HttpResponse postWithBody(final String url, final String data) {
        return request(url, data, "POST");
    }

    @Override
    public HttpResponse delete(final String url) {
        return request(url, "", "DELETE");
    }


    @Override
    public void downloadToFile(String url, File targetFile) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        OutputStream outputStream = null;
        try {
            RequestConfig config = RequestConfig.custom().setConnectTimeout(3600).setSocketTimeout(36000).build();
            RequestBuilder builder = getRequestBuilder(url, "GET", config);

            HttpUriRequest httpUriRequest = builder.build();
            httpClient = httpClientBuilder.build();
            response = httpClient.execute(httpUriRequest);

            outputStream = new FileOutputStream(targetFile);
            IOUtils.copy(response.getEntity().getContent(), outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(response);
            IOUtils.closeQuietly(httpClient);
        }
    }

    @Override
    public void downloadToFile(String url, File targetFile, int tryTime) {
        while (tryTime > 0) {
            CloseableHttpClient httpClient = null;
            CloseableHttpResponse response = null;
            try {
                RequestConfig config = RequestConfig.custom().setConnectTimeout(3600).setSocketTimeout(10000).build();
                RequestBuilder builder = RequestBuilder.create("GET").setUri(url)
                        .addHeader("Range", "bytes=" + targetFile.length() + "-")
                        .setConfig(config);
                HttpUriRequest httpUriRequest = builder.build();
                httpClient = httpClientBuilder.build();

                response = httpClient.execute(httpUriRequest);
                if (response.getStatusLine().getStatusCode() != 206) {
                    logger.warn("don't support range download");
                    targetFile.deleteOnExit();
                }
                appendContent(response.getEntity().getContent(), targetFile);
                return;
            } catch (IOException e) {
                logger.warn("download failed, {}, try again", e.getMessage());
                tryTime--;
            } finally {
                IOUtils.closeQuietly(response);
                IOUtils.closeQuietly(httpClient);
            }
        }
        throw new RuntimeException("download failed");
    }

    @Override
    public HttpResponse request(String url, Map<String, String> paras, String method) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            RequestBuilder builder = getRequestBuilder(url, method);
            addParameter(paras, builder);

            HttpUriRequest httpUriRequest = builder.build();
            httpClient = httpClientBuilder.build();

            response = httpClient.execute(httpUriRequest);

            return getHttpResponse(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(response);
            IOUtils.closeQuietly(httpClient);
        }
    }

    @Override
    public HttpResponse request(String url, String body, String method) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            RequestBuilder builder = getRequestBuilder(url, method);

            StringEntity parameterEntity = new StringEntity(body, ContentType.create("plain/text", Consts.UTF_8));
            builder.setEntity(parameterEntity);

            HttpUriRequest httpUriRequest = builder.build();
            httpClient = httpClientBuilder.build();

            response = httpClient.execute(httpUriRequest);

            return getHttpResponse(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(response);
            IOUtils.closeQuietly(httpClient);
        }
    }

    private HttpResponse request(String url, Map<String, String> headerMap, String body, String httpMethod) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            RequestBuilder builder = getRequestBuilder(url, httpMethod);

            for (Map.Entry entry : headerMap.entrySet()) {
                builder.setHeader(entry.getKey().toString(), String.valueOf(entry.getValue()));
            }

            StringEntity parameterEntity = new StringEntity(body, "UTF-8");
            builder.setEntity(parameterEntity);

            HttpUriRequest httpUriRequest = builder.build();

            httpClient = httpClientBuilder.build();

            response = httpClient.execute(httpUriRequest);

            return getHttpResponse(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(response);
            IOUtils.closeQuietly(httpClient);
        }
    }

    private void appendContent(InputStream inputStream, File target) throws IOException {
        RandomAccessFile fos = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            if (!target.exists()) {
                boolean mkdirState = target.getParentFile().mkdirs();
                boolean createState = target.createNewFile();
                if (!mkdirState || !createState) {
                    logger.info("mkdir state: {}, create new file state: {}", mkdirState, createState);
                }
            }

            fos = new RandomAccessFile(target, "rw");
            fos.seek(target.length());

            byte[] bytes = new byte[1024];
            int read;
            bufferedInputStream = new BufferedInputStream(inputStream);

            while ((read = bufferedInputStream.read(bytes)) > 0) {
                fos.write(bytes, 0, read);
                if (Thread.interrupted()) {
                    throw new RuntimeException("interrupted");
                }
            }
        } finally {
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(bufferedInputStream);
        }
    }

    private void addParameter(Map<String, String> paras, RequestBuilder builder) {
        if (MapUtils.isNotEmpty(paras)) {
            for (Map.Entry<String, String> entry : paras.entrySet()) {
                builder.addParameter(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
    }

    private RequestBuilder getRequestBuilder(String url, String method) {
        return getRequestBuilder(url, method, 1000);
    }

    private RequestBuilder getRequestBuilder(String url, String method, int connectTimeout) {
        return RequestBuilder.create(method).setUri(url)
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Content-Type", "application/json;charset=UTF-8;")
                .setConfig(RequestConfig.custom().setConnectTimeout(connectTimeout)
                        .setSocketTimeout(120000)
                        .build());
    }

    private RequestBuilder getRequestBuilder(String url, String method, RequestConfig config) {
        return RequestBuilder.create(method).setUri(url)
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Content-Type", "application/json;charset=UTF-8;")
                .setConfig(config);
    }

    private HttpResponse getHttpResponse(CloseableHttpResponse response) throws IOException {
        HttpResponse res = new HttpResponse();
        HttpEntity entity = null;
        try {
            entity = response.getEntity();
            res.setStatusCode(response.getStatusLine().getStatusCode());
            res.setContent(EntityUtils.toString(entity, "UTF-8"));
            return res;
        } finally {
            EntityUtils.consume(entity);
        }
    }
}
