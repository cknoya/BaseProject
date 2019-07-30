package com.aigame.baselib.utils.calc;

/**
 * 浮点帮助类
 *
 * @author zhaokaiyuan
 */
public final class FloatUtils {

    private static final float EPSILON = 1.0E-5F;

    /**
     * 比较两个浮点数大小
     *
     * @param f1 浮点数1
     * @param f2 浮点数2
     * @return 两个浮点数是否相等
     */
    public static boolean floatsEqual(float f1, float f2) {
        return !Float.isNaN(f1) && !Float.isNaN(f2) ? (Math.abs(f2 - f1) < EPSILON) : (Float.isNaN(f1) && Float.isNaN(f2));
    }
}
