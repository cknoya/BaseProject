package com.yuxi.jxs.sp;

/**
 * Created by songguobin on 2018/7/27.
 */

public class CommonThreadPool {

    private static final CachedThreadPoolExecutor COMMON_THREAD_POOL = ThreadPoolFactory.createCacheThreadPool();

    public static void submit(Runnable runnable){
        COMMON_THREAD_POOL.submit(runnable);
    }

}
