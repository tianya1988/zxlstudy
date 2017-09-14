package utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;

import java.util.Iterator;

/**
 * Created by jason on 17-9-14.
 */
public class AvroUtils {
    public static GenericRecord convert(final Object object, final Schema schema) {
        JSONObject jsonObject = (JSONObject) object;
        GenericRecord genericRecord = new GenericData.Record(schema);
        final Iterator<Schema.Field> iterator = schema.getFields().iterator();
        while (iterator.hasNext()) {
            Schema.Field field = iterator.next();
            final String name = field.name();
            final Schema.Type type = field.schema().getType();
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
            } else {
                throw new RuntimeException("Not supported field");
            }
        }
        return genericRecord;
    }
}
