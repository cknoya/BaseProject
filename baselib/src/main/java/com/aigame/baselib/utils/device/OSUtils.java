package com.aigame.baselib.utils.device;

import android.text.TextUtils;

import com.aigame.baselib.utils.crash.ExceptionUtils;

import java.lang.reflect.Method;

/**
 * 手机系统判断
 */
public class OSUtils {

    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_EMUI_VERSION_NAME = "ro.build.version.emui";
    private static final String KEY_DISPLAY = "ro.build.display.id";

    /**
     * 判断是否为miui
     * Is miui boolean.
     *
     * @return the boolean
     */
    public static boolean isMIUI() {
        String property = getSystemProperty(KEY_MIUI_VERSION_NAME, "");
        return !TextUtils.isEmpty(property);
    }

    /**
     * 判断miui版本是否大于等于6
     * Is miui 6 more boolean.
     *
     * @return the boolean
     */
    public static boolean isMIUI6More() {
        String version = getMIUIVersion();
        int ver = 0;
        if (!TextUtils.isEmpty(version)) {
            try {
                ver = Integer.parseInt(version.substring(1));
            } catch (Exception e) {
                ExceptionUtils.printStackTrace(e);
            }
        }
        if ((!TextUtils.isEmpty(version) && ver >= 6)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获得miui的版本
     * Gets miui version.
     *
     * @return the miui version
     */
    public static String getMIUIVersion() {
        return isMIUI() ? getSystemProperty(KEY_MIUI_VERSION_NAME, "") : "";
    }

    /**
     * 判断是否为emui
     * Is emui boolean.
     *
     * @return the boolean
     */
    public static boolean isEMUI() {
        String property = getSystemProperty(KEY_EMUI_VERSION_NAME, "");
        return !TextUtils.isEmpty(property);
    }

    /**
     * 获取emui的版本
     * Gets emui version.
     *
     * @return the emui version
     */
    public static String getEMUIVersion() {
        return isEMUI() ? getSystemProperty(KEY_EMUI_VERSION_NAME, "") : "";
    }

    /**
     * 判断是否为emui3.1版本
     * Is emui 3 1 boolean.
     *
     * @return the boolean
     */
    public static boolean isEMUI3_1() {
        String property = getEMUIVersion();
        if ("EmotionUI 3".equals(property) || property.contains("EmotionUI_3.1")) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为flymeOS
     * Is flyme os boolean.
     *
     * @return the boolean
     */
    public static boolean isFlymeOS() {
        return getFlymeOSFlag().toLowerCase().contains("flyme");
    }

    /**
     * 判断flymeOS的版本是否大于等于4
     * Is flyme os 4 more boolean.
     *
     * @return the boolean
     */
    public static boolean isFlymeOS4More() {
        String version = getFlymeOSVersion();
        int num = 0;
        if (!version.isEmpty()) {
            try {
                if (version.toLowerCase().contains("os")) {
                    num = Integer.parseInt(version.substring(9, 10));
                } else {
                    num = Integer.parseInt(version.substring(6, 7));
                }
            } catch (Exception e) {
                ExceptionUtils.printStackTrace(e);

            }
            if (num >= 4) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取flymeOS的版本
     * Gets flyme os version.
     *
     * @return the flyme os version
     */
    public static String getFlymeOSVersion() {
        return isFlymeOS() ? getSystemProperty(KEY_DISPLAY, "") : "";
    }

    private static String getFlymeOSFlag() {
        return getSystemProperty(KEY_DISPLAY, "");
    }

    public static String getSystemProperty(String key, String defaultValue) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("get", String.class, String.class);
            return (String) get.invoke(clz, key, defaultValue);
        } catch (Exception e) {
            ExceptionUtils.printStackTrace(e);

        }
        return defaultValue;
    }
}
