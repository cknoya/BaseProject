package com.yuxi.jxs.eventbus;

/**
 * author 陈开.
 * Date: 2019/7/19
 * Time: 14:31
 */
public class AvatarUpdataMeaasage {
    boolean success;
    public AvatarUpdataMeaasage(boolean success){
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
