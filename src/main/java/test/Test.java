package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Test {

    // 罗马字符转化为数字
    public static int getNumber(String s) {

        int result = 0;
        int prev = 0; // 记录前一个数字的值

        for (int i = s.length() - 1; i > -1; i--) {
            switch (s.charAt(i)) {
                case 'I': // 1
                    if (1 < prev) {
                        result -= 1;
                    } else {
                        result += 1;

                    }
                    prev = 1;
                    break;

                case 'V': // 5

                    if (5 < prev) {
                        result -= 5;
                    } else {
                        result += 5;
                    }

                    prev = 5;

                    break;
                case 'X': // 10
                    if (10 < prev) {
                        result -= 10;
                    } else {
                        result += 10;
                    }

                    prev = 10;
                    break;
                case 'L': // 50
                    if (50 < prev) {
                        result -= 50;
                    } else {
                        result += 50;
                    }

                    prev = 50;
                    break;
                case 'C': // 100
                    if (100 < prev) {
                        result -= 100;
                    } else {
                        result += 100;
                    }

                    prev = 100;
                    break;
                case 'D': // 500
                    if (500 < prev) {
                        result -= 500;
                    } else {
                        result += 500;
                    }

                    prev = 500;
                    break;
                case 'M': // 1000
                    result += 1000;
                    prev = 1000;
                    break;
            }
        }

        return result;
    }

    // 逐行解析输入文本
    public static void ParseInput(String filePath) throws IOException {
        // 存储中间转化的数据  {tegj=L, glob=I, prok=V, pish=X}
        HashMap<String, String> changeMap = new HashMap<String, String>();

        // 存储计算得出的商品价值  {Silver=17.0, Gold=14450.0, Iron=195.5}
        HashMap<String, Double> commodityValue = new HashMap<String, Double>();

//        FileInputStream fileInputStream = new FileInputStream(new File(filePath));
//        List<String> lines = IOUtils.readLines(fileInputStream);

        BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));

        String line = null;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("how much is")) {
                String[] questionParts = line.split(" is ");
                String substring = questionParts[1].substring(0, questionParts[1].length() - 2);

                String[] split = substring.split(" ");

                String willChangeStr = "";
                for (String s : split) {
                    s = s.trim();
                    if (!"".equals(s) && s != null) {
                        if (changeMap.get(s) != null) {
                            willChangeStr += changeMap.get(s);
                        } else {
                            System.out.println("I have no idea what you are talking about");
                        }

                    }

                }
                System.out.println(substring + " is " + getNumber(willChangeStr));

            } else if (line.startsWith("how many Credits")) {
                String[] split = line.split(" is ");
                String substring = split[1].substring(0, split[1].length() - 2);
                String[] questionTerms = substring.split(" ");

                String willChangeStr = "";
                Double commodityPrice = 0.0;
                for (String questionTerm : questionTerms) {
                    if (changeMap.containsKey(questionTerm)) {
                        willChangeStr += changeMap.get(questionTerm);
                    } else if (commodityValue.containsKey(questionTerm)) {
                        commodityPrice = commodityValue.get(questionTerm);
                    }
                }

                int number = getNumber(willChangeStr);

                System.out.println(substring + " is " + number * commodityPrice + " Credits");

            } else if (line.endsWith("Credits")) {
                String[] split = line.split(" is ");
                String[] part1 = split[0].split(" "); //glob glob Silver

                String willChangeStr = "";
                String commodity = "";
                for (String s : part1) {
                    if (changeMap.containsKey(s)) {
                        willChangeStr += changeMap.get(s);
                    } else {
                        commodity = s;
                    }
                }

                int number = getNumber(willChangeStr);

                String[] part2 = split[1].split(" ");
                commodityValue.put(commodity, (Double.parseDouble(part2[0])) / number);

            } else if (line.split(" ").length == 3) {
                String[] split = line.split(" ");
                changeMap.put(split[0], split[2]);

            } else {
                System.out.println("I have no idea what you are talking about");
            }

        }

        br.close();

    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("please input the question file path !");
            return;
        }
        ParseInput(args[0]);
    }


}
