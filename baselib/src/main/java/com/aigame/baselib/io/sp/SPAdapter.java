package com.aigame.baselib.io.sp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by songguobin on 2017/11/11.
 */

public class SPAdapter {


    /**
     * 默认的sharedPreferences 中是否存在_key
     */
    public static boolean hasKey(Context context, String _key) {
        return SharedPreferencesFactory.hasKey(context, _key);
    }

    /**
     * 指定的sharedPreferences 中是否存在_key
     */
    public static boolean hasKey(Context context, String _key, String sharedPreferenceName) {
        return SharedPreferencesFactory.hasKey(context, _key, sharedPreferenceName);
    }

    /**
     * 存储String 在默认的 sharedPreferences 中
     */
    public static void set(Context context, String _key, String _value) {
        SharedPreferencesFactory.set(context, _key, _value);
    }

    public static void set(Context context, String _key, String _value, boolean saveImmediately) {
        SharedPreferencesFactory.set(context, _key, _value, saveImmediately);
    }

    /**
     * 存储Long 在默认的 sharedPreferences 中
     */
    public static void set(Context context, String _key, long _value) {
        SharedPreferencesFactory.set(context, _key, _value);
    }

    /**
     * 存储Long 在默认的 sharedPreferences 中
     */
    public static void set(Context context, String _key, float _value) {
        SharedPreferencesFactory.set(context, _key, _value);
    }

    /**
     * 存储float  value到某个指定sharedPreferences
     */
    public static void set(Context context, String _key, float value, String sharedPreferencesName) {
        SharedPreferencesFactory.set(context, _key, value, sharedPreferencesName);
    }

    public static void set(Context context, String _key, float value, String sharedPreferencesName, boolean saveImmediately) {
        SharedPreferencesFactory.set(context, _key, value, sharedPreferencesName, saveImmediately);
    }

    /**
     * 获取float 在默认的 sharedPreferences 中
     */
    public static float get(Context context, String _key, float _defaultValue) {
        return SharedPreferencesFactory.get(context, _key, _defaultValue);
    }

    /**
     * 获取某个指定sharedPreferences 的float  value
     */
    public static float get(Context context, String _key, float _defaultValue, String sharedPreferencesName) {
        return SharedPreferencesFactory.get(context, _key, _defaultValue, sharedPreferencesName);
    }

    public static void set(Context context, String _key, long _value, boolean saveImmediately) {
        SharedPreferencesFactory.set(context, _key, _value, saveImmediately);
    }

    public static void set(Context context, String _key, float _value, boolean saveImmediately) {
        SharedPreferencesFactory.set(context, _key, _value, saveImmediately);
    }

    /**
     * 存储int 在默认的 sharedPreferences 中
     */
    public static void set(Context context, String _key, int _value) {
        SharedPreferencesFactory.set(context, _key, _value);
    }

    public static void set(Context context, String _key, int _value, boolean saveImmediately) {
        SharedPreferencesFactory.set(context, _key, _value, saveImmediately);
    }

    /**
     * 存储boolean 在默认的 sharedPreferences 中
     */
    public static void set(Context context, String _key, boolean _value) {
        SharedPreferencesFactory.set(context, _key, _value);
    }

    public static void set(Context context, String _key, boolean _value, boolean saveImmediately) {
        SharedPreferencesFactory.set(context, _key, _value, saveImmediately);
    }

    /**
     * 获取String 在默认的 sharedPreferences 中
     */
    public static String get(Context context, String _key, String _defaultValue) {
        return SharedPreferencesFactory.get(context, _key, _defaultValue);
    }

    /**
     * 获取long 在默认的 sharedPreferences 中
     */
    public static long get(Context context, String _key, long _defaultValue) {
        return SharedPreferencesFactory.get(context, _key, _defaultValue);
    }

    /**
     * 获取int 在默认的 sharedPreferences 中
     */
    public static int get(Context context, String _key, int _defaultValue) {
        return SharedPreferencesFactory.get(context, _key, _defaultValue);
    }

    /**
     * 获取boolean 在默认的 sharedPreferences 中
     */
    public static boolean get(Context context, String _key, boolean _defaultValue) {
        return SharedPreferencesFactory.get(context, _key, _defaultValue);
    }


    /**
     * 获取sharedPreferences
     */
    public static SharedPreferences getSharedPrefs(Context mContext, String sharedPreferencesName) {
        return SharedPreferencesFactory.getSharedPrefs(mContext, sharedPreferencesName);
    }

