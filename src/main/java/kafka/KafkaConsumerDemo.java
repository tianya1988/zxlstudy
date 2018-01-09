package kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * Created by jason on 18-1-9.
 */
public class KafkaConsumerDemo {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("auto.offset.reset", "earliest");
        props.put("bootstrap.servers", "11.11.60.127:6667");
        props.put("group.id", "test-zxl7");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("offsets.storage", "kafka");// org.apache.kafka.clients.consumer在此包中,只能往kafka存储,即使配置成zookeeper,也是只保存offset到kafka.
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList("asap-agent-command"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("record=%d, offset = %d, key = %s, value = %s%n", record.partition(), record.offset(), record.key(), record.value());
            consumer.commitAsync();
        }

    }

}
