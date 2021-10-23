package com.zzp.caches.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description 超时缓存测试
 * @Author Garyzeng
 * @since 2019.11.22
 **/
public class TimeCacheTest {

    public static void main(String[] args) {

        Cache<String, TimeValue> caches = new TimeCache();

        final AtomicInteger atomicInteger = new AtomicInteger(0);

        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    String key = "TEST-A";
                    TimeValue cache = caches.get(key);
                    if (!caches.cacheValid(cache)) {
                        // 缓存校验不通过，需要重新获取
                        TimeValue timeValue = new TimeValue();
                        List<String> list = new ArrayList<String>();
                        list.add(key + "-" + atomicInteger.incrementAndGet());
                        timeValue.setList(list);
                        timeValue.setTimestamp(System.currentTimeMillis());
                        caches.put(key, timeValue);
                        cache = caches.get(key);
                    }
                    String nowDateStr = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
                    System.out.println(nowDateStr + " -- " + Thread.currentThread().getName() + " -- key:" + key + ",value:" + JSON.toJSONString(cache) + ",cache.count:" + caches.getCount());
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
                    TimeValue cache = caches.get(key);
                    if (!caches.cacheValid(cache)) {
                        // 缓存校验不通过，需要重新获取
                        TimeValue timeValue = new TimeValue();
                        List<String> list = new ArrayList<String>();
                        list.add(key + "-" + atomicInteger.incrementAndGet());
                        timeValue.setList(list);
                        timeValue.setTimestamp(System.currentTimeMillis());
                        caches.put(key, timeValue);
                        cache = caches.get(key);
                    }
                    String nowDateStr = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
                    System.out.println(nowDateStr + " -- " + Thread.currentThread().getName() + " -- key:" + key + ",value:" + JSON.toJSONString(cache) + ",cache.count:" + caches.getCount());
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
