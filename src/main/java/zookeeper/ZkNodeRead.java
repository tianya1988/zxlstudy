package zookeeper;

import kafka.OffsetUtil;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang.StringUtils;
import utils.StringZkSerializer;

import java.util.HashMap;

/**
 * Created by jason on 17-8-8.
 */
public class ZkNodeRead {

    public static void main(String[] args) {
        String area = "bj";

        //C1
        String kafkaBrokers = "11.11.60.128:6667";
        String zkServers = "11.11.60.128:2181";
        String zkPath = "/spark/offset/Dns2Es/" + area + "-pro-dns3-spark-agent";

        /*//生产
        String kafkaBrokers = "11.11.60.2:6667";
        String zkServers = "11.11.127.2:2181";
        String zkPath = "/spark/offset/DnsToEs/" + area + "-pro-dns-spark-agent";*/

        HashMap<Integer, Object> earliestOffsetInfoFromKafka = OffsetUtil.getOffsetInfo(kafkaBrokers, "avro-" + area + "-pro-dns", -2);
        HashMap<Integer, Object> latestOffsetInfoFromKafka = OffsetUtil.getOffsetInfo(kafkaBrokers, "avro-" + area + "-pro-dns", -1);


        ZkClient zkClient = new ZkClient(zkServers, 6000, 6000, new StringZkSerializer());

        if (zkClient.exists(zkPath)) {
            final String data = zkClient.readData(zkPath);
            /**
             * data demo is below
             avro-dzm-pro-dns,27,9196974,9197108
             avro-dzm-pro-dns,31,12871066,12871222
             avro-dzm-pro-dns,3,9208919,9209066
             avro-dzm-pro-dns,7,12857507,12857626
             avro-dzm-pro-dns,34,9137815,9137961
             avro-dzm-pro-dns,6,9203408,9203534
             avro-dzm-pro-dns,1,12882761,12882893
             avro-dzm-pro-dns,0,9199975,9200102
             avro-dzm-pro-dns,18,9200224,9200372
             avro-dzm-pro-dns,33,9201392,9201531
             avro-dzm-pro-dns,4,9067498,9067634
             avro-dzm-pro-dns,24,9204184,9204342
             avro-dzm-pro-dns,30,9217817,9217949
             avro-dzm-pro-dns,16,5720746,5720898
             avro-dzm-pro-dns,21,9216270,9216400
             avro-dzm-pro-dns,17,13117638,13117769
             avro-dzm-pro-dns,9,9198245,9198394
             avro-dzm-pro-dns,28,9090950,9091079
             avro-dzm-pro-dns,10,9072420,9072546
             avro-dzm-pro-dns,2,9227455,9227587
             avro-dzm-pro-dns,29,13121248,13121371
             avro-dzm-pro-dns,35,13119558,13119687
             avro-dzm-pro-dns,11,13126858,13126988
             avro-dzm-pro-dns,23,13111063,13111202
             avro-dzm-pro-dns,19,12882371,12882519
             avro-dzm-pro-dns,15,9212357,9212512
             avro-dzm-pro-dns,25,12862391,12862524
             avro-dzm-pro-dns,12,9211412,9211553
             avro-dzm-pro-dns,20,9229179,9229295
             avro-dzm-pro-dns,32,9195569,9195682
             avro-dzm-pro-dns,14,9222315,9222462
             avro-dzm-pro-dns,22,9058344,9058482
             avro-dzm-pro-dns,8,9210630,9210770
             avro-dzm-pro-dns,13,12883011,12883138
             avro-dzm-pro-dns,5,13122035,13122177
             avro-dzm-pro-dns,26,9229125,9229262
             *
             *
             */

            System.out.println("===============================");
            System.out.println(data);
            System.out.println("================================");

            if (data != null) {
                final String[] rows = StringUtils.split(data, "\n");
                for (String row : rows) {
                    final String[] fieldValues = StringUtils.split(row, ",");
                    System.out.println("The zk info include, topic : " + fieldValues[0]
                            + ", partition : " + fieldValues[1]
                            + ", fromOffset : " + fieldValues[2]
                            + ", untilOffset : " + fieldValues[3]);

                    Long earliestOffset = (Long) earliestOffsetInfoFromKafka.get(Integer.parseInt(fieldValues[1]));
                    Long latestOffset = (Long) latestOffsetInfoFromKafka.get(Integer.parseInt(fieldValues[1]));

                    if (Long.parseLong(fieldValues[3].trim()) <= earliestOffset) {
                        System.out.println("Something is error : offset from zookeeper is too smaller, "
                                + "The topic : " + fieldValues[0]
                                + ", partition is : " + fieldValues[1]
                                + ", zk untilOffset is : " + fieldValues[3]
                                + ", but offset from kafka is : " + earliestOffset + "-" + latestOffset
                        );
                    } else if (Long.parseLong(fieldValues[3].trim()) >= latestOffset) {
                        System.out.println("Something is error : offset from zookeeper is too bigger, "
                                + "The topic : " + fieldValues[0]
                                + ", partition is : " + fieldValues[1]
                                + ", zk untilOffset is : " + fieldValues[3]
                                + ", but offset from kafka is : " + earliestOffset + "-" + latestOffset
                        );
                    }


                }
            }


        }
    }
}
