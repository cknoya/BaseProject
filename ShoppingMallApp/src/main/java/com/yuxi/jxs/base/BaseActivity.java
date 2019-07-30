package com.yuxi.jxs.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;


import com.gyf.immersionbar.ImmersionBar;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.wang.avi.AVLoadingIndicatorView;
import com.yuxi.jxs.R;
import com.yuxi.jxs.util.ActivityUtils;
import com.yuxi.jxs.util.PermissionListener;
import com.yuxi.jxs.widget.LoadingDialog;
import com.yuxi.jxs.widget.NetworkStateView;


import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by chenkai 2019-5-23
 * Activity基类
 */

public abstract class BaseActivity extends AppCompatActivity implements NetworkStateView.OnRefreshListener {

    private Unbinder unbinder;
    private NetworkStateView networkStateView;

    private static PermissionListener mPermissionListener;
    private static final int CODE_REQUEST_PERMISSION = 1;
    public ImmersionBar mImmersionBar;
    private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        initImmersionBar();
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        ActivityUtils.addActivity(this);
        loadingDialog = LoadingDialog.newInstance(this);
        afterCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
    }

    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true).keyboardEnable(true).init();
    }
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = getLayoutInflater().inflate(R.layout.activity_base, null);
        //设置填充activity_base布局
        super.setContentView(view);
        //加载子类Activity的布局
        initDefaultView(layoutResID);
    }

    /**
     * 初始化默认布局的View
     * @param layoutResId 子View的布局id
     */
    private void initDefaultView(int layoutResId) {
        networkStateView = (NetworkStateView) findViewById(R.id.nsv_state_view);
        FrameLayout container = (FrameLayout) findViewById(R.id.fl_activity_child_container);
        View childView = LayoutInflater.from(this).inflate(layoutResId, null);
        container.addView(childView, 0);
    }

    protected abstract int getLayoutId();

    protected abstract void afterCreate(Bundle savedInstanceState);


    /**
     * 显示加载中的布局
     */
    public void showLoadingView() {
        networkStateView.showLoading();
    }

    /**
     * 显示加载完成后的布局(即子类Activity的布局)
     */
    public void showContentView() {
        networkStateView.showSuccess();
    }

    /**
     * 显示没有网络的布局
     */
    public void showNoNetworkView(String title, boolean cangoback) {
        networkStateView.showNoNetwork(title,cangoback);
        networkStateView.setOnRefreshListener(this);
    }

    /**
     * 显示没有数据的布局
     */
    public void showEmptyView(String title,boolean cangoback) {
        networkStateView.showEmpty(title,cangoback);
        networkStateView.setOnRefreshListener(this);
    }

    /**
     * 显示数据错误，网络错误等布局
     */
    public void showErrorView(String title,boolean cangoback) {
        networkStateView.showError(title,cangoback);
        networkStateView.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        onNetworkViewRefresh();
    }

    @Override
    public void goBack() {

    }

    @Override
    public void goShopping() {

    }

    /**
     * 重新请求网络
     */
    public void onNetworkViewRefresh() {
    }

    /**
     * 显示加载的ProgressDialog
     */
    public void showProgressDialog() {
        loadingDialog.show();
    }


    /**
     * 隐藏加载的ProgressDialog
     */
    public void dismissProgressDialog() {
        loadingDialog.dismiss();
    }

    /**
     * 申请权限
     * @param permissions 需要申请的权限(数组)
     * @param listener 权限回调接口
     */
    public static void requestPermissions(String[] permissions, PermissionListener listener) {
        Activity activity = ActivityUtils.getTopActivity();
        if (null == activity) {
            return;
        }

        mPermissionListener = listener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            //权限没有授权
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionList.toArray(new String[permissionList.size()]), CODE_REQUEST_PERMISSION);
        } else {
            mPermissionListener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CODE_REQUEST_PERMISSION:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int result = grantResults[i];
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            String permission = permissions[i];
                            deniedPermissions.add(permission);
                        }
                    }

                    if (deniedPermissions.isEmpty()) {
                        mPermissionListener.onGranted();
                    } else {
                        mPermissionListener.onDenied(deniedPermissions);
                    }
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        ActivityUtils.removeActivity(this);
    }
}
