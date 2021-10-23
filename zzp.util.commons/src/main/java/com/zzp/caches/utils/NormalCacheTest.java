package com.zzp.caches.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description 非超时缓存测试
 * @Author Garyzeng
 * @since 2019.11.22
 **/
public class NormalCacheTest {

    public static void main(String[] args) {
        Cache<String, Object> caches = new NormalCache();

        AtomicInteger atomicInteger = new AtomicInteger(0);

        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    String key = "TEST-A";
                    Object value = caches.get(key);
                    if (!caches.cacheValid(value)){
                        // 缓存校验不通过，需要重新获取
                        String newValue = key + "-" + atomicInteger.incrementAndGet();
                        caches.put(key, newValue);
                        value = caches.get(key);
                    }
                    String nowDateStr = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
                    System.out.println(nowDateStr + " -- " + Thread.currentThread().getName() + " -- key:" + key + ",value:" + value);
                    try {
                        Thread.sleep(6 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    String key = "TEST-A";
                    Object value = caches.get(key);
                    if (!caches.cacheValid(value)){
                        // 缓存校验不通过，需要重新获取
                        String newValue = key + "-" + atomicInteger.incrementAndGet();
                        caches.put(key, newValue);
                        value = caches.get(key);
                    }
                    String nowDateStr = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
                    System.out.println(nowDateStr + " -- " + Thread.currentThread().getName() + " -- key:" + key + ",value:" + value);
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        threadA.start();
        threadB.start();

    }

}
