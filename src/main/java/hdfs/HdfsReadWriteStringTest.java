package hdfs;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class HdfsReadWriteStringTest {
    private Configuration conf;
    private FileSystem fs;
    private Map<String, String> properties = new HashMap<String, String>();

    public static void main(String[] args) throws IOException {
        HdfsReadWriteStringTest test = new HdfsReadWriteStringTest();
        String dir = "hdfs://f14cp-p1-zk001:8020/httpstatistic/temp/1526478293879/";

        FileSystem fs2 = test.getFs(dir);
        FileStatus[] fileStatuses = fs2.listStatus(new Path(dir));

        FileSystem fs1 = new HdfsReadWriteStringTest().getFs("hdfs://f14cp-p1-zk001:8020");
        FSDataOutputStream fsDataOutputStream = fs1.create(new Path("/httpstatistic/temp/HttpAgentJieGuo6.csv"));

        for (FileStatus fileStatus : fileStatuses) {
            if (fileStatus.isFile() && fileStatus.getLen() > 0) {
                String filePath = fileStatus.getPath().toString();
                System.out.println(filePath);

                FSDataInputStream fsDataInputStream = fs2.open(new Path(filePath));

                byte[] ioBuffer = new byte[10240];
                int readLen = fsDataInputStream.read(ioBuffer);

                while (readLen != -1) {
                    String s = new String(ioBuffer, 0, readLen);
                    s = s.replace("mt,src_ip,ua,rt","");
//                    System.out.println(s);
                    System.out.println("====");

                    fsDataOutputStream.writeBytes(s);

//                    System.out.write(ioBuffer, 0, readLen);
                    readLen = fsDataInputStream.read(ioBuffer);
                }

            }

        }
        fsDataOutputStream.close();
        IOUtils.closeQuietly(fsDataOutputStream);
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


