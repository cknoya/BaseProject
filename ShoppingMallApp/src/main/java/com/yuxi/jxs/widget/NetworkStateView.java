package com.yuxi.jxs.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.yuxi.jxs.R;
import com.yuxi.jxs.util.UIUtils;

import java.util.List;


/**
 * Created by ZHT on 2017/4/15.
 * 加载状态的自定义View
 */

public class NetworkStateView extends LinearLayout {

    //当前的加载状态
    private int mCurrentState;
    private static final int STATE_SUCCESS = 0;
    private static final int STATE_LOADING = 1;
    private static final int STATE_NETWORK_ERROR = 2;
    private static final int STATE_NO_NETWORK = 3;
    private static final int STATE_EMPTY = 4;

    private int mLoadingViewId;

    private int mErrorViewId;
    private int mErrorImageId;
    private String mErrorText;

    private int mNoNetworkViewId;
    private int mNoNetworkImageId;
    private String mNoNetworkText;

    private int mEmptyViewId;
    private int mEmptyImageId;
    private String mEmptyText;

    private int mRefreshViewId;

    private int mTextColor;
    private int mTextSize;

    private View mLoadingView;
    private View mErrorView;
    private View mNoNetworkView;
    private View mEmptyView;

    private LayoutInflater mInflater;
    private ViewGroup.LayoutParams params;

    private OnRefreshListener mRefreshListener;

    public NetworkStateView(@NonNull Context context) {
        this(context, null);
    }

