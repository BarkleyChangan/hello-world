package com.sxpost.icbcapi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date工具类
 *
 * @author Barkley.Chang
 * @date 2018年7月16日 下午4:48:51
 */
public final class DateUtil {
    private static ThreadLocal<SimpleDateFormat> tlSimpleDateFormat = new ThreadLocal<SimpleDateFormat>();
    public static final String PATTERN_DEFAULT_DATE = "yyyy-MM-dd";
    public static final String PATTERN_DEFAULT_DATETIME = "yyyy-MM-dd HH:mm:ss";

    private DateUtil(){
        throw new UnsupportedOperationException();
    }

    public static String getOrDefaultDate(Date date) {
        if (date == null) {
            return "1970-01-01 08:00:01";
        }

        String result = dateToString(date, com.sxpost.yyqdapi.util.DateUtil.PATTERN_DEFAULT_DATETIME);
        remove();

        return result;
    }

    /**
     * 根据指定的日期格式获取SimpleDateFormat对象 (默认为yyyy-MM-dd)
     *
     * @param datePattern
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat getSimpleDateFormat(String datePattern) {
        if (datePattern == null || datePattern.trim().length() == 0) {
            datePattern = PATTERN_DEFAULT_DATE;
        }

        SimpleDateFormat sdf = tlSimpleDateFormat.get();

        if (sdf == null) {
            sdf = new SimpleDateFormat(datePattern);
            tlSimpleDateFormat.set(sdf);
        } else {
            sdf.applyPattern(datePattern);
        }

        return sdf;
    }

    /**
     * 根据指定的日期格式获取SimpleDateFormat对象 (默认为yyyy-MM-dd HH:mm:ss)
     *
     * @param dateTimePattern
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat getSimpleDateTimeFormat(String dateTimePattern) {
        if (dateTimePattern == null || dateTimePattern.trim().length() == 0) {
            dateTimePattern = PATTERN_DEFAULT_DATETIME;
        }

        SimpleDateFormat sdf = tlSimpleDateFormat.get();

        if (sdf == null) {
            sdf = new SimpleDateFormat(dateTimePattern);
            tlSimpleDateFormat.set(sdf);
        } else {
            sdf.applyPattern(dateTimePattern);
        }

        return sdf;
    }

    /**
     * 根据指定的日期格式获取Date对象
     *
     * @param strDate
     * @param pattern
     * @return Date
     */
    public static Date getDate(String strDate, String pattern) {
        Date date = null;

        if (strDate != null && strDate.trim().length() > 0) {
            try {
                SimpleDateFormat sdf = DateUtil.getSimpleDateFormat(pattern);
                sdf.setLenient(false);
                date = sdf.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return date;
    }

    /**
     * 根据默认的日期格式yyyy-MM-dd获取Date对象
     *
     * @param strDate
     * @return Date
     */
    public static Date getDate(String strDate) {
        Date date = null;

        if (strDate != null && strDate.trim().length() > 0) {
            try {
                SimpleDateFormat sdf = DateUtil.getSimpleDateFormat(PATTERN_DEFAULT_DATE);
                sdf.setLenient(false);
                date = sdf.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return date;
    }

    /**
     * 根据默认的日期时间格式yyyy-MM-dd HH:mm:ss获取Date对象
     *
     * @param strDateTime
     * @return Date
     */
    public static Date getDateTime(String strDateTime) {
        Date date = null;

        if (strDateTime != null && strDateTime.trim().length() > 0) {
            try {
                SimpleDateFormat sdf = DateUtil.getSimpleDateFormat(PATTERN_DEFAULT_DATETIME);
                sdf.setLenient(false);
                date = sdf.parse(strDateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return date;
    }

    /**
     * 根据指定的日期格式获取格式化后的日期字符串
     *
     * @param date
     * @param pattern
     * @return String
     */
    public static String dateToString(Date date, String pattern) {
        if (date == null) {
            return "";
        }

        String result = DateUtil.getSimpleDateFormat(pattern).format(date);
        return result;
    }

    /**
     * 根据默认的日期格式yyyy-MM-dd获取格式化后的日期字符串
     *
     * @param date
     * @return String
     */
    public static String dateToString(Date date) {
        if (date == null) {
            return "";
        }

        String result = DateUtil.getSimpleDateFormat(PATTERN_DEFAULT_DATE).format(date);
        return result;
    }

    /**
     * 根据默认的日期时间格式yyyy-MM-dd HH:mm:ss获取格式化后的日期时间字符串
     *
     * @param dateTime
     * @return String
     */
    public static String dateTimeToString(Date dateTime) {
        if (dateTime == null) {
            return "";
        }

        String result = DateUtil.getSimpleDateFormat(PATTERN_DEFAULT_DATETIME).format(dateTime);
        return result;
    }

    /**
     * 清理ThreadLocal操作
     */
    public static void remove() {
        tlSimpleDateFormat.remove();
    }

    /**
     * 获取当前微秒
     *
     * @param currentTimeMillis 当前毫秒数
     * @return
     */
    public static long getMicTime(long currentTimeMillis) {
        long cutime = currentTimeMillis * 1000;    // 微秒
        long nanoTime = System.nanoTime();         // 纳秒
        return cutime + (nanoTime - nanoTime / 1000000 * 1000000) / 1000;
    }
}