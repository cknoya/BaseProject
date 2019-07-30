package com.yuxi.jxs.update;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.vector.update_app.utils.ColorUtil;
import com.yuxi.jxs.MyApplaction;
import com.yuxi.jxs.R;
import com.yuxi.jxs.global.Global;
import com.yuxi.jxs.http.OkGoUpdateHttpUtil;
import com.yuxi.jxs.sp.SharedPreferencesFactory;
import com.yuxi.jxs.util.MD5Utils;
import com.yuxi.jxs.util.SystemUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.internal.Utils;

public class UpdateUtil {
    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final Random RANDOM = new Random();

    public static void doUpdate(Activity activity, String url) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("apptype", "android");
        params.put("uhash", Global.getUser().getUhash());
        params.put("system", "jxs");
        params.put("nonce", getRndStr(6 + RANDOM.nextInt(8)));
        params.put("secret", "api_yuxi");
        params.put("time", getTime());
        params.put("utoken", Global.getUser().getUtoken());
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.delete(sb.length() - 1, sb.length());
        String sign = MD5Utils.md5(sb.toString());
        params.put("sign", sign);
        new UpdateAppManager
                .Builder()
                //当前Activity
                .setActivity(activity)
                //更新地址
                .setUpdateUrl(url)
                //实现httpManager接口的对象
                .setHttpManager(new OkGoUpdateHttpUtil())
                //以下设置，都是可选
                //设置请求方式，默认get
                .setPost(true)
                //添加自定义参数，默认version=order_cancle.0.0（app的versionName）；apkKey=唯一表示（在AndroidManifest.xml配置）
                .setParams(params)
                //设置点击升级后，消失对话框，默认点击升级后，对话框显示下载进度
                .hideDialogOnDownloading()
                //设置头部，不设置显示默认的图片，设置图片后自动识别主色调，然后为按钮，进度条设置颜色
                .setTopPic(R.mipmap.top_8)
                //为按钮，进度条设置颜色，默认从顶部图片自动识别。
                .setThemeColor(activity.getResources().getColor(R.color.color_ff9933))
                //设置apk下砸路径，默认是在下载到sd卡下/Download/order_cancle.0.0/test.apk
//                .setTargetPath(path)
                //不显示通知栏进度条
//                .dismissNotificationProgress()
                .build()
                //检测是否有新版本
                .checkNewApp(new UpdateCallback() {
                    /**
                     * 解析json,自定义协议
                     *
                     * @param json 服务器返回的json
                     * @return UpdateAppBean
                     */
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        String update;
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            String data = jsonObject.optString("data");
                            JSONObject jsonObjectdata = new JSONObject(data);
                            String version_data = jsonObjectdata.optString("version_data");
                            JSONObject jsonObjectversion = new JSONObject(version_data);
                            int force = jsonObjectversion.optInt("force");
                            if (force == 1 && Global.getSingleVersionCode().equals(jsonObjectversion.optString("versionCode"))) {
                                return null;
                            }
                            if (force == 1) {
                                SharedPreferencesFactory.set(MyApplaction.getInstance(), "MySingleVersion", jsonObjectversion.optString("versionCode"));
                            }
                            int code = Integer.parseInt(jsonObjectversion.optString("versionCode"));
                            int isupdate = jsonObjectdata.optInt("isupdate");
                            if (code > SystemUtil.getVersionCode(activity) && isupdate == 1){
                                update = "Yes";
                            }else {
                                update = "No";
                            }
                            updateAppBean
                                    //（必须）是否更新Yes,No
                                    .setUpdate(update)
                                    //（必须）新版本号，
                                    .setNewVersion(jsonObjectversion.optString("versionCode"))
                                    //（必须）下载地址
                                    .setApkFileUrl(jsonObjectversion.optString("updateUrl"))
                                    //（必须）更新内容
                                    .setUpdateLog(jsonObjectversion.optString("updateInfo"))
                                    //大小，不设置不显示大小，可以不设置
//                                    .setTargetSize(jsonObject.optString("target_size"))
                                    //是否强制更新，可以不设置
                                    .setConstraint(jsonObjectversion.optInt("force") == 2)
                                    .setUpdateDefDialogTitle("是否升级到v" + jsonObjectversion.optString("versionName") + "版本");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return updateAppBean;
                    }

                    @Override
                    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        super.hasNewApp(updateApp, updateAppManager);
                    }

                    /**
                     * 网络请求之前
                     */
                    @Override
                    public void onBefore() {
//                        CProgressDialogUtils.showProgressDialog(JavaActivity.this);
                    }

                    /**
                     * 网路请求之后
                     */
                    @Override
                    public void onAfter() {
//                        CProgressDialogUtils.cancelProgressDialog(JavaActivity.this);
                    }

                    @Override
                    protected void noNewApp(String error) {
                        super.noNewApp(error);
                    }
                });

    }

    private static String getTime() {
        long curTimestamp = System.currentTimeMillis();
        return String.valueOf(curTimestamp);
    }

    /**
     * 获取随机数
     */
    private static String getRndStr(int length) {
        StringBuilder sb = new StringBuilder();
        char ch;
        for (int i = 0; i < length; i++) {
            ch = CHARS.charAt(RANDOM.nextInt(CHARS.length()));
            sb.append(ch);
        }
        return sb.toString();
    }
}
