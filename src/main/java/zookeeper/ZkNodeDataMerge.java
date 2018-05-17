package zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkException;
import org.apache.commons.io.IOUtils;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import utils.StringZkSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 18-4-11.
 */
public class ZkNodeDataMerge {
    public static void main(String[] args) throws IOException {
        String zkServers = "11.11.127.1:2181";
        String[] logTypes = {"cert", "icmp", "socks", "filter", "pop3", "imap4", "smtp"};
        String zkPath = "/cnpc/spark/profile/conf/";

        String area = "dq";
        String algorithmType = "p1kafka";
        String destFileName = "avro-other-" + area + "-to-" + algorithmType;

//        avro-socks-lh-to-p2kafka

        ZkClient zkClient = new ZkClient(zkServers, 6000, 6000, new StringZkSerializer());

        String totalConfigStr = "";

        for (int i = 0; i < logTypes.length; i++) {
            String filePath = zkPath + "avro-" + logTypes[i] + "-" + area + "-to-" + algorithmType;

            if (zkClient.exists(filePath)) {
                String configStr = zkClient.readData(filePath);
                if (i > 0) {
                    int j = i + 1;
                    int begin = configStr.indexOf("###############");
                    String tempConfigStr = configStr.substring(begin).replace("k1", "k" + j).replace("s1", "s" + j);
                    totalConfigStr += tempConfigStr;
                } else {
                    String tempConfigStr = configStr.replace("app.sources=k1", "app.sources=k1,k2,k3,k4,k5,k6,k7");
                    totalConfigStr += tempConfigStr;
                }
            }
        }
        System.out.println(totalConfigStr);

        FileOutputStream outputStream = new FileOutputStream(new File("/home/jason/Desktop/other-config/p1/" + destFileName + ".properties"));
        IOUtils.write(totalConfigStr, outputStream);
        IOUtils.closeQuietly(outputStream);

        List<ACL> acls = createAcls("admin:secret+3s");
        zkClient.addAuthInfo("digest", "admin:secret+3s".getBytes());

        if (!zkClient.exists(zkPath + destFileName)) {
            zkClient.createPersistent(zkPath + destFileName, true, acls);
            zkClient.writeData(zkPath + destFileName, totalConfigStr);
        }
        zkClient.close();
    }

    public static List<ACL> createAcls(final String digestAuthInfo) {
        List<ACL> acls = new ArrayList<ACL>();
        //添加第一个id，采用用户名密码形式
        try {
            Id id1 = new Id("digest", DigestAuthenticationProvider.generateDigest(digestAuthInfo));
            ACL acl1 = new ACL(ZooDefs.Perms.ALL, id1);
            acls.add(acl1);
        } catch (NoSuchAlgorithmException e) {
            throw new ZkException("create acl id error!", e);
        }
        //添加第二个id，所有用户可读权限
        Id id2 = new Id("world", "anyone");
        ACL acl2 = new ACL(ZooDefs.Perms.READ, id2);
        acls.add(acl2);
        return acls;
    }
}
