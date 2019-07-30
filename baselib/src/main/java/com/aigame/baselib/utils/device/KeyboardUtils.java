/*
 * Copyright (C) 2015-2016 Jacksgong(blog.dreamtobe.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aigame.baselib.utils.device;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.aigame.baselib.io.sp.SPAdapter;
import com.aigame.baselib.utils.ui.ScreenUtils;
import com.aigame.baselib.utils.ui.UIUtils;


/**
 * Created by Jacksgong on 15/7/6.
 * <p/>
 * For save the keyboard height.
 * <p/>
 * Adapt the panel height with the keyboard height just relate {@link #attach(Activity, OnKeyboardShowingListener)}.
 *
 * @author zhaokaiyuan
 *         2017-07-10
 * @see "https://github.com/Jacksgong/JKeyboardPanelSwitch"
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class KeyboardUtils {

    /**
     * Log Tag
     */
    private static final String TAG = "KeyboardUtils";
    /**
     * 存储键盘高度的SP的Key
     */
    private static final String SP_KEYBOARD_HEIGHT_KEY = "sp_keyboard_height";

    /**
     * 上次存储的键盘高度
     */
    private static int LAST_SAVE_KEYBOARD_HEIGHT = 0;
    /**
     * 最小键盘高度
     */
    private static int MIN_KEYBOARD_HEIGHT = 0;

    /**
     * 避免输入法面板遮挡
     * <p>在manifest.xml中activity中设置</p>
     * <p>android:windowSoftInputMode="adjustPan"</p>
     */
    private KeyboardUtils() {
    }

    /**
     * 动态隐藏软键盘
     *
     * @param context context
     * @param editText 输入控件
     */
    public static void hideSoftInput(Context context, EditText editText) {
        if (context == null || editText == null) {
            return;
        }
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager == null) {
            return;
        }
        manager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 动态显示软键盘
     *
     * @param context 上下文
     */
    public static void showSoftInput(Context context) {
        if (context == null) {
            return;
        }
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager == null) {
            return;
        }
        manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 动态显示软键盘
     *
     * @param view view
     */
    public static void showKeyboard(final View view) {
        if (view == null) {
            return;
        }
        view.requestFocus();
        InputMethodManager manager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager == null) {
            return;
        }
        manager.showSoftInput(view, 0);
    }

    /**
     * 动态隐藏软键盘
     *
     * @param view view
     */
    public static void hideKeyboard(final View view) {
        if (view == null) {
            return;
        }
        InputMethodManager manager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager == null) {
            return;
        }
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 存储键盘高度
     *
     * @param context        context
     * @param keyboardHeight 键盘高度
     * @return 是否有改变
     */
    private static boolean saveKeyboardHeight(final Context context, int keyboardHeight) {
        if (LAST_SAVE_KEYBOARD_HEIGHT == keyboardHeight) {
            return false;
        }
        if (keyboardHeight < 0) {
            return false;
        }
        LAST_SAVE_KEYBOARD_HEIGHT = keyboardHeight;
        SPAdapter.set(context, SP_KEYBOARD_HEIGHT_KEY, keyboardHeight);
        return true;
    }

    /**
     * 获取键盘高度
     * 必须先调用attach 否则获取的仅仅是上次缓存得到的数据
     *
     * @param context 上下文
     * @return 键盘高度
     * @see #attach(Activity, OnKeyboardShowingListener)
     * <p/>
     * Handle and refresh the keyboard height by {@link #attach(Activity, OnKeyboardShowingListener)}.
     */
    public static int getKeyboardHeight(final Context context) {
        if (LAST_SAVE_KEYBOARD_HEIGHT == 0) {
            LAST_SAVE_KEYBOARD_HEIGHT = SPAdapter.get(context, SP_KEYBOARD_HEIGHT_KEY,
                    getMinKeyboardHeight(context));
        }

        return LAST_SAVE_KEYBOARD_HEIGHT;
    }

    /**
     * 获取最小键盘高度
     *
     * @param context 上下文
     * @return 最小键盘高度
     */
    public static int getMinKeyboardHeight(Context context) {
        if (MIN_KEYBOARD_HEIGHT == 0) {
            MIN_KEYBOARD_HEIGHT = UIUtils.dip2px(context, 80);
        }
        return MIN_KEYBOARD_HEIGHT;
    }

    /**
     * Recommend invoked by {@link Activity#onCreate(Bundle)}
     * For align the height of the keyboard to {@code target} as much as possible.
     * For save the refresh the keyboard height to shared-preferences.
     *
     * @param activity contain the view
     * @param listener the listener to listen in: keyboard is showing or not.
     * @see #saveKeyboardHeight(Context, int)
     */
    public static ViewTreeObserver.OnGlobalLayoutListener attach(final Activity activity, OnKeyboardShowingListener listener) {
        View contentView = activity.findViewById(android.R.id.content);
        if (contentView == null) {
            contentView = activity.getWindow().getDecorView();
        }
        ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener =
                new KeyboardStatusListener(activity, contentView, listener);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        return globalLayoutListener;
    }

    /**
     * Remove the OnGlobalLayoutListener from the activity root view
     *
     * @param activity same activity used in {@link #attach} method
     * @param l        ViewTreeObserver.OnGlobalLayoutListener returned by {@link #attach} method
     */
    public static void detach(Activity activity, ViewTreeObserver.OnGlobalLayoutListener l) {
        if (l == null) {
            return;
        }
        View contentView = activity.findViewById(android.R.id.content);
        if (contentView == null) {
            contentView = activity.getWindow().getDecorView();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            contentView.getViewTreeObserver().removeOnGlobalLayoutListener(l);
        } else {
            //noinspection deprecation
            contentView.getViewTreeObserver().removeGlobalOnLayoutListener(l);
        }
    }

    /**
     * The interface is used to listen the keyboard showing state.
     *
     * @see #attach(Activity, OnKeyboardShowingListener)
     */
    public interface OnKeyboardShowingListener {

        /**
         * Keyboard showing state callback method.
         * <p>
         * This method is invoked in {@link ViewTreeObserver.OnGlobalLayoutListener#onGlobalLayout()} which is one of the
         * ViewTree lifecycle callback methods. So deprecating those time-consuming operation(I/O, complex calculation,
         * alloc objects, etc.) here from blocking main ui thread is recommended.
         * </p>
         *
         * @param isShowing Indicate whether keyboard is showing or not.
         */
        void onKeyboardShowing(boolean isShowing);

        /**
         * Keyboard height change callback method.
         * 存在透明虚拟导航键且非fitWindow时键盘加上了导航键高度
         *
         * @param height keyboard height
         */
        void onKeyboardHeightChanged(int height);

    }

    private static class KeyboardStatusListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private final static String TAG = "KeyboardStatusListener";
        private final View mContentView;
        private final int mStatusBarHeight;
        private final OnKeyboardShowingListener mKeyboardShowingListener;
        private boolean mLastKeyboardShowing;
        private Rect mDisplayRect = new Rect();
        private Point mScreenSize = new Point();
        private Activity mActivity;
        private boolean mFirstCalled = false;
        private boolean mShouldMinusStatusHeight = true;

        KeyboardStatusListener(Activity activity, View contentView,
                               OnKeyboardShowingListener listener) {
            this.mActivity = activity;
            if (contentView == null) {
                this.mContentView = mActivity.getWindow().getDecorView();
            } else {
                this.mContentView = contentView;
            }
            this.mStatusBarHeight = ScreenUtils.getStatusBarHeight(mActivity);
            this.mKeyboardShowingListener = listener;
        }

        @Override
        public void onGlobalLayout() {
            mContentView.getWindowVisibleDisplayFrame(mDisplayRect);
            int displayHeight = mDisplayRect.bottom - mDisplayRect.top;
            calculateKeyboardHeight(displayHeight);
        }

        private void calculateKeyboardHeight(final int displayHeight) {
            final boolean isFullScreen = ScreenUtils.isFullScreen(mActivity);
            Display display = mActivity.getWindowManager().getDefaultDisplay();
            display.getSize(mScreenSize);
            boolean isTranslucentStatue = ScreenUtils.isTranslucentStatus(mActivity);
            if (isTranslucentStatue && !isFullScreen && mScreenSize.y == displayHeight || isFullScreen) {
                mShouldMinusStatusHeight = false;
            }
            final int phoneDisplayHeight = mScreenSize.y - (mShouldMinusStatusHeight ? mStatusBarHeight : 0);
            int keyboardHeight = phoneDisplayHeight - displayHeight;
            // 透明虚拟导航键且非fitWindow时键盘需要加上导航键高度 但实际上键盘的高度不需要增加
            boolean hasNavigationBar = ScreenUtils.hasNavigationBar(mActivity);
            if (hasNavigationBar) {
                final boolean isTranslucentNavigation = ScreenUtils.isTranslucentNavigation(mActivity);
                final boolean isFitWindow = ScreenUtils.isFitsSystemWindows(mActivity);
                if (!isFitWindow && isTranslucentNavigation) {
                    keyboardHeight += ScreenUtils.getNavigationBarHeight(mActivity);
                }
            }
            if (keyboardHeight <= getMinKeyboardHeight(mActivity) || Math.abs(keyboardHeight) == mStatusBarHeight) {
                if (mKeyboardShowingListener != null && mLastKeyboardShowing) {
                    mKeyboardShowingListener.onKeyboardShowing(false);
                }
                mLastKeyboardShowing = false;
                return;
            }
            // save the keyboardHeight
            boolean changed = KeyboardUtils.saveKeyboardHeight(mActivity, keyboardHeight);
            if ((changed || !mFirstCalled) && mKeyboardShowingListener != null) {
                this.mKeyboardShowingListener.onKeyboardHeightChanged(keyboardHeight);
                mFirstCalled = true;
            }
            if (mKeyboardShowingListener != null && !mLastKeyboardShowing) {
                mKeyboardShowingListener.onKeyboardShowing(true);
            }
            mLastKeyboardShowing = true;
        }
    }
}
