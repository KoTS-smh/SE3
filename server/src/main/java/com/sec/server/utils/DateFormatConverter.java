package com.sec.server.utils;

import java.util.Date;

public class DateFormatConverter {

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

    public static void main(String[] args){
        System.out.println(DateFormatConverter.convert("Wed Apr 18 15:45:37 CST 2018"));
    }
}
