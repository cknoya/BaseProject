package com.aigame.baselib.io.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

import com.aigame.baselib.constance.CacheConstants;
import com.aigame.baselib.utils.crash.ExceptionUtils;
import com.aigame.baselib.utils.app.LifeCycleUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <pre>
 *     author: songguobin
 *     desc  : 磁盘缓存相关工具类
 * </pre>
 */
public final class CacheDiskUtils implements CacheConstants {

    private static final long DEFAULT_MAX_SIZE = Long.MAX_VALUE;
    private static final int DEFAULT_MAX_COUNT = Integer.MAX_VALUE;

    private static final SimpleArrayMap<String, CacheDiskUtils> CACHE_MAP = new SimpleArrayMap<>();
    private final String mCacheKey;
    private final DiskCacheManager mDiskCacheManager;

    /**
     * 获取CacheDiskUtils单例 {@link CacheDiskUtils}.
     * <p>缓存目录: /data/data/package/cache/cacheUtils</p>
     * <p>缓存大小: 不限</p>
     * <p>缓存数量: 不限</p>
     *
     * @return CacheDiskUtils单例 {@link CacheDiskUtils}
     */
    public static CacheDiskUtils getInstance() {
        return getInstance("", DEFAULT_MAX_SIZE, DEFAULT_MAX_COUNT);
    }

    /**
     * 获取CacheDiskUtils单例 {@link CacheDiskUtils}.
     * <p>缓存目录: /data/data/package/cache/cacheUtils</p>
     * <p>缓存大小: 不限</p>
     * <p>缓存数量: 不限</p>
     *
     * @param cacheName 缓存文件名称.
     * @return CacheDiskUtils单例 {@link CacheDiskUtils}
     */
    public static CacheDiskUtils getInstance(final String cacheName) {
        return getInstance(cacheName, DEFAULT_MAX_SIZE, DEFAULT_MAX_COUNT);
    }

    /**
     * 获取CacheDiskUtils单例 {@link CacheDiskUtils}.
     * <p>缓存目录: /data/data/package/cache/cacheUtils</p>
     *
     * @param maxSize  缓存最大大小, 单位：bytes.
     * @param maxCount 缓存最大数量.
     * @return CacheDiskUtils单例 {@link CacheDiskUtils}
     */
    public static CacheDiskUtils getInstance(final long maxSize, final int maxCount) {
        return getInstance("", maxSize, maxCount);
    }

    /**
     * 获取CacheDiskUtils单例 {@link CacheDiskUtils}.
     * <p>缓存目录: /data/data/package/cache/cacheUtils</p>
     *
     * @param cacheName 缓存文件名称
     * @param maxSize   缓存最大大小, 单位：bytes.
     * @param maxCount  缓存最大数量.
     * @return CacheDiskUtils单例 {@link CacheDiskUtils}
     */
    public static CacheDiskUtils getInstance(String cacheName, final long maxSize, final int maxCount) {
        if (isSpace(cacheName)) {
            cacheName = "cacheUtils";
        }
        File file = new File(LifeCycleUtils.getApp().getCacheDir(), cacheName);
        return getInstance(file, maxSize, maxCount);
    }

    /**
     * 获取CacheDiskUtils单例 {@link CacheDiskUtils}.
     * <p>缓存大小: 不限</p>
     * <p>缓存数量: 不限</p>
     *
     * @param cacheDir 缓存目录.
     * @return CacheDiskUtils单例  {@link CacheDiskUtils}
     */
    public static CacheDiskUtils getInstance(@NonNull final File cacheDir) {
        return getInstance(cacheDir, DEFAULT_MAX_SIZE, DEFAULT_MAX_COUNT);
    }

    /**
     * 获取CacheDiskUtils单例 {@link CacheDiskUtils}.
     *
     * @param cacheDir 缓存目录.
     * @param maxSize  缓存最大大小, 单位bytes.
     * @param maxCount 缓存最大数量.
     * @return CacheDiskUtils单例 {@link CacheDiskUtils}
     */
    public static CacheDiskUtils getInstance(@NonNull final File cacheDir,
                                             final long maxSize,
                                             final int maxCount) {
        final String cacheKey = cacheDir.getAbsoluteFile() + "_" + maxSize + "_" + maxCount;
        CacheDiskUtils cache = CACHE_MAP.get(cacheKey);
        if (cache == null) {
            if (!cacheDir.exists() && !cacheDir.mkdirs()) {
                throw new RuntimeException("can't make dirs in " + cacheDir.getAbsolutePath());
            }
            cache = new CacheDiskUtils(cacheKey, new DiskCacheManager(cacheDir, maxSize, maxCount));
            CACHE_MAP.put(cacheKey, cache);
        }
        return cache;
    }

