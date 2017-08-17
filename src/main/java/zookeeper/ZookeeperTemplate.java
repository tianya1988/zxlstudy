package zookeeper;


import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.data.ACL;
import utils.StringZkSerializer;

import java.util.List;


public class ZookeeperTemplate {

    private static final String SLASH = "/";

    private volatile ZkClient zkClient = null;

    private String zkServer = null;

    public void setZkServer(final String zkServer) {
        this.zkServer = zkServer;
    }

    public void init() {
        getClient();
    }

    void setZkClient(final ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    private int sessionTimeout = 10000;

    private int connectionTimeout = 10000;

    private ZkClient getClient() {
        if (zkClient == null) {
            if (StringUtils.isEmpty(zkServer)) {
                throw new IllegalStateException("zookeeper server can't be null");
            }
            synchronized (this) {
                if (zkClient == null) {
                    zkClient = new ZkClient(zkServer, sessionTimeout, connectionTimeout, new StringZkSerializer());
                }
            }
        }
        return zkClient;
    }

    public List<String> getChildren(String path) {
        return getClient().getChildren(path);
    }

    public void createEphemeral(final String path) {
        getClient().createEphemeral(path);
    }

    public boolean exists(String path) {
        return getClient().exists(path);
    }

    public boolean notExists(String path) {
        return !exists(path);
    }

    public void createPersistentWithData(String path, Object data) {
        getClient().createPersistent(path, data);
    }

    public void createPersistentWithData(final String path, final Object data, final List<ACL> acls) {
        getClient().createPersistent(path, data, acls);
    }

    public void createPersistentWithParent(String path, boolean data) {
        getClient().createPersistent(path, data);
    }

    public void createPersistentWithParent(final String path, final boolean createParents, final List<ACL> acls) {
        getClient().createPersistent(path, createParents, acls);
    }


    public void delete(String path) {
        getClient().delete(adjustPath(path));
    }

    public void deleteRecursive(String path) {
        getClient().deleteRecursive(adjustPath(path));
    }

    public void writeData(String path, Object data) {
        getClient().writeData(path, data);
    }

    public void close() {
        if (zkClient != null) {
            zkClient.close();
        }
    }

    public String readData(final String path) {
        return getClient().readData(path);
    }

    public void addAuthInfo(final String scheme, final byte[] auth) {
        getClient().addAuthInfo(scheme, auth);
    }

    public synchronized void setSessionTimeout(final int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public synchronized void setConnectionTimeout(final int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    private String adjustPath(String path) {
        if (StringUtils.isEmpty(path)) {
            throw new IllegalArgumentException("The zk path can't be empty");
        }
        if (path.length() > 1) {
            if (!path.startsWith(SLASH)) {
                path = SLASH + path;
            }
            path = StringUtils.removeEnd(path, SLASH);
        }
        return path;
    }
}
