package zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import utils.StringZkSerializer;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jason on 17-8-8.
 */
public class ZkNodeCopy {
    private static String zkSrcPath = "/spark/offset/test";
    private static String zkDestPath = "/spark/offsetbak/test2";

    public static void main(String[] args) throws IOException {

        String zkServers = "11.11.127.1:2181";


        ZkClient zkClient = new ZkClient(zkServers, 6000, 6000, new StringZkSerializer());

        List<String> childrenNodes = zkClient.getChildren(zkSrcPath);

        for (String childrenNode : childrenNodes) {
            String data = zkClient.readData(zkSrcPath + "/" + childrenNode);
            System.out.println(data);


            String destNodePath = zkDestPath + "/" + childrenNode;

            if (!zkClient.exists(destNodePath)) {
                zkClient.createPersistent(destNodePath, true);
            }
            zkClient.writeData(destNodePath, data);
        }

    }

   /* private static List<String> copy(ZkClient zkClient, String node) {
        List<String> childrenNodes = zkClient.getChildren(node);

        if (CollectionUtils.isEmpty(childrenNodes)) {

        }


        return copy();
    }*/


}