    private CacheDiskUtils(final String cacheKey, final DiskCacheManager cacheManager) {
        mCacheKey = cacheKey;
        mDiskCacheManager = cacheManager;
    }

    @Override
    public String toString() {
        return mCacheKey + "@" + Integer.toHexString(hashCode());
    }


    /**
     * byte类型存入缓存.
     *
     * @param key   缓存key.
     * @param value 缓存值.
     */
    public void put(@NonNull final String key, final byte[] value) {
        put(key, value, -1);
    }

    /**
     * byte类型存入缓存.
     *
     * @param key      缓存key.
     * @param value    缓存值.
     * @param saveTime 缓存保存时间，单位秒
     */
    public void put(@NonNull final String key, byte[] value, final int saveTime) {
        if (value == null) {
            return;
        }
        if (saveTime >= 0) {
            value = DiskCacheHelper.newByteArrayWithTime(saveTime, value);
        }
        File file = mDiskCacheManager.getFileBeforePut(key);
        writeFileFromBytes(file, value);
        mDiskCacheManager.updateModify(file);
        mDiskCacheManager.put(file);
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
     * @param defaultValue 缓存不存在时，返回该默认值
     * @return 缓存不存在返回默认值, 否则返回缓存数据
     */
    public byte[] getBytes(@NonNull final String key, final byte[] defaultValue) {
        final File file = mDiskCacheManager.getFileIfExists(key);
        if (file == null) {
            return defaultValue;
        }
        byte[] data = readFile2Bytes(file);
        if (DiskCacheHelper.isDue(data)) {
            mDiskCacheManager.removeByKey(key);
            return defaultValue;
        }
        mDiskCacheManager.updateModify(file);
        return DiskCacheHelper.getDataWithoutDueTime(data);
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
        put(key, string2Bytes(value), saveTime);
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
        byte[] bytes = getBytes(key);
        if (bytes == null) {
            return defaultValue;
        }
        return bytes2String(bytes);
    }

    /**
     * JSONObject类型存入缓存.
     *
     * @param key   缓存key.
     * @param value 缓存值.
     */
    public void put(@NonNull final String key, final JSONObject value) {
        put(key, value, -1);
    }

    /**
     * JSONObject类型存入缓存.
     *
     * @param key      缓存key.
     * @param value    缓存值.
     * @param saveTime 缓存保存时间，单位秒
     */
    public void put(@NonNull final String key,
                    final JSONObject value,
                    final int saveTime) {
        put(key, jsonObject2Bytes(value), saveTime);
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
        byte[] bytes = getBytes(key);
        if (bytes == null) {
            return defaultValue;
        }
        return bytes2JSONObject(bytes);
    }

    /**
     * JSONArray类型存入缓存.
     *
     * @param key   缓存key.
     * @param value 缓存值.
     */
    public void put(@NonNull final String key, final JSONArray value) {
        put(key, value, -1);
    }

    /**
     * JSONArray类型存入缓存.
     *
     * @param key      缓存key.
     * @param value    缓存值.
     * @param saveTime 缓存保存时间，单位秒
     */
    public void put(@NonNull final String key, final JSONArray value, final int saveTime) {
        put(key, jsonArray2Bytes(value), saveTime);
    }

    /**
     * 获取JSONArray类型缓存.
     *
     * @param key 缓存key.
     * @return 缓存不存在返回null, 否则返回缓存数据
     */
    public JSONArray getJSONArray(@NonNull final String key) {
        return getJSONArray(key, null);
    }

    /**
     * 获取JSONArray类型缓存.
     *
     * @param key          缓存key.
     * @param defaultValue 缓存不存在时，返回该默认值
     * @return 缓存不存在返回默认值, 否则返回缓存数据
     */
    public JSONArray getJSONArray(@NonNull final String key, final JSONArray defaultValue) {
        byte[] bytes = getBytes(key);
        if (bytes == null) {
            return defaultValue;
        }
        return bytes2JSONArray(bytes);
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
        put(key, bitmap2Bytes(value), saveTime);
    }

    /**
     * 获取bitmap类型缓存.
     *
     * @param key 缓存key.
     * @return 缓存不存在返回null, 否则返回缓存数据
     */
    public Bitmap getBitmap(@NonNull final String key) {
        return getBitmap(key, null);
    }

    /**
     * 获取bitmap类型缓存.
     *
     * @param key          缓存key.
     * @param defaultValue 缓存不存在时，返回该默认值
     * @return 缓存不存在返回默认值, 否则返回缓存数据
     */
    public Bitmap getBitmap(@NonNull final String key, final Bitmap defaultValue) {
        byte[] bytes = getBytes(key);
        if (bytes == null) {
            return defaultValue;
        }
        return bytes2Bitmap(bytes);
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
        put(key, drawable2Bytes(value), saveTime);
    }

    /**
     * 获取Drawable类型缓存.
     *
     * @param key 缓存key.
     * @return 缓存不存在返回null, 否则返回缓存数据
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
        byte[] bytes = getBytes(key);
        if (bytes == null) {
            return defaultValue;
        }
        return bytes2Drawable(bytes);
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
        put(key, parcelable2Bytes(value), saveTime);
    }

    /**
     * 获取parcelable类型缓存.
     *
     * @param key     缓存Key.
     * @param creator creator.
     * @param <T>     缓存值对象类型.
     * @return 缓存不存在返回null, 否则返回缓存数据
     */
    public <T> T getParcelable(@NonNull final String key,
                               @NonNull final Parcelable.Creator<T> creator) {
        return getParcelable(key, creator, null);
    }

    /**
     * 获取parcelable类型缓存.
     *
     * @param key          缓存Key.
     * @param creator      creator.
     * @param defaultValue 缓存不存在时，返回该默认值
     * @param <T>          缓存值对象类型.
     * @return 缓存不存在返回null, 否则返回缓存数据
     */
    public <T> T getParcelable(@NonNull final String key,
                               @NonNull final Parcelable.Creator<T> creator,
                               final T defaultValue) {
        byte[] bytes = getBytes(key);
        if (bytes == null) {
            return defaultValue;
        }
        return bytes2Parcelable(bytes, creator);
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
        put(key, serializable2Bytes(value), saveTime);
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
        byte[] bytes = getBytes(key);
        if (bytes == null) {
            return defaultValue;
        }
        return bytes2Object(getBytes(key));
    }

    /**
     * 获取存大小，单位bytes.
     *
     * @return 缓存大小
     */
    public long getCacheSize() {
        return mDiskCacheManager.getCacheSize();
    }

    /**
     * 获取缓存数量.
     *
     * @return 缓存数量
     */
    public int getCacheCount() {
        return mDiskCacheManager.getCacheCount();
    }

    /**
     * 删除缓存.
     *
     * @param key 缓存key.
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public boolean remove(@NonNull final String key) {
        return mDiskCacheManager.removeByKey(key);
    }

    /**
     * 清空所有缓存.
     *
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public boolean clear() {
        return mDiskCacheManager.clear();
    }

    private static final class DiskCacheManager {
        private final AtomicLong cacheSize;
        private final AtomicInteger cacheCount;
        private final long sizeLimit;
        private final int countLimit;
        private final Map<File, Long> lastUsageDates
                = Collections.synchronizedMap(new HashMap<File, Long>());
        private final File cacheDir;
        private final Thread mThread;

        private DiskCacheManager(final File cacheDir, final long sizeLimit, final int countLimit) {
            this.cacheDir = cacheDir;
            this.sizeLimit = sizeLimit;
            this.countLimit = countLimit;
            cacheSize = new AtomicLong();
            cacheCount = new AtomicInteger();
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    int size = 0;
                    int count = 0;
                    final File[] cachedFiles = cacheDir.listFiles();
                    if (cachedFiles != null) {
                        for (File cachedFile : cachedFiles) {
                            size += cachedFile.length();
                            count += 1;
                            lastUsageDates.put(cachedFile, cachedFile.lastModified());
                        }
                        cacheSize.getAndAdd(size);
                        cacheCount.getAndAdd(count);
                    }
                }
            });
            mThread.start();
        }

        private long getCacheSize() {
            try {
                mThread.join();
            } catch (InterruptedException e) {
                ExceptionUtils.printStackTrace(e);
            }
            return cacheSize.get();
        }

        private int getCacheCount() {
            try {
                mThread.join();
            } catch (InterruptedException e) {
                ExceptionUtils.printStackTrace(e);
            }
            return cacheCount.get();
        }

        private File getFileBeforePut(final String key) {
            File file = new File(cacheDir, String.valueOf(key.hashCode()));
            if (file.exists()) {
                cacheCount.addAndGet(-1);
                cacheSize.addAndGet(-file.length());
            }
            return file;
        }

        private File getFileIfExists(final String key) {
            File file = new File(cacheDir, String.valueOf(key.hashCode()));
            if (!file.exists()) {
                return null;
            }
            return file;
        }

        private void put(final File file) {
            cacheCount.addAndGet(1);
            cacheSize.addAndGet(file.length());
            while (cacheCount.get() > countLimit || cacheSize.get() > sizeLimit) {
                cacheSize.addAndGet(-removeOldest());
                cacheCount.addAndGet(-1);
            }
        }

        private void updateModify(final File file) {
            Long millis = System.currentTimeMillis();
            file.setLastModified(millis);
            lastUsageDates.put(file, millis);
        }

        private boolean removeByKey(final String key) {
            File file = getFileIfExists(key);
            if (file == null) {
                return true;
            }
            if (!file.delete()) {
                return false;
            }
            cacheSize.addAndGet(-file.length());
            cacheCount.addAndGet(-1);
            lastUsageDates.remove(file);
            return true;
        }

        private boolean clear() {
            File[] files = cacheDir.listFiles();
            if (files == null || files.length <= 0) {
                return true;
            }
            boolean flag = true;
            for (File file : files) {
                if (!file.delete()) {
                    flag = false;
                    continue;
                }
                cacheSize.addAndGet(-file.length());
                cacheCount.addAndGet(-1);
                lastUsageDates.remove(file);
            }
            if (flag) {
                lastUsageDates.clear();
                cacheSize.set(0);
                cacheCount.set(0);
            }
            return flag;
        }

        /**
         * 删除最不经常使用的文件.
         *
         * @return 被删除文件的大小, 单位bytes
         */
        private long removeOldest() {
            if (lastUsageDates.isEmpty()) {
                return 0;
            }
            Long oldestUsage = Long.MAX_VALUE;
            File oldestFile = null;
            Set<Map.Entry<File, Long>> entries = lastUsageDates.entrySet();
            synchronized (lastUsageDates) {
                for (Map.Entry<File, Long> entry : entries) {
                    Long lastValueUsage = entry.getValue();
                    if (lastValueUsage < oldestUsage) {
                        oldestUsage = lastValueUsage;
                        oldestFile = entry.getKey();
                    }
                }
            }
            if (oldestFile == null) {
                return 0;
            }
            long fileSize = oldestFile.length();
            if (oldestFile.delete()) {
                lastUsageDates.remove(oldestFile);
                return fileSize;
            }
            return 0;
        }
    }

