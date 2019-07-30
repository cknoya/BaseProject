package com.aigame.baselib.utils.device;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;

import com.aigame.baselib.io.sp.SPAdapter;
import com.aigame.baselib.utils.crash.ExceptionUtils;

import java.util.Locale;

/**
 * Created by songguobin on 2017/12/4.
 */

public class LocaleUtils {

    private static final String TAG = "LocaleUtils";

    /**
     * APP语言
     */
    public static final String APP_LANGUAGE = "sp_app_language";

    public static final String APP_LANGUAGE_FLOW_SYSTEM = "system";
    public static final String APP_LANGUAGE_SIMPLIFIED_CHINESE = "zh_CN";
    public static final String APP_LANGUAGE_TRADITIONAL_CHINESE = "zh_TW";


    public static void initAppLanguage(Context context) {
        Locale currentLocale = getLanguageLocale(context);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(currentLocale);
        } else {
            configuration.locale = currentLocale;
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }


    public static boolean ifLocalChanged(Context context) {
        String language = SPAdapter.get(context, APP_LANGUAGE, APP_LANGUAGE_FLOW_SYSTEM);
        Locale previousLocale;
        if (TextUtils.equals(language, APP_LANGUAGE_FLOW_SYSTEM)) {
            return false;
        } else if (TextUtils.equals(language, APP_LANGUAGE_TRADITIONAL_CHINESE)) {
            previousLocale = Locale.TRADITIONAL_CHINESE;
        } else {
            previousLocale = Locale.SIMPLIFIED_CHINESE;
        }
        Locale currentLocale;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            currentLocale = context.getResources().getConfiguration().locale;
        } else {
            currentLocale = context.getResources().getConfiguration().getLocales().get(0);
        }
        boolean result = !currentLocale.equals(previousLocale);
        return result;
    }


    /**
     * 如果不是简体中文、繁体中文，默认返回简体中文
     */
    public static Locale getLanguageLocale(Context context) {
        String language = SPAdapter.get(context, APP_LANGUAGE, APP_LANGUAGE_FLOW_SYSTEM);
        if (TextUtils.equals(language, APP_LANGUAGE_FLOW_SYSTEM)) {
            Locale sysType;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                sysType = context.getResources().getConfiguration().locale;
            } else {//7.0以上获取方式需要特殊处理一下
                sysType = context.getResources().getConfiguration().getLocales().get(0);
            }

            if (sysType.equals(Locale.TRADITIONAL_CHINESE)) {
                return Locale.TRADITIONAL_CHINESE;
            } else if (TextUtils.equals(sysType.getLanguage(), Locale.CHINA.getLanguage())) {//zh
                if (TextUtils.equals(sysType.getCountry(), Locale.SIMPLIFIED_CHINESE.getCountry())) {//适配华为mate9  zh_CN_#Hans;华为P9  zh_TW#HANT
                    return Locale.SIMPLIFIED_CHINESE;
                } else {
                    return Locale.TRADITIONAL_CHINESE;
                }
            } else {
                return Locale.SIMPLIFIED_CHINESE;
            }
        } else if (TextUtils.equals(language, APP_LANGUAGE_SIMPLIFIED_CHINESE)) {
            return Locale.SIMPLIFIED_CHINESE;
        } else if (TextUtils.equals(language, APP_LANGUAGE_TRADITIONAL_CHINESE)) {
            return Locale.TRADITIONAL_CHINESE;
        }
        return Locale.SIMPLIFIED_CHINESE;
    }

    /**
     * 获取语言和区域信息
     *
     * @param context
     * @return
     */
    public static Locale getLocale(Context context) {
        Locale locale;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !LocaleList.getDefault().isEmpty()) {
                locale = LocaleList.getDefault().get(0);
            } else {
                locale = Locale.getDefault();
            }
        } catch (Exception e) {
            locale = null;
            ExceptionUtils.printStackTrace(e);
        }

        if (locale == null) {
            try {
                if (context != null && context.getResources() != null && context.getResources().getConfiguration() != null) {
                    locale = context.getResources().getConfiguration().locale;
                }
            } catch (Exception e) {
                ExceptionUtils.printStackTrace(e);
            }
        }
        return locale;
    }

    /**
     * 判断当期系统语言是否为繁体语言
     *
     * @return
     */
    public static boolean isTraditional(Context context) {
        boolean isTraditional;
        boolean countryCondition = false;
        boolean languageCondition = false;

        Locale locale = getLanguageLocale(context);

        if (locale != null) {
            String language = locale.getLanguage();
            if (!TextUtils.isEmpty(language)) {
                languageCondition = language.equalsIgnoreCase("zh");
            }

            String country = locale.getCountry();
            if (!TextUtils.isEmpty(country)) {
                boolean isTW = country.equalsIgnoreCase("TW");
                boolean isHK = country.equalsIgnoreCase("HK");
                boolean isMo = country.equalsIgnoreCase("MO");
                countryCondition = isTW || isHK || isMo;
            }
        }

        isTraditional = languageCondition && countryCondition;
        return isTraditional;
    }

    /**
     * 获取系统区域信息
     *
     * @param context
     * @return
     */
    public static String getCountry(Context context) {
        return getCountry(context, "");
    }

    /**
     * 获取系统区域信息，可指定默认区域信息
     *
     * @param context
     * @param defaultCountry 默认区域信息
     * @return
     */
    public static String getCountry(Context context, String defaultCountry) {
        String country = defaultCountry;

        Locale locale = getLanguageLocale(context);
        if (locale != null) {
            country = locale.getCountry();
        }
        return country;

    }


    /**
     * 获取语言信息
     *
     * @param context
     * @param defaultLanguage 默认语言
     * @return
     */
    public static String getLanguage(Context context, String defaultLanguage) {
        String language = defaultLanguage;
        Locale locale = getLanguageLocale(context);
        if (locale != null) {
            language = locale.getLanguage();
        }
        return language;
    }

}
