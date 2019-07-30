package com.yuxi.jxs.base;

import android.support.v4.app.Fragment;

import com.gyf.immersionbar.components.SimpleImmersionFragment;

/**
 * author 陈开.
 * Date: 2019/6/17
 * Time: 11:26
 */
public abstract class LazyFragment extends SimpleImmersionFragment {
    protected boolean isVisible;
    /**
     * 在这里实现Fragment数据的缓加载.
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {//当可见的时候执行操作
            isVisible = true;
            onVisible();
        } else {//不可见时执行相应的操作
            isVisible = false;
            onInvisible();
        }
    }
    protected void onVisible(){
        lazyLoad();
    }
    protected abstract void lazyLoad();//子类实现
    protected void onInvisible(){}
}
