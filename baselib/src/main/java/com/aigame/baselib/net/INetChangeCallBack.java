package com.aigame.baselib.net;

public interface INetChangeCallBack {
    /**
     * 仅仅监控网络，对类型没有要求
     *
     * @param isConnected true有网络
     */
    void onNetworkChange(boolean isConnected);
}