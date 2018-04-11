package base.file;

import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * Created by jason on 18-4-9.
 */
public class WriteExternalFile {
    public static void main(String[] args) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(new File("/home/jason/Desktop/rich.txt"));
        IOUtils.write("qwtyuio222", outputStream);
        IOUtils.write("\r\n", outputStream);
        IOUtils.write("qwtyuio333", outputStream);
        IOUtils.closeQuietly(outputStream);


    }
}
