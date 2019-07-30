package com.yuxi.jxs.callback;

import android.content.Intent;
import android.text.TextUtils;

import com.lzy.okgo.convert.StringConvert;
import com.yuxi.jxs.base.BaseBean;
import com.yuxi.jxs.login.LoginActivity;
import com.yuxi.jxs.login.LoginBean;
import com.yuxi.jxs.util.JsonUtil;
import com.yuxi.jxs.util.MD5Utils;
import com.yuxi.jxs.MyApplaction;
import com.yuxi.jxs.sp.SharedPreferencesFactory;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class MyStringCallBack extends StringCallback {
    private static final Random RANDOM = new Random();
    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyz";


    @Override
    public void onStart(Request<String, ? extends Request> request) {
        super.onStart(request);
        sign(request.getParams());
    }

    @Override
    public void onSuccess(Response<String> response) {

    }

    /**
     * 针对URL进行签名，关于这几个参数的作用，详细请看
     * http://www.cnblogs.com/bestzrz/archive/2011/09/03/2164620.html
     */
    private void sign(HttpParams params) {
        String loginInfoString = SharedPreferencesFactory.get(MyApplaction.getInstance(), "loginInfo", "");
        Gson gson = new Gson();
        LoginBean loginResponse = gson.fromJson(loginInfoString, LoginBean.class);
        params.put("nonce", getRndStr(6 + RANDOM.nextInt(8)));
        params.put("secret", "api_yuxi");
        params.put("time", getTime());
        if (!TextUtils.isEmpty(loginInfoString)) {
            params.put("company_hash", loginResponse.getCompany_hash());
            params.put("utoken", loginResponse.getUtoken());
        }
        StringBuilder sb = new StringBuilder();
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : params.urlParamsMap.entrySet()) {
            map.put(entry.getKey(), entry.getValue().get(0));
        }
        for (Map.Entry<String, String> entry : getSortedMapByKey(map).entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.delete(sb.length() - 1, sb.length());
        String sign = MD5Utils.md5(sb.toString());
        params.put("sign", sign);
    }

    private String getTime() {
        long curTimestamp = System.currentTimeMillis();
        return String.valueOf(curTimestamp);
    }

    /**
     * 获取随机数
     */
    private String getRndStr(int length) {
        StringBuilder sb = new StringBuilder();
        char ch;
        for (int i = 0; i < length; i++) {
            ch = CHARS.charAt(RANDOM.nextInt(CHARS.length()));
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * 按照key的自然顺序进行排序，并返回
     */
    private Map<String, String> getSortedMapByKey(Map<String, String> map) {
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        };
        Map<String, String> treeMap = new TreeMap<>(comparator);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            treeMap.put(entry.getKey(), entry.getValue());
        }
        return treeMap;
    }

    @Override
    public String convertResponse(okhttp3.Response response) throws Throwable {
        StringConvert convert = new StringConvert();
        String s = convert.convertResponse(response);
        BaseBean baseBean = JsonUtil.commonResponse(s);
        if (baseBean.getCode().equals("1003")) {
            //token过期
            SharedPreferencesFactory.set(MyApplaction.getInstance(), "isLogin", false);
            Intent intent = new Intent(MyApplaction.getInstance(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplaction.getInstance().startActivity(intent);
            return s;
        } else if (baseBean.getCode().equals("500")) {
            throw new IllegalStateException("服务器内部错误:500");
        }else if (baseBean.getCode().equals("404")){
            throw new IllegalStateException("请求路径出错:404");
        }else {
            return s;
        }
    }
}
