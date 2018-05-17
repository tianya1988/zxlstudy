package kafka;

import com.alibaba.fastjson.JSONObject;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.commons.io.IOUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by jason on 17-9-14.
 */
public class ConsumerString {
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put("auto.offset.reset", "smallest"); //必须要加，如果要读旧数据
        props.put("zookeeper.connect", "localhost:2181");
        props.put("group.id", "group4");
        props.put("zookeeper.session.timeout.ms", "40000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");

        //将offset保存到kafka的设置,如果没有此设置,则会将offset保存到zookeeper中
        /**注意这是在旧版本的consumer的用法,在新版本的org.apache.kafka.clients.consumer.KafkaConsumer中
         默认是把offset信息存储在__consumer_offsets这个topic中的**/
        props.put("offsets.storage", "kafka");
        props.put("dual.commit.enabled", "true");

        ConsumerConfig conf = new ConsumerConfig(props);
        ConsumerConnector consumer = kafka.consumer.Consumer.createJavaConsumerConnector(conf);

        String topic = "test-dns-2";
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

        KafkaStream<byte[], byte[]> stream = streams.get(0);
        ConsumerIterator<byte[], byte[]> it = stream.iterator();


        while (it.hasNext()) {
            try {

                byte[] data = it.next().message();
                System.out.println(new String(data));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
