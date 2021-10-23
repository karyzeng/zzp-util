package com.zzp.threadpool.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description 线程池工具类
 * @Author karyzeng
 * @since 2021.01.21
 **/
public class ThreadPoolUtils {

    /**
     * 自定义线程池
     * @param corePoolSize 核心线程大小
     * @param maxPoolSize 最大线程大小
     * @param keepAliveTime 线程存活时间（毫秒）
     * @param queueCapacity 队列容量
     * @param threadPoolName 线程名称
     * @return
     */
    public static ExecutorService newThreadPoolExecutor(int corePoolSize, int maxPoolSize, long keepAliveTime, int queueCapacity, final String threadPoolName) {
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(queueCapacity);
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, queue, new ThreadFactory() {
            // 自定义ThreadFactory，重新命名线程池名称以及线程池的线程名称
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, threadPoolName + "-" + r.hashCode());
                return thread;
            }
        });
    }

    /**
     * 自定义线程池
     * @return
     */
    public static ExecutorService newDefaultThreadPoolExecutor() {
        return newThreadPoolExecutor(5, 10, 60 * 1000L, 100, "Custom-Thread-Pool");
    }

    /**
     * 自定义线程池
     * @param threadPoolName 线程池名称
     * @return
     */
    public static ExecutorService newThreadPoolExecutor(String threadPoolName) {
        return newThreadPoolExecutor(5, 10, 60 * 1000L, 100, threadPoolName);
    }

}
