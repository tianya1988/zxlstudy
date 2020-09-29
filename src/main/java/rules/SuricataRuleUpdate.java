package rules;

import org.apache.commons.io.IOUtils;
import translator.GoogleTranslator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jason on 20-9-27.
 */
public class SuricataRuleUpdate {
    public static void main(String[] args) throws Exception {

        String originRuleDir = "/home/jason/Desktop/suricata/rule-update/origin/";
        String originRuleFileName = "compromised.rules";

        String newRuleDir = "/home/jason/Desktop/suricata/rule-update/20200925/";
        String newRuleFileName = "compromised.rules";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String outputDir = "/home/jason/Desktop/suricata/rule-update/update/";
        String outputFileName = "compromised-" + sdf.format(new Date()) + ".rules";

        String selfDefAlarmType = "恶意IP";


        FileInputStream fileInputStream = new FileInputStream(new File(originRuleDir + originRuleFileName));

        HashMap<Long, HashMap<String, String>> oldSignatures = new HashMap<Long, HashMap<String, String>>();

        //按行返回
        List<String> lines = IOUtils.readLines(fileInputStream);
        for (String line : lines) {
            // System.out.println(line);
            if (line.startsWith("#") || line.isEmpty()) {
                continue;
            }
            int i = line.indexOf("(msg:");
            String message = line.substring(i);
            // System.out.println(message);

            // replace 和replaceAll 都是全局替换，区别是是replaceAll支持正则
            String messageReplaced = message.replaceAll(" +", " ").replace("(", "").replace(")", "").replace("; ", ";");
            System.out.println(messageReplaced);


            HashMap<String, String> ruleFieldMap = new HashMap<String, String>();

            String[] fieldArray = messageReplaced.split(";");

            for (String field : fieldArray) {
                if (field.startsWith("sid:")) {
                    String[] sigArray = field.split(":");
                    oldSignatures.put(Long.parseLong(sigArray[1]), ruleFieldMap);
                    continue;
                }
                // 有用信息msg字段
                if (field.startsWith("msg:")) {
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

        System.out.println(oldSignatures.size());
        IOUtils.closeQuietly(fileInputStream);


        // 解析新的规则文件
        FileInputStream newInputStream = new FileInputStream(new File(newRuleDir + newRuleFileName));
        List<String> newLines = IOUtils.readLines(newInputStream);

        FileOutputStream outputStream = new FileOutputStream(new File(outputDir + outputFileName));

        int newRuleCount = 0;
        int oldRule = 0;
        int newRule = 0;
        for (String newline : newLines) {
            if (newline.startsWith("#") || newline.isEmpty()) {
                continue;
            }
            newRuleCount++;
            int i = newline.indexOf("(msg:");
            String newMessage = newline.substring(i);
            String ipInfo = newline.substring(0, i);

            // 替换掉(); 分号+空格的替换是为了后续切分的时候方便
            String newMessageReplaced = newMessage.replaceAll(" +", " ").replace("(", "").replace(")", "").replace("; ", ";");
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
                System.out.println("oldRule : " + oldRule);
            } else {
                newRule++;
                System.out.println("newRule : " + newRule);
            }

            // 新规则的字段信息
            String[] newFieldArray = newMessageReplaced.split(";");

            for (int index = 0; index < newFieldArray.length; index++) {
                String field = newFieldArray[index];

                // 如果旧规则的map有值，则获取就规则的有用信息
                if (originRuleFieldMap != null) {

                    if (field.startsWith("msg:")) {
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
                    if (field.startsWith("msg:")) {
                        Thread.sleep(5000);// 调用翻译接口的时候不能太快，否则会被封掉一段时间
                        String[] msgArray = field.split(":");
                        String msgValue = msgArray[1].replace("\"", "");
                        String translatedStr = GoogleTranslator.translate("en", "zh-CN", msgValue);
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
            mainPart = "(" + mainPart + ")";
            String updateRule = ipInfo + mainPart;


            // 输出一条新规则
            IOUtils.write(updateRule, outputStream);
            IOUtils.write("\r\n", outputStream);

        }

        System.out.println("new rule count is : " + newRuleCount);
        IOUtils.closeQuietly(newInputStream);
        IOUtils.closeQuietly(outputStream);
    }
}
