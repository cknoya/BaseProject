package com.aigame.baselib.io.cache;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

import com.aigame.baselib.constance.CacheConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * <pre>
 *     author: songguobin
 *     desc  : 内存缓存和磁盘缓存相关工具类
 * </pre>
 */
public final class CacheDoubleUtils implements CacheConstants {

    private static final SimpleArrayMap<String, CacheDoubleUtils> CACHE_MAP = new SimpleArrayMap<>();
    private CacheMemoryUtils mCacheMemoryUtils;
    private CacheDiskUtils mCacheDiskUtils;

    /**
     * 获取CacheDoubleUtils的单例 {@link CacheDoubleUtils}.
     *
     * @return CacheDoubleUtils的单例{@link CacheDoubleUtils}
     */
    public static CacheDoubleUtils getInstance() {
        return getInstance(CacheMemoryUtils.getInstance(), CacheDiskUtils.getInstance());
    }

    /**
     * 获取CacheDoubleUtils的单例 {@link CacheDoubleUtils}.
     *
     * @param cacheMemoryUtils CacheMemoryUtils实例 {@link CacheMemoryUtils}.
     * @param cacheDiskUtils   CacheDiskUtils实例 {@link CacheDiskUtils}.
     * @return CacheDoubleUtils的单例 {@link CacheDoubleUtils}
     */
    public static CacheDoubleUtils getInstance(@NonNull final CacheMemoryUtils cacheMemoryUtils,
                                               @NonNull final CacheDiskUtils cacheDiskUtils
    ) {
        final String cacheKey = cacheDiskUtils.toString() + "_" + cacheMemoryUtils.toString();
        CacheDoubleUtils cache = CACHE_MAP.get(cacheKey);
        if (cache == null) {
            cache = new CacheDoubleUtils(cacheMemoryUtils, cacheDiskUtils);
            CACHE_MAP.put(cacheKey, cache);
        }
        return cache;
    }

    private CacheDoubleUtils(CacheMemoryUtils cacheMemoryUtils, CacheDiskUtils cacheUtils) {
        mCacheMemoryUtils = cacheMemoryUtils;
        mCacheDiskUtils = cacheUtils;
    }

    /**
     * byte类型存入缓存.
     *
     * @param key   缓存key.
     * @param value 缓存value.
     */
    public void put(@NonNull final String key, final byte[] value) {
        put(key, value, -1);
    }

    /**
     * byte类型存入缓存.
     *
     * @param key      缓存key.
     * @param value    缓存value.
     * @param saveTime 缓存保存时间，单位秒.
     */
    public void put(@NonNull final String key, byte[] value, final int saveTime) {
        mCacheMemoryUtils.put(key, value, saveTime);
        mCacheDiskUtils.put(key, value, saveTime);
    }

    /**
     * 获取byte类型缓存.
     *
     * @param key 缓存key.
     * @return 缓存不存在返回null, 否则返回缓存数据
     */
    public byte[] getBytes(@NonNull final String key) {
        return getBytes(key, null);
    }

    /**
     * 获取byte类型缓存.
     *
     * @param key          缓存key.
     * @param defaultValue 缓存不存在时，返回该默认值.
     * @return 缓存不存在返回默认值, 否则返回缓存数据
     */
    public byte[] getBytes(@NonNull final String key, final byte[] defaultValue) {
        byte[] obj = mCacheMemoryUtils.get(key);
        if (obj != null) {
            return obj;
        }
        return mCacheDiskUtils.getBytes(key, defaultValue);
    }

    /**
     * String类型存入缓存.
     *
     * @param key   缓存key.
     * @param value 缓存值.
     */
    public void put(@NonNull final String key, final String value) {
        put(key, value, -1);
    }

    /**
     * String类型存入缓存.
     *
     * @param key      缓存key.
     * @param value    缓存值.
     * @param saveTime 缓存保存时间，单位秒
     */
    public void put(@NonNull final String key, final String value, final int saveTime) {
        mCacheMemoryUtils.put(key, value, saveTime);
        mCacheDiskUtils.put(key, value, saveTime);
    }

    /**
     * 获取String类型缓存.
     *
     * @param key 缓存key.
     * @return 缓存不存在返回null, 否则返回缓存数据
     */
    public String getString(@NonNull final String key) {
        return getString(key, null);
    }

    /**
     * 获取String类型缓存.
     *
     * @param key          缓存key.
     * @param defaultValue 缓存不存在时，返回该默认值
     * @return 缓存不存在返回默认值, 否则返回缓存数据
     */
    public String getString(@NonNull final String key, final String defaultValue) {
        String obj = mCacheMemoryUtils.get(key);
        if (obj != null) {
            return obj;
        }
        return mCacheDiskUtils.getString(key, defaultValue);
    }

