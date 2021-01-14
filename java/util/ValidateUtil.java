package com.post.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Barkley.Chang
 * @className:ValidateUtil
 * @description: 验证工具类
 * @date 2021-01-14 08:32
 */
public final class ValidateUtil {
    private final static Map<Integer, String> zoneNum = new HashMap<Integer, String>();
    private final static int[] PARITYBIT = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
    private final static int[] POWER_LIST = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    private ValidateUtil() {
    }

    static {
        zoneNum.put(11, "北京");
        zoneNum.put(12, "天津");
        zoneNum.put(13, "河北");
        zoneNum.put(14, "山西");
        zoneNum.put(15, "内蒙古");
        zoneNum.put(21, "辽宁");
        zoneNum.put(22, "吉林");
        zoneNum.put(23, "黑龙江");
        zoneNum.put(31, "上海");
        zoneNum.put(32, "江苏");
        zoneNum.put(33, "浙江");
        zoneNum.put(34, "安徽");
        zoneNum.put(35, "福建");
        zoneNum.put(36, "江西");
        zoneNum.put(37, "山东");
        zoneNum.put(41, "河南");
        zoneNum.put(42, "湖北");
        zoneNum.put(43, "湖南");
        zoneNum.put(44, "广东");
        zoneNum.put(45, "广西");
        zoneNum.put(46, "海南");
        zoneNum.put(50, "重庆");
        zoneNum.put(51, "四川");
        zoneNum.put(52, "贵州");
        zoneNum.put(53, "云南");
        zoneNum.put(54, "西藏");
        zoneNum.put(61, "陕西");
        zoneNum.put(62, "甘肃");
        zoneNum.put(63, "青海");
        zoneNum.put(64, "宁夏");
        zoneNum.put(65, "新疆");
        zoneNum.put(71, "台湾");
        zoneNum.put(81, "香港");
        zoneNum.put(82, "澳门");
        zoneNum.put(91, "外国");
    }

    /**
     * 验证码密码是否合规
     *
     * @param password
     * @return
     */
    public static boolean isPasswordValid(String password) {
        if (password == null || password.trim().length() == 0) {
            return false;
        }

        return Pattern.matches("^[0-9A-Za-z#@!~%^&*]{6,20}$", password);
    }

    /**
     * 验证日期格式:yyyy-MM-dd HH:mm:ss
     *
     * @param dateTime
     * @return
     */
    public static boolean isDateTimeValid(String dateTime) {
        if (dateTime == null || dateTime.trim().length() == 0) {
            return false;
        }

        return Pattern.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$", dateTime);
    }

    /**
     * 验证中文姓名
     *
     * @param name
     * @return
     */
    public static boolean isChineseNameValid(String name) {
        if (name == null || name.trim().length() == 0) {
            return false;
        }

        return Pattern.matches("^[\u4E00-\u9FA5]{2,}$", name);
    }

    /**
     * 验证中文
     *
     * @param content
     * @return
     */
    public static boolean isChineseValid(String content) {
        if (content == null || content.trim().length() == 0) {
            return false;
        }

        return Pattern.matches("^[\u4e00-\u9fa5]{0,}$", content);
    }

    /**
     * 验证大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 145,147,149
     * 15+除4的任意数(不要写^4，这样的话字母也会被认为是正确的)
     * 166
     * 17+3,5,6,7,8
     * 18+任意数
     * 198,199
     *
     * @param mobile
     * @return
     */
    public static boolean isChinaPhoneValid(String mobile) {
        if (mobile == null || mobile.trim().length() == 0) {
            return false;
        }

        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        return Pattern.matches("^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])|(18[0-9])|(19[8,9]))\\d{8}$", mobile);
    }

    /**
     * 验证香港手机号码8位数，5|6|8|9开头+7位任意数
     *
     * @param mobile
     * @return
     */
    public static boolean isHKPhoneValid(String mobile) {
        if (mobile == null || mobile.trim().length() == 0) {
            return false;
        }

        // ^ 匹配输入字符串开始的位置
        // \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
        // $ 匹配输入字符串结尾的位置
        return Pattern.matches("^(5|6|8|9)\\d{7}$", mobile);
    }

    /**
     * 身份证号验证
     *
     * @param certNo 身份证号
     * @return 是否有效null和""都是false
     */
    public static boolean isIDCardValid(String certNo) {
        if (certNo == null || (certNo.length() != 15 && certNo.length() != 18)) {
            return false;
        }

        final char[] cs = certNo.toUpperCase().toCharArray();
        //校验位数
        int power = 0;
        for (int i = 0; i < cs.length; i++) {
            if (i == cs.length - 1 && cs[i] == 'X') {
                break;//最后一位可以是X或x
            }

            if (cs[i] < '0' || cs[i] > '9') {
                return false;
            }

            if (i < cs.length - 1) {
                power += (cs[i] - '0') * POWER_LIST[i];
            }
        }

        //校验区位码
        if (!zoneNum.containsKey(Integer.valueOf(certNo.substring(0, 2)))) {
            return false;
        }

        //校验年份
        String year = null;
        year = certNo.length() == 15 ? getIdcardCalendar(certNo) : certNo.substring(6, 10);

        final int iyear = Integer.parseInt(year);
        if (iyear < 1900 || iyear > Calendar.getInstance().get(Calendar.YEAR)) {
            return false;//1900年的PASS，超过今年的PASS
        }

        //校验月份
        String month = certNo.length() == 15 ? certNo.substring(8, 10) : certNo.substring(10, 12);
        final int imonth = Integer.parseInt(month);
        if (imonth < 1 || imonth > 12) {
            return false;
        }

        //校验天数
        String day = certNo.length() == 15 ? certNo.substring(10, 12) : certNo.substring(12, 14);
        final int iday = Integer.parseInt(day);
        if (iday < 1 || iday > 31) {
            return false;
        }

        //校验"校验码"
        if (certNo.length() == 15) {
            return true;
        }

        return cs[cs.length - 1] == PARITYBIT[power % 11];
    }

    /**
     * 获取身份证号中的日期
     *
     * @param certNo
     * @return
     */
    private static String getIdcardCalendar(String certNo) {
        // 获取出生年月日
        String birthday = certNo.substring(6, 12);
        SimpleDateFormat ft = new SimpleDateFormat("yyMMdd");
        Date birthdate = null;

        try {
            birthdate = ft.parse(birthday);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        Calendar cday = Calendar.getInstance();
        cday.setTime(birthdate);
        String year = String.valueOf(cday.get(Calendar.YEAR));

        return year;
    }
}