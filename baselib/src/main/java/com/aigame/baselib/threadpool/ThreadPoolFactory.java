package com.aigame.baselib.threadpool;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by songguobin on 2018/7/27.
 */

public class ThreadPoolFactory {

    public static synchronized CachedThreadPoolExecutor createCacheThreadPool() {
        CachedThreadPoolExecutor mExecutor = new CachedThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(), new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "CommonThreadPool#" + mCount.getAndIncrement());
            }
        });
        return mExecutor;
    }

}
