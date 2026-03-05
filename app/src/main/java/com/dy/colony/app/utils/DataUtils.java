package com.dy.colony.app.utils;

import com.apkfuns.logutils.LogUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛Code is far away from bug with the animal protecting
 * 　　　　┃　　　┃    神兽保佑,代码无bug
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * <p>
 *
 * @author wangzhenxiong
 * @date 2019-08-19
 */
public class DataUtils {
    public static Long getOneDayStartTime(Date date) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = format1.format(date) + " 00:00:00";
        try {
            Date date1 = format2.parse(s);
            return date1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    public static Long getOneDayStopTime(Date date) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = format1.format(date) + " 23:59:59";
        try {
            Date date1 = format2.parse(s);
            return date1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    public static Long stringyyymmddDataLong(String data) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date parse = format.parse(data);
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Long stringHHMMSSDataLong(String data) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            Date parse = format.parse(data);
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long getNowtimeYYIMMIDD(String data) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date parse = format.parse(data);
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long stringyyymmddhhmmssDataLong(String data) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date parse = format.parse(data);
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getNowtimeYYYYMMDD() {
        Long testingtime = new Date(System.currentTimeMillis()).getTime();
        String format = null;
        if (testingtime == null) {
            format = "";
        } else {
            format = new SimpleDateFormat("yyyyMMdd").format(testingtime);
        }
        return format;
    }
    public static String getYestodaytimeYYYYMMDD() {
        Long testingtime = new Date(System.currentTimeMillis()).getTime()-24*60*60*1000;
        String format = null;
        if (testingtime == null) {
            format = "";
        } else {
            format = new SimpleDateFormat("yyyyMMdd").format(testingtime);
        }
        return format;
    }



    public static String getNowtimeYYMMDD() {
        Long testingtime = new Date(System.currentTimeMillis()).getTime();
        String format = null;
        if (testingtime == null) {
            format = "";
        } else {
            format = new SimpleDateFormat("yyMMdd").format(testingtime);
        }
        return format;
    }

    public static String getNowtimeYY_MM_DD() {
        Long testingtime = new Date(System.currentTimeMillis()).getTime();
        String format = null;
        if (testingtime == null) {
            format = "";
        } else {
            format = new SimpleDateFormat("yyyy-MM-dd").format(testingtime);
        }
        return format;
    }



    public static String getFileNameNowtimeYYMMDD() {
        Long testingtime = new Date(System.currentTimeMillis()).getTime();
        String format = null;
        if (testingtime == null) {
            format = "";
        } else {
            format = new SimpleDateFormat("yyyy_MM_dd").format(testingtime);
        }
        return format;
    }

    public static String getNowtimeyyymmddhhmmss() {
        Long testingtime = new Date(System.currentTimeMillis()).getTime();
        String format = null;
        if (testingtime == null) {
            format = "";
        } else {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(testingtime);
        }
        return format;
    }

    public static Long getNowtimehhmmss_long() {
        Long testingtime = new Date(System.currentTimeMillis()).getTime();
        String format = null;
        if (testingtime == null) {
            format = "";
        } else {
            format = new SimpleDateFormat("HH:mm:ss").format(testingtime);
        }
        return stringHHMMSSDataLong(format);
    }


    public static String getNowtimeyyymmddThhmmss() {
        Long testingtime = new Date(System.currentTimeMillis()).getTime();
        String format = null;
        if (testingtime == null) {
            format = "";
        } else {
            format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(testingtime);
        }
        return format;
    }

    public static String getFIleNameNowtimeyyymmddhhmmss() {
        Long testingtime = new Date(System.currentTimeMillis()).getTime();
        String format = null;
        if (testingtime == null) {
            format = "";
        } else {
            format = new SimpleDateFormat("yyyyMMddHHmmss").format(testingtime);
        }
        LogUtils.d(format);
        //速度太快生成的文件名一样会导致文件被覆盖
        return format+"_"+System.currentTimeMillis();
    }

    public static String getNowtimehhmmss() {
        Long testingtime = new Date(System.currentTimeMillis()).getTime();
        String format = null;
        if (testingtime == null) {
            format = "";
        } else {
            format = new SimpleDateFormat("HH:mm:ss").format(testingtime);
        }

        return format;
    }


    public static String getTimeYYMMDD(Long testingtime) {
        String format = null;
        if (testingtime == null) {
            format = "";
        } else {
            format = new SimpleDateFormat("yyyy-MM-dd").format(testingtime);
        }
        return format;
    }

    public static String getTimeyyymmddhhmmss(Long testingtime) {
        String format = null;
        if (testingtime == null) {
            format = "";
        } else {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(testingtime);
        }
        return format;
    }

    public static String getTimehhmmss(Long testingtime) {
        String format = null;
        if (testingtime == null) {
            format = "";
        } else {
            format = new SimpleDateFormat("HH:mm:ss").format(testingtime);
        }

        return format;
    }

    public static final SimpleDateFormat DEFAULT_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static String currentTime() {
        Date date = new Date();
        return DEFAULT_FORMAT.format(date);
    }
}
