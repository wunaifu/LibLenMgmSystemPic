package com.zhuolang.main.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtil {
	
	public static String dateToString(Date date){
		String dateStr = "";
        //format的格式可以任意
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH/mm/ss");
        try {
            dateStr = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
	}

    public static String dateToStrNoTime(Date date){
        String dateStr = "";
        //format的格式可以任意
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateStr = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

	public static Date stringToDate(String dateStr){
		Date date = new Date();
        //注意format的格式要与日期String的格式相匹配
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(dateStr);
            System.out.println(date.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
	}

    public static Date strToDateNoTime(String dateStr){
        Date date = null;
        //注意format的格式要与日期String的格式相匹配
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
             date = sdf.parse(dateStr);
            System.out.println(date.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
	
	public static Date timestampToDate(Timestamp ts){
        Date date = new Date();
        try {
            date = ts;
            System.out.println(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
	}
	
	public static String timestampToString(Timestamp ts){
		String tsStr = "";
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            //方法一
            tsStr = sdf.format(ts);
            System.out.println(tsStr);
            //方法二
            tsStr = ts.toString();
            System.out.println(tsStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tsStr;
	}
	
	public static Timestamp stringToTimestamp(String tsStr){
		Timestamp ts = new Timestamp(System.currentTimeMillis());
        try {
            ts = Timestamp.valueOf(tsStr);
            System.out.println(ts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ts;
	}

    public static Date longToDateNoTime(String dateStr){
        Date date = new Date(Long.parseLong(dateStr));
        //注意format的格式要与日期String的格式相匹配
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(dateStr);
            System.out.println(date.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static int oleTimeTonowTime(String str1,String str2){
        System.out.print("开始时间:");

        //"yyyyMMdd"格式 如 20131022
        System.out.println("\n结束时间:");
        //"yyyyMMdd"格式 如 20131022
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//输入日期的格式
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(str1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        GregorianCalendar cal1 = new GregorianCalendar();
        GregorianCalendar cal2 = new GregorianCalendar();
        cal1.setTime(date1);
        cal2.setTime(date2);
        double dayCount = (cal2.getTimeInMillis()-cal1.getTimeInMillis())/(1000*3600*24);//从间隔毫秒变成间隔天数
        int days=0;
        if (dayCount%1!=0){
            days= (int) dayCount+1;
        }else {
            days= (int) dayCount;
        }
        System.out.println("\n相差"+dayCount+"天");
        return days;
    }

    public static String oleTimeAddDay(String str1,int days){
        System.out.print("开始时间:");
        String temp = "";
        //"yyyyMMdd"格式 如 20131022
        System.out.println("\n结束时间:");
        //"yyyyMMdd"格式 如 20131022
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//输入日期的格式
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(str1);
            Calendar cl = Calendar.getInstance();
            cl.setTime(date1);
            cl.add(Calendar.DATE,days);
            temp = simpleDateFormat.format(cl.getTime());
            System.out.println(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return temp;
    }
}
