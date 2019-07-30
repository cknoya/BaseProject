package com.yuxi.jxs.global;


import com.google.gson.Gson;
import com.yuxi.jxs.MyApplaction;
import com.yuxi.jxs.login.LoginBean;
import com.yuxi.jxs.sp.SharedPreferencesFactory;

/**
 * author 陈开.
 * Date: 2019/6/1
 * Time: 14:06
 */
public class Global {

    //上传图片保存记录最多几条 要大于9
    public static final int UPLOAD_BEAN_SIZE = 100;

    //上传又拍云的路径
    public static final String OFFLINE_PAY = "offline";
    public static final String JXS_FEEDBACK = "jxsfeedback";
    public static final String AVATAR = "avatar";

    public static final String MIDDLE = "!middle";

    public static LoginBean getUser() {
        String loginJson = SharedPreferencesFactory.get(MyApplaction.getInstance(), "loginInfo", "");
        Gson goson = new Gson();
        LoginBean loginBean = goson.fromJson(loginJson, LoginBean.class);
        return loginBean;
    }

    //单次更新版本提示
    public static String getSingleVersionCode() {
        String singleVersioncode = SharedPreferencesFactory.get(MyApplaction.getInstance(), "MySingleVersion", "");
        return singleVersioncode;
    }
}
