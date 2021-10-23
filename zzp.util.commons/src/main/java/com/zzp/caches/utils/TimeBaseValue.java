package com.zzp.caches.utils;

/**
 * @Description 超时缓存基础类
 * @Author Garyzeng
 * @since 2019.11.24
 **/
public abstract class TimeBaseValue {

    /**
     * 创建该缓存的时间戳
     */
    private Long timestamp;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
