package avro;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;

import java.io.File;
import java.io.IOException;

/**
 * Created by jason on 18-4-11.
 */
public class ReadLocalAvroFile {
    public static void main(String[] args) throws Exception {

        Schema schema = new Schema.Parser().parse(new File("/home/jason/Desktop/testSchema/schema_order_test.json"));

        DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(schema);
        DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(new File("/home/jason/Desktop/testSchema/testSchemaData1.json"), datumReader);
        GenericRecord record = null;
        while (dataFileReader.hasNext()) {

            record = dataFileReader.next(record);
            System.out.println(record);
        }

    }
}
