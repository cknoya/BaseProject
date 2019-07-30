package com.yuxi.jxs;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.yuxi.jxs.base.BaseActivity;
import com.yuxi.jxs.login.LoginActivity;
import com.yuxi.jxs.sp.SharedPreferencesFactory;

/**
 * author 陈开.
 * Date: 2019/6/1
 * Time: 13:59
 */
public class WelcomeActivity extends Activity {
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_layout);
        dispatchActivity();
    }
    private void dispatchActivity() {
        handler.postDelayed(() -> {
            boolean isLogin = SharedPreferencesFactory.get(MyApplaction.getInstance(), "isLogin", false);
            if (isLogin) {
                //若登录，则跳转到首页
                MainActivity.showClass(WelcomeActivity.this);
                finish();
            } else {
                //若未登录，则跳转到登录页面
                LoginActivity.showClass(WelcomeActivity.this);
                finish();
            }
        }, 500L);
    }
    /**
     * 屏蔽物理返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (handler != null) {
            //If token is null, all callbacks and messages will be removed.
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

}
