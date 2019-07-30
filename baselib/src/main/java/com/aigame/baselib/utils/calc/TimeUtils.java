package com.aigame.baselib.utils.calc;

import android.support.annotation.NonNull;

import com.aigame.baselib.constance.TimeConstants;
import com.aigame.baselib.utils.StringUtils;
import com.aigame.baselib.utils.crash.ExceptionUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <pre>
 *     author: songguobin
 *     desc  : time相关工具类
 * </pre>
 */
public final class TimeUtils {

    private static final DateFormat DEFAULT_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    private TimeUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取“yyyy-MM-dd HH:mm:ss”格式化时间
     *
     * @return “yyyy-MM-dd HH:mm:ss”格式化时间
     */
    public static String formatDate() {
        return DEFAULT_FORMAT.format(new Date());
    }

    /**
     * 获取格式化的时间
     *
     * @param pattern 时间格式
     * @return 格式化的时间
     */
    public static String formatDate(String pattern) {
        SimpleDateFormat sdf = null;
        if (StringUtils.isEmpty(pattern)) {
            sdf = (SimpleDateFormat) DEFAULT_FORMAT;
        } else {
            sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        }
        return sdf.format(new Date());
    }

    /**
     * 获取时区信息
     *
     * @return
     */
    public static String getTimeArea() {

        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
        TimeZone zone = dateFormat.getTimeZone();
        int timeArea = zone.getOffset(date.getTime()) / 1000;

        return timeArea + "";
    }

    /**
     * 获取时长
     *
     * @param duration
     * @return
     */
    public static String getDuration(String duration) {
        if (StringUtils.isEmpty(duration)) {
            return "00:00";
        }
        if (duration.contains(":")) {
            if (duration.startsWith("00:")) {
                return duration.substring("00:".length());
            }
            return duration;
        }
        long s = StringUtils.toLong(duration, 0L);
        String str = convertSecondsToDuration(s);
        if (str.startsWith("00:")) {
            return str.substring("00:".length());
        }
        return str;
    }

    /**
     * 把秒转化为时长
     *
     * @param seconds
     * @return
     */
    public static String convertSecondsToDuration(long seconds) {
        long days = seconds / (60 * 60 * 24);
        seconds -= days * (60 * 60 * 24);
        long hours = seconds / (60 * 60);
        seconds -= hours * (60 * 60);
        long minutes = seconds / 60;
        seconds -= minutes * (60);

        StringBuffer sb = new StringBuffer();
        if (hours < 10) {
            sb.append("0");
        }
        sb.append(hours);
        sb.append(":");
        if (minutes < 10) {
            sb.append("0");
        }
        sb.append(minutes);
        sb.append(":");
        if (seconds < 10) {
            sb.append("0");
        }
        sb.append(seconds);

        if (days > 0) {
            return "" + days + "天 " + sb.toString();
        } else {
            return "" + sb.toString();
        }
    }


    public static String convertSecondsToString(long seconds) {
        long days = seconds / (60 * 60 * 24);
        seconds -= days * (60 * 60 * 24);
        long hours = seconds / (60 * 60);
        seconds -= hours * (60 * 60);
        long minutes = seconds / 60;

        StringBuffer sb = new StringBuffer();

        if (hours < 10) {
            sb.append("0");
        }
        sb.append(hours);
        sb.append("时");
        if (minutes < 10) {
            sb.append("0");
        }
        sb.append(minutes);
        sb.append("分");

        if (days > 0) {
            return "" + days + "天" + sb.toString();
        } else {
            return "" + sb.toString();
        }
    }


    /**
     * 把秒转化为时长 格式：HH:mm:ss
     */
    public static String convertSecondsToDuration2(long seconds) {
        long hours = seconds / (60 * 60);
        seconds -= hours * (60 * 60);
        long minutes = seconds / 60;
        seconds -= minutes * (60);

        StringBuffer sb = new StringBuffer();
        if (hours < 10) {
            sb.append("0");
        }
        sb.append(hours);
        sb.append(":");
        if (minutes < 10) {
            sb.append("0");
        }
        sb.append(minutes);
        sb.append(":");
        if (seconds < 10) {
            sb.append("0");
        }
        sb.append(seconds);

        return "" + sb.toString();
    }