    private static final class DiskCacheHelper {

        static final int TIME_INFO_LEN = 14;

        private static byte[] newByteArrayWithTime(final int second, final byte[] data) {
            byte[] time = createDueTime(second).getBytes();
            byte[] content = new byte[time.length + data.length];
            System.arraycopy(time, 0, content, 0, time.length);
            System.arraycopy(data, 0, content, time.length, data.length);
            return content;
        }

        /**
         * 获取当前在seconds之后的时间的字符串.
         *
         * @param seconds 秒.
         * @return 当前在seconds之后的时间的字符串
         */
        private static String createDueTime(final int seconds) {
            return String.format(
                    Locale.getDefault(), "_$%010d$_",
                    System.currentTimeMillis() / 1000 + seconds
            );
        }

        private static boolean isDue(final byte[] data) {
            long millis = getDueTime(data);
            return millis != -1 && System.currentTimeMillis() > millis;
        }

        private static long getDueTime(final byte[] data) {
            if (hasTimeInfo(data)) {
                String millis = new String(copyOfRange(data, 2, 12));
                try {
                    return Long.parseLong(millis) * 1000;
                } catch (NumberFormatException e) {
                    return -1;
                }
            }
            return -1;
        }

        private static byte[] getDataWithoutDueTime(final byte[] data) {
            if (hasTimeInfo(data)) {
                return copyOfRange(data, TIME_INFO_LEN, data.length);
            }
            return data;
        }

