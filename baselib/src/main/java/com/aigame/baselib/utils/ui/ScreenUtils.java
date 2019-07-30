package com.aigame.baselib.utils.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ImageView;

import com.aigame.baselib.utils.crash.ExceptionUtils;
import com.aigame.baselib.utils.app.LifeCycleUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * 屏幕帮助类
 */
public class ScreenUtils {

    private static final String TAG = "ScreenTool";

    /**
     * 屏幕在横竖屏下的宽高/实际宽高
     */
    private static int mScreenWidth = 0;
    private static int mScreenHeight = 0;

    private static int mScreenWidth_land = 0;
    private static int mScreenHeight_land = 0;

    private static int mRealScreenWidth = 0;
    private static int mRealScreenHeight = 0;

    private static int mRealScreenWidth_land = 0;
    private static int mRealScreenHeight_land = 0;

    /**
     * 屏幕DPI
     */
    private static int mScreenDpi = 0;

    /**
     * 屏幕密度
     */
    private static float mScreenDensity = 0;
    /**
     * 网络图片大小
     */
    private static final int IMAGE_WIDTH = 220;
    private static final int IMAGE_HIGH = 124;

    /**
     * 获取屏幕真实宽度
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int getWidth(Activity activity) {
        boolean isLand = isLandScape(activity);
        int width = isLand ? mRealScreenWidth_land : mRealScreenWidth;
        if (width <= 0) {
            try {
                DisplayMetrics dm = new DisplayMetrics();
                if (Build.VERSION.SDK_INT >= 17) {
                    activity.getWindowManager().getDefaultDisplay().getRealMetrics(dm);
                } else {
                    activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
                }
                width = dm.widthPixels;
                if (isLand) {
                    mRealScreenWidth_land = width;
                } else {
                    mRealScreenWidth = width;
                }
            } catch (Exception e) {
                ExceptionUtils.printStackTrace(e);
            }
        }
        return width;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getWidth(Context context) {
        boolean isLand = isLandScape(context);
        int width = isLand ? mScreenWidth_land : mScreenWidth;
        if (width <= 0) {
            width = getWidthRealTime(context);
            if (isLand) {
                mScreenWidth_land = width;
            } else {
                mScreenWidth = width;
            }
        }
        return width;
    }

    /**
     * 实时获取屏幕宽度
     */
    public static int getWidthRealTime(Context context) {
        int width = 0;
        try {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            manager.getDefaultDisplay().getMetrics(dm);
            width = dm.widthPixels;
        } catch (Exception e) {
            ExceptionUtils.printStackTrace(e);
        }
        return width;
    }

    /**
     * 实时获取屏幕高度
     */
    public static int getHeightRealTime(Context context) {
        int height = 0;
        try {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            manager.getDefaultDisplay().getMetrics(dm);
            height = dm.heightPixels;
        } catch (Exception e) {
            ExceptionUtils.printStackTrace(e);
        }
        return height;
    }

    /**
     * 获取屏幕高度
     */
    public static int getHeight(Context context) {
        boolean isLand = isLandScape(context);
        int height = isLand ? mScreenHeight_land : mScreenHeight;
        if (height <= 0) {
            height = getHeightRealTime(context);
            if (isLand) {
                mScreenHeight_land = height;
            } else {
                mScreenHeight = height;
            }
        }
        return height;
    }