    /**
     * 判断两个时间是否是同一天
     */
    public static boolean isToday(long ms1, long ms2) {
        long interval = Math.abs(ms1 - ms2);
        return interval < 86400000L && toDay(ms1) == toDay(ms2);
    }

    /**
     * 根据时区转为天数
     */
    public static long toDay(long millis) {
        return (millis + (long) TimeZone.getDefault().getOffset(millis)) / 86400000L;
    }

    /**
     * 当前日期
     * 格式：2018-01-01
     */
    public static String localData() {
        Calendar calendar = Calendar.getInstance();
        int yearValue = calendar.get(Calendar.YEAR);
        int monthValue = calendar.get(Calendar.MONTH) + 1;
        int dayValue = calendar.get(Calendar.DAY_OF_MONTH);
        StringBuilder buf = new StringBuilder(10);
        buf.append(yearValue);
        return buf.append(monthValue < 10 ? "-0" : "-")
                .append(monthValue)
                .append(dayValue < 10 ? "-0" : "-")
                .append(dayValue)
                .toString();
    }

    /**
     * 格式化时间戳
     *
     * @param timeStamp String类型的时间戳
     * @param pattern   时间格式
     * @return 格式化时间戳
     */
    public static String formatTime(String timeStamp, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        long time = StringUtils.toLong(timeStamp, 0L);
        return format.format(time);
    }

    public static long parseTime(String str, String pattern) throws ParseException {
        Date date = new SimpleDateFormat(pattern, Locale.getDefault()).parse(str);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        if (year == 1970) {
            calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        }
        return calendar.getTime().getTime();
    }

    /**
     * 将时间戳转化为“yyyy-MM-dd HH:mm:ss”格式的时间.
     *
     * @param millis 时间戳.
     * @return the “yyyy-MM-dd HH:mm:ss”格式化的时间
     */
    public static String millis2String(final long millis) {
        return millis2String(millis, DEFAULT_FORMAT);
    }

    /**
     * 时间戳转化为格式化的时间.
     *
     * @param millis 时间戳.
     * @param format 时间格式.
     * @return 格式化的时间
     */
    public static String millis2String(final long millis, @NonNull final DateFormat format) {
        return format.format(new Date(millis));
    }

    /**
     * 将“yyyy-MM-dd HH:mm:ss”格式的时间转化为时间戳.
     *
     * @param time “yyyy-MM-dd HH:mm:ss”格式化的时间.
     * @return 时间戳
     */
    public static long string2Millis(final String time) {
        return string2Millis(time, DEFAULT_FORMAT);
    }

