package com.yuxi.jxs.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.gyf.immersionbar.ImmersionBar;
import com.yuxi.jxs.R;
import com.yuxi.jxs.widget.LoadingDialog;
import com.yuxi.jxs.widget.NetworkStateView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends LazyFragment implements NetworkStateView.OnRefreshListener{

    private View mView;

    private Unbinder unbinder;
    protected Activity mActivity;
    private NetworkStateView networkStateView;
    private LoadingDialog loadingDialog;
    public ImmersionBar mImmersionBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_base, container, false);

        ViewGroup parent = (ViewGroup) mView.getParent();
        if (null != parent) {
            parent.removeView(mView);
        }

        addChildView(inflater);

        unbinder = ButterKnife.bind(this, mView);
        loadingDialog = LoadingDialog.newInstance(getContext());
        afterCreate(savedInstanceState);
        initImmersionBar();
        return mView;
    }

    @Override
    public void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.keyboardEnable(true).statusBarDarkFont(true).init();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 添加子Fragment的布局文件
     * @param inflater
     */
    private void addChildView(LayoutInflater inflater) {
        networkStateView = (NetworkStateView) mView.findViewById(R.id.nsv_state_view);
        FrameLayout container = (FrameLayout) mView.findViewById(R.id.fl_fragment_child_container);
        View child = inflater.inflate(getLayoutId(), null);
        container.addView(child, 0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
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
    public void showNoNetworkView(String title,boolean cangoback) {
        networkStateView.showNoNetwork(title, cangoback);
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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}