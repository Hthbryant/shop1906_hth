package com.qf.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    public static  String getNowTimeStr(){
        String time = new SimpleDateFormat("yyMMddHH").format(new Date());
        return time;
    }
    public static String getTimeStr(Date date){
        String time = new SimpleDateFormat("yyMMddHH").format(date);
        return time;
    }
    public static String getNextTime(int next){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY,next);
        calendar.add(Calendar.MINUTE,0);
        calendar.add(Calendar.SECOND,0);
        calendar.add(Calendar.MILLISECOND,0);
        String nextTime = new SimpleDateFormat("yyMMddHH").format(calendar.getTime());
        return nextTime;
    }
}
