package com.aigame.baselib.io.storage;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by songguobin on 2017/8/20.
 *
 * 存储路径管理
 *
 */

public class CommonPathManager {


    /**
     * 获取data存储files目录
     *
     * @param mContext
     * @param subFolder
     * @return
     */
    public static String getFilesDir(Context mContext, String subFolder) {
        File dataFile = null;
        if(!TextUtils.isEmpty(subFolder)) {
            dataFile = StorageCheckor.getInternalDataFilesDir(mContext,  subFolder + "/");
        } else {
            dataFile = StorageCheckor.getInternalDataFilesDir(mContext, "/");
        }
        return dataFile.getAbsolutePath() + File.separator;
    }

    /**
     * 获取data存储cache目录
     *
     * @param mContext
     * @param subFolder
     * @return
     */
    public static String getCacheDir(Context mContext, String subFolder) {

        File cacheFile = null;
        if(!TextUtils.isEmpty(subFolder)) {
            cacheFile = StorageCheckor.getInternalDataCacheDir(mContext,  subFolder+ "/");

        } else {
            cacheFile =   StorageCheckor.getInternalDataCacheDir(mContext,  "/");

        }


        return cacheFile.getAbsolutePath()+ File.separator;

    }

    /**
     * 获取内置存储区files目录
     *
     * @param context
     * @param subFolder
     * @return
     */
    public static String getExternalFilesDir(Context context, String subFolder) {

        File externalFile = null;

        if(!TextUtils.isEmpty(subFolder)){
            externalFile = StorageCheckor.getInternalStorageFilesDir(context,  subFolder + "/");
        } else {
            externalFile = StorageCheckor.getInternalStorageFilesDir(context, "/");
        }

        if (externalFile != null) {
            return externalFile.getAbsolutePath()+ File.separator;
        } else {
            return getFilesDir(context, subFolder);
        }

    }

    /**
     * 获取内置存储区cache目录
     *
     * @param context
     * @param subFolder
     * @return
     */
    public static String getExternalCacheDir(Context context, String subFolder) {

        File externalCache = null;

        if(!TextUtils.isEmpty(subFolder)){
            externalCache = StorageCheckor.getInternalDataCacheDir(context,  subFolder+ "/");
        } else {
            externalCache = StorageCheckor.getInternalDataCacheDir(context, "/");
        }

        if (externalCache != null) {
            return externalCache.getAbsolutePath()+ File.separator;
        } else {
            return getCacheDir(context, subFolder);
        }
    }

    /**
     * 用户选择的存储卡
     *
     * @param context
     * @param subFolder
     * @return
     */
    public static String getUserPrefsExternalFilesDir(Context context,String subFolder){

        File prefsFile = null;

        if(!TextUtils.isEmpty(subFolder)){
            prefsFile = StorageCheckor.getUserPreferFilesDir(context, subFolder + "/");
        } else {
            prefsFile = StorageCheckor.getUserPreferFilesDir(context, "/");
        }

        return prefsFile.getAbsolutePath() + File.separator;

    }

}
