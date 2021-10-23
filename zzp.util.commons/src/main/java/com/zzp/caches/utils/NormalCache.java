package com.zzp.caches.utils;

/**
 * @Description 非超时缓存
 * @Author Garyzeng
 * @since 2019.11.22
 **/
public class NormalCache extends Cache<String, Object>{


    @Override
    protected boolean cacheValid(Object value) {
        return value != null;
    }

}
