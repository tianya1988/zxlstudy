package base.file;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by jason on 17-7-20.
 */
public class ReadExternalFileByLine {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File("/home/jason/Desktop/ElasticsearchTemplateTest.scala"));
        //按行返回
        List<String> lines = IOUtils.readLines(fileInputStream);
        for (String line : lines) {
            System.out.println(line);
        }

        IOUtils.closeQuietly(fileInputStream);
    }
}
