package com.aigame.baselib.utils;

import android.graphics.Paint;
import android.text.TextUtils;

import com.aigame.baselib.utils.crash.ExceptionUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *     author: songguobin
 *     desc  : string相关工具类
 * </pre>
 */
public final class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static final String DIGIT_PATTERN = "[0-9]*";

    private static final String HOST_PATTERN = "(?<=//|)([\\w\\-]+\\.)+\\w+";

    public static String encoding(String str) {
        return isEmpty(str) ? "" : URLEncoder.encode(str);
    }

    public static String decoding(String str) {

        return isEmpty(str) ? "" : URLDecoder.decode(str);
    }

    public static String getValue(String str) {
        return (str != null) ? str : "null";
    }

    public static String maskNull(String str) {
        return isEmpty(str) ? "" : str;
    }


    /**
     * 获取url里面所有的请求参数
     */
    public static final HashMap<String, String> getQueryParams(String strUrl) {
        HashMap<String, String> queryParams = null;
        if (strUrl != null) {
            int start = strUrl.indexOf('?');
            String substring = strUrl;
            if (start > 0) {
                substring = strUrl.substring(start, strUrl.length());
            }
            //a=b& Worst
            if (substring.length() >= 4) {
                queryParams = new HashMap<>();
                String[] split = substring.split("&");
                for (String s : split) {
                    String[] pair = s.split("=");
                    if (pair.length == 2) {
                        queryParams.put(pair[0], pair[1]);
                    }
                }
            }
        }
        return queryParams;
    }

    /**
     * 获取url里面请求参数
     */
    public static final String getQueryParams(String strUrl, String paramKey) {
        HashMap<String, String> queryParams = getQueryParams(strUrl);
        if (queryParams != null) {
            return queryParams.get(paramKey);
        }
        return null;
    }

    public static boolean isEmpty(String str) {
        if (null == str || "".equals(str) || "null".equals(str)) {
            return true;
        } else {
            return str.length() == 4 && str.toLowerCase().equals("null");
        }
    }

    /**
     * int 默认值
     */
    public static boolean isEmpty(int num) {
        return isEmpty(num, 0);
    }

    /**
     * int 默认值
     */
    public static boolean isEmpty(int num, int defauleNum) {
        return num == defauleNum;
    }

    public static boolean isEmptyStr(String str) {
        return null == str || "".equals(str);
    }

    public static String toStr(Object _obj, String _defaultValue) {
        if (TextUtils.isEmpty(String.valueOf(_obj))) {
            return _defaultValue;
        }

        return String.valueOf(_obj);
    }

    /**
     * 判断两个字符串是否相等.
     *
     * @param a 第一个字符串
     * @param b 第二个字符串.
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        return TextUtils.equals(a, b);
    }

    /**
     * 忽略大小写判断两个字符串是否相等
     *
     * @param s1 第一个字符串.
     * @param s2 第二个字符串.
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equalsIgnoreCase(final String s1, final String s2) {
        return s1 == null ? s2 == null : s1.equalsIgnoreCase(s2);
    }


    /**
     * 根据传入的分隔符参数将字符串分割并传入数组;
     */
    public static String[] split(String regex, String res) {
        if (regex == null || res == null) {
            return null;
        }

        Vector<String> vector = new Vector<String>();
        int index = res.indexOf(regex);

        if (index == -1) {
            vector.addElement(res);
        } else {
            while (index != -1) {
                vector.addElement(res.substring(0, index));
                res = res.substring(index + 1, res.length());
                index = res.indexOf(regex);
            }

            if (index != res.length() - 1) {
                vector.addElement(res);
            }
        }

        final String[] array = new String[vector.size()];
        vector.copyInto(array);
        vector = null;

        return array;
    }

    public static int toInt(Object _obj, int _defaultValue) {
        if (_obj == null) {
            return _defaultValue;
        }
        try {
            return Integer.parseInt(String.valueOf(_obj));
        } catch (Exception e) {
            ExceptionUtils.printStackTrace(e);
        }

        return _defaultValue;
    }

    public static boolean toBoolean(Object _obj, boolean _defaultValue) {
        if (_obj == null) {
            return _defaultValue;
        }
        try {
            return Boolean.parseBoolean(String.valueOf(_obj));
        } catch (Exception e) {
            ExceptionUtils.printStackTrace(e);
        }
        return _defaultValue;
    }

    public static int getInt(String intString, int defaultValue) {
        try {
            if (!StringUtils.isEmpty(intString)) {
                defaultValue = Integer.parseInt(intString);
            }
        } catch (Exception e) {
            ExceptionUtils.printStackTrace(e);
        }

        return defaultValue;
    }

    public static final float toFloat(Object _obj, float _defaultValue) {
        if (_obj == null) {
            return _defaultValue;
        }
        try {
            return Float.parseFloat(String.valueOf(_obj));
        } catch (Exception e) {
            ExceptionUtils.printStackTrace(e);
        }
        return _defaultValue;
    }

    public static final double toDouble(Object _obj, double _defaultValue) {
        if (_obj == null) {
            return _defaultValue;
        }
        try {
            return Double.parseDouble(String.valueOf(_obj));
        } catch (Exception e) {
            ExceptionUtils.printStackTrace(e);
        }

        return _defaultValue;
    }

    public static final long toLong(Object _obj, long _defaultValue) {
        if (_obj == null) {
            return _defaultValue;
        }
        try {
            return Long.parseLong(String.valueOf(_obj));
        } catch (Exception e) {
            ExceptionUtils.printStackTrace(e);
        }

        return _defaultValue;
    }

    public static boolean isEmptyList(List<?> list) {
        return null == list || list.size() == 0;
    }

    public static boolean isEmpty(Collection<?> list) {
        return isEmpty(list, 1);
    }

    public static <T> boolean isEmpty(T[] array) {
        return isEmpty(array, 1);
    }

    public static <T> boolean isEmpty(T[] array, int len) {
        return null == array || array.length < len;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return isEmpty(map, 1);
    }

    public static boolean isEmpty(Collection<?> list, int len) {
        return null == list || list.size() < len;
    }

    public static boolean isEmpty(Map<?, ?> map, int len) {
        return null == map || map.size() < len;
    }

    public static boolean isEmptyList(List<?> list, int len) {
        return null == list || list.size() < len;
    }

    public static boolean isEmptyMap(Map<?, ?> map) {
        return null == map || map.size() == 0;
    }

    public static boolean isEmptyArray(Object[] array) {
        return isEmptyArray(array, 1);
    }

    public static boolean isEmptyArray(Object array) {

        return null == array;
    }

    public static boolean isEmptyArray(Object[] array, int len) {
        return null == array || array.length < len;
    }

    public static String dataFormat(Date date, String format) {
        return null == date ? "" : new SimpleDateFormat(format).format(date);
    }

    public static String dateFormat(Date date) {
        return dataFormat(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String byte2XB(long b) {
        long i = 1L << 10L;
        if (b < i) {
            return b + "B";
        }
        i = 1L << 20L;
        if (b < i) {
            return calXB(1F * b / (1L << 10L)) + "K";
        }

        i = 1L << 30L;
        if (b < i) {
            return calXB(1F * b / (1L << 20L)) + "M";
        }
        i = 1L << 40L;
        if (b < i) {
            return calXB(1F * b / (1L << 30L)) + "G";
        }
        i = 1L << 50L;
        if (b < i) {
            return calXB(1F * b / (1L << 40L)) + "T";
        }
        return b + "B";
    }

    public static String calXB(float r) {
        String result = r + "";
        int index = result.indexOf(".");
        String s = result.substring(0, index + 1);

        String n = result.substring(index + 1);
        if (n.length() >= 1) {
            n = n.substring(0, 1);
        }

        return s + n;
    }

    /**
     * 判断字符串 s 是否是一个integer
     */
    public static boolean isInteger(String s) {
        if (isEmpty(s)) {
            return false;
        }

        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) {
                    return false;
                } else {
                    continue;
                }
            }
            if (Character.digit(s.charAt(i), 10) < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 从jsonobject中读取字符串
     */
    public static String readString(JSONObject resObj, String key) {
        return readString(resObj, key, "");
    }

    /**
     * 从jsonobject中读取字符串
     */
    public static String readString(JSONObject jObj, String key, String _defaultValue) {
        String rtnStr = _defaultValue;
        if (null == jObj || StringUtils.isEmpty(key)) {
            return rtnStr;
        }
        try {
            if (jObj.has(key)) {
                rtnStr = jObj.optString(key, _defaultValue);
                rtnStr = StringUtils.maskNull(rtnStr);
            }
        } catch (Exception e) {
            ExceptionUtils.printStackTrace(e);
        }
        return rtnStr;
    }


    /**
     * 读取object对象
     */
    public static JSONObject readObj(JSONObject jObj, String name) {
        try {
            return jObj.optJSONObject(name);
        } catch (Exception e) {
            ExceptionUtils.printStackTrace(e);
        } finally {
            jObj = null;
            name = null;
        }
        return null;
    }



    /**
     * 从对象数字中取出index的数据
     */
    public static String getStringFromParas(Object[] obj, int index) {
        String ret = "";
        if (obj != null) {
            if (!isEmptyArray(obj, index + 1) && obj[index] != null) {
                ret += obj[index];
            }
        }
        return ret;
    }

    public static String getStringFromParasNew(String[] obj, int index) {
        String ret = null;
        if (obj != null) {
            if (!isEmptyArray(obj, index + 1) && obj[index] != null) {
                ret = obj[index];
            }
        }
        return ret;
    }


    /**
     * 为url添加 gateway参数（假如没有该参数，有的话则不做任何操作）
     */
    public static String appendGateway(String url, int from_type, int from_sub_type) {

        if (!isEmpty(url) && (from_type + from_sub_type) > 0 && !url.contains("gateway=")) {
            if (url.contains("?")) {
                if (url.endsWith("?") || url.endsWith("&")) {
                    url += "gateway=" + from_type + ":" + from_sub_type;
                } else {
                    url += "&gateway=" + from_type + ":" + from_sub_type;
                }
            } else {
                url += "?gateway=" + from_type + ":" + from_sub_type;
            }
        }

        return url;
    }



    public static String valueOf(Object msg) {
        if (msg instanceof Object[]) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Object o : ((Object[]) msg)) {
                stringBuilder.append(String.valueOf(o));
            }
            return stringBuilder.toString();
        } else if (msg instanceof Collection) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Object o : ((Collection) msg)) {
                stringBuilder.append(String.valueOf(o));
            }
            return stringBuilder.toString();
        }
        return String.valueOf(msg);
    }

    public static String getNumString(String s, int count) {
        if (isEmpty(s)) {
            return s;
        }
        if (s.length() > count) {
            s = s.substring(0, count);
        }
        return s;
    }

    /**
     * 验证字符串是否是数字（允许以0开头的数字）
     * 通过正则表达式验证。
     */
    public static boolean isNumber(String numStr) {
        if (TextUtils.isEmpty(numStr)) {
            return false;
        }
        Pattern pattern = Pattern.compile(DIGIT_PATTERN);
        Matcher match = pattern.matcher(numStr);
        return match.matches();
    }

    public static float parseFloat(String strFloat, float defaultfloat) {
        if (StringUtils.isEmpty(strFloat)) {
            return defaultfloat;
        }
        try {
            defaultfloat = Float.parseFloat(strFloat);
        } catch (Exception e) {
            ExceptionUtils.printStackTrace(e);
            return defaultfloat;
        }
        return defaultfloat;
    }

    public static int parseInt(String strInt, int defaultInt) {
        if (isEmpty(strInt)) {
            return defaultInt;
        }
        try {
            defaultInt = Integer.parseInt(strInt);
        } catch (Exception e) {
            ExceptionUtils.printStackTrace(e);
            return defaultInt;
        }
        return defaultInt;
    }

    public static int parseInt(Object intObj, int defaultInt) {
        if (intObj instanceof Integer) {
            return (int) intObj;
        }
        if (intObj instanceof String) {
            String strInt = (String) intObj;
            return parseInt(strInt, defaultInt);
        }
        return defaultInt;
    }

    public static long parseLong(String strLong, long defaultLong) {
        if (StringUtils.isEmpty(strLong)) {
            return defaultLong;
        }
        try {
            defaultLong = Long.parseLong(strLong);
        } catch (Exception e) {
            ExceptionUtils.printStackTrace(e);
            return defaultLong;
        }
        return defaultLong;
    }

    public static long parseLong(Object longObj, long defaultLong) {
        if (longObj instanceof Long) {
            return (long) longObj;
        }
        if (longObj instanceof String) {
            String strLong = (String) longObj;
            return parseLong(strLong, defaultLong);
        }
        return defaultLong;
    }

    /**
     * 截取url中的host地址。
     */
    public static String getHost(String url) {
        if (url == null || "".equals(url.trim())) {
            return null;
        }

        Pattern p = Pattern.compile(HOST_PATTERN);
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

    public static String getCountDisplay(long count) {
        String str = "";
        DecimalFormat df0 = new DecimalFormat("#");
        DecimalFormat df1 = new DecimalFormat("#.#");
        if (count >= 1E9) {
            str += df0.format((double) count / 1E8) + "亿";
        } else if (count >= 1E8) {
            str += df1.format((double) count / 1E8) + "亿";
        } else if (count < 1E8 && count >= 1E5) {
            str += df0.format((double) count / 1E4) + "万";
        } else if (count >= 1E4) {
            str += df1.format((double) count / 1E4) + "万";
        } else {
            str += count;
        }
        return str;
    }

    /**
     * 移除 str末尾的sign
     */
    public static String removeLastSign(String str, String sign) {
        String ret = null;
        if (!TextUtils.isEmpty(str)) {
            if (str.endsWith(sign)) {
                ret = str.substring(0, str.lastIndexOf(sign));
            }
        }
        return ret;
    }

    /***
     * String转换成int
     *
     * @param numStr
     * @return
     */
    public static final int parseInt(String numStr) {
        int val = 0;
        try {
            val = Integer.parseInt(numStr);
        } catch (NumberFormatException e) {
            ExceptionUtils.printStackTrace(e);
        }
        return val;
    }

    /**
     * 测试获取JsonObject
     */
    public static JSONObject optJSONObject(String json) {
        if (isEmpty(json)) {
            return null;
        }
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            ExceptionUtils.printStackTrace(e);
        }
        return null;
    }

    /**
     * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0
     */
    public static int compareVersion(String version1, String version2) {
        if (TextUtils.equals(version1, version2)) {
            return 0;
        }
        if (version1 == null) {
            return -1;
        }
        if (version2 == null) {
            return 1;
        }
        if (!version1.contains(".") || !version2.contains(".")) {
            return version1.compareTo(version2);
        }

        String[] versionArray1 = version1.split("\\.");//注意此处为正则匹配，不能用"."
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
        int diff = 0;
        while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
            ++idx;
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }

    public static boolean isNotEmpty(Collection<?> list) {
        return !isEmpty(list);
    }

    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 获取位置
     */
    public static <T> int indexOf(Object key, T... arrays) {
        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i].equals(key)) {
                return i;
            }
        }
        return -1;
    }

    public static String encodingUTF8(String str) {
        try {
            return isEmpty(str) ? "" : URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            ExceptionUtils.printStackTrace(e);
        }
        return str;
    }

    /**
     * 判断字符串数组中是否包含某字符串元素
     */
    public static boolean isInStringArray(String substring, String[] source) {
        if (source == null || source.length == 0 || substring == null) {
            return false;
        }
        for (int i = 0; i < source.length; i++) {
            String aSource = source[i];
            if (substring.equals(aSource)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取str的measured Width
     *
     * @return 0:str为null或空
     */
    public static float getStringMeasuredWidth(String str, int fontSize) {
        if (StringUtils.isEmpty(str)) {
            return 0;
        }

        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        return paint.measureText(str);
    }

    public static String getParamByKey(String params, String key) {
        if (TextUtils.isEmpty(params) || TextUtils.isEmpty(key)) {
            return null;
        }

        try {
            String[] parts = params.split("&");
            for (String tmp : parts) {
                if (!TextUtils.isEmpty(tmp)) {
                    if (tmp.startsWith(key + "=")) {
                        String value = tmp.substring(key.length() + 1);
                        if (!TextUtils.isEmpty(value)) {
                            return URLDecoder.decode(value, "UTF-8");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将字符串首字母大写.
     *
     * @param s 字符串.
     * @return 首字母大写的字符串.
     */
    public static String upperFirstLetter(final String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        if (!Character.isLowerCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 将字符串首字母小写.
     *
     * @param s 字符串.
     * @return 首字母小写的字符串.
     */
    public static String lowerFirstLetter(final String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        if (!Character.isUpperCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    /**
     * 字符串倒序.
     *
     * @param s 字符串.
     * @return 倒序后的字符串.
     */
    public static String reverse(final String s) {
        if (s == null) {
            return "";
        }
        int len = s.length();
        if (len <= 1) {
            return s;
        }
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }
}
