package com.yuxi.jxs.util;

import com.yuxi.jxs.base.BaseBean;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {
    public static BaseBean commonResponse(String json) {
        if (json == null || "".equals(json)) {
            return null;
        }
        BaseBean baseBean = new BaseBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String code = jsonObject.optString("code");
            String msg = jsonObject.optString("msg");
            String data = jsonObject.optString("data");
            baseBean.setCode(code);
            baseBean.setMsg(msg);
            if (data != null) {
                baseBean.setData(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return baseBean;
    }
}
