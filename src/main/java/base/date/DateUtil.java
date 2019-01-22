package base.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jason on 17-1-8.
 */

public class DateUtil {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

    public static  String formatDate(Date date)throws ParseException {
        return sdf.format(date);
    }

    public static Date parse(String strDate) throws ParseException{

        return sdf.parse(strDate);
    }

    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);

        return now.getTime();
    }

    public static void main(String[] args) {
        Date dateBefore = getDateBefore(new Date(), 6);
        String dateStr = sdf.format(dateBefore);

        System.out.println(dateStr);
        try {
            System.out.println(sdf.parse(dateStr).getTime());
        } catch (ParseException e) {


        }
    }
}
