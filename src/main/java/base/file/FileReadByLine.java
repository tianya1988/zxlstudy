package base.file;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.time.DateFormatUtils;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

/**
 *
 * Created by jason on 17-6-27.
 */
public class FileReadByLine {
    public static void main(String[] args) throws Exception {
        File dir = new File("/home/jason/document/events");

        File[] files = dir.listFiles();

        File outFile = new File("/home/jason/document/output.txt");

        FileWriter writer = new FileWriter(outFile);

        for (File file : files) {

            if(file.isDirectory()) continue;

            LineIterator it = FileUtils.lineIterator(file, "UTF-8");


                while (it.hasNext()) {
                    String line = it.nextLine();
                    JSONObject json = JSON.parseObject(line);
                    JSONObject newJSON = new JSONObject();
                    newJSON.put("src_ip", json.getString("src_ip"));
                    newJSON.put("dst_ip", json.getString("dst_ip"));
                    newJSON.put("time_received", DateFormatUtils.format(new Date(json.getLong("time_received")), "yyyy-MM-dd HH:mm:ss"));
                    newJSON.put("http_dns", json.getString("http_dns"));
                    writer.write(JSON.toJSONString(newJSON) + "\r\n");
                }


                LineIterator.closeQuietly(it);
        }

        writer.close();



    }
}
