package com.yuxi.jxs.eventbus;

/**
 * author 陈开.
 * Date: 2019/6/3
 * Time: 12:24
 */
public class BadgeMessage {
    private int count;
    private int type;
    public BadgeMessage(int count, int type){
        this.count = count;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public int getCount() {
        return count;
    }
}
