package com.aigame.baselib.io.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.aigame.baselib.threadpool.CommonThreadPool;
import com.aigame.baselib.log.DebugLog;
import com.aigame.baselib.utils.crash.ExceptionUtils;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;


public class ConfigurationHelper {

    private static final String TAG = ConfigurationHelper.class.getSimpleName();

    // Default save preference period after modification(5seconds) //1 minutes
    private static final long DEFAULT_SAVE_PERIOD = 1000 * 5L;

    private static final int ACTION_FOR_SAVE_PREFS = 1;
    private static final int ACTION_FOR_KEY_LISTENER = 2;


    private static Object sInitLock = new Object();
    private static Map<String, ConfigurationHelper> sPreferences =
            new HashMap<String, ConfigurationHelper>();

    private ConcurrentMap<String, String> mCurrentConfigurations =
            new ConcurrentHashMap<String, String>();
    private SharedPreferences mPref;
    private Editor mEditor;
    private Vector<String> mDirty = new Vector<String>();
    private Context mAppContext;
    private final String mAppDataDir;
    private final String mPrefName;
    /**
     * Lock for modification
     */
    private Object mModifyLock = new Object();
    private long mCurrentModifyTimer = DEFAULT_SAVE_PERIOD;

    // Handler to handle save preference task
    private Handler mHandler;
    protected WeakHashMap<String, HashSet<IOnSharedChangeListener>> listenersMap
            = new WeakHashMap<String, HashSet<IOnSharedChangeListener>>();

    private final static String CAST_EXCEPTION_TIPS = "Cannot cast defValue: ";

    /**
     * sharepreference set listener
     */
    public interface IOnSharedChangeListener {
        /**
         * 当前key的值被重新设置的监听（* 有可能在非主线程）
         *
         * @param key
         */
        public void onSharedPreferenceChanged(String key);
    }



    public static ConfigurationHelper getInstance(Context context, String prefName) {
        synchronized (sInitLock) {
            if (sPreferences.get(prefName) == null) {
                try {
                    sPreferences.put(prefName, new ConfigurationHelper(context, prefName));
                } catch (Exception e) {
                    ExceptionUtils.printStackTrace(e);
                }
            }
        }
        return sPreferences.get(prefName);
    }

    /**
     * 为某个key增加监听
     *
     * @param key
     * @param listener
     */
    public void addOnSharedPreferenceChangListener(String key, IOnSharedChangeListener listener) {
        if (TextUtils.isEmpty(key)) {
            throw new RuntimeException("key can not be null");
        }
        if (listener == null) {
            throw new RuntimeException("listener can not be null");
        }
        if (listenersMap.get(key) == null) {
            HashSet<IOnSharedChangeListener> listeners = new HashSet<IOnSharedChangeListener>();
            listeners.add(listener);
            listenersMap.put(key, listeners);
        }
        listenersMap.get(key).add(listener);
    }

    /**
     * Save all dirty change to preference
     *
     * @param async to save async
     */
    public static void save(boolean async) {
        ConfigurationHelper temp;
        synchronized (sInitLock) {
            for (Map.Entry<String, ConfigurationHelper> entry : sPreferences.entrySet()) {
                temp = entry.getValue();
                if (temp.mHandler != null) {
                    temp.mHandler.removeMessages(ACTION_FOR_SAVE_PREFS);
                }
                temp.commit(async);
            }
        }
    }

