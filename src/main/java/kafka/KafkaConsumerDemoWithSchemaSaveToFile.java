package kafka;

import com.alibaba.fastjson.JSONObject;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.commons.io.IOUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import utils.ByteUtil;
import zookeeper.ZookeeperTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by jason on 18-1-9.
 */
public class KafkaConsumerDemoWithSchemaSaveToFile {

    public static void main(String[] args) throws InterruptedException {

        if (args.length < 5) {
            System.out.printf("Please input: kafkaServer  destTopic groupId schemaPath saveDataFilePath");
        }
        /*String kafkaServer = "localhost:9092";
        String destTopic = "bj-pro-dns";
        String groupId = "test-zxl003";
        String schemaPath = "/home/jason/project/zxlstudy/src/main/resources/schemas/10000.json";
        String saveDataFilePath = "/home/jason/Desktop/bj-pro-dns.txt";*/

        String kafkaServer = args[0];
        String destTopic = args[1];
        String groupId = args[2];
        String schemaPath = args[3];
        String saveDataFilePath = args[4];

        Properties props = new Properties();
        props.put("auto.offset.reset", "earliest");
        props.put("bootstrap.servers", kafkaServer);
        props.put("group.id", groupId);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("max.partition.fetch.bytes", "15000");
        props.put("max.poll.record", "10");

        props.put("key.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put("offsets.storage", "kafka");// org.apache.kafka.clients.consumer在此包中,只能往kafka存储,即使配置成zookeeper,也是只保存offset到kafka.
        KafkaConsumer<byte[], byte[]> consumer = new KafkaConsumer<byte[], byte[]>(props);
        consumer.subscribe(Arrays.asList(destTopic));


        Schema schema = null;
        try {
            schema = new Schema.Parser().parse(new File(schemaPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(schema);

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(saveDataFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            ConsumerRecords<byte[], byte[]> records = consumer.poll(100);
            for (ConsumerRecord<byte[], byte[]> kafkaRecord : records) {
                byte[] data = kafkaRecord.value();
                int schemaId = ByteUtil.littleEndianToInt(data, 0);
                System.out.println("schemaId is : " + schemaId);

                final GenericDatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>(schema);
                final BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(data, 4, data.length - 4, null);
                try {
                    GenericRecord genericRecord = reader.read(null, decoder);
                    JSONObject record = new JSONObject();
                    final Iterator<Schema.Field> iterator = schema.getFields().iterator();
                    while (iterator.hasNext()) {
                        final Schema.Field field = iterator.next();
                        final String name = field.name();
                        if (field.schema().getType() == Schema.Type.STRING) {
                            record.put(name, genericRecord.get(name).toString());
                        } else {
                            record.put(name, genericRecord.get(name));
                        }
                    }
                    System.out.println("json is : " + record.toJSONString());

                    IOUtils.write(record.toJSONString(), outputStream);
                    IOUtils.write("\r\n", outputStream);
                } catch (IOException ioe) {
                    throw new RuntimeException(ioe);
                }

            }
            consumer.commitAsync();
        }
    }

}
