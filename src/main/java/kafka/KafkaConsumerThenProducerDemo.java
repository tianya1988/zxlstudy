package kafka;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by jason on 18-1-9.
 */
public class KafkaConsumerThenProducerDemo {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("auto.offset.reset", "latest");
        // props.put("bootstrap.servers", "11.59.1.176:6667");
        props.put("bootstrap.servers", "172.16.0.208:9092");
        props.put("group.id", "test-02");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("max.partition.fetch.bytes", "1500000");
        props.put("max.poll.record", "10");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("offsets.storage", "kafka");// org.apache.kafka.clients.consumer在此包中,只能往kafka存储,即使配置成zookeeper,也是只保存offset到kafka.

        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList("scsc-pro-other"));
        String lineSeparator = System.getProperty("line.separator", "\n");

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(props);

        long errorMessageCount = 0l;

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                record.timestamp();
//                System.out.printf("topic = %s, record=%d, offset = %d, key = %s, value = %s%n", record.topic(), record.partition(), record.offset(), record.key(), record.value());

                JSONObject singleMessage = null;

                String[] lines = record.value().split(lineSeparator);
                for (String line : lines) {
                    try {
                        singleMessage = JSONObject.parseObject(line);
                    } catch (Exception e) {
                        errorMessageCount++;
                        System.out.println("error message count total : " + errorMessageCount);
                    }

                    if (singleMessage == null) {
                        continue;
                    }

                    String transportLayerProtocol = null;
                    String applicationLayerProtocol = null;

                    int protocolCnt = singleMessage.getIntValue("protocolCnt");
                    if (protocolCnt >= 2) {
                        JSONArray protocolArray = singleMessage.getJSONArray("protocol");
                        for (Object o : protocolArray) {
                            String protocol = (String) o;
                            if ("http".equals(protocol)) {
                                applicationLayerProtocol = "http";
                            } else if ("dns".equals(protocol)) {
                                applicationLayerProtocol = "dns";
                            } else if ("tls".equals(protocol)) {
                                applicationLayerProtocol = "tls";
                            } else if ("ssh".equals(protocol)) {
                                applicationLayerProtocol = "ssh";
                            } else if ("tcp".equals(protocol)) {
                                transportLayerProtocol = "tcp";
                            } else if ("udp".equals(protocol)) {
                                transportLayerProtocol = "udp";
                            }
                        }
                    }

                    singleMessage.put("log-id", UUID.randomUUID());
                    singleMessage.put("handleTime", System.currentTimeMillis());

                    if (StringUtils.isNotEmpty(transportLayerProtocol)) {
                        if (StringUtils.isNotEmpty(applicationLayerProtocol)) {
                            kafkaProducer.send(new ProducerRecord<String, String>("scsc-pro-" + applicationLayerProtocol, singleMessage.toJSONString()));
                            singleMessage.remove(applicationLayerProtocol);
                            kafkaProducer.send(new ProducerRecord<String, String>("scsc-pro-" + transportLayerProtocol, singleMessage.toJSONString()));
                        }
                    }
                }
            }
            consumer.commitAsync();
        }

    }

}
