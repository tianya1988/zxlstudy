package base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by jason on 19-8-13.
 */
public class DiGui {

    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File("/home/jason/Downloads/project.json"));


        //返回全部内容
        final String projectJson = IOUtils.toString(fileInputStream, "UTF-8");
//        System.out.println(configJson);

        IOUtils.closeQuietly(fileInputStream);

        JSONObject projectJsonObject = JSON.parseObject(projectJson);


        JSONArray dependencies = projectJsonObject.getJSONArray("dependencies");


        for (Object dependency : dependencies) {
            JSONObject jsonObject = (JSONObject) dependency;
            int i = 0;
            diGui(jsonObject, i);
        }

    }

    public static void diGui(JSONObject jsonObject, int i) {
        String key = jsonObject.getString("key");
        JSONArray properties = jsonObject.getJSONArray("properties");

        //停止
        if (properties == null) {
            return;
        }

        i++;
        for (Object property : properties) {
            JSONObject json = (JSONObject) property;
            String value = json.getString("key");

            if (value.endsWith("host")) {
                String defaultValue = json.getString("default");
                System.out.println(i + " === "  + key + " : " + defaultValue);
            }

            diGui(json, i);
        }
    }
}
