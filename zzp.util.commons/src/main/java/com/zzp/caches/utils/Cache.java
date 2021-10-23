package com.zzp.caches.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Description 缓存模板，使用读写锁实现的缓存
 * @Author Garyzeng
 * @since 2019.11.22
 **/
public abstract class Cache<K, V> {

    private final Map<K, V> m = new HashMap<K, V>();

    private final ReadWriteLock rwl = new ReentrantReadWriteLock();

    private final Lock readLock = rwl.readLock();

    private final Lock writeLock = rwl.writeLock();

    private long count = 0;

    /**
     * get值
     * @param key
     * @return
     */
    public V get(K key){
        // 获取读锁
        readLock.lock();
        try {
            V value = m.get(key);
            return value;
        } finally {
            // 释放读锁
            readLock.unlock();
        }
    }

    /**
     * put值，会校验原来的值是否能通过校验
     * 如果能通过校验则value不会替换oldValue
     * 如果不能通过校验则value会替换oldValue
     * @param key
     * @param value
     * @return 返回oldValue
     */
    public V put(K key, V value) {
        // 获取读锁
        readLock.lock();
        V oldValue = m.get(key);
        if (!cacheValid(oldValue)) {
            // 释放读锁，因为不允许读锁升级为写锁
            readLock.unlock();
            // 获取写锁
            writeLock.lock();
            try {
                // 再次检查状态，防止多线程访问的时候，重复put值
                oldValue = m.get(key);
                if (!cacheValid(oldValue)) {
                    // put值
                    m.put(key, value);
                    count++;
                }
                // 释放写锁前，降级为读锁
                readLock.lock();
            } finally {
                // 释放写锁
                writeLock.unlock();
            }
        }

        // 此时还拥有读锁
        try {
            return oldValue;
        } finally {
            // 释放读锁
            readLock.unlock();
        }
    }

    /**
     * 对缓存的校验，需要由子类实现
     * @param value
     * @return
     */
    protected abstract boolean cacheValid(V value);

    public long getCount() {
        return count;
    }
}
