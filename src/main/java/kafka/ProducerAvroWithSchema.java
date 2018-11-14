package kafka;

import com.alibaba.fastjson.JSONObject;
import org.apache.avro.Schema;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by jason on 17-9-14.
 */
public class ProducerAvroWithSchema {
    public static void main(String[] args) throws IOException {
        /**
         * 交通部
         */
        /*int schemaId = 10006;
        String zkServer = "10.30.111.1:2181";
        String destTopic = "avro-bjg-pro-http";
        String kafkaServer = "10.30.111.1:6667";
        String schemaPath = "/asap/schema/avro/";
        String dataFile = "http-remote.json";*/


        /***
         * 石油小开发
         */
        /*int schemaId = 10000;
        String zkServer = "11.11.184.183:2181";
        String sourceTopic = "avro-zxl-pro-http";
        String kafkaServer = "11.11.184.183:6667";
        String schemaPath = "/cnpc/schema/avro/";
        String dataFile = "http-attack.json";*/

        /**
         * P1环境
         */
        int schemaId = 1;
//        String zkServer = "11.11.127.1:2181";
        String schemaFile = "schema/dns.json";
        String destTopic = "avro-bj-pro-dns2";
        String kafkaServer = "10.30.111.1:6667";
        String schemaPath = "/asap/schema/avro/";
        String dataFile = "http-remote.json";


        byte[] schemaIdByte = ByteUtil.littleEndian(schemaId);


        InputStream inputStream = ProducerAvroWithSchema.class.getClassLoader().getResourceAsStream(dataFile);
        String dnsStr = IOUtils.toString(inputStream);
        System.out.println(dnsStr);
        JSONObject dnsJsonObject = JSONObject.parseObject(dnsStr);

//        Schema schema = getSchema(schemaId, zkServer, schemaPath);
        Schema schema = null;
        try {
            schema = new Schema.Parser().parse(new File(schemaFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(schema);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(schemaIdByte);

        GenericDatumWriter<GenericRecord> writer = new GenericDatumWriter<GenericRecord>(schema);

        GenericRecord genericRecord = AvroUtils.convert(dnsJsonObject, schema);
        BinaryEncoder encoder = EncoderFactory.get().directBinaryEncoder(outputStream, null);
        writer.write(genericRecord, encoder);
        encoder.flush();

        byte[] resultBytes = outputStream.toByteArray();
        ProducerRecord<String, byte[]> record = new ProducerRecord<String, byte[]>(destTopic, null, resultBytes);

        Properties kafkaProps = new Properties();
        kafkaProps.put(ProducerConfig.ACKS_CONFIG, "all");
        //Defaults overridden based on config
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
        kafkaProps.put("compression.type", "gzip");
        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        KafkaProducer<String, byte[]> producer = new KafkaProducer<String, byte[]>(kafkaProps);

        int i = 0;
        while (i < 100) {
            producer.send(record);
            System.out.println(i);
            i++;
        }
        producer.flush();


    }

    private static Schema getSchema(int schemaId, String zkServer, String schemaPath) {
        ZookeeperTemplate zookeeperTemplate = new ZookeeperTemplate();
        zookeeperTemplate.setZkServer(zkServer);

        String schemaStr = zookeeperTemplate.readData(schemaPath + schemaId);

        return new Schema.Parser().parse(schemaStr);
    }


}
