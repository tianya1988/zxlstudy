package linuxshell;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.logging.Logger;

/**
 * Created by jason on 20-2-13.
 */
public class SSHLoginNoPassword {
    private static final Logger logger = Logger.getLogger(SSHLoginNoPassword.class.getName());

    public static void main(String[] args) {
        SSHConfig sshConfig = new SSHConfig();
        sshConfig.setHost("172.16.254.1");
        sshConfig.setUsername("litao");
//        sshConfig.setPrivateKeyFilePath("/home/jason/.ssh/id_rsa");
        sshConfig.setPassword("Litao990087");

        Connection connection = getConnection(sshConfig);
//        String s = execCommand(connection, "tailf -n100 /var/log/messages", true);
        String s = execCommand(connection, "display acl 3200", true);

        System.out.println(s);
    }


    public static String execCommand(Connection connection, final String cmd, final boolean readReturn) {
        Session session = null;
        BufferedReader br = null;
        try {
            session = connection.openSession();
            session.execCommand(cmd);
            if (readReturn) {
                InputStream stdout = new StreamGobbler(session.getStdout());
                br = new BufferedReader(new InputStreamReader(stdout));
                int i = 0;
                while (true) {
                    String line = br.readLine();
                    i++;
                    System.out.println("===== " + i + " : " + line);
                    if (i == 20) {
                        closeQuietly(session);
                        break;
                    }
                }
                System.out.println("status : " + session.getExitStatus());
                return IOUtils.toString(br);
            } else {
                return null;
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        } finally {
            IOUtils.closeQuietly(br);
            closeQuietly(session);
            closeQuietly(connection);
        }
    }


    public static Connection getConnection(SSHConfig config) {
        logger.info("create the ssh connection");
        Connection conn = null;
        try {
            conn = new Connection(config.getHost(), config.getPort());
            conn.connect();
            boolean isAuthenticated;
            if (config.getPassword() != null) {
                isAuthenticated = conn.authenticateWithPassword(config.getUsername(), config.getPassword());
            } else {
                File privateKeyFile = new File(config.getPrivateKeyFilePath());
                if (!privateKeyFile.exists()) {
                    throw new IllegalArgumentException("Private key file is not exist.");
                }
                isAuthenticated = conn.authenticateWithPublicKey(config.getUsername(),
                        privateKeyFile, "");
            }

            if (isAuthenticated == false) {
                throw new RuntimeException("Authentication failed.");
            }
            logger.info("create the ssh connection success");
            return conn;
        } catch (IOException exception) {
            closeQuietly(conn);
            throw new RuntimeException(exception);
        }
    }

    public static void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                logger.info("close the ssh connection");
                conn.close();
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }
    }

    private static void closeQuietly(final Session session) {
        try {
            if (session != null) {
                session.close();
            }
        } catch (Exception e) {
            //do nothing;
        }
    }
    
}
