package linuxshell;

/**
 * Created by h on 16-6-4.
 */
public class SSHConfig {

    private String host;
    private int port = 22;
    private String username;
    private String password;

    private String privateKeyFilePath;
    private String privateKeyFilePassword;

    public String getPrivateKeyFilePath() {
        return privateKeyFilePath;
    }

    public void setPrivateKeyFilePath(final String privateKeyFilePath) {
        this.privateKeyFilePath = privateKeyFilePath;
    }

    public String getPrivateKeyFilePassword() {
        return privateKeyFilePassword;
    }

    public void setPrivateKeyFilePassword(final String privateKeyFilePassword) {
        this.privateKeyFilePassword = privateKeyFilePassword;
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
