package com.yuxi.jxs.eventbus;

/**
 * author 陈开.
 * Date: 2019/7/9
 * Time: 14:06
 */
public class AvatarUpMessage {
    boolean success;
    public AvatarUpMessage(boolean success){
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
