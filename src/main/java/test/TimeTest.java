package test;

import java.text.SimpleDateFormat;

public class TimeTest {
    public static void main(String[] args) {
        String time1 = "2021:11:2610:19:25";
        String time2 = "202:11:26+10:19:33";

        String[] testArray = new String[2];
        testArray[0] = time1;
        testArray[1] = time2;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");

        for (String s : testArray) {
            String temp;
            int length = s.length();
            System.out.println(length);

            StringBuffer sb = new StringBuffer();
            for (int i = 0 ; i < length; i++) {
                boolean isNum = Character.isDigit(s.charAt(i));
                if (isNum) {
                    sb.append(s.charAt(i));
                } else {

                }
            }
            System.out.println(sb.toString());
            sb.setLength(0);
        }
    }


}
