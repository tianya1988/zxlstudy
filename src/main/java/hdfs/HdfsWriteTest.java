package hdfs;

import org.apache.avro.file.DataFileStream;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class HdfsWriteTest {
    private Configuration conf;
    private FileSystem fs;
    private Map<String, String> properties = new HashMap<String, String>();

    public static void main(String[] args) throws IOException {
        HdfsWriteTest test = new HdfsWriteTest();
        FSDataInputStream fsDataInputStream = null;
//        String hdfsPath = "/bj/pro/dns/20170817/16/112-898092113420812288.avro";
        String hdfsPath = "/mr-history/zxl4";
        try {

            FileSystem fsTemp = test.getFs("hdfs://11.11.60.127:8020");
            FSDataOutputStream fsDataOutputStream = fsTemp.create(new Path(hdfsPath));
            fsDataOutputStream.writeBytes("zhang");
            fsDataOutputStream.writeBytes("\n");
            fsDataOutputStream.writeBytes("xuelong");
            fsDataOutputStream.writeBytes("\n");
            fsDataOutputStream.writeBytes("test");
            fsDataOutputStream.close();



        } catch (IOException e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(fsDataInputStream);
        }
    }

    public FileSystem getFs(String url) {
        init(url);
        return fs;
    }

    @PostConstruct
    public void init(String url) {
        try {
            if (fs == null) {
                conf = new Configuration();
                conf.set("ipc.client.connect.timeout", "1000");
                conf.set("ipc.client.connect.max.retries.on.timeouts", "1");
                conf.set("dfs.replication", "1");
                conf.setBoolean("fs.hdfs.impl.disable.cache", true);
                if (properties != null) {
                    for (Map.Entry<String, String> entry : properties.entrySet()) {
                        conf.set(entry.getKey(), entry.getValue());
                    }
                }
                fs = FileSystem.get(URI.create(url), conf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


