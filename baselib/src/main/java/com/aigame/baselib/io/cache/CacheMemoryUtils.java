package com.aigame.baselib.io.cache;

import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.support.v4.util.SimpleArrayMap;

import com.aigame.baselib.constance.CacheConstants;


/**
 * <pre>
 *     author: songguobin
 *     desc  : 内存缓存相关工具类
 * </pre>
 */
public final class CacheMemoryUtils implements CacheConstants {

    private static final int DEFAULT_MAX_COUNT = 256;

    private static final SimpleArrayMap<String, CacheMemoryUtils> CACHE_MAP = new SimpleArrayMap<>();

    private final String mCacheKey;
    private final LruCache<String, CacheValue> mMemoryCache;

    /**
     * 获取CacheMemoryUtils单例 {@link CacheMemoryUtils}.
     *
     * @return CacheMemoryUtils单例 {@link CacheMemoryUtils}
     */
    public static CacheMemoryUtils getInstance() {
        return getInstance(DEFAULT_MAX_COUNT);
    }

    /**
     * 获取CacheMemoryUtils单例 {@link CacheMemoryUtils}.
     *
     * @param maxCount 缓存最大数量.
     * @return 获取CacheMemoryUtils单例 {@link CacheMemoryUtils}
     */
    public static CacheMemoryUtils getInstance(final int maxCount) {
        final String cacheKey = String.valueOf(maxCount);
        CacheMemoryUtils cache = CACHE_MAP.get(cacheKey);
        if (cache == null) {
            cache = new CacheMemoryUtils(cacheKey, new LruCache<String, CacheValue>(maxCount));
            CACHE_MAP.put(cacheKey, cache);
        }
        return cache;
    }

    private CacheMemoryUtils(String cacheKey, LruCache<String, CacheValue> memoryCache) {
        mCacheKey = cacheKey;
        mMemoryCache = memoryCache;
    }

    @Override
    public String toString() {
        return mCacheKey + "@" + Integer.toHexString(hashCode());
    }

    /**
     * 存入缓存.
     *
     * @param key   缓存key.
     * @param value 缓存值.
     */
    public void put(@NonNull final String key, final Object value) {
        put(key, value, -1);
    }

    /**
     * 存入缓存.
     *
     * @param key      缓存key.
     * @param value    缓存值.
     * @param saveTime 缓存保存时间，单位秒
     */
    public void put(@NonNull final String key, final Object value, int saveTime) {
        if (value == null) {
            return;
        }
        long dueTime = saveTime < 0 ? -1 : System.currentTimeMillis() + saveTime * 1000;
        mMemoryCache.put(key, new CacheValue(dueTime, value));
    }

    /**
     * 获取缓存.
     *
     * @param key 缓存key.
     * @return 缓存不存在返回null, 否则返回缓存数据
     */
    public <T> T get(@NonNull final String key) {
        return get(key, null);
    }

    /**
     * 获取缓存.
     *
     * @param key          缓存key.
     * @param defaultValue 缓存不存在时，返回该默认值.
     * @return 缓存不存在返回默认值, 否则返回缓存数据
     */
    public <T> T get(@NonNull final String key, final T defaultValue) {
        CacheValue val = mMemoryCache.get(key);
        if (val == null) {
            return defaultValue;
        }
        if (val.dueTime == -1 || val.dueTime >= System.currentTimeMillis()) {
            return (T) val.value;
        }
        mMemoryCache.remove(key);
        return defaultValue;
    }

    /**
     * 获取缓存数量.
     *
     * @return 缓存数量
     */
    public int getCacheCount() {
        return mMemoryCache.size();
    }

    /**
     * 删除缓存.
     *
     * @param key 缓存key.
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public Object remove(@NonNull final String key) {
        CacheValue remove = mMemoryCache.remove(key);
        if (remove == null) {
            return null;
        }
        return remove.value;
    }

    /**
     * 清空所有缓存.
     */
    public void clear() {
        mMemoryCache.evictAll();
    }

    private static final class CacheValue {
        long dueTime;
        Object value;

        private CacheValue(long dueTime, Object value) {
            this.dueTime = dueTime;
            this.value = value;
        }
    }
}