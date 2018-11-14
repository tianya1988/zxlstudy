package kafka;

import com.alibaba.fastjson.JSONObject;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import utils.ByteUtil;
import zookeeper.ZookeeperTemplate;

import java.io.IOException;
import java.util.*;


public class AvroLogConsumerAutoSchema {
    public static void main(String[] args) throws Exception {

        String zkServer = "11.11.60.127:2181";

        Properties props = new Properties();
        props.put("auto.offset.reset", "smallest"); //必须要加，如果要读旧数据
        props.put("zookeeper.connect", zkServer);
        props.put("group.id", "scsc-test");
        props.put("zookeeper.session.timeout.ms", "40000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        props.put("offsets.storage", "zookeeper");
        ConsumerConfig conf = new ConsumerConfig(props);
        ConsumerConnector consumer = kafka.consumer.Consumer.createJavaConsumerConnector(conf);

        String topic = "avro-bj-pro-dns2";
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

        KafkaStream<byte[], byte[]> stream = streams.get(0);
        ConsumerIterator<byte[], byte[]> it = stream.iterator();

        int i = 0;

        while (it.hasNext()) {
            try {

                byte[] data = it.next().message();
                System.out.println("data is : " + Arrays.toString(data));

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
                if (i > 10) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
