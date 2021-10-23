package com.zzp.caches.utils;

/**
 * @Description 超时缓存
 * @Author Garyzeng
 * @since 2019.11.22
 **/
public class TimeCache extends Cache<String, TimeValue> {

    private final long effectiveTime = 30 * 1000;// 缓存有效时间长度，单位毫秒

    @Override
    protected boolean cacheValid(TimeValue value) {
        if (value != null) {
            long nowTime = System.currentTimeMillis();
            return nowTime - value.getTimestamp() <= effectiveTime;
        }
        return false;
    }

}
