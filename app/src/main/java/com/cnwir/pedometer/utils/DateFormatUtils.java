package com.cnwir.pedometer.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by heaven on 2015/7/29.
 */
public class DateFormatUtils {


    public static List<Date> dayToWeek(Date curDate){

        long sevenAgoTime = curDate.getTime() - 7 * 24 * 3600000;
        List<Date> dates = new ArrayList<Date>();
        for(int i = 1; i <= 7; i++){

            Date date = new Date();
            date.setTime(sevenAgoTime + (i * 24 * 3600000));
            dates.add(date);
        }
        return dates;
    }

    public static String getMonthByDate(String date)  {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String month = null;

        try {
            Date mDate = sdf.parse(date);
            month = String.valueOf(mDate.getMonth() + 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }finally {
            return month  + "月";

        }
    }

    public static String getDayByDate(String date){


        return date.substring(5, 10);

    }
}