    /**
     * JsonObject类型存入缓存.
     *
     * @param key   缓存key.
     * @param value 缓存值.
     */
    public void put(@NonNull final String key, final JSONObject value) {
        put(key, value, -1);
    }

    /**
     * JsonObject类型存入缓存.
     *
     * @param key      缓存key.
     * @param value    缓存值.
     * @param saveTime 缓存保存时间，单位秒
     */
    public void put(@NonNull final String key,
                    final JSONObject value,
                    final int saveTime) {
        mCacheMemoryUtils.put(key, value, saveTime);
        mCacheDiskUtils.put(key, value, saveTime);
    }

    /**
     * 获取JsonObject类型缓存.
     *
     * @param key 缓存key.
     * @return 缓存不存在返回null, 否则返回缓存数据
     */
    public JSONObject getJSONObject(@NonNull final String key) {
        return getJSONObject(key, null);
    }

    /**
     * 获取JsonObject类型缓存.
     *
     * @param key          缓存key.
     * @param defaultValue 缓存不存在时，返回该默认值
     * @return 缓存不存在返回默认值, 否则返回缓存数据
     */
    public JSONObject getJSONObject(@NonNull final String key, final JSONObject defaultValue) {
        JSONObject obj = mCacheMemoryUtils.get(key);
        if (obj != null) {
            return obj;
        }
        return mCacheDiskUtils.getJSONObject(key, defaultValue);
    }

    /**
     * JsonArray类型存入缓存.
     *
     * @param key   缓存key.
     * @param value 缓存值.
     */
    public void put(@NonNull final String key, final JSONArray value) {
        put(key, value, -1);
    }

    /**
     * JsonArray类型存入缓存.
     *
     * @param key      缓存key.
     * @param value    缓存值.
     * @param saveTime 缓存保存时间，单位秒
     */
    public void put(@NonNull final String key, final JSONArray value, final int saveTime) {
        mCacheMemoryUtils.put(key, value, saveTime);
        mCacheDiskUtils.put(key, value, saveTime);
    }

    /**
     * 获取JsonArray类型缓存.
     *
     * @param key 缓存key.
     * @return 缓存不存在返回null, 否则返回缓存数据
     */
    public JSONArray getJSONArray(@NonNull final String key) {
        return getJSONArray(key, null);
    }

    /**
     * 获取JsonArray类型缓存.
     *
     * @param key          缓存key.
     * @param defaultValue 缓存不存在时，返回该默认值
     * @return 缓存不存在返回默认值, 否则返回缓存数据
     */
    public JSONArray getJSONArray(@NonNull final String key, final JSONArray defaultValue) {
        JSONArray obj = mCacheMemoryUtils.get(key);
        if (obj != null) {
            return obj;
        }
        return mCacheDiskUtils.getJSONArray(key, defaultValue);
    }

    /**
     * Bitmap类型存入缓存.
     *
     * @param key   缓存key.
     * @param value 缓存值.
     */
    public void put(@NonNull final String key, final Bitmap value) {
        put(key, value, -1);
    }

    /**
     * Bitmap类型存入缓存.
     *
     * @param key      缓存key.
     * @param value    缓存值.
     * @param saveTime 缓存保存时间，单位秒
     */
    public void put(@NonNull final String key, final Bitmap value, final int saveTime) {
        mCacheMemoryUtils.put(key, value, saveTime);
        mCacheDiskUtils.put(key, value, saveTime);
    }

    /**
     * 获取Bitmap类型缓存.
     *
     * @param key 缓存key.
     * @return 缓存不存在返回null, 否则返回缓存数据
     */
    public Bitmap getBitmap(@NonNull final String key) {
        return getBitmap(key, null);
    }

    /**
     * 获取Bitmap类型缓存.
     *
     * @param key          缓存key.
     * @param defaultValue 缓存不存在时，返回该默认值
     * @return 缓存不存在返回默认值, 否则返回缓存数据
     */
    public Bitmap getBitmap(@NonNull final String key, final Bitmap defaultValue) {
        Bitmap obj = mCacheMemoryUtils.get(key);
        if (obj != null) {
            return obj;
        }
        return mCacheDiskUtils.getBitmap(key, defaultValue);
    }

    /**
     * Drawable类型存入缓存.
     *
     * @param key   缓存key.
     * @param value 缓存值.
     */
    public void put(@NonNull final String key, final Drawable value) {
        put(key, value, -1);
    }

    /**
     * Drawable类型存入缓存.
     *
     * @param key      缓存key.
     * @param value    缓存值.
     * @param saveTime 缓存保存时间，单位秒
     */
    public void put(@NonNull final String key, final Drawable value, final int saveTime) {
        mCacheMemoryUtils.put(key, value, saveTime);
        mCacheDiskUtils.put(key, value, saveTime);
    }

