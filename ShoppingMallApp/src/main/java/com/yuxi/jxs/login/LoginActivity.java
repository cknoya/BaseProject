package com.yuxi.jxs.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yuxi.jxs.MainActivity;
import com.yuxi.jxs.MyApplaction;
import com.yuxi.jxs.R;
import com.yuxi.jxs.base.BaseActivity;
import com.yuxi.jxs.base.BaseBean;
import com.yuxi.jxs.callback.MyStringCallBack;
import com.yuxi.jxs.global.Url;
import com.yuxi.jxs.sp.SharedPreferencesFactory;
import com.yuxi.jxs.util.JsonUtil;
import com.yuxi.jxs.util.KeyboardHelper;
import com.yuxi.jxs.util.MD5Utils;
import com.yuxi.jxs.util.ToastUtil;
import com.yuxi.jxs.util.UIUtils;
import com.yuxi.jxs.util.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_account)
    TextInputEditText etAccount;
    @BindView(R.id.et_password)
    TextInputEditText etPasswd;
    @BindView(R.id.btn_login)
    AppCompatButton btnLogin;
    @BindView(R.id.et_company)
    TextInputEditText etCompany;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;
    private String account;
    private String password;
    private String company;
    private KeyboardHelper keyboardHelper;
    private int bottomHeight;

    public static void showClass(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_layout;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        account = SharedPreferencesFactory.get(MyApplaction.getInstance(), "loginname", "");
        company = SharedPreferencesFactory.get(MyApplaction.getInstance(), "logincompany", "");
        if (!TextUtils.isEmpty(account)) {
            ViewUtil.bindView(etAccount, account);
        }
        if (!TextUtils.isEmpty(company)) {
            ViewUtil.bindView(etCompany, company);
        }
        keyboardHelper = new KeyboardHelper(this);
        keyboardHelper.onCreate();
        keyboardHelper.setOnKeyboardStatusChangeListener(onKeyBoardStatusChangeListener);
        btnLogin.post(() -> bottomHeight = btnLogin.getHeight() + UIUtils.dp2px(70));
    }

    private void login() {
        String pushTocken = SharedPreferencesFactory.get(MyApplaction.getInstance(), "SP_KEY_UMENG_TOKEN", "");
        OkGo.<String>post(Url.LOGIN_URL)
                .params("company_yu", company)
                .params("username", account)
                .params("password", MD5Utils.md5(password))
                .params("push_token",pushTocken)
//                .params("device_type", "android")
//                .params("device_version", "")
                .execute(new MyStringCallBack() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dismissProgressDialog();
                        BaseBean baseBean = JsonUtil.commonResponse(response.body());
                        if (baseBean.getCode().equals("1")) {
                            SharedPreferencesFactory.set(MyApplaction.getInstance(), "isLogin", true);
                            SharedPreferencesFactory.set(MyApplaction.getInstance(), "loginInfo", baseBean.getData());
                            SharedPreferencesFactory.set(MyApplaction.getInstance(), "logincompany", company);
                            SharedPreferencesFactory.set(MyApplaction.getInstance(), "loginname", account);
                            MainActivity.showClass(LoginActivity.this);
                            finish();
                        } else {
                            ToastUtil.longs(LoginActivity.this, baseBean.getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dismissProgressDialog();
                        ToastUtil.longs(LoginActivity.this, response.getException().getMessage());
                    }
                });

    }

    private KeyboardHelper.OnKeyboardStatusChangeListener onKeyBoardStatusChangeListener = new KeyboardHelper.OnKeyboardStatusChangeListener() {

        @Override
        public void onKeyboardPop(int keyboardHeight) {

            final int height = keyboardHeight;
            int offset = bottomHeight - height;
            final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mainLayout
                    .getLayoutParams();
            lp.topMargin = offset;
            mainLayout.setLayoutParams(lp);

        }

        @Override
        public void onKeyboardClose(int keyboardHeight) {
            final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mainLayout
                    .getLayoutParams();
            if (lp.topMargin != 0) {
                lp.topMargin = 0;
                mainLayout.setLayoutParams(lp);
            }

        }
    };

    @OnClick({R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                account = etAccount.getText().toString().trim();
                password = etPasswd.getText().toString().trim();
                company = etCompany.getText().toString().trim();
                if (TextUtils.isEmpty(company)) {
                    ToastUtil.longs(this, "请填写公司域");
                    return;
                }
                if (TextUtils.isEmpty(account)) {
                    ToastUtil.longs(this, "请填写是账号");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtil.longs(this, "请填写密码");
                    return;
                }
                showProgressDialog();
                login();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        keyboardHelper.onDestroy();
    }
}
