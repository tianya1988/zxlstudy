package zookeeper;

import kafka.OffsetUtil;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang.StringUtils;
import utils.StringZkSerializer;

import java.util.HashMap;

/**
 * Created by jason on 17-8-8.
 */
public class ZkNodeReadBasic {

    public static void main(String[] args) {
        String zkServers = "11.11.165.33:2181";
        String zkPath = "/cnpc/algorithm/spark-agent/TcpToHdfs";

        ZkClient zkClient = new ZkClient(zkServers, 6000, 6000, new StringZkSerializer());

        if (zkClient.exists(zkPath)) {
            final String data = zkClient.readData(zkPath);
            System.out.println(data);
        } else {
            System.out.println("=====null=====");
        }
    }
}
