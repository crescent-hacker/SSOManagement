package com.jetsun.utility;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Company: jetsun
 * Author: dick
 * Date: 7/20/14
 * Desc:
 */
public class CacheUtil<T> {
    private final Map<String, T> m_objects;
    private final Map<String, Long> m_expiredObjects;
    private final long m_lExpireTime;
    private final ExecutorService m_executor;
    public CacheUtil() {
        this(500);
    }
    public CacheUtil(final int nExpireTime) {
        m_objects = Collections.synchronizedMap(new HashMap<String, T>());
        m_expiredObjects = Collections.synchronizedMap(new HashMap<String, Long>());
        m_lExpireTime = nExpireTime;
        m_executor = Executors.newFixedThreadPool(256);
        Executors.newScheduledThreadPool(5).scheduleWithFixedDelay(removeExpiredObjects(), m_lExpireTime / 2,
                m_lExpireTime, TimeUnit.SECONDS);
    }
    private final Runnable removeExpiredObjects() {
        return new Runnable() {
            public void run() {
                for (final String name : m_expiredObjects.keySet()) {
                    if (System.currentTimeMillis() > m_expiredObjects.get(name)) {
                        m_executor.execute(createRemoveRunnable(name));
                    }
                }
            }
        };
    }
    private final Runnable createRemoveRunnable(final String name) {
        return new Runnable() {
            public void run() {
                m_objects.remove(name);
                m_expiredObjects.remove(name);
            }
        };
    }
    public long getExpireTime() {
        return m_lExpireTime;
    }
    public void put(final String name, final T obj) {
        put(name, obj, m_lExpireTime);
    }
    public void put(final String name, final T obj, final long expireTime) {
        m_objects.put(name, obj);
        m_expiredObjects.put(name, System.currentTimeMillis() + expireTime * 1000);
    }
    public T get(final String name) {
        final Long expireTime = m_expiredObjects.get(name);
        if (expireTime == null)
            return null;
        if (System.currentTimeMillis() > expireTime) {
            m_executor.execute(createRemoveRunnable(name));
            return null;
        }
        return m_objects.get(name);
    }
    @SuppressWarnings("unchecked")
    public <R extends T> R get(final String name, final Class<R> type) {
        return (R) get(name);
    }
}