    /**
     * 获取Drawable类型缓存.
     *
     * @param key 缓存key.
     * @return 缓存不存在null, 否则返回缓存数据
     */
    public Drawable getDrawable(@NonNull final String key) {
        return getDrawable(key, null);
    }

    /**
     * 获取Drawable类型缓存.
     *
     * @param key          缓存key.
     * @param defaultValue 缓存不存在时，返回该默认值
     * @return 缓存不存在返回默认值, 否则返回缓存数据
     */
    public Drawable getDrawable(@NonNull final String key, final Drawable defaultValue) {
        Drawable obj = mCacheMemoryUtils.get(key);
        if (obj != null) {
            return obj;
        }
        return mCacheDiskUtils.getDrawable(key, defaultValue);
    }

    /**
     * Parcelable类型存入缓存.
     *
     * @param key   缓存key.
     * @param value 缓存值.
     */
    public void put(@NonNull final String key, final Parcelable value) {
        put(key, value, -1);
    }

    /**
     * Parcelable类型存入缓存.
     *
     * @param key      缓存key.
     * @param value    缓存值.
     * @param saveTime 缓存保存时间，单位秒
     */
    public void put(@NonNull final String key, final Parcelable value, final int saveTime) {
        mCacheMemoryUtils.put(key, value, saveTime);
        mCacheDiskUtils.put(key, value, saveTime);
    }

    /**
     * 获取Parcelable类型缓存.
     *
     * @param key     缓存key.
     * @param creator creator.
     * @param <T>     缓存值对象类型.
     * @return 缓存不存在返回null, 否则返回缓存数据
     */
    public <T> T getParcelable(@NonNull final String key,
                               @NonNull final Parcelable.Creator<T> creator) {
        return getParcelable(key, creator, null);
    }

    /**
     * 获取Parcelable类型缓存.
     *
     * @param key          缓存key.
     * @param creator      creator.
     * @param defaultValue 缓存不存在时，返回该默认值
     * @param <T>          缓存对象类型.
     * @return 缓存不存在返回默认值, 否则返回缓存数据.
     */
    public <T> T getParcelable(@NonNull final String key,
                               @NonNull final Parcelable.Creator<T> creator,
                               final T defaultValue) {
        T value = mCacheMemoryUtils.get(key);
        if (value != null) {
            return value;
        }
        return mCacheDiskUtils.getParcelable(key, creator, defaultValue);
    }

    /**
     * Serializable类型存入缓存.
     *
     * @param key   缓存key.
     * @param value 缓存值.
     */
    public void put(@NonNull final String key, final Serializable value) {
        put(key, value, -1);
    }

    /**
     * Serializable类型存入缓存.
     *
     * @param key      缓存key.
     * @param value    缓存值.
     * @param saveTime 缓存保存时间，单位秒
     */
    public void put(@NonNull final String key, final Serializable value, final int saveTime) {
        mCacheMemoryUtils.put(key, value, saveTime);
        mCacheDiskUtils.put(key, value, saveTime);
    }

    /**
     * 获取Serializable类型缓存.
     *
     * @param key 缓存key.
     * @return 缓存不存在返回null, 否则返回缓存数据
     */
    public Object getSerializable(@NonNull final String key) {
        return getSerializable(key, null);
    }

    /**
     * 获取Serializable类型缓存.
     *
     * @param key          缓存key.
     * @param defaultValue 缓存不存在时，返回该默认值
     * @return 缓存不存在返回默认值, 否则返回缓存数据
     */
    public Object getSerializable(@NonNull final String key, final Object defaultValue) {
        Object obj = mCacheMemoryUtils.get(key);
        if (obj != null) {
            return obj;
        }
        return mCacheDiskUtils.getSerializable(key, defaultValue);
    }

    /**
     * 获取磁盘缓存大小.
     *
     * @return 磁盘缓存大小
     */
    public long getCacheDiskSize() {
        return mCacheDiskUtils.getCacheSize();
    }

    /**
     * 获取磁盘缓存数量.
     *
     * @return 磁盘缓存数量
     */
    public int getCacheDiskCount() {
        return mCacheDiskUtils.getCacheCount();
    }

    /**
     * 获取内存存数量.
     *
     * @return 内存缓存数量
     */
    public int getCacheMemoryCount() {
        return mCacheMemoryUtils.getCacheCount();
    }

    /**
     * 删除缓存.
     *
     * @param key 缓存key.
     */
    public void remove(@NonNull String key) {
        mCacheMemoryUtils.remove(key);
        mCacheDiskUtils.remove(key);
    }

    /**
     * 清空所有的缓存.
     */
    public void clear() {
        mCacheMemoryUtils.clear();
        mCacheDiskUtils.clear();
    }
}
