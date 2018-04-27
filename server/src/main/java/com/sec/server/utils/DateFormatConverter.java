package com.sec.server.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatConverter {
    private static final String DATE_FORMAT = "EEE MMM dd HH:mm:ss z yyyy";

    public static String convert(String nativeDate){
        StringBuffer sb = new StringBuffer();
        sb.append(nativeDate.substring(nativeDate.length() - 4) + "-");

        //month
        String month = nativeDate.substring(4, 7);
        switch (month){
            case "Jan":
            {
                sb.append("01-");
                break;
            }
            case "Feb":
            {
                sb.append("02-");
                break;
            }
            case "Mar":
            {
                sb.append("03-");
                break;
            }
            case "Apr":
            {
                sb.append("04-");
                break;
            }
            case "May":
            {
                sb.append("05-");
                break;
            }
            case "Jun":
            {
                sb.append("06-");
                break;
            }
            case "Jul":
            {
                sb.append("07-");
                break;
            }
            case "Aug":
            {
                sb.append("08-");
                break;
            }
            case "Sep":
            {
                sb.append("09-");
                break;
            }
            case "Oct":
            {
                sb.append("10-");
                break;
            }
            case "Nov":
            {
                sb.append("11-");
                break;
            }
            case "Dec":
            {
                sb.append("12-");
                break;
            }
        }

        //day
        sb.append(nativeDate.substring(8, 10).trim());

        //time

        String nativeTime = nativeDate.substring(11, 19);
        nativeTime.trim();
        sb.append("T");

        sb.append(DateFormatConverter.convertTime(nativeTime));
        sb.append(".000Z");


        return sb.toString();
    }

    public static String convertTime(String nativeTime){
        int hour = Integer.parseInt(nativeTime.substring(0, 2));
        if(hour >= 8){
            hour = hour - 8;
        }else{
            hour = 24 - (8 - hour);
        }

        if(hour < 10){
            return "0" + hour + nativeTime.substring(2);
        }else{
            return hour + nativeTime.substring(2);
        }


    }

    //用于类似"2018-3-3 15:45"这样的时间转换
    public static String simpleDateConvert(String nativeTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = sdf.parse(nativeTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.toString();
    }

    public static String complexDataConvert(String nativeTime){


        Date date = null;
        try {
            date = new SimpleDateFormat(DATE_FORMAT, Locale.US).parse(nativeTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return String.valueOf(date);
    }

    public static void main(String[] args){
        String d1 = DateFormatConverter.simpleDateConvert("2018-3-3 15:45");
        System.out.println(DateFormatConverter.convert(d1));
    }
}