    /**
     * 获取屏幕真实高度
     *
     * @param activity 上下文环境
     * @return 屏幕高度
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int getHeight(Activity activity) {
        boolean isLand = isLandScape(activity);
        int height = isLand ? mRealScreenHeight_land : mRealScreenHeight;
        if (height <= 0) {
            try {
                DisplayMetrics dm = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
                if (Build.VERSION.SDK_INT >= 17) {
                    activity.getWindowManager().getDefaultDisplay().getRealMetrics(dm);
                } else {
                    activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
                }
                height = dm.heightPixels;
                if (isLand) {
                    mRealScreenHeight_land = height;
                } else {
                    mRealScreenHeight = height;
                }
            } catch (Exception e) {
                ExceptionUtils.printStackTrace(e);
            }
        }
        return height;
    }


    /**
     * 获取屏幕的真实高度，包含虚拟按键的高度
     *
     * @param context
     * @return
     */
    public static int getRealHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = wm.getDefaultDisplay();
        int realScreenHeight = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(dm);
            realScreenHeight = dm.heightPixels;
        } else {
            try {
                Class c = Class.forName("android.view.Display");
                Method method = c.getDeclaredMethod("getRealMetrics", DisplayMetrics.class);
                method.invoke(display, dm);
                realScreenHeight = dm.heightPixels;
            } catch (Exception e) {
                display.getMetrics(dm);
                realScreenHeight = dm.heightPixels;
            }
        }
        return realScreenHeight;
    }


    /**
     * 获取虚拟按键的高度，如果弹出，则高度大于零，隐藏则高度为0
     *
     * @param context
     * @return
     */
    public static int getVirtualKeyHeight(Context context) {

        int fullScreenHeight = getRealHeight(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        return fullScreenHeight - dm.heightPixels;
    }


    /**
     * 获取屏幕分辨率
     *
     * @param context 上下文
     * @param split   分隔符
     * @return 屏幕分辨率
     */
    public static String getResolution(Context context, String split) {

        String resolution = "";
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            resolution = ScreenUtils.getWidth(context) + split + ScreenUtils.getHeight(context);
        } else {
            resolution = ScreenUtils.getHeight(context) + split + ScreenUtils.getWidth(context);
        }
        return resolution;

    }

    /**
     * 获取屏幕分辨率，反向的
     *
     * @param context
     * @param split
     * @return
     */
    @Deprecated
    public static String getResolution_IR(Context context, String split) {

        String resolution = "";
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            resolution = ScreenUtils.getHeight(context) + split + ScreenUtils.getWidth(context);
        } else {
            resolution = ScreenUtils.getWidth(context) + split + ScreenUtils.getHeight(context);
        }

        return resolution;
    }

    /**
     * 获取屏幕dpi
     *
     * @param mContext 上下文
     * @return 屏幕dpi
     */
    public static int getScreenDpi(Context mContext) {
        if (mScreenDpi <= 0) {
            if (mContext != null) {
                try {
                    DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
                    mScreenDpi = dm.densityDpi;
                } catch (Exception e) {
                    ExceptionUtils.printStackTrace(e);
                    return DisplayMetrics.DENSITY_MEDIUM;
                }
            } else {
                return DisplayMetrics.DENSITY_MEDIUM;
            }
        }
        return mScreenDpi;
    }

    /**
     * 获取屏幕密度
     *
     * @param context 上下文
     * @return 屏幕密度
     */
    public static float getScreenDensity(Context context) {
        if (mScreenDensity <= 0 && context != null) {
            try {
                DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
                mScreenDensity = dm.density;
            } catch (Exception e) {
                    ExceptionUtils.printStackTrace(e);

            }
        }
        return mScreenDensity;
    }

    /**
     * 根据屏幕密度（取整）获取合适的图片倍数1x、2x、3x等
     *
     * @param context 上下文
     * @return 屏幕密度倍数
     */
    public static int getScreenScale(Context context) {
        float density = getScreenDensity(context);
        return Math.round(density);
    }

    /**
     * 判断是否是横屏
     *
     * @param mContext 上下文
     * @return 是否是横屏
     */
    public static boolean isLandScape(Context mContext) {

        if (mContext == null || mContext.getResources() == null) {
            return false;
        }

        Configuration configuration = mContext.getResources().getConfiguration();
        return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE;
    }


    /**
     * 获取屏幕尺寸
     * <p>
     * 这个尺寸没有包括action bar, 例如华为P8，值为4.33, 720*1184
     *
     * @param context 上下文
     * @return 屏幕尺寸
     */
    public static float getScreenSize(Context context) {
        float ret = 0;
        try {
            int w = getWidth(context);
            int h = getHeight(context);
            float dpi = getScreenDpi(context);
            BigDecimal bd = BigDecimal.valueOf(Math.sqrt(w * w + h * h * 1.0) / dpi);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            ret = bd.floatValue();
        } catch (Exception e) {
            ExceptionUtils.printStackTrace(e);
        }
        return ret;
    }

    /**
     * 获取屏幕尺寸
     * <p>
     * 这个尺寸准确, 例如华为P8，值为4.59, 720*1280
     *
     * @param context
     * @return
     */
    public static float getScreenRealSize(Context context) {
        if (Build.VERSION.SDK_INT >= 17) {
            float ret = 0;
            try {
                WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                Display mDisplay = mWindowManager.getDefaultDisplay();
                DisplayMetrics mDisplayMetrics = new DisplayMetrics();
                mDisplay.getRealMetrics(mDisplayMetrics);
                int w = mDisplayMetrics.widthPixels;
                int h = mDisplayMetrics.heightPixels;
                float dpi = mDisplayMetrics.densityDpi;
                BigDecimal bd = BigDecimal.valueOf(Math.sqrt(w * w + h * h * 1.0) / dpi);
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                ret = bd.floatValue();
                return ret;
            } catch (Exception e) {
                ExceptionUtils.printStackTrace(e);
                return getScreenSize(context);
            }
        } else {
            return getScreenSize(context);
        }
    }


    /**
     * 获取手机状态栏高度
     *
     * @param context 上下文
     * @return 状态栏高度
     * @link 此方法已废弃，请使用UIUtils.getStatusBarHeight
     */
    @Deprecated
    public static int getStatusBarHeight(Context context) {
        Class<?> c;
        Object obj;
        Field field;
        int x;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            ExceptionUtils.printStackTrace(e);
        }
        return statusBarHeight;
    }

    /**
     * 获取NavigationBar的高度值
     *
     * @param activity 上下文
     * @return 高度值，如果拥有navigationBar，返回该navigationBar的实际高度值，否则返回0.
     */
    public static int getNavigationBarHeight(Activity activity) {
        if (!hasNavigationBar(activity)) {
            return 0;
        }
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        //获取NavigationBar的高度
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 是否有导航栏
     *
     * @param activity 上下文
     * @return 是否有导航栏
     */
    public static boolean hasNavigationBar(Activity activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Display display = activity.getWindowManager().getDefaultDisplay();
                Point size = new Point();
                Point realSize = new Point();
                display.getSize(size);
                display.getRealSize(realSize);
                return realSize.y != size.y;
            } else {
                boolean menu = ViewConfiguration.get(activity).hasPermanentMenuKey();
                boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
                return !(menu || back);
            }
        } catch (Exception e) {
            ExceptionUtils.printStackTrace(e);
            return false;
        }
    }

    @Deprecated
    private static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 调整图片显示大小
     *
     * @param imageView 图片控件
     */
    public static void resizeItemIcon(ImageView imageView) {

        Context context = imageView.getContext();
        if (context == null) {
            return;
        }

        int windowsWidth = context.getResources().getDisplayMetrics().widthPixels;
        int windowsHeight = context.getResources().getDisplayMetrics().heightPixels;

        int spaceWidth = UIUtils.dip2px(context, 25f);

        int realImageWidth = (windowsWidth - spaceWidth) / 2;
        int realImageHeight = realImageWidth * IMAGE_HIGH / IMAGE_WIDTH;

        ViewGroup.LayoutParams lp = imageView.getLayoutParams();

        if (windowsWidth < windowsHeight) {
            lp.width = realImageWidth * 7 / 10;
            lp.height = realImageHeight * 7 / 10;
        } else {
            lp.width = realImageWidth * 4 / 10;
            lp.height = realImageHeight * 4 / 10;
        }

        imageView.setLayoutParams(lp);
    }

    /**
     * 是否是全屏
     *
     * @param activity 上下文
     * @return 是否全屏
     */
    public static boolean isFullScreen(@NonNull final Activity activity) {
        Window window = activity.getWindow();
        return window != null && (window.getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
    }

    /**
     * 是否是半透明状态栏
     *
     * @param activity activity
     * @return 是否半透明状态栏
     */
    public static boolean isTranslucentStatus(@NonNull final Activity activity) {
        Window window = activity.getWindow();
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && window != null &&
                (window.getAttributes().flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) != 0;
    }

    /**
     * 是否是半透明虚拟导航栏
     *
     * @param activity activity
     * @return 是否半透明虚拟导航栏
     */
    public static boolean isTranslucentNavigation(final Activity activity) {
        Window window = activity.getWindow();
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && window != null &&
                (window.getAttributes().flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) != 0;
    }

    /**
     * Activity布局是否FitsSystemWindows
     *
     * @param activity activity
     * @return 布局是否FitsSystemWindows
     */
    public static boolean isFitsSystemWindows(@NonNull final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return false;
        }
        View view = activity.findViewById(android.R.id.content);
        if (view instanceof ViewGroup) {
            View child = ((ViewGroup) view).getChildAt(0);
            return child != null && child.getFitsSystemWindows();
        }
        return false;
    }

    /**
     * OEM-华为Mate 10需求，HDMI投射状态判定
     */
    public static boolean isHuaWeiTVModel(Context cxt) {
        boolean ret = false;
        String brand = Build.BRAND;
        if (brand.equalsIgnoreCase("Huawei") ||
                brand.equalsIgnoreCase("honor")) {
            try {
                Class<?> clazz = Class.forName("android.util.HwPCUtils");
                Method mth = clazz.getMethod("isValidExtDisplayId", Context.class);
                ret = (boolean) mth.invoke(null, cxt);
            } catch (Exception e) {
                ExceptionUtils.printStackTrace(e);
            }
        }
        return ret;
    }


    /**
     * Android P 刘海屏判断
     */
    public static boolean isAndroidPNotch(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        Object displayCutout = null;
        if (decorView != null && Build.VERSION.SDK_INT >= 28) {
            WindowInsets windowInsets = decorView.getRootWindowInsets();
            if (windowInsets != null) {
                Class<?> windowInsetsClass = windowInsets.getClass();
                try {
                    Method method = windowInsetsClass.getDeclaredMethod("getDisplayCutout");
                    displayCutout = method.invoke(windowInsets);
                } catch (NoSuchMethodException e) {
                    ExceptionUtils.printStackTrace(e);
                } catch (IllegalAccessException e) {
                    ExceptionUtils.printStackTrace(e);
                } catch (InvocationTargetException e) {
                    ExceptionUtils.printStackTrace(e);
                }
            }
        }
        if (displayCutout != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取屏幕密度.
     *
     * @return 屏幕密度
     */
    public static float getScreenDensity() {
        return LifeCycleUtils.getApp().getResources().getDisplayMetrics().density;
    }

    /**
     * 获取屏幕密度（每英寸包含像素个数）.
     *
     * @return 屏幕密度（每英寸包含像素个数）
     */
    public static int getScreenDensityDpi() {
        return LifeCycleUtils.getApp().getResources().getDisplayMetrics().densityDpi;
    }

    /**
     * 设置全屏.
     *
     * @param activity 当前activity.
     */
    public static void setFullScreen(@NonNull final Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * 设置非全屏.
     *
     * @param activity 当前activity.
     */
    public static void setNonFullScreen(@NonNull final Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * Toggle full screen.
     *
     * @param activity 上下文.
     */
    public static void toggleFullScreen(@NonNull final Activity activity) {
        int fullScreenFlag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = activity.getWindow();
        if ((window.getAttributes().flags & fullScreenFlag) == fullScreenFlag) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }


    /**
     * 设置横屏.
     *
     * @param activity 当前activity.
     */
    public static void setLandscape(@NonNull final Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 设置竖屏.
     *
     * @param activity 当前activity.
     */
    public static void setPortrait(@NonNull final Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 获取当前屏幕是否为横屏.
     *
     * @return {@code true}: 横屏<br>{@code false}: 非横屏
     */
    public static boolean isLandscape() {
        return LifeCycleUtils.getApp().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 获取当前屏幕是否为竖屏.
     *
     * @return {@code true}: 竖屏<br>{@code false}: 竖屏
     */
    public static boolean isPortrait() {
        return LifeCycleUtils.getApp().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 获取屏幕旋转方向.
     *
     * @param activity 当前activity.
     * @return 屏幕旋转方向
     */
    public static int getScreenRotation(@NonNull final Activity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
            default:
                return 0;
        }
    }

    /**
     * 获取当前屏幕截图(不包含状态栏).
     *
     * @param activity 当前activity.
     * @return 当前屏幕截图
     */
    public static Bitmap screenShot(@NonNull final Activity activity) {
        return screenShot(activity, false);
    }

    /**
     * 获取当前屏幕截图.
     *
     * @param activity          当前activity.
     * @param isDeleteStatusBar True：不包含状态栏, false：包含状态栏.
     * @return 当前屏幕截图
     */
    public static Bitmap screenShot(@NonNull final Activity activity, boolean isDeleteStatusBar) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap bmp = decorView.getDrawingCache();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Bitmap ret;
        if (isDeleteStatusBar) {
            Resources resources = activity.getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            int statusBarHeight = resources.getDimensionPixelSize(resourceId);
            ret = Bitmap.createBitmap(
                    bmp,
                    0,
                    statusBarHeight,
                    dm.widthPixels,
                    dm.heightPixels - statusBarHeight
            );
        } else {
            ret = Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels);
        }
        decorView.destroyDrawingCache();
        return ret;
    }

    /**
     * 获取屏幕是否锁屏.
     *
     * @return {@code true}: 锁屏<br>{@code false}: 非锁屏
     */
    public static boolean isScreenLock() {
        KeyguardManager km =
                (KeyguardManager) LifeCycleUtils.getApp().getSystemService(Context.KEYGUARD_SERVICE);
        return km != null && km.inKeyguardRestrictedInputMode();
    }

    /**
     * 垂直方向适配屏幕.
     *
     * @param designWidthInDp 屏幕宽度, 单位是dp,例如：如果屏幕宽度是720px,在xhdpi设备上，designWidthInDp=720 / 2
     */
    public static void adaptScreen4VerticalSlide(final Activity activity,
                                                 final int designWidthInDp) {
        adaptScreen(activity, designWidthInDp, true);
    }

    /**
     * 水平方向适配屏幕.
     *
     * @param designHeightInDp 屏幕高度, 单位是dp,例如：如果屏幕高度是1080px,在xxdpi设备上，designHeightInDp=1080/3
     */
    public static void adaptScreen4HorizontalSlide(final Activity activity,
                                                   final int designHeightInDp) {
        adaptScreen(activity, designHeightInDp, false);
    }

    /**
     * 取消适配屏幕.
     *
     * @param activity 当前activity.
     */
    public static void cancelAdaptScreen(final Activity activity) {
        final DisplayMetrics appDm = LifeCycleUtils.getApp().getResources().getDisplayMetrics();
        final DisplayMetrics activityDm = activity.getResources().getDisplayMetrics();
        activityDm.density = appDm.density;
        activityDm.scaledDensity = appDm.scaledDensity;
        activityDm.densityDpi = appDm.densityDpi;
    }

    /**
     * 参考：https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA
     */
    private static void adaptScreen(final Activity activity,
                                    final float sizeInDp,
                                    final boolean isVerticalSlide) {
        final DisplayMetrics appDm = LifeCycleUtils.getApp().getResources().getDisplayMetrics();
        final DisplayMetrics activityDm = activity.getResources().getDisplayMetrics();
        if (isVerticalSlide) {
            activityDm.density = activityDm.widthPixels / sizeInDp;
        } else {
            activityDm.density = activityDm.heightPixels / sizeInDp;
        }
        activityDm.scaledDensity = activityDm.density * (appDm.scaledDensity / appDm.density);
        activityDm.densityDpi = (int) (160 * activityDm.density);
    }
}
