package kafka;

import com.alibaba.fastjson.JSONObject;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import utils.ByteUtil;
import zookeeper.ZookeeperTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by jason on 18-1-9.
 */
public class KafkaConsumerDemoWithSchema {

    public static void main(String[] args) throws InterruptedException {
        String zkServer = "11.11.127.1:2181";
        String kafkaServer = "11.11.127.40:6667";
        String destTopic = "avro-bj-pro-dns2-test";

        Properties props = new Properties();
        props.put("auto.offset.reset", "earliest");
        // props.put("bootstrap.servers", "11.59.1.176:6667");
        props.put("bootstrap.servers", kafkaServer);
        props.put("group.id", "test-zxl7");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("max.partition.fetch.bytes", "15000");
        props.put("max.poll.record", "10");

        props.put("key.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put("offsets.storage", "kafka");// org.apache.kafka.clients.consumer在此包中,只能往kafka存储,即使配置成zookeeper,也是只保存offset到kafka.
        KafkaConsumer<byte[], byte[]> consumer = new KafkaConsumer<byte[], byte[]>(props);
        consumer.subscribe(Arrays.asList(destTopic));
        while (true) {
            ConsumerRecords<byte[], byte[]> records = consumer.poll(100);
            for (ConsumerRecord<byte[], byte[]> kafkaRecord : records) {
                byte[] data = kafkaRecord.value();
                int schemaId = ByteUtil.littleEndianToInt(data, 0);
                System.out.println("schemaId is : " + schemaId);

                ZookeeperTemplate zookeeperTemplate = new ZookeeperTemplate();
                zookeeperTemplate.setZkServer(zkServer);

                String schemaStr = zookeeperTemplate.readData("/cnpc/schema/avro/" + schemaId);

                Schema schema = new Schema.Parser().parse(schemaStr);


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
                } catch (IOException ioe) {
                    throw new RuntimeException(ioe);
                }
                zookeeperTemplate.close();

            }
            consumer.commitAsync();
        }

    }

}
