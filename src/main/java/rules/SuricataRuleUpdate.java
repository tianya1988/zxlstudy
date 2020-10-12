package rules;


import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import translator.GoogleTranslator;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jason on 20-9-27.
 */
public class SuricataRuleUpdate {

    private static final Logger logger = LoggerFactory.getLogger(SuricataRuleUpdate.class);

    public static void main(String[] args) {

        long beginTime = System.currentTimeMillis();

        HashMap<String, String> fileAlarmTypeMap = new HashMap<String, String>();
        fileAlarmTypeMap.put("ciarmy", "恶意IP");
        fileAlarmTypeMap.put("compromised", "恶意IP");
        fileAlarmTypeMap.put("dshield", "恶意IP");
//        fileAlarmTypeMap.put("emerging-attack_response", "命令执行");
        fileAlarmTypeMap.put("emerging-scan", "恶意扫描");
//        fileAlarmTypeMap.put("emerging-activex", "ActiveX利用");
        fileAlarmTypeMap.put("emerging-coinminer", "挖矿");
//        fileAlarmTypeMap.put("emerging-dns", "DNS攻击");
//        fileAlarmTypeMap.put("emerging-dos", "DDoS");
//        fileAlarmTypeMap.put("emerging-exploit", "漏洞利用");
//        fileAlarmTypeMap.put("emerging-exploit_kit", "漏洞利用");
//        fileAlarmTypeMap.put("emerging-ftp", "漏洞利用");
//        fileAlarmTypeMap.put("emerging-icmp_info", "ICMP事件");
//        fileAlarmTypeMap.put("emerging-imap", "漏洞利用");
        fileAlarmTypeMap.put("emerging-ja3", "恶意TLS");
//        fileAlarmTypeMap.put("emerging-malware", "恶意软件");
//        fileAlarmTypeMap.put("emerging-netbios", "漏洞利用");
//        fileAlarmTypeMap.put("emerging-rpc", "漏洞利用");
//        fileAlarmTypeMap.put("emerging-shellcode", "Webshell");
//        fileAlarmTypeMap.put("emerging-snmp", "漏洞利用");
//        fileAlarmTypeMap.put("emerging-sql", "数据库访问异常");// 非单一
//        fileAlarmTypeMap.put("emerging-telnet", "Telnet访问异常");// 非单一
//        fileAlarmTypeMap.put("emerging-tftp", "漏洞利用");
//        fileAlarmTypeMap.put("emerging-user_agents", "木马病毒");
//        fileAlarmTypeMap.put("emerging-web_client", "漏洞利用");
//        fileAlarmTypeMap.put("emerging-web_server", "漏洞利用");
//        fileAlarmTypeMap.put("emerging-worm", "木马病毒");


        // 统计更新后所有的有效规则一共有多少条
        int totalRuleCount = 0;

        // 统计更新后的所有的有效规则中旧规则一共有多少条
        int totalOldRule = 0;

        // 统计更新后的所有的有效规则中新规则一共有多少条
        int totalNewRule = 0;

        Set<Map.Entry<String, String>> entrySet = fileAlarmTypeMap.entrySet();
        for (Map.Entry<String, String> fileAlarmTypeEntry : entrySet) {

            logger.info("========= " + fileAlarmTypeEntry.getKey() + " ========= begin !");

            String originRuleDir = "/home/jason/Desktop/suricata/rule-update/update/20200925/";
            String originRuleFileName = fileAlarmTypeEntry.getKey() + ".rules";

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String todayStr = sdf.format(new Date());
//            String newRuleDir = "/home/jason/Desktop/suricata/rule-update/" + todayStr + "/";
            String newRuleDir = "/home/jason/Desktop/suricata/rule-update/20201010/";
            String newRuleFileName = fileAlarmTypeEntry.getKey() + ".rules";

//            String outputDir = "/home/jason/Desktop/suricata/rule-update/update/" + todayStr + "/";
            String outputDir = "/home/jason/Desktop/suricata/rule-update/update/20201010/";
            String outputFileName = fileAlarmTypeEntry.getKey() + ".rules";

            String selfDefAlarmType = fileAlarmTypeEntry.getValue();


            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(new File(originRuleDir + originRuleFileName));
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage());
                continue;
            }

            HashMap<Long, HashMap<String, String>> oldSignatures = new HashMap<Long, HashMap<String, String>>();

