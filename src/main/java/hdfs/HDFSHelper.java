package hdfs;

import com.google.common.collect.Maps;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileStream;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Map;

/**
 * Created by jason on 17-5-24.
 */
public class HDFSHelper {
    public static void main(String[] args) throws Exception {
        FileSystem fileSystem = getFileSystem("hdfs://f14cp-sc-hdp002:8020");
        System.out.println(fileSystem);
        Map<String, Long> allFilePath = getAllFilePath(fileSystem, "/bj/flow/aside/2017-05-23");
        System.out.println(allFilePath);


        String path = "http://11.11.184.182:9000/bj-flow-aside-hdfs.json";
        InputStream in = new URL(path).openStream();
        Schema schema = new Schema.Parser().parse(in);
        long logSizeByFilePath = getLogSizeByFilePath(fileSystem, "/bj/flow/aside/2017-05-23/SparkData.f14cp-sc-hdp029.1495517851337.avro", schema);
        System.out.println(logSizeByFilePath);
    }

    public static Map<String, Long> getAllFilePath(FileSystem fileSystem, String dirPath) {
        FileStatus[] listStatus = null;
        Map<String, Long> filePathList = Maps.newHashMap();
        try {
            listStatus = fileSystem.listStatus(new Path(dirPath));
            for (FileStatus fileStatus : listStatus) {
                filePathList.put(fileStatus.getPath().toString(), fileStatus.getLen());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePathList;
    }


    public static long getLogSizeByFilePath(FileSystem fileSystem, String filePath, Schema schema) throws Exception {
        Path destFile = new Path(filePath);
        InputStream is = fileSystem.open(destFile);
        GenericRecord record = null;
        long count = 0l;
        if (is.available() > 0) {
            DatumReader<Object> userDatumReader = new SpecificDatumReader<Object>(schema);
            DataFileStream<Object> dataFileReader = new DataFileStream(is, userDatumReader);
            while (dataFileReader.hasNext()) {
                count += dataFileReader.getBlockCount();
                dataFileReader.nextBlock();
            }
            IOUtils.closeQuietly(dataFileReader);
        }
        IOUtils.closeQuietly(is);
        return count;
    }


    public static FileSystem getFileSystem(String hdfsPath) {
        FileSystem fileSystem = null;
        try {
            Configuration configuration = new Configuration();
            configuration.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
            fileSystem = FileSystem.get(new URI(hdfsPath), configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileSystem;
    }


}
