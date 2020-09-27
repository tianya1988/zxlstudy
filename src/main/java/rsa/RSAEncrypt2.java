package rsa;

/**
 * Created by jason on 20-9-7.
 */

import org.apache.commons.codec.binary.Base64;
import utils.MD5Util;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class RSAEncrypt2 {
    // 0-公钥 1-私钥
    private static Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥

    /**
     * 私钥加密 公钥解密
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //生成公钥和私钥
        genKeyPair();

        String domainNameMD5 = MD5Util.getMD5("xingtu.scsc.tech");
        System.out.println("domainNameMD5 is : " + domainNameMD5);//验证代码中，这个串被写死进去了。所以message中xingtu.scsc.tech这个字符串不可以变


        // 1. 过期时间
        String endDateStr = "2020-12-31 23:59:59";//参与验证
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long endTime = sdf.parse(endDateStr).getTime();

        String ip = "172.16.0.205";

        // 2. 管理ip对应的mac地址
        String mac = "20:04:0F:E6:2A:00"; //注意要大写,参与验证

        // 3. os version
        String osVersion = "3.10.0-514.el7.x86_64";//参与验证

        //加密字符串
        String message = ip + "," + mac + ",www.scsc.tech,xingtu.scsc.tech," + endTime + "," + osVersion;
        System.out.println("随机生成的公钥为:" + keyMap.get(0));
        System.out.println("随机生成的私钥为:" + keyMap.get(1));
        String messageEncrypt = encrypt(message, keyMap.get(1));
//        String messageEncrypt = "dDgD5DBmdsdutUu/l+FSvM8dO0LRk6gWlrBc3K0ZZB0LHgSDovgcjhtK232nUg1RvwG9AaR7sOqok/8wVc6458EKjm0SM4WPF1yakcsb5rTCTM+EG5m2+tSKUkHH1swTLQtrI0uEwnBqo46KGK9PjTeGSl+wMGvYyL2eUtJpVZo=";
        System.out.println("加密后的字符串为:" + messageEncrypt);
        String messageDe = decrypt(messageEncrypt, keyMap.get(0));
        System.out.println("还原后的字符串为:" + messageDe);

        System.out.println("license key is : " + messageEncrypt + "AASCSCXingTuBB" + keyMap.get(0));

    }

    /**
     * 随机生成密钥对
     *
     * @throws java.security.NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");

        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());

        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥

        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        keyMap.put(0, publicKeyString);  //0表示公钥
        keyMap.put(1, privateKeyString);  //1表示私钥
    }

    /**
     * RSA公钥加密
     *
     * @param str        加密字符串
     * @param privateKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str, String privateKey) {
        try {
            //base64编码的公钥
            byte[] decoded = Base64.decodeBase64(privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
            //RSA加密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, priKey);
            String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
            return outStr;
        } catch (Exception e) {
            System.out.println("私钥加密失败");
        }
        return null;
    }

    /**
     * RSA私钥解密
     *
     * @param str       加密字符串
     * @param publicKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String publicKey) {
        try {
            //64位解码加密后的字符串
            byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
            //base64编码的私钥
            byte[] decoded = Base64.decodeBase64(publicKey);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            //RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            String outStr = new String(cipher.doFinal(inputByte));
            return outStr;
        } catch (Exception e) {
            System.out.println("公钥解密失败");
        }

        return null;
    }

}

