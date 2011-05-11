// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2010  Medsphere Systems Corporation
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
// </editor-fold>
package org.medsphere.auth;

import com.medsphere.common.cache.GenericCache;
import com.medsphere.common.cache.GenericCacheException;
import com.medsphere.common.cache.GenericCacheReaper;
import com.medsphere.vistarpc.RPCException;

/**
 * Caches <code>VistaSubject</code> objects, with optional expiration. To turn
 * off expiration, set the shelf life to a negative value.
 */
public class SubjectCache {

    private static SubjectCache _instance = null;
    private GenericCacheImpl cache = null;
    private Long shelfLife;

    private SubjectCache() {
        shelfLife = Long.valueOf(60 * 1000 * 60); // 60 minute timeout
    }

    /**
     * Get singleton instance.
     * @return An instantiation of this class.
     */
    public static synchronized SubjectCache getInstance() {
        if (_instance == null) {
            _instance = new SubjectCache();
        }
        return _instance;
    }

    /**
     * Sets the amount of time that a subject may remain idle in the cache. If
     * a subject is expired, it's <code>dispose</code> method is called.
     * @param shelfLife Expiration time in milliseconds.
     */
    public void setShelfLife(Long shelfLife) {
        if (shelfLife != null) {
            this.shelfLife = shelfLife;
            if (getCache() != null) {
                getCache().setShelfLifeInMilliseconds(shelfLife);
            }
        }
    }

    /**
     * Associate a subject to a key and add it to the cache.
     * @param key The string that can be used to retrieve this subject.
     * @param subject The value to cache.
     */
    public void addToCache(String key, VistaSubject subject) {
        getCache().addToCache(key, subject);
    }

    /**
     * Remove the subject identified by the key from the cache. Caller is
     * responsible for freeing any resources associated with the subject.
     * @param key
     */
    public void removeFromCache(String key) {
        getCache().removeKey(key);
    }

    /**
     * Retrieve a subject from the cache that was identified by a key. 
     * @param key The key used to cache the subject
     * @return The cached subject, or null if no subject could be found.
     */
    public VistaSubject getByKey(String key) {
        try {
            return getCache().getByKey(key);
        } catch (GenericCacheException ex) {
            return null;
        }
    }

    public void dispose() {
        if (cache!=null) {
            cache.dispose();
        }
    }

    private GenericCacheImpl getCache() {
        if (cache == null) {
            cache = new GenericCacheImpl();
            if (shelfLife != null) {
                cache.setShelfLifeInMilliseconds(shelfLife);
            }
            GenericCacheReaper.getInstance().addCache(cache);
        }
        return cache;
    }

    private static class GenericCacheImpl extends GenericCache<String, VistaSubject> {

        public GenericCacheImpl() {
        }

        @Override
        protected boolean expireValue(VistaSubject subject) {
            try {
                // Reaping thread is working for the parent cache, so we are
                // the ultimate caller and are responsible for calling dispose.
                subject.dispose();
            } catch (RPCException ex) {
                // Do nothing - we're disposing anyway
            }
            return true;
        }

    }
}
