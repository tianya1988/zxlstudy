package rsa;

/**
 * Created by jason on 20-9-7.
 */

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
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

        //测试匹配的秘钥对的情况
//        keyMap.put(0, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCHWoyoE3e6OVbFdrg2+dUHEjUB/QY+zjNhe1HuvpBDCu1rqeFXA068GTW8NwHqvO67BAOp6/xdzP1uMyOVGA6aWa/i9mEj1E2UFep+2iP1NgDgCBpj0GOYogurAKlBBdGr85Db7Sb5WgnD/HYoK/C8c4yKxR8WxG/m93lA4ipaEQIDAQAB");
//        keyMap.put(1, "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMdwclRYY19MrJNovmbSdUS/iKFH+g4ec0NsYeMhHDf9xpsPgYDngVeNkPmZcY8XisgbsGc1zupXNbUj7iQU/uOnQIlXKyi6P6pUIRsC5WlH3I58CAm4ibHCmLd5GqtuGthW0tuC/FnK65KFl80fPboA3kiDCRqe+SgG7T2NT8p7AgMBAAECgYEAwhrGqyB7Vr0hGTbQWPJr/8UKDS4oSKWKOQy7GHuZI0VyjmfA+txWHghCGSsmQsX+5FNOlKTiBWFrfCjEFYn5p40MhlYew1Q9ib+s2eL+cYnFIS3cJ3StCs40GOBOzACEeNbeYezTMjVuMU/KX6N30BcqoebcT0EjrPgONHCa9YECQQD8fatxB844YzHmEgiMPIvfL56ZkcIqyApmLfFKFzs+XkrVTBducEADTwt1njIt2fV2vH5V5HgY3L/fDCQxNc3TAkEAyjYGokXMzQ424DWxNTixMd80+EY5W8cdP/k15zA2TD8yPZaXmOjVYusBZ0NL1in9kXEX1yoBKE+H7ZcBzAifuQJBAOvouzObuGlk/S1ashPdSk7Y3lXcI0/3ogfAa07vj9IBJehO9SGhzZ2J0Eov6fB/UmKDUYMNOcz5DGjpnjNAczMCQF2iVMIRhjsxqNF7q1oUrWCFlhadFfRcqDu2X3J1Tb9SyCXitMIWlrDeV2EEtXovKX6OmtaEWdLbuPqYoTlkiOkCQFHPPAjr0TGoSIFBzOg8hoUjql853Wltcy5eMjhWWvs4lX2PeWUiKHx7EzZLX1eM8Nc/D/3GelKPTMYDZiLKaic=");

        //加密字符串
        String message = "172.16.0.204,mac,server4,2020-09-30 12:00:00,jason";
        System.out.println("随机生成的公钥为:" + keyMap.get(0));
        System.out.println("随机生成的私钥为:" + keyMap.get(1));
        String messageEncrypt = encrypt(message, keyMap.get(1));
        System.out.println("加密后的字符串为:" + messageEncrypt);
        String messageDe = decrypt(messageEncrypt, keyMap.get(0));
        System.out.println("还原后的字符串为:" + messageDe);
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

