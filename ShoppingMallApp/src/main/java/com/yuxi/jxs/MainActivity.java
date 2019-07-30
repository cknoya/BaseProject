package com.yuxi.jxs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.KeyEvent;
import com.yuxi.jxs.base.BaseActivity;
import com.yuxi.jxs.util.ActivityUtils;
import com.yuxi.jxs.util.ToastUtil;


import org.greenrobot.eventbus.EventBus;


/**
 * author 陈开.
 * Date: 2019/5/24
 * Time: 9:48
 */
public class MainActivity extends BaseActivity {

    public static void showClass(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {


    }

    //退出时的时间
    private long mExitTime;

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            ToastUtil.longs(this, "再按一次退出");
            mExitTime = System.currentTimeMillis();
        } else {
            ActivityUtils.removeAllActivity();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
