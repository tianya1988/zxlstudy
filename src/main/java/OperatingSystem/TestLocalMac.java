package OperatingSystem;

/**
 * Created by jason on 20-9-8.
 */

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 获取本机的Mac地址 (物理地址) 如:58-02-E3-58-4E-E5
 *
 */
public class TestLocalMac {


    private static void getLocalMac(InetAddress ia) throws SocketException {
        //获取网卡，获取地址
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        System.out.println("mac数组长度："+mac.length);
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append(":");
            }
            //字节转换为整数
            int temp = mac[i] & 0xff;
            String str = Integer.toHexString(temp);
            //System.out.println("每8位:"+str);
            if (str.length() == 1) {
                sb.append("0" + str);
            } else {
                sb.append(str);
            }
        }
        System.out.println("本机MAC地址 :    " + sb.toString().toUpperCase());
    }

    public static void main(String[] args) throws UnknownHostException {

        // 根据hosts文件中的域名查询对应的网卡信息
        InetAddress ia = InetAddress.getByName("xingtu.scsc.tech");

        // localhost 127.0.0.1 ,此ip没有 mac地址
        // InetAddress ia = InetAddress.getLocalHost();
        String ip = ia.toString().split("/")[1];
        System.out.println(ia);
        System.out.println("IP:" + ip);
        try {
            getLocalMac(ia);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

}