    /**
     * 为默认sharedPreferences某个key增加监听
     */
    public static void addOnSharedPreferenceChangListener(
            Context mContext,
            String key, ConfigurationHelper.IOnSharedChangeListener listener
    ) {
        SharedPreferencesFactory.addOnSharedPreferenceChangListener(mContext, key, listener);
    }

    /**
     * 为指定的sharedPreferences的某个key 增加监听
     */
    public static void addOnSharedPreferenceChangListener(
            Context mContext, String sharePreferencesName,
            String key, ConfigurationHelper.IOnSharedChangeListener listener
    ) {
        SharedPreferencesFactory.addOnSharedPreferenceChangListener(mContext, sharePreferencesName, key, listener);
    }

    /**
     * 清除某个指定sharedPreferences的所有数据
     */
    public static void clearAllData(Context context, String sharedPreferencesName) {
        SharedPreferencesFactory.clearAllData(context, sharedPreferencesName);
    }

    public static HashMap<String, String> getAppVersion(String result) {
        return SharedPreferencesFactory.getAppVersion(result);
    }

    /**
     * 存储int  value到某个指定sharedPreferences
     */
    public static void set(Context context, String _key, int value, String sharedPreferencesName) {
        SharedPreferencesFactory.set(context, _key, value, sharedPreferencesName);
    }

    public static void set(Context context, String _key, int value, String sharedPreferencesName, boolean saveImmediately) {
        SharedPreferencesFactory.set(context, _key, value, sharedPreferencesName, saveImmediately);
    }

    public static int get(Context context, String _key, int _defaultValue, String sharedPreferencesName) {
        return SharedPreferencesFactory.get(context, _key, _defaultValue, sharedPreferencesName);
    }


    /**
     * 存储String  value到某个指定sharedPreferences
     */
    public static void set(Context context, String _key, String value, String sharedPreferencesName) {
        SharedPreferencesFactory.set(context, _key, value, sharedPreferencesName);
    }

    public static void set(Context context, String _key, String value, String sharedPreferencesName, boolean saveImmediately) {
        SharedPreferencesFactory.set(context, _key, value, sharedPreferencesName, saveImmediately);
    }

    public static String get(Context context, String _key, String _defaultValue, String sharedPreferencesName) {
        return SharedPreferencesFactory.get(context, _key, _defaultValue, sharedPreferencesName);
    }


    /**
     * 存储boolean  value到某个指定sharedPreferences
     */
    public static void set(Context context, String _key, boolean value, String sharedPreferencesName) {
        SharedPreferencesFactory.set(context, _key, value, sharedPreferencesName);
    }

    public static void set(Context context, String _key, boolean value, String sharedPreferencesName, boolean saveImmediately) {
        SharedPreferencesFactory.set(context, _key, value, sharedPreferencesName, saveImmediately);
    }

    public static boolean get(Context context, String _key, boolean _defaultValue, String sharedPreferencesName) {
        return SharedPreferencesFactory.get(context, _key, _defaultValue, sharedPreferencesName);
    }


    /**
     * 存储long  value到某个指定sharedPreferences
     */
    public static void set(Context context, String _key, long value, String sharedPreferencesName) {
        SharedPreferencesFactory.set(context, _key, value, sharedPreferencesName);
    }

    public static void set(Context context, String _key, long value, String sharedPreferencesName, boolean saveImmediately) {
        SharedPreferencesFactory.set(context, _key, value, sharedPreferencesName, saveImmediately);
    }

    /**
     * 获取某个指定sharedPreferences 的long  value
     */
    public static long get(Context context, String _key, long _defaultValue, String sharedPreferencesName) {
        return SharedPreferencesFactory.get(context, _key, _defaultValue, sharedPreferencesName);
    }

    public static void remove(Context context, String _key, String sharedPreferenceName) {
        SharedPreferencesFactory.remove(context, _key, sharedPreferenceName);
    }

    public static void remove(Context context, String _key, String sharedPreferenceName, boolean saveImmediately) {
        SharedPreferencesFactory.remove(context, _key, sharedPreferenceName, saveImmediately);
    }

    public static void remove(Context mContext, String _key) {
        SharedPreferencesFactory.remove(mContext, _key);
    }

    public static void remove(Context mContext, String _key, boolean saveImmediately) {
        SharedPreferencesFactory.remove(mContext, _key, saveImmediately);
    }


}
