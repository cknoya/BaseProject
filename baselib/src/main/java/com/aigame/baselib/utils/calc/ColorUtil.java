package com.aigame.baselib.utils.calc;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.text.TextUtils;
import android.view.View;


/**
 * 颜色工具类
 * Created by shisong on 2016/6/27.
 */
public class ColorUtil {

    /**
     * 解析字符串成color值,如果解析失败则返回透明颜色值
     *
     * @param colorStr color字符串
     * @return 解析结果
     */
    public static int parseColor(String colorStr) {
        return parseColor(colorStr, 0x00000000);
    }

    /**
     * 解析字符串成color值
     *
     * @param colorStr     color字符串
     * @param defaultColor 默认color值
     * @return 解析结果
     */
    public static int parseColor(String colorStr, int defaultColor) {
        try {
            if (!TextUtils.isEmpty(colorStr)) {
                return Color.parseColor(colorStr);
            } else {
                return defaultColor;
            }
        } catch (Exception e) {
            return defaultColor;
        }
    }

    /**
     * 重置颜色透明度
     *
     * @param alpha 取值范围0~1 0代表全透明，1代表完全不透明
     * @param color 初始颜色值
     * @return 重置后的颜色值
     */
    public static int alphaColor(float alpha, int color) {
        if (alpha > 1.0f) {
            alpha = 1.0f;
        }
        if (alpha < 0.0f) {
            alpha = 0.0f;
        }
        return Color.argb((int) (alpha * 255), Color.red(color), Color.green(color), Color.blue(color));
    }

    /***
     * 从资源里获取颜色
     *
     * @param resources resources对象
     * @param id        颜色ID
     * @return 颜色值
     */
    @ColorInt
    public static int getColor(Resources resources, @ColorRes int id, int defaultColor) {
        try {
            if (resources != null) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    return resources.getColor(id);
                } else {

                    return resources.getColor(id, null);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return defaultColor;

    }

    /**
     * 设置背景色的alpha值
     *
     * @param view  view对象
     * @param alpha value from 0 to 255
     */
    public static void setAlpha(View view, int alpha, int color) {
        if (view == null) {
            return;
        }
        Drawable drawable = view.getBackground();
        if (drawable instanceof ColorDrawable) {
            int baseColor = ((ColorDrawable) drawable).getColor();
            int newColor = (baseColor << 8 >>> 8) | (alpha << 24);
            view.setBackgroundColor(newColor);
        } else if (drawable != null) {
            view.getBackground().setAlpha(alpha);
        } else {
            int newColor = (color << 8 >>> 8) | (alpha << 24);
            view.setBackgroundColor(newColor);
        }
    }

    /**
     * 计算两个Color的中间值
     */
    public static int getMedianColor(int start, int end) {
        int red = (Color.red(start) + Color.red(end)) / 2;
        int blue = (Color.blue(start) + Color.blue(end)) / 2;
        int green = (Color.green(start) + Color.green(end)) / 2;
        return Color.rgb(red, green, blue);
    }

    /**
     * 计算两个Color的中间值
     *
     * @param alpha 取值范围0.0-1.0, 0.0颜色最接近start， 1.0颜色最接近end
     */
    public static int getMedianColor(int start, int end, float alpha) {
        if (start == end) {
            return start;
        }
        int red = (int) (Color.red(start) * (1.0f - alpha) + Color.red(end) * alpha);
        int blue = (int) (Color.blue(start) * (1.0f - alpha) + Color.blue(end) * alpha);
        int green = (int) (Color.green(start) * (1.0f - alpha) + Color.green(end) * alpha);
        return Color.rgb(red, green, blue);
    }

    @ColorInt
    public static int adjustAlpha(@ColorInt int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

}
