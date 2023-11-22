/*
package linuxshelltelnet;


import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

*/
/**
 * Created by jason on 20-3-13.
 *//*

public class SSH2 {
    public static void main(String[] args) throws IOException, JSchException, InterruptedException {
        String host = "172.16.254.1";
        int port = 22;
        String userName = "litao";
        String password = "Litao990087";

//        String host="127.0.0.1";
//        int port=22;
//        String userName="jason";
//        String password="adminzhangxl";

        JSch jsch = new JSch();
        Session session = jsch.getSession(userName, host, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setTimeout(6000);
        session.connect();
        //建立连接结束
        //发送指令
        ChannelExec exec = (ChannelExec) session.openChannel("exec");
        InputStream in = exec.getInputStream();
//        exec.setCommand("cat /home/jason/Desktop/APPbushu/jiaotong/DeviceGeoInfo.json");
//        exec.setCommand("display acl 3200");
        exec.setCommand("display ssh server status");
        exec.setCommand("display acl 3200");

        exec.connect();
//        String s = IOUtils.toString(in, "UTF-8");
        BufferedReader br = null;

        br = new BufferedReader(new InputStreamReader(in));
        int i = 0;
        while (true) {
            String line = br.readLine();
            i++;
            System.out.println("===== " + i + " : " + line);
            if (i == 20) {
                session.disconnect();
                break;
            }
        }

        in.close();
        session.disconnect();
    }
}
*/
