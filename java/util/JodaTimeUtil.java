package com.post.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.Date;

/**
 * @author Barkley.Chang
 * @className:JodaTimeUtil
 * @description: joda-time工具类
 * @date 2021-02-19 09:12
 */
public final class JodaTimeUtil {
    // 时间格式
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private JodaTimeUtil() {
    }

    // 获取当前时间
    public static DateTime getCurrentTime() {
        return new DateTime();
    }

    // 获取指定时间
    public static DateTime getDateTime(Object obj) {
        return new DateTime(obj);
    }

    // 把时间以指定格式转换为字符串
    public static String getNowDate(Date date, String format) {
        return new DateTime(date).toString(format);
    }

    // 获取星期时间
    public static String getWeek(Object date) {
        DateTime time = getDateTime(date);
        String week = null;
        switch (time.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                week = "星期日";
                break;
            case DateTimeConstants.MONDAY:
                week = "星期一";
                break;
            case DateTimeConstants.TUESDAY:
                week = "星期二";
                break;
            case DateTimeConstants.WEDNESDAY:
                week = "星期三";
                break;
            case DateTimeConstants.THURSDAY:
                week = "星期四";
                break;
            case DateTimeConstants.FRIDAY:
                week = "星期五";
                break;
            case DateTimeConstants.SATURDAY:
                week = "星期六";
                break;
            default:
                break;
        }
        return week;
    }
}