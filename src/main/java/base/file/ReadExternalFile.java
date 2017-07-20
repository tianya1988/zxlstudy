package base.file;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by jason on 17-7-20.
 */
public class ReadExternalFile {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File("/home/jason/Desktop/ElasticsearchTemplateTest.scala"));
        //返回全部内容
        final String schemaString = IOUtils.toString(fileInputStream, "UTF-8");
        System.out.println(schemaString);

        IOUtils.closeQuietly(fileInputStream);
    }
}
