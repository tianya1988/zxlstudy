package kafka;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * Created by jason on 17-9-14.
 */
public class ProducerString {
    public static void main(String[] args) throws IOException, InterruptedException {
        String bootstrapServers = "172.16.0.113:6667";

        InputStream inputStream = ProducerString.class.getClassLoader().getResourceAsStream("alert/dns.json");
        String dnsStr = IOUtils.toString(inputStream);
        System.out.println(dnsStr);
        JSONObject dnsJsonObject = JSONObject.parseObject(dnsStr);

        ProducerRecord<String, String> record = new ProducerRecord<String, String>("test2", "zxl", dnsJsonObject.toJSONString());

        System.out.println("=======" + dnsJsonObject.toJSONString().getBytes().length);

        Properties kafkaProps = new Properties();
        kafkaProps.put(ProducerConfig.ACKS_CONFIG, "all");
        //Defaults overridden based on config
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        kafkaProps.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 10000);
        kafkaProps.put("compression.type", "gzip");

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(kafkaProps);
        int i = 0;
        while (i < 5) {
            Future<RecordMetadata> send = producer.send(record);
            Thread.sleep(10);
            i++;

        }
        producer.flush();


    }
}
