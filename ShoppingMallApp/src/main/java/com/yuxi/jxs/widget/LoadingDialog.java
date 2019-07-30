package com.yuxi.jxs.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;
import com.yuxi.jxs.R;

/**
 * author 陈开.
 * Date: 2019/6/10
 * Time: 13:48
 */
public class LoadingDialog extends Dialog {

    private AVLoadingIndicatorView avi;
    private TextView messagetv;
    private RelativeLayout loadingbg;
    public static LoadingDialog loadingDialog;

    public static LoadingDialog newInstance(Context context) {
        if (loadingDialog == null) {
            return new LoadingDialog(context, R.style.dialog);
        } else {
            return loadingDialog;
        }
    }

    /**
     * 自定义主题及布局的构造方法
     *
     * @param context
     * @param theme// 去除顶部蓝色线条
     */
    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        /**设置对话框背景透明*/
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.loading_layout);
        loadingbg = (RelativeLayout) findViewById(R.id.loadingbg);
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        messagetv = (TextView) findViewById(R.id.message);
    }

    /**
     * 为加载进度个对话框设置不同的提示消息
     *
     * @param message 给用户展示的提示信息
     * @return build模式设计，可以链式调用
     */
    public LoadingDialog setMessage(String message) {
        messagetv.setText(message);
        return this;
    }

    @Override
    public void show() {
        super.show();
        avi.smoothToShow();
    }

    /***
     * 设置loading背景色
     * @param Colorbg
     * @return
     */
    public LoadingDialog setLoadingBg(int Colorbg) {
        loadingbg.setBackgroundColor(Colorbg);
        return this;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        avi.smoothToHide();
    }
}