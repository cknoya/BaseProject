package com.yuxi.jxs.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.yuxi.jxs.R;

/**
 * author 陈开.
 * Date: 2019/5/25
 * Time: 16:08
 */
public class DialogHelper {
    public static void showNormalDialog(Activity activity, String title, String message) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确认", (dialog, which) -> dialog.dismiss())
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss()).show();
    }
    public static void showSingleDialog(Activity activity, String title, String message){
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确认", (dialog, which) -> dialog.dismiss()).show();
    }
    //带回调的弹窗
    public static void showNormalDialog(Activity activity, String title, String message, final PositiveCallBack postiveCallBack, final NegativeCallBack negativeCallBack) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确认", (dialog, which) -> postiveCallBack.CallBack(dialog))
                .setNegativeButton("取消", (dialog, which) -> negativeCallBack.CallBack(dialog)).show();
    }
    public interface PositiveCallBack {
        void CallBack(DialogInterface dialog);
    }


    public interface NegativeCallBack {
        void CallBack(DialogInterface dialog);
    }

}
