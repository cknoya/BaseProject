package com.yuxi.jxs.eventbus;

/**
 * author 陈开.
 * Date: 2019/6/14
 * Time: 16:19
 */
public class RefreshMessage {
    private boolean refresh;
    public RefreshMessage(boolean refresh){
        this.refresh = refresh;
    }

    public boolean isRefresh() {
        return refresh;
    }
}
