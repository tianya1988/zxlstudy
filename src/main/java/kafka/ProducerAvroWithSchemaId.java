package kafka;

import com.alibaba.fastjson.JSONObject;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.commons.io.IOUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import utils.AvroUtils;
import utils.ByteUtil;
import zookeeper.ZookeeperTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Created by jason on 17-9-14.
 */
public class ProducerAvroWithSchemaId {
    public static void main(String[] args) throws IOException {
        int schemaId = 10029;
        byte[] schemaIdByte = ByteUtil.littleEndian(schemaId);
        String zkServer = "11.11.60.127:2181";

        InputStream inputStream = ProducerAvroWithSchemaId.class.getClassLoader().getResourceAsStream("dns.json");
        String dnsStr = IOUtils.toString(inputStream);
        System.out.println(dnsStr);
        JSONObject dnsJsonObject = JSONObject.parseObject(dnsStr);

        Schema schema = getSchema(schemaId, zkServer);
        System.out.println(schema);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(schemaIdByte);

        GenericDatumWriter<GenericRecord> writer = new GenericDatumWriter<GenericRecord>(schema);

        GenericRecord genericRecord = AvroUtils.convert(dnsJsonObject, schema);
        BinaryEncoder encoder = EncoderFactory.get().directBinaryEncoder(outputStream, null);
        writer.write(genericRecord, encoder);
        encoder.flush();

        byte[] resultBytes = outputStream.toByteArray();
        ProducerRecord<String, byte[]> record = new ProducerRecord<String, byte[]>("test-topic", null, resultBytes);

        Properties kafkaProps = new Properties();
        kafkaProps.put(ProducerConfig.ACKS_CONFIG, "all");
        //Defaults overridden based on config
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
        kafkaProps.put("compression.type", "snappy");
        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "11.0.44.114:9092");

        KafkaProducer<String, byte[]> producer = new KafkaProducer<String, byte[]>(kafkaProps);

        producer.send(record);
        producer.flush();


    }

    private static Schema getSchema(int schemaId, String zkServer) {
        ZookeeperTemplate zookeeperTemplate = new ZookeeperTemplate();
        zookeeperTemplate.setZkServer(zkServer);

        String schemaStr = zookeeperTemplate.readData("/cnpc/schema/avro/" + schemaId);

        return new Schema.Parser().parse(schemaStr);
    }


}
