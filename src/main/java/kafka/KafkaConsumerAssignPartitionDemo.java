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
import org.apache.kafka.common.TopicPartition;
import utils.ByteUtil;
import zookeeper.ZookeeperTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by jason on 18-1-9.
 */
public class KafkaConsumerAssignPartitionDemo {

    public static void main(String[] args) throws InterruptedException {
        String kafkaServer = "11.11.60.127:6667";
        String zkServer = "11.11.60.127:2181";

        Properties props = new Properties();
        props.put("auto.offset.reset", "earliest");
        // props.put("bootstrap.servers", "11.59.1.176:6667");
        props.put("bootstrap.servers", kafkaServer);
//        props.put("group.id", "test-zxl7");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("max.partition.fetch.bytes", "15000");
        props.put("max.poll.record", "10");

        props.put("key.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put("offsets.storage", "kafka");// org.apache.kafka.clients.consumer在此包中,只能往kafka存储,即使配置成zookeeper,也是只保存offset到kafka.
        KafkaConsumer<byte[], byte[]> consumer = new KafkaConsumer<byte[], byte[]>(props);

        //指定分区
        TopicPartition partition1 = new TopicPartition("avro-cp-log-fw", 2);

        ArrayList<TopicPartition> partitionArrayList = new ArrayList<TopicPartition>();
        partitionArrayList.add(partition1);

        consumer.assign(partitionArrayList);

        ZookeeperTemplate zookeeperTemplate = new ZookeeperTemplate();
        zookeeperTemplate.setZkServer(zkServer);
        HashMap<Integer, Schema> schemaHashMap = new HashMap<Integer, Schema>();


        while (true) {
            ConsumerRecords<byte[], byte[]> messages = consumer.poll(100);
            for (ConsumerRecord<byte[], byte[]> message : messages) {
                Thread.sleep(100);
                System.out.printf("partition=%d, offset = %d, key = %s, value = %s%n", message.partition(), message.offset(), message.key(), message.value());

                byte[] data = message.value();
                int schemaId = ByteUtil.littleEndianToInt(data, 0);
                System.out.println("schemaId is : " + schemaId);

                if (!schemaHashMap.containsKey(schemaId)) {
                    String schemaStr = zookeeperTemplate.readData("/cnpc/schema/avro/" + schemaId);
                    Schema schema = new Schema.Parser().parse(schemaStr);
                    schemaHashMap.put(schemaId, schema);
                }

                Schema schema = schemaHashMap.get(schemaId);
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
            }
            consumer.commitAsync();
        }

    }

}
