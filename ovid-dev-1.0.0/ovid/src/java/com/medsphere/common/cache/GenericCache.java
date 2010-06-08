/*
 * Copyright (C) 2007-2009  Medsphere Systems Corporation
 * All rights reserved.
 *
 * This source code contains the intellectual property
 * of its copyright holder(s), and is made available
 * under a license. If you do not know the terms of
 * the license, please stop and do not read further.
 *
 * Please read LICENSES for detailed information about
 * the license this source code file is available under.
 * Questions should be directed to legal@medsphere.com
 *
 */

package com.medsphere.common.cache;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.apache.log4j.Logger;

public abstract class GenericCache<K, V> implements Comparable {

    @SuppressWarnings("unchecked")
    private FutureTask preloadTask = null;
    private static ExecutorService executor = Executors.newCachedThreadPool();
    private Logger logger = Logger.getLogger(GenericCache.class);
    private ConcurrentHashMap<K, V> cacheMap = null;
    private ConcurrentHashMap<K, Long> shelfLifeMap = null;
    private long shelfLifeInMilliseconds = 60 * 1000 * 5; // 5 minutes in the cache by default...
    protected boolean loaded = false;
    protected final Long id = new Date().getTime();

    protected GenericCache() {
        cacheMap = new ConcurrentHashMap<K, V>();
        shelfLifeMap = new ConcurrentHashMap<K, Long>();
        setShelfLifeInMilliseconds(this.shelfLifeInMilliseconds);
    }

    @SuppressWarnings("unchecked")
    protected Callable getCacheLoader() {
        return null;
    }

    protected V queryValueByKey(K key) throws GenericCacheException {
        return null;
    }

    public void flush() {
        cacheMap.clear();
        shelfLifeMap.clear();
        loaded = false;
    }

    public Long getShelfLifeInMilliseconds() {
        return this.shelfLifeInMilliseconds;
    }

    public void setShelfLifeInMilliseconds(Long shelfLifeInMilliseconds) {
        logger.info("shelf life for this cache is set to " + shelfLifeInMilliseconds);
        if (shelfLifeInMilliseconds == 0) {
            logger.info("cache always expires -- items are never cached.");
        } else if (shelfLifeInMilliseconds < 0) {
            logger.info("shelf life for this cache is set to never expire -- cached forever");
        }
        this.shelfLifeInMilliseconds = shelfLifeInMilliseconds;
    }

    @SuppressWarnings("unchecked")
    public void preload() {
        logger.info("preloading cache");
        Callable cacheLoader = getCacheLoader();
        if (cacheLoader!=null) {
            preloadTask = new FutureTask(cacheLoader);
            executor.submit(preloadTask);
        }
        loaded = true;
    }

    public void addToCache(K key, V value) {
        if (key != null) {
            logger.debug("adding key " + key.toString() + " referencing " + value.toString() + " to the cache");
            cacheMap.put(key, value);
            shelfLifeMap.put(key, System.currentTimeMillis());
        }
    }

    protected void waitForPreload() throws GenericCacheException {
        if (!loaded) {
            preload();
        }
        if (preloadTask != null) {
            try {
                preloadTask.get();
                preloadTask = null;
                logger.info("cache preload complete");
            } catch (Exception e) {
                e.printStackTrace();
                throw new GenericCacheException(e);
            }
        } else {
            logger.debug("preload task is null");
        }
    }

    public V getByKey(K key) throws GenericCacheException {

        logger.debug("getByKey: " + key.toString());
        if (expired(key)) {
            logger.debug("key " + key.toString() + " is expired");
            if (cacheMap.get(key) != null) {
                cacheMap.remove(key);
                shelfLifeMap.remove(key);
            }
        }

        V value = cacheMap.get(key);
        if (value == null) {
            logger.debug("unable to find key " + key.toString() + " in cache, querying...");
            value = queryValueByKey(key);
            if (value != null) {
                addToCache(key, value);
            }
        } else {
            shelfLifeMap.put(key, System.currentTimeMillis());
        }
        return value;
    }

    public Collection<V> getCacheValues() throws GenericCacheException {
        try {
            waitForPreload();
        } catch (GenericCacheException e) {
            loaded = false;
            throw e;
        }
        refreshExpiredEntries();
        for (K key : cacheMap.keySet()) {
            shelfLifeMap.put(key, System.currentTimeMillis());
        }
        return cacheMap.values();
    }

    public void removeKey(K key) {
        cacheMap.remove(key);
        shelfLifeMap.remove(key);
    }

    public void refreshExpiredEntries() throws GenericCacheException {
        for (K key : cacheMap.keySet()) {
            if (expired(key)) {
                logger.info("Key " + key + " is expired.");
                V value = queryValueByKey(key);
                if (value != null) {
                    addToCache(key, value);
                } else {
                    removeKey(key);
                }
            }
        }
    }

    public void removeExpiredEntries() throws GenericCacheException {
        for (K key : cacheMap.keySet()) {
            if (expired(key)) {
                logger.info("Key " + key + " is expired.");
                V value = cacheMap.get(key);
                if (value==null || expireValue(value)) {
                    removeKey(key);
                } else {
                    addToCache(key, value);
                }
            }
        }
    }

    protected boolean expireValue(V value) {
        // Override. Return false if particular value should not expire.
        return true;
    }

    private boolean expired(K key) throws GenericCacheException {
        // 0:   never cache
        // < 0: never expire
        if (shelfLifeInMilliseconds == 0) {
            logger.info("Key " + key + " is expired because cache shelf life is set always to expire (e.g. no cacheing");
            return true;
        } else if (shelfLifeInMilliseconds < 0) {
            return false;
        }
        if (!cacheMap.keySet().contains(key)) {
            logger.debug("No key entry for " + key + ", so returning not expired");
            return false;
        }

        Long keyShelfLife = shelfLifeMap.get(key);
        if (keyShelfLife == null) {
            keyShelfLife = new Long(0);
        }

        Long timeOnShelf = System.currentTimeMillis() - keyShelfLife;

        logger.debug("comparing current time on shelf of " + timeOnShelf + " against " + shelfLifeInMilliseconds);
        if (timeOnShelf > shelfLifeInMilliseconds) {
            logger.info("Key " + key + " expired with a shelf life of " + timeOnShelf);
            return true;
        } else {
            return false;
        }

    }

    public Collection<K> getKeys() throws GenericCacheException {
        waitForPreload();
        refreshExpiredEntries();

        return cacheMap.keySet();
    }

    public int compareTo(Object o) {
        return id.compareTo(((GenericCache) o).id);
    }
}