            //按行返回
            List<String> lines = null;
            try {
                lines = IOUtils.readLines(fileInputStream);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            for (String line : lines) {
                // System.out.println(line);
                if (line.startsWith("#") || line.isEmpty()) {
                    continue;
                }
                int i = line.indexOf("(msg:");
                String message = line.substring(i);
                // System.out.println(message);

                // replace 和replaceAll 都是全局替换，区别是是replaceAll支持正则
                String messageReplaced = message.replaceAll(" +", " ").replace("; ", ";");
//                System.out.println(messageReplaced);


                HashMap<String, String> ruleFieldMap = new HashMap<String, String>();

                String[] fieldArray = messageReplaced.split(";");

                for (String field : fieldArray) {
                    if (field.startsWith("sid:")) {
                        String[] sigArray = field.split(":");
                        oldSignatures.put(Long.parseLong(sigArray[1]), ruleFieldMap);
                        continue;
                    }
                    // 有用信息msg字段
                    if (field.startsWith("(msg:")) {
                        String[] msgArray = field.split(":");
                        ruleFieldMap.put("msg", msgArray[1]);
                        continue;
                    }

                    // // 有用信息metadata字段
                    if (field.startsWith("metadata:")) {
                        String[] metadataArray = field.split(":");
                        ruleFieldMap.put("metadata", metadataArray[1]);
                        continue;
                    }
                }

            }

            logger.info("old signatures size is : " + oldSignatures.size());
            IOUtils.closeQuietly(fileInputStream);


            // 解析新的规则文件
            FileInputStream newInputStream = null;
            List<String> newLines = null;
            try {
                newInputStream = new FileInputStream(new File(newRuleDir + newRuleFileName));
                newLines = IOUtils.readLines(newInputStream);
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage());
                continue;
            } catch (IOException e) {
                logger.error(e.getMessage());
                continue;
            }


            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(new File(outputDir + outputFileName));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            int ruleCount = 0;
            int oldRule = 0;
            int newRule = 0;
            for (String newline : newLines) {
                // 文件存在，但是规则网址上该文件不存在，下载后的文件内容为404内容，此类文件直接跳过，不解析
                if (newline.startsWith("<!DOCTYPE HTML")) {
                    break;
                }

                if (newline.startsWith("#") || newline.isEmpty()) {
                    continue;
                }

                ruleCount++;
                int i = newline.indexOf("(msg:");
                String newMessage = newline.substring(i);
                String ipInfo = newline.substring(0, i);

                // 替换掉(); 分号+空格的替换是为了后续切分的时候方便
                String newMessageReplaced = newMessage.replaceAll(" +", " ").replace("; ", ";");
                // System.out.println(newMessageReplaced);

                // 获取新规则的sid，方便从就规则的map中get有用信息
                int sidIndex = newMessageReplaced.indexOf("sid:");
                String sidSubStr = newMessageReplaced.substring(sidIndex);
                String[] tempArray = sidSubStr.split(";");
                String[] sidArray = tempArray[0].split(":");
                long newSid = Long.parseLong(sidArray[1]);

                // 存储旧规则的有用信息
                HashMap<String, String> originRuleFieldMap = null;
                if (oldSignatures.get(newSid) != null) {
                    originRuleFieldMap = oldSignatures.get(newSid);
                    oldRule++;
                    logger.info("oldRule : " + oldRule);
                } else {
                    newRule++;
                    logger.info("newRule : " + newRule);
                }

                // 新规则的字段信息
                String[] newFieldArray = newMessageReplaced.split(";");

                for (int index = 0; index < newFieldArray.length; index++) {
                    String field = newFieldArray[index];

                    // 如果旧规则的map有值，则获取就规则的有用信息
                    if (originRuleFieldMap != null) {

                        if (field.startsWith("(msg:")) {
                            String[] msgArray = field.split(":");
                            msgArray[1] = originRuleFieldMap.get("msg");
                            newFieldArray[index] = msgArray[0] + ":" + msgArray[1];
                            continue;
                        }

                        if (field.startsWith("metadata:")) {
                            String[] metadataArray = field.split(":");
                            metadataArray[1] = originRuleFieldMap.get("metadata");
                            newFieldArray[index] = metadataArray[0] + ":" + metadataArray[1];
                            continue;
                        }
                    } else {
                        // 给新规则的msg字段做翻译
                        if (field.startsWith("(msg:")) {
                            int randomNumber = new Random().nextInt(5) + 10;
                            try {
                                Thread.sleep(randomNumber * 1000);// 调用翻译接口的时候不能太快，否则会被封掉一段时间
                            } catch (InterruptedException e) {
                                logger.error(e.getMessage());
                            }
                            String[] msgArray = field.split(":");
                            String msgValue = msgArray[1].replace("\"", "");
                            String translatedStr = null;
                            try {
                                translatedStr = GoogleTranslator.translate("en", "zh-CN", msgValue);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (translatedStr.startsWith("ET") && (translatedStr.contains("恶意主机") || translatedStr.contains("恶意的主机") || translatedStr.contains("敌对的主机流量"))) {
                                translatedStr = "恶意IP";
                            }
                            msgArray[1] = "\"" + translatedStr + "\"";
                            newFieldArray[index] = msgArray[0] + ":" + msgArray[1];
                            continue;
                        }

                        // 添加新规则的告警类型
                        if (field.startsWith("metadata:")) {
                            String[] metadataArray = field.split(":");
                            metadataArray[1] = "alarm_type " + selfDefAlarmType;
                            newFieldArray[index] = metadataArray[0] + ":" + metadataArray[1];
                            continue;
                        }
                    }
                }

                // 拼接新规则字符串
                String mainPart = "";
                for (String field : newFieldArray) {
                    mainPart = mainPart + field + ";";
                }
                mainPart = mainPart.substring(0, mainPart.length() - 1);
                String updateRule = ipInfo + mainPart;

                // 输出一条新规则
                try {
                    IOUtils.write(updateRule, outputStream);
                    IOUtils.write("\r\n", outputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            logger.info("========== " + fileAlarmTypeEntry.getKey() + " file rule count is : " + ruleCount);
            logger.info("========== " + fileAlarmTypeEntry.getKey() + " file old rule count is : " + oldRule);
            logger.info("========== " + fileAlarmTypeEntry.getKey() + " file new rule count is : " + newRule);
            IOUtils.closeQuietly(newInputStream);
            IOUtils.closeQuietly(outputStream);

            totalRuleCount = totalRuleCount + ruleCount;
            totalOldRule = totalOldRule + oldRule;
            totalNewRule = totalNewRule + newRule;

        }

        long endTime = System.currentTimeMillis();
        logger.info("Total use time is : " + (endTime - beginTime) / 1000 + " s");
        logger.info("Total rule count is : " + totalRuleCount);
        logger.info("Total old rule count is : " + totalOldRule);
        logger.info("Total new rule count is : " + totalNewRule);
    }
}
