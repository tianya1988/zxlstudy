package avro;

import com.alibaba.fastjson.JSONObject;
import org.apache.avro.Schema;
import org.apache.avro.file.CodecFactory;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.*;
import java.net.URI;
import java.util.Iterator;

/**
 * Created by jason on 18-4-3.
 */
public class SchemaOrderTest {

    public static void main(String[] args) throws IOException {
//        WriteLocalFile();
        FileSystem fileSystem = getFileSystem();
        WriteHdfsFile(fileSystem);


    }

    private static void WriteLocalFile() throws IOException {

        String schemaStr = IOUtils.toString(SchemaOrderTest.class.getClassLoader().getResourceAsStream("schema_order_test.json"));
        Schema schema = new Schema.Parser().parse(schemaStr);


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pid", 1);
        jsonObject.put("age", 10);
        jsonObject.put("name", "zhangsan");
        jsonObject.put("address", "aaaaaaaaaaaaaaaaa");

        GenericRecord record = convert(jsonObject, schema);

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("pid", 2);
        jsonObject2.put("age", 20);
        jsonObject2.put("name", "zhangsan2");
        jsonObject2.put("address", "bbbbbbbbbbbbbbbbb");
        GenericRecord record2 = convert(jsonObject2, schema);


        DataFileWriter dataFileWriter = new DataFileWriter(new GenericDatumWriter(schema));
        File file = new File("/home/jason/Desktop/testSchema/testSchemaData1.json");
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        dataFileWriter = dataFileWriter.create(schema, fileOutputStream);
//        dataFileWriter.setCodec(CodecFactory.snappyCodec());
        dataFileWriter.append(record);
        dataFileWriter.append(record2);
        dataFileWriter.flush();
        fileOutputStream.flush();
        dataFileWriter.close();
    }


    private static FileSystem getFileSystem() throws IOException {
        Configuration conf = new Configuration();
        conf.set("ipc.client.connect.timeout", "5000");
        conf.set("ipc.client.connect.max.retries.on.timeouts", "10");
        FileSystem fileSystem = FileSystem.get(URI.create("hdfs://f14cp-p1-zk001"), conf);

        return fileSystem;

    }

    private static void WriteHdfsFile(FileSystem fs) throws IOException {
        String schemaStr = IOUtils.toString(SchemaOrderTest.class.getClassLoader().getResourceAsStream("schema_order_test.json"));
        Schema schema = new Schema.Parser().parse(schemaStr);

        FSDataOutputStream fsDataOutputStream = null;
        DataFileWriter dataFileWriter = null;

        Path path = new Path("/xn/test6.avro");
        boolean exists = fs.exists(path);
        if (!exists) {
            fsDataOutputStream = fs.create(path, true);
        }

        dataFileWriter = new DataFileWriter(new GenericDatumWriter(schema));
//        dataFileWriter.setCodec(CodecFactory.snappyCodec());
        dataFileWriter.create(schema, fsDataOutputStream);


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pid", 1);
//        jsonObject.put("age", 10);
        jsonObject.put("name", "zhangsan");
//        jsonObject.put("address", "aaaaaaaaaaaaaaaaa");
//        jsonObject.put("address2", "aaaaaaaaaaaaaaaaa2222");

        GenericRecord record = convert(jsonObject, schema);

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("pid", 2);
        jsonObject2.put("age", 20);
        jsonObject2.put("name", "zhangsan2");
//        jsonObject2.put("address", "bbbbbbbbbbbbbbbbb");
//        jsonObject2.put("address2", "bbbbbbbbbbbbbbbbb222");
        GenericRecord record2 = convert(jsonObject2, schema);


        dataFileWriter.append(record);
        dataFileWriter.append(record2);
        dataFileWriter.flush();
        fsDataOutputStream.flush();
        dataFileWriter.close();
    }


    private static GenericRecord convert(final Object object, final Schema schema) {
        JSONObject jsonObject = (JSONObject) object;
        GenericRecord genericRecord = new GenericData.Record(schema);
        final Iterator<Schema.Field> iterator = schema.getFields().iterator();
        while (iterator.hasNext()) {
            Schema.Field field = iterator.next();
            final String name = field.name();
            final Schema.Type type = field.schema().getType();
            try {
                if (type == Schema.Type.INT) {
                    genericRecord.put(name, jsonObject.getIntValue(name));
                } else if (type == Schema.Type.LONG) {
                    genericRecord.put(name, jsonObject.getLongValue(name));
                } else if (type == Schema.Type.STRING) {
                    String string = jsonObject.getString(name);
                    if (string == null) {
                        string = "";
                    }
                    genericRecord.put(name, string);
                }
            } catch (Exception e) {
                System.out.println(
                        "name is " + name + ", json is " + jsonObject.toJSONString());
            }
        }
        return genericRecord;
    }

}
