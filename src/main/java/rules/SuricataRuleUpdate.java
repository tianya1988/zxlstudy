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

        FileInputStream fileInputStream = new FileInputStream(new File("/home/jason/Desktop/suricata/rule-update/origin/compromised.rules"));

        HashMap<Long, HashMap<String, String>> oldSignatures = new HashMap<Long, HashMap<String, String>>();

        //按行返回
        List<String> lines = IOUtils.readLines(fileInputStream);
        for (String line : lines) {
            //System.out.println(line);
            if (line.startsWith("#") || line.isEmpty()) {
                continue;
            }
            int i = line.indexOf("(msg:");
            String message = line.substring(i);
//            System.out.println(message);

            String messageReplaced = message.replace("(", "").replace(")", "").replace(" ", "").replace("alarm_type", "alarm_type ");
            System.out.println(messageReplaced);


            HashMap<String, String> ruleFieldMap = new HashMap<String, String>();

            String[] fieldArray = messageReplaced.split(";");

            for (String field : fieldArray) {
                if (field.startsWith("sid:")) {
                    String[] sigArray = field.split(":");
                    oldSignatures.put(Long.parseLong(sigArray[1]), ruleFieldMap);
                    continue;
                }

                if (field.startsWith("msg:")) {
                    String[] msgArray = field.split(":");
                    ruleFieldMap.put("msg", msgArray[1]);
                    continue;
                }

                if (field.startsWith("metadata:")) {
                    String[] metadataArray = field.split(":");
                    ruleFieldMap.put("metadata", metadataArray[1]);
                    continue;
                }
            }

        }

        System.out.println(oldSignatures.size());
        IOUtils.closeQuietly(fileInputStream);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        FileInputStream newInputStream = new FileInputStream(new File("/home/jason/Desktop/suricata/rule-update/20200925/compromised.rules"));
        List<String> newLines = IOUtils.readLines(newInputStream);

        FileOutputStream outputStream = new FileOutputStream(new File("/home/jason/Desktop/suricata/rule-update/update/compromised" + sdf.format(new Date()) + ".rules"));

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

            String newMessageReplaced = newMessage.replace("(", "").replace(")", "").replace("; ", ";");
            System.out.println(newMessageReplaced);


            int sidIndex = newMessageReplaced.indexOf("sid:");
            String sidSubStr = newMessageReplaced.substring(sidIndex);
            String[] tempArray = sidSubStr.split(";");
            String[] sidArray = tempArray[0].split(":");
            long newSid = Long.parseLong(sidArray[1]);


            HashMap<String, String> originRuleFieldMap = null;

            if (oldSignatures.get(newSid) != null) {
                originRuleFieldMap = oldSignatures.get(newSid);
                oldRule++;
                System.out.println("oldRule : " + oldRule);
            } else {
                newRule++;
                System.out.println("newRule : " + newRule);
            }

            String[] newFieldArray = newMessageReplaced.split(";");

            for (int index = 0; index < newFieldArray.length; index++) {
                String field = newFieldArray[index];

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

                    if (field.startsWith("msg:")) {
                        String[] msgArray = field.split(":");
                        Thread.sleep(5000);
                        String msgValue = msgArray[1].replace("\"", "");
                        msgArray[1] = "\"" + GoogleTranslator.translate("en", "zh-CN", msgValue) + "\"";
                        newFieldArray[index] = msgArray[0] + ":" + msgArray[1];
                        continue;
                    }

                    if (field.startsWith("metadata:")) {
                        String[] metadataArray = field.split(":");
                        metadataArray[1] = "alarm_type " + "恶意IP";
                        newFieldArray[index] = metadataArray[0] + ":" + metadataArray[1];
                        continue;
                    }
                }
            }

            String mainPart = "";
            for (String field : newFieldArray) {
                mainPart = mainPart + field + ";";
            }
            mainPart = "(" + mainPart + ")";

            String updateRule = ipInfo + mainPart;


            IOUtils.write(updateRule, outputStream);
            IOUtils.write("\r\n", outputStream);

        }

        System.out.println("new rule count is : " + newRuleCount);
    }
}
