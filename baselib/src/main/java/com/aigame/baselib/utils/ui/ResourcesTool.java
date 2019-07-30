package com.aigame.baselib.utils.ui;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;


/**
 * 资源反射类
 */
public class ResourcesTool {

    static final String DRAWABLE = "drawable";
    static final String STRING = "string";
    static final String STYLE = "style";
    static final String LAYOUT = "layout";
    static final String ID = "id";
    static final String COLOR = "color";
    static final String RAW = "raw";
    static final String ANIM = "anim";
    static final String ATTR = "attr";
    static final String DIMEN = "dimen";


    static String mPackageName;
    static Resources mResources;
    private static Object sInitLock = new Object();

    public static void init(Context appContext) {
        synchronized (sInitLock) {
            if (mResources == null && TextUtils.isEmpty(mPackageName)) {
                mPackageName = appContext.getPackageName();
                mResources = appContext.getResources();
            }
        }
    }

    /**
     * 获取资源id
     *
     * @param sourceName 资源名称
     * @param sourceType 资源类型
     * @return 资源id
     */
    private static int getResourceId(String sourceName, String sourceType) {
        assetContext();
        if (mResources == null || TextUtils.isEmpty(sourceName)) {
            return -1;
        } else {
            return mResources.getIdentifier(sourceName, sourceType, mPackageName);
        }

    }

    /**
     * 获取字符串资源id
     *
     * @param sourceName 资源名称
     * @return 字符串资源id
     */
    public static int getResourceIdForString(String sourceName) {
        if (TextUtils.isEmpty(sourceName)) {
            sourceName = "emptey_string_res";
        }
        return getResourceId(sourceName, STRING);
    }

    /**
     * 获取资源id
     *
     * @param sourceName 资源名称
     * @return 资源id
     */
    public static int getResourceIdForID(String sourceName) {
        return getResourceId(sourceName, ID);
    }

    /**
     * 获取布局资源id
     *
     * @param sourceName 资源名称
     * @return 布局id
     */
    public static int getResourceIdForLayout(String sourceName) {
        return getResourceId(sourceName, LAYOUT);
    }

    /**
     * 获取图片资源id
     *
     * @param sourceName 资源名称
     * @return 图片id
     */
    public static int getResourceIdForDrawable(String sourceName) {
        if (TextUtils.isEmpty(sourceName)) {
            sourceName = "default_empty_drawable_transparent";// 默认一个透明图片资源
        }

        return getResourceId(sourceName, DRAWABLE);
    }

    /**
     * 获取style资源id
     *
     * @param sourceName 资源名称
     * @return style id
     */
    public static int getResourceIdForStyle(String sourceName) {
        return getResourceId(sourceName, STYLE);
    }

    /**
     * 获取颜色资源id
     *
     * @param sourceName 资源名称
     * @return 颜色id
     */
    public static int getResourceIdForColor(String sourceName) {
        return getResourceId(sourceName, COLOR);
    }

    /**
     * 获取raw资源id
     *
     * @param sourceName 资源名称
     * @return raw id
     */
    public static int getResourceIdForRaw(String sourceName) {
        return getResourceId(sourceName, RAW);
    }

    /**
     * 获取动画资源id
     *
     * @param sourceName 资源名称
     * @return 动画id
     */
    public static int getResourceForAnim(String sourceName) {
        return getResourceId(sourceName, ANIM);
    }

    /**
     * 获取属性资源id
     *
     * @param sourceName 资源名称
     * @return 属性id
     */
    public static int getResourceForAttr(String sourceName) {
        return getResourceId(sourceName, ATTR);
    }

    @Deprecated
    public static int getResourceForDimen(String sourceName) {
        return getResourceId(sourceName, DIMEN);
    }

    /**
     * 获取像素资源id
     *
     * @param sourceName 资源名称
     * @return 像素id
     */
    public static int getResourceIdForDimen(String sourceName) {
        return getResourceId(sourceName, DIMEN);
    }

    /**
     * 获取像素资源id
     *
     * @param sourceName 资源名称
     * @param dft        默认值
     * @return 像素id
     */
    public static float getDimention(String sourceName, int dft) {
        int id = getResourceIdForDimen(sourceName);
        if (id > 0) {
            return mResources.getDimension(id);
        } else {
            return dft;
        }
    }

    /**
     * 获取图片资源id
     *
     * @param context    上下文
     * @param sourceName 资源名称
     * @return 图片id
     */
    public static int getResourceIdForDrawable(Context context, String sourceName) {
        if (context == null || TextUtils.isEmpty(sourceName)) {
            return -1;
        }
        if (mPackageName == null) {
            mPackageName = context.getPackageName();
        }

        Resources mResources = context.getResources();
        return mResources.getIdentifier(sourceName, DRAWABLE, mPackageName);

    }

    /**
     * 获取布局资源id
     *
     * @param context    上下文
     * @param sourceName 资源名称
     * @return 布局id
     */
    public static int getResourceIdForLayout(Context context, String sourceName) {
        if (context == null || TextUtils.isEmpty(sourceName)) {
            return -1;
        }
        Resources mResources = context.getResources();
        String currentPackageName;
        if (mPackageName == null) {
            currentPackageName = context.getPackageName();
        } else {
            currentPackageName = mPackageName;
        }
        return mResources.getIdentifier(sourceName, LAYOUT, currentPackageName);

    }

    private static void assetContext() {


    }

    @Deprecated
    public static void assetContext(Context context) {
        if (context != null) {
            mResources = context.getResources();
            mPackageName = context.getPackageName();
        }
    }

    /**
     * NPE risk,do not call directly
     */
    @Deprecated
    public static String getmPackageName() {
        return mPackageName;
    }
}
