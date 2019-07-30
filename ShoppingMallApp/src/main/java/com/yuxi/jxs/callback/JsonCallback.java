/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version second.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yuxi.jxs.callback;

import com.yuxi.jxs.login.LoginBean;
import com.yuxi.jxs.util.MD5Utils;
import com.yuxi.jxs.MyApplaction;
import com.yuxi.jxs.sp.SharedPreferencesFactory;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.base.Request;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：first.0
 * 创建日期：2016/first/14
 * 描    述：默认将返回的数据解析成需要的Bean,可以是 BaseBean，String，List，Map
 * 修订历史：
 * ================================================
 */
public abstract class JsonCallback<T> extends AbsCallback<T> {

    private Type type;
    private Class<T> clazz;
    private static final Random RANDOM = new Random();
    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyz";
    public JsonCallback() {
    }

    public JsonCallback(Type type) {
        this.type = type;
    }

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        // 主要用于在所有请求之前添加公共的请求头或请求参数
        // 例如登录授权的 token
        // 使用的设备信息
        // 可以随意添加,也可以什么都不传
        // 还可以在这里对所有的参数进行加密，均在这里实现
        sign(request.getParams());
    }

    /**
     * 针对URL进行签名，关于这几个参数的作用，详细请看
     * http://www.cnblogs.com/bestzrz/archive/2011/09/03/2164620.html
     */
    private void sign(HttpParams params) {
        String loginInfoString = SharedPreferencesFactory.get(MyApplaction.getInstance(),"loginInfo","");
        Gson gson = new Gson();
        LoginBean loginResponse = gson.fromJson(loginInfoString,LoginBean.class);
//        String uhash = loginResponse==null?"":loginResponse.getUhash();
//        int companyId = loginResponse==null?0:loginResponse.getCompany_id();
        if (loginResponse!=null){
            params.put("token",loginResponse.getUhash());
//            params.put("usercompany",loginResponse.getCompany_id());
        }
        params.put("nonce", getRndStr(6 + RANDOM.nextInt(8)));
        params.put("time", getTime());

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
    /** 获取随机数 */
    private String getRndStr(int length) {
        StringBuilder sb = new StringBuilder();
        char ch;
        for (int i = 0; i < length; i++) {
            ch = CHARS.charAt(RANDOM.nextInt(CHARS.length()));
            sb.append(ch);
        }
        return sb.toString();
    }

    /** 按照key的自然顺序进行排序，并返回 */
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
    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象,生产onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertResponse(Response response) throws Throwable {

        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用
        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用
        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用

        //详细自定义的原理和文档，看这里： https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback
//
//        if (type == null) {
//            if (clazz == null) {
//                Type genType = getClass().getGenericSuperclass();
//                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
//            } else {
//                JsonConvert<T> convert = new JsonConvert<>(clazz);
//                return convert.convertResponse(response);
//            }
//        }
//
//        JsonConvert<T> convert = new JsonConvert<>(type);
//        return convert.convertResponse(response);
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
        Type type = params[0];
        if (!(type instanceof ParameterizedType)) throw new IllegalStateException("没有填写泛型");
        Type rawType = ((ParameterizedType)type).getRawType();                     // 泛型的实际类型
        Type typeArgument = ((ParameterizedType)type).getActualTypeArguments()[0]; // 泛型的参数
        ResponseBody responseBody = response.body();
        if (responseBody == null) return null;
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(responseBody.charStream());
        if (rawType != LzyResponse.class) {
            // 泛型格式如下： new JsonCallback<外层BaseBean<内层JavaBean>>(this)
            T t = gson.fromJson(jsonReader, type);
            response.close();
            return t;
        } else {
            if (typeArgument == Void.class) {
                // 泛型格式如下： new JsonCallback<LzyResponse<Void>>(this)
                SimpleResponse simpleResponse = gson.fromJson(jsonReader, SimpleResponse.class);
                response.close();
                //noinspection unchecked
                return (T) simpleResponse.toLzyResponse();
            }else {
                // 泛型格式如下： new JsonCallback<LzyResponse<内层JavaBean>>(this)
                LzyResponse lzyResponse = gson.fromJson(jsonReader, type);
                response.close();
                String code = lzyResponse.code;
                //这里的0是以下意思
                //一般来说服务器会和客户端约定一个数表示成功，其余的表示失败，这里根据实际情况修改
                if (code.equals("1")) {
                    //noinspection unchecked
                    return (T) lzyResponse;
                } else if (code.equals("108")) {
                    throw new IllegalStateException("token过期");
                } else {
//                    //直接将服务端的错误信息抛出，onError中可以获取
                    throw new IllegalStateException(lzyResponse.msg);
                }
            }
        }
    }
}
