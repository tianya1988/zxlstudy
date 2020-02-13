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
        FileInputStream fileInputStream = new FileInputStream(new File("/home/jason/Desktop/北航.txt"));
        //返回全部内容

        //文件流只能读取一遍，所以下面的方法只能打开一个
        //readAllContent(fileInputStream);
        readByLine(fileInputStream);


        IOUtils.closeQuietly(fileInputStream);
    }

    private static void readByLine(FileInputStream fileInputStream) throws IOException {
        List<String> lines = IOUtils.readLines(fileInputStream);
        for (String line : lines) {
//            System.out.println("line is : " + line);

            if (line.contains("sql注入")) {
                System.out.println("请注意，正在遭受攻击...");
                System.out.println("攻击内容为 ： " + line);
            }
        }
    }

    private static void readAllContent(FileInputStream fileInputStream) throws IOException {
        String content = IOUtils.toString(fileInputStream, "UTF-8");

        System.out.println("文件内容为 ： " + content);
    }
}