    private ConfigurationHelper(Context context, String prefName) {
        mAppContext = context.getApplicationContext() == null ?
                context : context.getApplicationContext();
        initCurrentConfigurations(prefName);
        mAppDataDir = context.getApplicationInfo().dataDir;
        mPrefName = prefName;
        mHandler = new Handler(context.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ACTION_FOR_SAVE_PREFS:
                        commit(true);
                        break;
                    case ACTION_FOR_KEY_LISTENER:
                        HashSet<IOnSharedChangeListener> listeners = listenersMap.get(msg.obj);
                        for (IOnSharedChangeListener listener : listeners) {
                            listener.onSharedPreferenceChanged(msg.obj.toString());
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void initCurrentConfigurations(final String prefName) {
        mPref = mAppContext.getSharedPreferences(prefName, 0);
        if (mPref != null) {
            Map<String, ?> map = mPref.getAll();
            if (map != null) {
                for (Map.Entry<String, ?> entry : map.entrySet()) {
                    if (!mDirty.contains(entry.getKey())) {
                        mCurrentConfigurations.put(entry.getKey(), String.valueOf(entry.getValue()));
                    }
                }
            }
        }
    }


    public String getString(String key, String defValue) {
        if (TextUtils.isEmpty(key)) {
            return defValue;
        }

        String temp = mCurrentConfigurations.get(key);
        // only check temp with null, due to value maybe ""
        if (temp != null) {
            return temp;
        } else if (mDirty.contains(key)) {
            // Has been removed.
            return defValue;
        } else {
            if (mPref != null) {
                if (mPref.contains(key)) {
                    temp = mPref.getString(key, defValue);
                    mCurrentConfigurations.put(key, temp);
                } else {
                    if (temp == null) {
                        temp = defValue;
                    }
                }
                return temp;
            } else {
                return defValue;
            }
        }
    }

    public boolean getBoolean(String key, boolean defValue) {
        try {
            String result = getString(key, String.valueOf(defValue));
            return Boolean.valueOf(result);
        } catch (ClassCastException e) {
            try {
                if (mPref != null) {
                    return mPref.getBoolean(key, defValue);
                }
            } catch (Exception ex) {
                DebugLog.d(TAG, "Cannot get boolean defValue: ", defValue);
            }
            DebugLog.d(TAG, CAST_EXCEPTION_TIPS, defValue, " from sharepreference to boolean");
        }
        return defValue;
    }

    public int getInt(String key, int defValue) {
        int result = Integer.MIN_VALUE;
        try {
            result = Integer.parseInt(getString(key, String.valueOf(defValue)));
        } catch (NumberFormatException nef) {
            DebugLog.d(TAG, CAST_EXCEPTION_TIPS, defValue, " from sharepreference to int");
            result = defValue;
        } catch (ClassCastException e) {
            try {
                if (mPref != null) {
                    return mPref.getInt(key, defValue);
                }
            } catch (Exception ex) {
                DebugLog.d(TAG, "Cannot get int defValue: ", String.valueOf(defValue));
            }
            DebugLog.v(TAG, CAST_EXCEPTION_TIPS, defValue, " from sharepreference to int");
            result = defValue;
        }
        return result;
    }

    public float getFloat(String key, float defValue) {
        float result = defValue;
        try {
            String value = getString(key, null);
            if (value != null) {
                result = Float.parseFloat(value);
            }
        } catch (NumberFormatException nef) {
            DebugLog.d(TAG, CAST_EXCEPTION_TIPS, defValue, " from sharepreference to int");
            result = defValue;
        } catch (ClassCastException e) {
            try {
                if (mPref != null) {
                    return mPref.getFloat(key, defValue);
                }
            } catch (Exception ex) {
                DebugLog.d(TAG, "Cannot get int defValue: ", defValue);
            }
            DebugLog.v(TAG, CAST_EXCEPTION_TIPS, defValue, " from sharepreference to int");
            result = defValue;
        }
        return result;
    }

    public long getLong(String key, long defValue) {
        long result = Long.MIN_VALUE;
        try {
            result = Long.parseLong(getString(key, String.valueOf(defValue)));
        } catch (NumberFormatException nef) {
            DebugLog.d(TAG, CAST_EXCEPTION_TIPS, defValue, " from sharepreference to long");
            result = defValue;
        } catch (ClassCastException e) {
            try {
                if (mPref != null) {
                    return mPref.getLong(key, defValue);
                }
            } catch (Exception ex) {
                DebugLog.d(TAG, "Cannot get long defValue: ", defValue);
            }
            DebugLog.v(TAG, CAST_EXCEPTION_TIPS, defValue, " from sharepreference to long");
            result = defValue;
        }
        return result;
    }

    /**
     * set string to sharepreference
     *
     * @param key
     * @param value
     * @param saveImmediately flag for save this item immediately, false will save this item
     *                        together with others.
     */
    public void putString(final String key, String value, boolean saveImmediately) {
        if (mCurrentConfigurations == null||key == null||value==null) {
            return;
        }

        if(key == null || value == null) {
            //key or value cannot been null,for ConcurrentHashMap will check key and value,
            //if one of them is null, then throw NPE
            return;
        }

        mCurrentConfigurations.put(key, value);
        if (saveImmediately && mPref != null) {
            if (mEditor == null) {
                mEditor = mPref.edit();
            }
            mEditor.putString(key, value);
            asyncCommit();
            synchronized (mModifyLock) {
                mDirty.remove(key);
            }
        } else {
            synchronized (mModifyLock) {
                mDirty.add(key);
            }
            mCurrentModifyTimer -= 100;
            if (mCurrentModifyTimer < 0) {
                mCurrentModifyTimer = 0;
            }
            // Post delay to save
            mHandler.removeMessages(ACTION_FOR_SAVE_PREFS);
            Message msg = mHandler.obtainMessage();
            msg.what = ACTION_FOR_SAVE_PREFS;
            mHandler.sendMessageDelayed(msg, mCurrentModifyTimer);
        }

        if (listenersMap.containsKey(key)) {
            Message msg = mHandler.obtainMessage();
            msg.obj = key;
            msg.what = ACTION_FOR_KEY_LISTENER;
            mHandler.sendMessageDelayed(msg, 0);

        }
    }

    public void putBoolean(String key, boolean value, boolean saveImmediately) {
        putString(key, String.valueOf(value), saveImmediately);
    }

    public void putInt(String key, int value, boolean saveImmediately) {
        putString(key, String.valueOf(value), saveImmediately);
    }

    public void putLong(String key, long value, boolean saveImmediately) {
        putString(key, String.valueOf(value), saveImmediately);
    }

    public void putFloat(String key, float value, boolean saveImmediately) {
        putString(key, String.valueOf(value), saveImmediately);
    }

    public void remove(final String key, boolean saveImmediately) {
        mCurrentConfigurations.remove(key);
        if (saveImmediately && mPref != null) {
            if (mEditor == null) {
                mEditor = mPref.edit();
            }
            mEditor.remove(key);
            asyncCommit();
        } else {
            synchronized (mModifyLock) {
                mDirty.add(key);
            }
        }
    }

    public void clear() {
        synchronized (mModifyLock) {
            mDirty.clear();
            mCurrentConfigurations.clear();
        }

        if (mPref != null) {
            if (mEditor == null) {
                mEditor = mPref.edit();
            }
            mEditor.clear();
            asyncCommit();
        }
    }

    public boolean hasKey(String key) {
        if (mCurrentConfigurations.containsKey(key)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * commit modification to the disk
     *
     * @param asnyc to save async
     */
    private void commit(boolean asnyc) {
        if (mPref != null) {
            String temp = null;
            if (mEditor == null) {
                mEditor = mPref.edit();
            }
            synchronized (mModifyLock) {
                if (!asnyc) {
                    QueueWork.waitToFinish();
                }
                if (null == mDirty || mDirty.size() == 0) {
                    return;
                }
                for (String key : mDirty) {
                    temp = mCurrentConfigurations.get(key);
                    // if key was removed then mCurrentConfigurations will
                    // return null, So remove key from editor
                    if (temp == null) {
                        mEditor.remove(key);
                    } else {
                        mEditor.putString(key, temp);
                    }
                }
                mDirty.clear();
                mCurrentModifyTimer = DEFAULT_SAVE_PERIOD;
            }
            if (asnyc) {
                asyncCommit();
            } else {
                mEditor.commit();
            }
        }
    }

    /**
     * 异步提交commit，避免应用使用中apply导致ANR
     */
    private void asyncCommit() {
        final CountDownLatch commitWait = new CountDownLatch(1);
        QueueWork.add(commitWait);
        CommonThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                mEditor.commit();
                QueueWork.remove(commitWait);
                commitWait.countDown();
            }
        });
    }

    /**
     * Judge the SharedPreference with the name exist or not This new file logic is copy from
     * ContextImpl.java
     *
     * @param name
     * @param context
     * @param appDataDir dir for this app's data
     * @return
     */
    private static boolean isExistSharedPreferenceFile(Context context, String name,
                                                       String appDataDir) {
        File sFile = getPreferenceFile(context, name, appDataDir);
        if (sFile != null) {
            return sFile.exists();
        } else {
            return false;
        }
    }

    /**
     * Remove the SharedPreference with the name This new file logic is copy from ContextImpl.java
     *
     * @param name
     * @param context
     * @param appDataDir dir for this app's data
     * @return
     */
    private static boolean deleteSharedPreferenceFile(Context context, String name,
                                                      String appDataDir) {
        File sFile = getPreferenceFile(context, name, appDataDir);
        if (sFile != null && sFile.exists()) {
            return sFile.delete();
        } else {
            return false;
        }
    }

    /**
     * Get current app's data folder
     *
     * @param context
     * @return
     */
    private static File getPreferenceFile(Context context, String name, String appDataDir) {
        if (context != null && !TextUtils.isEmpty(name) && name.indexOf(File.separatorChar) < 0) {
            return new File(new File(appDataDir, "shared_prefs"), name + ".xml");
        } else {
            return null;
        }
    }

    /**
     * 用来保存所有异步asynccommit 任务的锁，提高退出应用或异常情况下数据保存数据的一致性的概率
     */
    private static class QueueWork {
        private static final ConcurrentLinkedQueue<CountDownLatch> sPendingWorkFinishers =
                new ConcurrentLinkedQueue<CountDownLatch>();

        public static void add(CountDownLatch finisher) {
            sPendingWorkFinishers.add(finisher);
        }

        public static void remove(CountDownLatch finisher) {
            sPendingWorkFinishers.remove(finisher);
        }

        public static void waitToFinish() {
            CountDownLatch toFinish;
            while ((toFinish = sPendingWorkFinishers.poll()) != null) {
                try {
                    toFinish.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    ExceptionUtils.printStackTrace(e);
                }
            }
        }
    }
}
