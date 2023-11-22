package xionganjianguan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import okhttp3.*;
import org.springframework.util.DigestUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.*;

public class HttpToken {

    public static void main(String[] args) throws UnsupportedEncodingException {
//        test();

        //启动区体育中心项目标段   KEY: 90158579EDB2407CA8336580182896C3
        // 密钥：434414B7D7114B2D9164B64EA67D06E2
        /* // 获取token
        String appKey = "90158579EDB2407CA8336580182896C3";
        AccessTokenRequest accessTokenRequest = new AccessTokenRequest();
        accessTokenRequest.setAppKey(appKey);
        String token = getToken(accessTokenRequest);
        System.out.println("获取token的响应：" + token);
        */

        String accessToken = "a90f41e3302447f892f3c531db577879";

        String responseString =  getLabourPerson(accessToken);
        System.out.println("劳务人员数据 ： " + responseString);

    }

    private static String getLabourPerson(String accessToken) throws UnsupportedEncodingException {
        //启动区体育中心项目标段   KEY: 90158579EDB2407CA8336580182896C3
        // 密钥：434414B7D7114B2D9164B64EA67D06E2

        String appSecret = "434414B7D7114B2D9164B64EA67D06E2";
        String host = "http://144.7.111.172:8083";

        String nonceStr = UUID.randomUUID().toString().replace("-", "");
        Long stamp = System.currentTimeMillis() / 1000L;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("noncestr", nonceStr);
        parameters.put("timestamp", String.valueOf(stamp));
        parameters.put("token", accessToken);

        String bodyString = "{}";
        String sign = sign(appSecret, parameters, bodyString);
        parameters.put("sign", sign);

        String url = host + "/api/open/labour/person";
        url = toUrlString(url, parameters);
        System.out.println("request url is : " + url);

        try {
            MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON_TYPE, bodyString);
            System.out.println("request body is : " + bodyString);
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            return responseString;
        } catch (UnknownHostException err) {
            return err.getMessage();
        } catch (Exception err) {
            return err.getMessage();
        }
    }

    private static void test() throws UnsupportedEncodingException {
        String noncestr = UUID.randomUUID().toString().replace("-", "");
        System.out.println(noncestr);

        long timestamp = System.currentTimeMillis() / 1000L;

        String appKey = "90158579EDB2407CA8336580182896C3";
        String appSecret = "434414B7D7114B2D9164B64EA67D06E2";

        JSONObject appKeyJsonObject = new JSONObject();
        appKeyJsonObject.put("appKey", appKey);

        String appKeyJsonStr = appKeyJsonObject.toString();
        System.out.println(appKeyJsonStr);

        String appKeyJsonStrURL = URLEncoder.encode(appKeyJsonStr, "UTF-8");
        System.out.println(appKeyJsonStrURL);

        String body = appKeyJsonStrURL + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&key=" + appSecret;

        String sign = DigestUtils.md5DigestAsHex(body.toString().getBytes()).toUpperCase();

        System.out.println(sign);
    }

    private static String getToken(AccessTokenRequest model) throws UnsupportedEncodingException {

        //启动区体育中心项目标段   KEY: 90158579EDB2407CA8336580182896C3
        // 密钥：434414B7D7114B2D9164B64EA67D06E2

        String appSecret = "434414B7D7114B2D9164B64EA67D06E2";
        String host = "http://144.7.111.172:8083";

        String nonceStr = UUID.randomUUID().toString().replace("-", "");
        Long stamp = System.currentTimeMillis() / 1000L;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("noncestr", nonceStr);
        parameters.put("timestamp", String.valueOf(stamp));

        String bodyString = JSON.toJSONString(model);
        String sign = sign(appSecret, parameters, bodyString);
        parameters.put("sign", sign);

        String url = host + "/api/open/token/get";
        url = toUrlString(url, parameters);
        System.out.println("request url is : " + url);

        try {
            MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON_TYPE, bodyString);
            System.out.println("request body is : " + bodyString);
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            return responseString;
        } catch (UnknownHostException err) {
            return err.getMessage();
        } catch (Exception err) {
            return err.getMessage();
        }
    }

    private static String sign(String appSecret, Map<String, String> parameters,
                               String bodyString) throws UnsupportedEncodingException {

        SortedMap<String, String> hash = new TreeMap<>();

        // parameters 包含参数 noncestr timestamp
        for (String key : parameters.keySet()) {
            hash.put(key, parameters.get(key));
        }

        // bodyString 包含参数 appKey;
        hash.put("body", bodyString);

        StringBuffer sb = new StringBuffer();

        Set entrySet = hash.entrySet();
        Iterator it = entrySet.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k))
            {
                sb.append(k + "=" + URLEncoder.encode(v, "UTF-8") + "&");
            }
        }
        sb.append("key=" + appSecret);
        String sign = DigestUtils.md5DigestAsHex(sb.toString().getBytes()).toUpperCase();
        return sign;
    }


    public static String toUrlString(String url, Map<String, String> params) {
        String paraString = toParameterString(params);
        if (paraString.equals("")) {
            return url;
        } else {
            return url + "?" + paraString;
        }
    }
    private static String toParameterString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        try {
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                String value = params.get(key);
                if (i == keys.size() - 1) {
                    prestr = prestr + key + "=" + URLEncoder.encode(value, "UTF-8");
                } else {
                    prestr = prestr + key + "=" + URLEncoder.encode(value, "UTF-8")
                            + "&";
                }
            }
        } catch (Exception err) {
        }
        return prestr;
    }
}
