package zookeeper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import kafka.OffsetUtil;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import utils.StringZkSerializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jason on 17-8-8.
 */
public class ZkNodeWrite {

    public static void main(String[] args) throws IOException {

        String zkServers = "11.11.127.1:2181";
//        String zkPath = "/cnpc/spark/profile/conf";
        String zkPath = "/cnpc/spark/profile/process";
        ZkClient zkClient = new ZkClient(zkServers, 6000, 6000, new StringZkSerializer());

//        File dir = new File("/home/jason/project/workspace/venustech/spark-app-cnpc-config");
        File dir = new File("/home/jason/project/workspace/venustech/spark-app-cnpc/src/main/resources/process");

        File[] files = dir.listFiles();

        for (File file : files) {
            /*if (file.isDirectory() || !(file.getName().endsWith("properties")) || (file.getName().startsWith("sparkSubmit"))) {
                continue;
            }*/
            if (file.isDirectory() || !(file.getName().endsWith("json")) || (file.getName().startsWith("sparkSubmit"))) {
                continue;
            }

            String fileContentStr = FileUtils.readFileToString(file, "UTF-8");
            List<ACL> acls = createAcls("admin:secret+3s");
            zkClient.addAuthInfo("digest", "admin:secret+3s".getBytes());

            String fileName = file.getName();
//            String subFileName = fileName.substring(0, fileName.lastIndexOf(".properties"));//TODO
            String subFileName = fileName.substring(0, fileName.lastIndexOf(".json"));//TODO

            String filePath = zkPath + "/" + subFileName;
            if (!zkClient.exists(filePath)) {
                System.out.println(filePath);
                // 创建并写入
                zkClient.createPersistent(filePath, true, acls);
                zkClient.writeData(filePath, fileContentStr);

                // 删除
                // zkClient.delete(filePath);
            }
        }
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
