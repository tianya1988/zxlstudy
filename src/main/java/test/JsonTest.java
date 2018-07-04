package test;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by jason on 18-5-29.
 */
public class JsonTest {
    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key1", 0);
        jsonObject.put("key1", true);
        System.out.println(jsonObject.toJSONString());
    }
}
