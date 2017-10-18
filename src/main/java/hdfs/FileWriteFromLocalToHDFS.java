package hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jason on 17-10-18.
 */
public class FileWriteFromLocalToHDFS {

    private Configuration conf;
    private FileSystem fs;
    private Map<String, String> properties = new HashMap<String, String>();

    public static void main(String[] args) throws Exception {

//Source file in the local file system
        String localSrc = "/home/jason/Downloads/avro-dns-to-es-spark-agent.properties";
//Destination file in HDFS
        String dst = "/mr-history/zxl5";

//Input stream for the file in local file system to be written to HDFS
        InputStream in = new BufferedInputStream(new FileInputStream(localSrc));

//Get configuration of Hadoop system
        Configuration conf = new Configuration();
        System.out.println("Connecting to -- " + conf.get("fs.defaultFS"));

//Destination file in HDFS
        FileSystem fs = new FileWriteFromLocalToHDFS().getFs("hdfs://11.11.60.127:8020");
        OutputStream out = fs.create(new Path(dst));

//Copy file from local to HDFS
        IOUtils.copyBytes(in, out, 4096, true);

        System.out.println(dst + " copied to HDFS");

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