        private static byte[] copyOfRange(final byte[] original, final int from, final int to) {
            int newLength = to - from;
            if (newLength < 0) {
                throw new IllegalArgumentException(from + " > " + to);
            }
            byte[] copy = new byte[newLength];
            System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
            return copy;
        }

        private static boolean hasTimeInfo(final byte[] data) {
            return data != null
                    && data.length >= TIME_INFO_LEN
                    && data[0] == '_'
                    && data[1] == '$'
                    && data[12] == '$'
                    && data[13] == '_';
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // other utils methods
    ///////////////////////////////////////////////////////////////////////////

    private static byte[] string2Bytes(final String string) {
        if (string == null) {
            return null;
        }
        return string.getBytes();
    }

    private static String bytes2String(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return new String(bytes);
    }

    private static byte[] jsonObject2Bytes(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        return jsonObject.toString().getBytes();
    }

    private static JSONObject bytes2JSONObject(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            return new JSONObject(new String(bytes));
        } catch (Exception e) {
            ExceptionUtils.printStackTrace(e);
            return null;
        }
    }

    private static byte[] jsonArray2Bytes(final JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }
        return jsonArray.toString().getBytes();
    }

    private static JSONArray bytes2JSONArray(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            return new JSONArray(new String(bytes));
        } catch (Exception e) {
            ExceptionUtils.printStackTrace(e);
            return null;
        }
    }

    private static byte[] parcelable2Bytes(final Parcelable parcelable) {
        if (parcelable == null) {
            return null;
        }
        Parcel parcel = Parcel.obtain();
        parcelable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle();
        return bytes;
    }

    private static <T> T bytes2Parcelable(final byte[] bytes,
                                          final Parcelable.Creator<T> creator) {
        if (bytes == null) {
            return null;
        }
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0);
        T result = creator.createFromParcel(parcel);
        parcel.recycle();
        return result;
    }

    private static byte[] serializable2Bytes(final Serializable serializable) {
        if (serializable == null) {
            return null;
        }
        ByteArrayOutputStream baos;
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos = new ByteArrayOutputStream());
            oos.writeObject(serializable);
            return baos.toByteArray();
        } catch (Exception e) {
            ExceptionUtils.printStackTrace(e);
            return null;
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                ExceptionUtils.printStackTrace(e);
            }
        }
    }

    private static Object bytes2Object(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return ois.readObject();
        } catch (Exception e) {
            ExceptionUtils.printStackTrace(e);
            return null;
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                ExceptionUtils.printStackTrace(e);
            }
        }
    }

    private static byte[] bitmap2Bytes(final Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private static Bitmap bytes2Bitmap(final byte[] bytes) {
        return (bytes == null || bytes.length <= 0)
                ? null
                : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private static byte[] drawable2Bytes(final Drawable drawable) {
        return drawable == null ? null : bitmap2Bytes(drawable2Bitmap(drawable));
    }

    private static Drawable bytes2Drawable(final byte[] bytes) {
        return bytes == null ? null : bitmap2Drawable(bytes2Bitmap(bytes));
    }

    private static Bitmap drawable2Bitmap(final Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(
                    1,
                    1,
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565
            );
        } else {
            bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565
            );
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private static Drawable bitmap2Drawable(final Bitmap bitmap) {
        return bitmap == null
                ? null
                : new BitmapDrawable(LifeCycleUtils.getApp().getResources(), bitmap);
    }


    private static void writeFileFromBytes(final File file, final byte[] bytes) {
        FileChannel fc = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, false);
            fc = fos.getChannel();
            fc.write(ByteBuffer.wrap(bytes));
            fc.force(true);
        } catch (IOException e) {
            ExceptionUtils.printStackTrace(e);
        } finally {
            try {
                if (fc != null) {
                    fc.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                ExceptionUtils.printStackTrace(e);
            }
        }
    }

    private static byte[] readFile2Bytes(final File file) {
        FileChannel fc = null;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "r");
            fc = raf.getChannel();
            int size = (int) fc.size();
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, size).load();
            byte[] data = new byte[size];
            mbb.get(data, 0, size);
            return data;
        } catch (IOException e) {
            ExceptionUtils.printStackTrace(e);
            return null;
        } finally {
            try {
                if (fc != null) {
                    fc.close();
                }
                if (raf != null) {
                    raf.close();
                }
            } catch (IOException e) {
                ExceptionUtils.printStackTrace(e);
            }
        }
    }

    private static boolean isSpace(final String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}