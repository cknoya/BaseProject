package com.aigame.basecore.job.handler;


public interface IJobHandler {

    public static final int SUCCESS = 1;

    public static final int FAILED = SUCCESS + 1;

    void postResult(int resultCode, Object result);

    void postSuccess(Object result);

    void postFailed();
}
