package base.file;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by jason on 17-7-20.
 */
public class ReadResourceFile {
    public static void main(String[] args) throws IOException {
        InputStream inputStream = ReadResourceFile.class.getClassLoader().getResourceAsStream("dns-new-schema.json");
        //所有内容
        final String schemaString = IOUtils.toString(inputStream, "UTF-8");
        System.out.println(schemaString);

        IOUtils.closeQuietly(inputStream);
    }
}