    /**
     * 将format格式的时间转化为时间戳.
     *
     * @param time   格式化的时间.
     * @param format 时间格式.
     * @return 时间戳
     */
    public static long string2Millis(final String time, @NonNull final DateFormat format) {
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            ExceptionUtils.printStackTrace(e);
        }
        return -1;
    }

    /**
     * 将“yyyy-MM-dd HH:mm:ss”格式的时间转化为Date.
     *
     * @param time 格式化的时间.
     * @return date
     */
    public static Date string2Date(final String time) {
        return string2Date(time, DEFAULT_FORMAT);
    }

    /**
     * 将格式化的时间转化为Date
     *
     * @param time   格式化的时间.
     * @param format 时间格式.
     * @return Date
     */
    public static Date string2Date(final String time, @NonNull final DateFormat format) {
        try {
            return format.parse(time);
        } catch (ParseException e) {
            ExceptionUtils.printStackTrace(e);
        }
        return null;
    }

    /**
     * 将date转化为"yyyy-MM-dd HH:mm:ss"格式的时间.
     *
     * @param date date.
     * @return "yyyy-MM-dd HH:mm:ss"格式的时间
     */
    public static String date2String(final Date date) {
        return date2String(date, DEFAULT_FORMAT);
    }

    /**
     * 将Date转化为格式化的时间.
     *
     * @param date   date.
     * @param format 时间格式.
     * @return 格式化的时间
     */
    public static String date2String(final Date date, @NonNull final DateFormat format) {
        return format.format(date);
    }

    /**
     * 将Date转化为时间戳.
     *
     * @param date .
     * @return 时间戳
     */
    public static long date2Millis(final Date date) {
        return date.getTime();
    }

    /**
     * 时间戳转化为Date.
     *
     * @param millis 时间戳.
     * @return date
     */
    public static Date millis2Date(final long millis) {
        return new Date(millis);
    }

    /**
     * 计算yyyy-MM-dd HH:mm:ss格式的两个时间，在unit单位上的差值.
     *
     * @param time1 第一个yyyy-MM-dd HH:mm:ss格式的时间.
     * @param time2 第二个yyyy-MM-dd HH:mm:ss格式的时间.
     * @param unit  时间单位.
     *              <ul>
     *              <li>{@link TimeConstants#MSEC}</li>
     *              <li>{@link TimeConstants#SEC }</li>
     *              <li>{@link TimeConstants#MIN }</li>
     *              <li>{@link TimeConstants#HOUR}</li>
     *              <li>{@link TimeConstants#DAY }</li>
     *              </ul>
     * @return 时间单位上的差值
     */
    public static long getTimeSpan(final String time1,
                                   final String time2,
                                   final int unit) {
        return getTimeSpan(time1, time2, DEFAULT_FORMAT, unit);
    }

    /**
     * 计算格式的两个时间，在unit单位上的差值.
     *
     * @param time1  第一个格式化的时间.
     * @param time2  第二个格式化的时间.
     * @param format 时间格式.
     * @param unit   时间单位.
     *               <ul>
     *               <li>{@link TimeConstants#MSEC}</li>
     *               <li>{@link TimeConstants#SEC }</li>
     *               <li>{@link TimeConstants#MIN }</li>
     *               <li>{@link TimeConstants#HOUR}</li>
     *               <li>{@link TimeConstants#DAY }</li>
     *               </ul>
     * @return 时间单位上的差值
     */
    public static long getTimeSpan(final String time1,
                                   final String time2,
                                   @NonNull final DateFormat format,
                                   final int unit) {
        return millis2TimeSpan(string2Millis(time1, format) - string2Millis(time2, format), unit);
    }

    /**
     * 比较两个Date在时间单位unit上的差值.
     *
     * @param date1 第一个date.
     * @param date2 第二个date.
     * @param unit  时间单位.
     *              <ul>
     *              <li>{@link TimeConstants#MSEC}</li>
     *              <li>{@link TimeConstants#SEC }</li>
     *              <li>{@link TimeConstants#MIN }</li>
     *              <li>{@link TimeConstants#HOUR}</li>
     *              <li>{@link TimeConstants#DAY }</li>
     *              </ul>
     * @return 时间单位上的差值
     */
    public static long getTimeSpan(final Date date1,
                                   final Date date2,
                                   final int unit) {
        return millis2TimeSpan(date2Millis(date1) - date2Millis(date2), unit);
    }

    /**
     * 计算两个long类型时间，在unit单位上的差值.
     *
     * @param millis1 第一个long类型时间.
     * @param millis2 第二个long类型时间.
     * @param unit    时间单位.
     *                <ul>
     *                <li>{@link TimeConstants#MSEC}</li>
     *                <li>{@link TimeConstants#SEC }</li>
     *                <li>{@link TimeConstants#MIN }</li>
     *                <li>{@link TimeConstants#HOUR}</li>
     *                <li>{@link TimeConstants#DAY }</li>
     *                </ul>
     * @return 时间单位上的差值
     */
    public static long getTimeSpan(final long millis1,
                                   final long millis2,
                                   final int unit) {
        return millis2TimeSpan(millis1 - millis2, unit);
    }

    /**
     * 获取合适型两个时间差.
     * <p>time1和time2的格式都为yyyy-MM-dd HH:mm:ss.</p>
     *
     * @param time1     第一个时间字符串.
     * @param time2     第二个时间字符串.
     * @param precision 时间精度.
     *                  <ul>
     *                  <li>precision = 0,返回null</li>
     *                  <li>precision = 1, 返回天</li>
     *                  <li>precision = 2, 返回天和小时</li>
     *                  <li>precision = 3, 返回天,小时和分钟</li>
     *                  <li>precision = 4, 返回天, 小时, 分钟和秒</li>
     *                  <li>precision>=5，返回天, 小时, 分钟, 秒和毫秒</li>
     *                  </ul>
     * @return 合适型两个时间差
     */
    public static String getFitTimeSpan(final String time1,
                                        final String time2,
                                        final int precision) {
        long delta = string2Millis(time1, DEFAULT_FORMAT) - string2Millis(time2, DEFAULT_FORMAT);
        return millis2FitTimeSpan(delta, precision);
    }

    /**
     * 获取合适型两个时间差.
     * <p>time1和time2的格式都为format</p>
     *
     * @param time1     第一个时间字符串.
     * @param time2     第二个时间字符串.
     * @param format    T时间格式.
     * @param precision 时间精度.
     *                  <ul>
     *                  <li>precision = 0,返回null</li>
     *                  <li>precision = 1, 返回天</li>
     *                  <li>precision = 2, 返回天和小时</li>
     *                  <li>precision = 3, 返回天,小时和分钟</li>
     *                  <li>precision = 4, 返回天, 小时, 分钟和秒</li>
     *                  <li>precision>=5，返回天, 小时, 分钟, 秒和毫秒</li>
     *                  </ul>
     * @return 合适型两个时间差
     */
    public static String getFitTimeSpan(final String time1,
                                        final String time2,
                                        @NonNull final DateFormat format,
                                        final int precision) {
        long delta = string2Millis(time1, format) - string2Millis(time2, format);
        return millis2FitTimeSpan(delta, precision);
    }

    /**
     * 获取合适型两个时间差.
     *
     * @param date1     第一个Date类型时间.
     * @param date2     第二个Date类型时间.
     * @param precision 时间精度.
     *                  <ul>
     *                  <li>precision = 0,返回null</li>
     *                  <li>precision = 1, 返回天</li>
     *                  <li>precision = 2, 返回天和小时</li>
     *                  <li>precision = 3, 返回天,小时和分钟</li>
     *                  <li>precision = 4, 返回天, 小时, 分钟和秒</li>
     *                  <li>precision>=5，返回天, 小时, 分钟, 秒和毫秒</li>
     *                  </ul>
     * @return 合适型两个时间差
     */
    public static String getFitTimeSpan(final Date date1, final Date date2, final int precision) {
        return millis2FitTimeSpan(date2Millis(date1) - date2Millis(date2), precision);
    }

    /**
     * 获取合适型两个时间差.
     *
     * @param millis1   第一个毫秒时间戳.
     * @param millis2   第二个毫秒时间戳.
     * @param precision 时间精度.
     *                  <ul>
     *                  <li>precision = 0,返回null</li>
     *                  <li>precision = 1, 返回天</li>
     *                  <li>precision = 2, 返回天和小时</li>
     *                  <li>precision = 3, 返回天,小时和分钟</li>
     *                  <li>precision = 4, 返回天, 小时, 分钟和秒</li>
     *                  <li>precision>=5，返回天, 小时, 分钟, 秒和毫秒</li>
     *                  </ul>
     * @return 合适型两个时间差
     */
    public static String getFitTimeSpan(final long millis1,
                                        final long millis2,
                                        final int precision) {
        return millis2FitTimeSpan(millis1 - millis2, precision);
    }

    /**
     * 获取当前毫秒时间戳.
     *
     * @return 当前毫秒时间戳
     */
    public static long getNowMills() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间字符串
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @return 时间字符串
     */
    public static String getNowString() {
        return millis2String(System.currentTimeMillis(), DEFAULT_FORMAT);
    }

    /**
     * 获取当前时间字符串
     *
     * @param format 时间格式
     * @return 时间字符串
     */
    public static String getNowString(@NonNull final DateFormat format) {
        return millis2String(System.currentTimeMillis(), format);
    }

    /**
     * 获取当前Date
     *
     * @return Date类型时间
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取与当前时间在单位类型上的的差
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @param unit 单位类型.
     *             <ul>
     *             <li>{@link TimeConstants#MSEC}:毫秒</li>
     *             <li>{@link TimeConstants#SEC }:秒</li>
     *             <li>{@link TimeConstants#MIN }:分</li>
     *             <li>{@link TimeConstants#HOUR}:小时</li>
     *             <li>{@link TimeConstants#DAY }:天</li>
     *             </ul>
     * @return 单位类型时间差
     */
    public static long getTimeSpanByNow(final String time, final int unit) {
        return getTimeSpan(time, getNowString(), DEFAULT_FORMAT, unit);
    }

    /**
     * 获取与当前时间在单位类型上的的差
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time   时间字符串
     * @param format 时间格式
     * @param unit   单位类型.
     *               <ul>
     *               <li>{@link TimeConstants#MSEC}:毫秒</li>
     *               <li>{@link TimeConstants#SEC }:秒</li>
     *               <li>{@link TimeConstants#MIN }:分</li>
     *               <li>{@link TimeConstants#HOUR}:小时</li>
     *               <li>{@link TimeConstants#DAY }:天</li>
     *               </ul>
     * @return 单位类型时间差
     */
    public static long getTimeSpanByNow(final String time,
                                        @NonNull final DateFormat format,
                                        final int unit) {
        return getTimeSpan(time, getNowString(format), format, unit);
    }


    /**
     * 获取与当前时间在单位类型上的的差
     *
     * @param date Date类型时间
     * @param unit 单位类型.
     *             <ul>
     *             <li>{@link TimeConstants#MSEC}:毫秒</li>
     *             <li>{@link TimeConstants#SEC }:秒</li>
     *             <li>{@link TimeConstants#MIN }:分</li>
     *             <li>{@link TimeConstants#HOUR}:小时</li>
     *             <li>{@link TimeConstants#DAY }:天</li>
     *             </ul>
     * @return 单位类型时间差
     */
    public static long getTimeSpanByNow(final Date date, final int unit) {
        return getTimeSpan(date, new Date(), unit);
    }


    /**
     * 获取与当前时间在单位类型上的的差
     *
     * @param millis 时间戳
     * @param unit   单位类型.
     *               <ul>
     *               <li>{@link TimeConstants#MSEC}:毫秒</li>
     *               <li>{@link TimeConstants#SEC }:秒</li>
     *               <li>{@link TimeConstants#MIN }:分</li>
     *               <li>{@link TimeConstants#HOUR}:小时</li>
     *               <li>{@link TimeConstants#DAY }:天</li>
     *               </ul>
     * @return 单位类型时间差
     */
    public static long getTimeSpanByNow(final long millis, final int unit) {
        return getTimeSpan(millis, System.currentTimeMillis(), unit);
    }

    /**
     * 获取合适型与当前时间的差
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time      时间字符串.
     * @param precision 精度.
     *                  <ul>
     *                  <li>precision = 0，返回 null</li>
     *                  <li>precision = 1，返回天</li>
     *                  <li>precision = 2，返回天和小时</li>
     *                  <li>precision = 3，返回天、小时和分钟</li>
     *                  <li>precision = 4，返回天、小时、分钟和秒</li>
     *                  <li>precision 》=5，返回天、小时、分钟、秒和毫秒</li>
     *                  </ul>
     * @return 合适型与当前时间的差
     */
    public static String getFitTimeSpanByNow(final String time, final int precision) {
        return getFitTimeSpan(time, getNowString(), DEFAULT_FORMAT, precision);
    }

    /**
     * 获取合适型与当前时间的差
     *
     * @param time      时间字符串.
     * @param format    时间格式
     * @param precision 精度.
     *                  <ul>
     *                  <li>precision = 0，返回 null</li>
     *                  <li>precision = 1，返回天</li>
     *                  <li>precision = 2，返回天和小时</li>
     *                  <li>precision = 3，返回天、小时和分钟</li>
     *                  <li>precision = 4，返回天、小时、分钟和秒</li>
     *                  <li>precision 》=5，返回天、小时、分钟、秒和毫秒</li>
     *                  </ul>
     * @return 合适型与当前时间的差
     */
    public static String getFitTimeSpanByNow(final String time,
                                             @NonNull final DateFormat format,
                                             final int precision) {
        return getFitTimeSpan(time, getNowString(format), format, precision);
    }

    /**
     * 获取与当前时间的差，返回类型如下
     *
     * @param date      Date类型时间.
     * @param precision 精度.
     *                  <ul>
     *                  <li>precision = 0，返回 null</li>
     *                  <li>precision = 1，返回天</li>
     *                  <li>precision = 2，返回天和小时</li>
     *                  <li>precision = 3，返回天、小时和分钟</li>
     *                  <li>precision = 4，返回天、小时、分钟和秒</li>
     *                  <li>precision 》=5，返回天、小时、分钟、秒和毫秒</li>
     *                  </ul>
     * @return 合适型与当前时间的差
     */
    public static String getFitTimeSpanByNow(final Date date, final int precision) {
        return getFitTimeSpan(date, getNowDate(), precision);
    }

    /**
     * 获取与当前时间的差，返回类型如下
     *
     * @param millis    毫秒时间戳.
     * @param precision 精度.
     *                  <ul>
     *                  <li>precision = 0，返回 null</li>
     *                  <li>precision = 1，返回天</li>
     *                  <li>precision = 2，返回天和小时</li>
     *                  <li>precision = 3，返回天、小时和分钟</li>
     *                  <li>precision = 4，返回天、小时、分钟和秒</li>
     *                  <li>precision 》=5，返回天、小时、分钟、秒和毫秒</li>
     *                  </ul>
     * @return 合适型与当前时间的差
     */
    public static String getFitTimeSpanByNow(final long millis, final int precision) {
        return getFitTimeSpan(millis, System.currentTimeMillis(), precision);
    }


    /**
     * 获取友好型与当前时间的差
     *
     * @param millis 时间戳.
     * @return t友好型与当前时间的差
     * <ul>
     * <li>如果小于1秒钟内，显示刚刚</li>
     * <li>如果在1分钟内，显示 XXX秒前</li>
     * <li>如果在1小时内，显示 XXX分钟前</li>
     * <li>如果在1小时外的今天内，显示今天15:32</li>
     * <li>如果是昨天的，显示昨天15:32</li>
     * <li>其余显示，2016-10-15</li>
     * <li>时间不合法的情况全部日期和时间信息，如星期六 十月 27 14:21:20 CST 2007</li>
     * </ul>
     */
    public static String getFriendlyTimeSpanByNow(final long millis) {
        long now = System.currentTimeMillis();
        long span = now - millis;
        if (span < 0)
        // 参考：http://www.apihome.cn/api/java/Formatter.html to understand it.
        {
            return String.format("%tc", millis);
        }
        if (span < 1000) {
            return "刚刚";
        } else if (span < TimeConstants.MIN) {
            return String.format(Locale.getDefault(), "%d秒前", span / TimeConstants.SEC);
        } else if (span < TimeConstants.HOUR) {
            return String.format(Locale.getDefault(), "%d分钟前", span / TimeConstants.MIN);
        }
        // 获取当天 00:00
        long wee = getWeeOfToday();
        if (millis >= wee) {
            return String.format("今天%tR", millis);
        } else if (millis >= wee - TimeConstants.DAY) {
            return String.format("昨天%tR", millis);
        } else {
            return String.format("%tF", millis);
        }
    }

    private static long getWeeOfToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }


    /**
     * 判断是否是今天.
     *
     * @param millis 时间戳.
     * @return {@code true}: 是今天<br>{@code false}: 不是今天
     */
    public static boolean isToday(final long millis) {
        long wee = getWeeOfToday();
        return millis >= wee && millis < wee + TimeConstants.DAY;
    }

    /**
     * 判断是否闰年
     *
     * @param date Date类型时间.
     * @return {@code true}: 闰年<br>{@code false}: 平年
     */
    public static boolean isLeapYear(final Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        return isLeapYear(year);
    }

    /**
     * 判断是否闰年
     *
     * @param year 年份.
     * @return {@code true}: 闰年<br>{@code false}: 平年
     */
    public static boolean isLeapYear(final int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }


    /**
     * 获取date在Calendar变量(年，月，日等)的值;如field={@link Calendar#YEAR}，获取date是哪一年
     *
     * @param date  Date类型时间.
     * @param field Calendar变量域的.
     *              <ul>
     *              <li>{@link Calendar#ERA}:0表示在公元纪年之前，返回1表示在公元纪年之后</li>
     *              <li>{@link Calendar#YEAR}：年</li>
     *              <li>{@link Calendar#MONTH}：月</li>
     *              <li>...</li>
     *              <li>{@link Calendar#DST_OFFSET}：时间距夏令时的毫秒数</li>
     *              </ul>
     * @return tdate在Calendar变量(年, 月, 日等)的值
     */
    public static int getValueByCalendarField(final Date date, final int field) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(field);
    }

    /**
     * 获取date在Calendar变量(年，月，日等)的值;如field={@link Calendar#YEAR}，获取date是哪一年
     *
     * @param millis 时间戳.
     * @param field  Calendar变量域的.
     *               <ul>
     *               <li>{@link Calendar#ERA}:0表示在公元纪年之前，返回1表示在公元纪年之后</li>
     *               <li>{@link Calendar#YEAR}：年</li>
     *               <li>{@link Calendar#MONTH}：月</li>
     *               <li>...</li>
     *               <li>{@link Calendar#DST_OFFSET}：时间距夏令时的毫秒数</li>
     *               </ul>
     * @return tdate在Calendar变量(年, 月, 日等)的值
     */
    public static int getValueByCalendarField(final long millis, final int field) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return cal.get(field);
    }


    private static long timeSpan2Millis(final long timeSpan, final int unit) {
        return timeSpan * unit;
    }

    private static long millis2TimeSpan(final long millis, final int unit) {
        return millis / unit;
    }

    private static String millis2FitTimeSpan(long millis, int precision) {
        if (precision <= 0) {
            return null;
        }
        precision = Math.min(precision, 5);
        String[] units = {"天", "小时", "分钟", "秒", "毫秒"};
        if (millis == 0) {
            return 0 + units[precision - 1];
        }
        StringBuilder sb = new StringBuilder();
        if (millis < 0) {
            sb.append("-");
            millis = -millis;
        }
        int[] unitLen = {86400000, 3600000, 60000, 1000, 1};
        for (int i = 0; i < precision; i++) {
            if (millis >= unitLen[i]) {
                long mode = millis / unitLen[i];
                millis -= mode * unitLen[i];
                sb.append(mode).append(units[i]);
            }
        }
        return sb.toString();
    }
}