    public NetworkStateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.styleNetworkStateView);
    }

    public NetworkStateView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NetworkStateView, defStyleAttr, R.style.NetworkStateView_Style);

        mLoadingViewId = typedArray.getResourceId(R.styleable.NetworkStateView_loadingView, R.layout.view_loading);

        mErrorViewId = typedArray.getResourceId(R.styleable.NetworkStateView_errorView, R.layout.view_network_error);
        mErrorImageId = typedArray.getResourceId(R.styleable.NetworkStateView_nsvErrorImage, NO_ID);
        mErrorText = typedArray.getString(R.styleable.NetworkStateView_nsvErrorText);

        mNoNetworkViewId = typedArray.getResourceId(R.styleable.NetworkStateView_noNetworkView, R.layout.view_no_network);
        mNoNetworkImageId = typedArray.getResourceId(R.styleable.NetworkStateView_nsvNoNetworkImage, NO_ID);
        mNoNetworkText = typedArray.getString(R.styleable.NetworkStateView_nsvNoNetworkText);

        mEmptyViewId = typedArray.getResourceId(R.styleable.NetworkStateView_emptyView, R.layout.view_empty);
        mEmptyImageId = typedArray.getResourceId(R.styleable.NetworkStateView_nsvEmptyImage, NO_ID);
        mEmptyText = typedArray.getString(R.styleable.NetworkStateView_nsvEmptyText);

        mRefreshViewId = typedArray.getResourceId(R.styleable.NetworkStateView_nsvRefreshImage, NO_ID);

        mTextColor = typedArray.getColor(R.styleable.NetworkStateView_nsvTextColor, 0x8a000000);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.NetworkStateView_nsvTextSize, UIUtils.dp2px(14));

        typedArray.recycle();

        mInflater = LayoutInflater.from(context);
        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundColor(UIUtils.getColor(R.color.color_white));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        showSuccess();
    }

    /**
     * 加载成功，隐藏加载状态的View
     */
    public void showSuccess() {
        mCurrentState = STATE_SUCCESS;
        showViewByState(mCurrentState);
    }

    /**
     * 显示加载中的状态
     */
    public void showLoading() {
        mCurrentState = STATE_LOADING;
        if (null == mLoadingView) {
            mLoadingView = mInflater.inflate(mLoadingViewId, null);
            addView(mLoadingView, 0, params);
        }
        showViewByState(mCurrentState);
    }

    /**
     * 显示加载失败(网络错误)状态
     */
    public void showError(String title, boolean cangoback) {
        mCurrentState = STATE_NETWORK_ERROR;
        if (null == mErrorView) {
            mErrorView = mInflater.inflate(mErrorViewId, null);
            ImageView errorImage = (ImageView) mErrorView.findViewById(R.id.error_image);
            TextView errorText = (TextView) mErrorView.findViewById(R.id.error_text);
            ImageView errorRefreshView = (ImageView) mErrorView.findViewById(R.id.refresh_view);
            ImageView tv_back = (ImageView) mErrorView.findViewById(R.id.im_back);
            TextView tvtitle = mErrorView.findViewById(R.id.tv_title);
            LinearLayout titleBar = mErrorView.findViewById(R.id.toolbar_layout);
            tvtitle.setText(title);
            image(errorImage, mErrorImageId);
            text(errorText, mErrorText);
            image(errorRefreshView, mRefreshViewId);
            if (TextUtils.isEmpty(title)){
                titleBar.setVisibility(GONE);
            }else {
                titleBar.setVisibility(VISIBLE);
            }
            if (cangoback) {
                tv_back.setVisibility(VISIBLE);
            } else {
                tv_back.setVisibility(GONE);
            }
            if (null != errorRefreshView) {
                errorRefreshView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (null != mRefreshListener) {
                            mRefreshListener.onRefresh();
                        }
                    }
                });
            }
            if (null != tv_back) {
                tv_back.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (null != mRefreshListener) {
                            mRefreshListener.goBack();
                        }
                    }
                });
            }
            addView(mErrorView, 0, params);
        }
        showViewByState(mCurrentState);
    }

    /**
     * 显示没有网络状态
     */
    public void showNoNetwork(String title, boolean cangoback) {
        mCurrentState = STATE_NO_NETWORK;
        if (null == mNoNetworkView) {
            mNoNetworkView = mInflater.inflate(mNoNetworkViewId, null);
            ImageView noNetworkImage = (ImageView) mNoNetworkView.findViewById(R.id.no_network_image);
            TextView noNetworkText = (TextView) mNoNetworkView.findViewById(R.id.no_network_text);
            ImageView networkRefreshView = (ImageView) mNoNetworkView.findViewById(R.id.refresh_view);
            ImageView tv_back = (ImageView) mNoNetworkView.findViewById(R.id.im_back);
            TextView tvtitle = mNoNetworkView.findViewById(R.id.tv_title);
            LinearLayout titleBar = mNoNetworkView.findViewById(R.id.toolbar_layout);
            tvtitle.setText(title);
            image(noNetworkImage, mNoNetworkImageId);
            text(noNetworkText, mNoNetworkText);
            image(networkRefreshView, mRefreshViewId);
            if (TextUtils.isEmpty(title)){
                titleBar.setVisibility(GONE);
            }else {
                titleBar.setVisibility(VISIBLE);
            }
            if (cangoback) {
                tv_back.setVisibility(VISIBLE);
            } else {
                tv_back.setVisibility(GONE);
            }
            if (null != networkRefreshView) {
                networkRefreshView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (null != mRefreshListener) {
                            mRefreshListener.onRefresh();
                        }
                    }
                });
            }
            if (null != tv_back) {
                tv_back.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (null != mRefreshListener) {
                            mRefreshListener.goBack();
                        }
                    }
                });
            }
            addView(mNoNetworkView, 0, params);
        }
        showViewByState(mCurrentState);
    }

    /**
     * 显示无数据状态
     */
    public void showEmpty(String title, boolean cangoback) {
        mCurrentState = STATE_EMPTY;
        if (null == mEmptyView) {
            mEmptyView = mInflater.inflate(mEmptyViewId, null);
            ImageView emptyImage = (ImageView) mEmptyView.findViewById(R.id.empty_image);
            TextView emptyText = (TextView) mEmptyView.findViewById(R.id.empty_text);
            LinearLayout titleBar = mEmptyView.findViewById(R.id.toolbar_layout);
            ImageView emptyRefreshView = (ImageView) mEmptyView.findViewById(R.id.refresh_view);
            ImageView tv_back = (ImageView) mEmptyView.findViewById(R.id.im_back);
            TextView goshopping = (TextView) mEmptyView.findViewById(R.id.go_shopping);
            TextView tvtitle = mEmptyView.findViewById(R.id.tv_title);
            tvtitle.setText(title);
            image(emptyImage, mEmptyImageId);
            text(emptyText, mEmptyText);
            if (!TextUtils.isEmpty(title) && title.equals("购物车")){
                emptyRefreshView.setVisibility(GONE);
                emptyText.setVisibility(GONE);
                if (cangoback){
                    tv_back.setVisibility(VISIBLE);
                    goshopping.setVisibility(GONE);
                }else {
                    tv_back.setVisibility(GONE);
                    goshopping.setVisibility(VISIBLE);
                }
            }else if(TextUtils.isEmpty(title)){
                titleBar.setVisibility(GONE);
                image(emptyRefreshView, mRefreshViewId);
            }else {
                image(emptyRefreshView, mRefreshViewId);
            }
            if (null != emptyRefreshView) {
                emptyRefreshView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (null != mRefreshListener) {
                            mRefreshListener.onRefresh();
                        }
                    }
                });
            }
            if (null != tv_back) {
                tv_back.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (null != mRefreshListener) {
                            mRefreshListener.goBack();
                        }
                    }
                });
            }
            if (null != goshopping) {
                goshopping.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mRefreshListener) {
                            mRefreshListener.goShopping();
                        }
                    }
                });
            }
            addView(mEmptyView, 0, params);
        }

        showViewByState(mCurrentState);
    }

    private void showViewByState(int state) {

        //如果当前状态为加载成功，隐藏此View，反之显示
        this.setVisibility(state == STATE_SUCCESS ? View.GONE : View.VISIBLE);

        if (null != mLoadingView) {
            mLoadingView.setVisibility(state == STATE_LOADING ? View.VISIBLE : View.GONE);
        }

        if (null != mErrorView) {
            mErrorView.setVisibility(state == STATE_NETWORK_ERROR ? View.VISIBLE : View.GONE);
        }

        if (null != mNoNetworkView) {
            mNoNetworkView.setVisibility(state == STATE_NO_NETWORK ? View.VISIBLE : View.GONE);
        }

        if (null != mEmptyView) {
            mEmptyView.setVisibility(state == STATE_EMPTY ? View.VISIBLE : View.GONE);
        }
    }

    private void image(ImageView view, int imageId) {
        if (null != view && imageId != NO_ID) {
            view.setImageResource(imageId);
        }
    }

    private void text(TextView view, String str) {
        if (null != view && !TextUtils.isEmpty(str)) {
            view.setText(str);
            view.setTextColor(mTextColor);
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        }
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mRefreshListener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();

        void goBack();

        void goShopping();
    }
}
