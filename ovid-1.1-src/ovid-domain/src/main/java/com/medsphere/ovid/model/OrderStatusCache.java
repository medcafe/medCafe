// <editor-fold defaultstate="collapsed">
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
// </editor-fold>


package com.medsphere.ovid.model;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.medsphere.common.cache.GenericCache;
import com.medsphere.common.cache.GenericCacheReaper;
import com.medsphere.fmdomain.FMOrderStatus;

/**
 * 
 */
public class OrderStatusCache extends GenericCache<String, FMOrderStatus> {
    private static OrderStatusCache instance = null;
    private static Logger logger = LoggerFactory.getLogger(OrderDialogCache.class);    
    
    protected OrderStatusCache(Long shelfLife) {
        super();
        this.setShelfLifeInMilliseconds(shelfLife);
        logger.debug("instantiated OrderStatusCache with shelf life of " + shelfLife);
    }

    public static synchronized OrderStatusCache getInstance() {
        if (instance == null) {
            instance = new OrderStatusCache();
            GenericCacheReaper.getInstance().addCache(instance);
        }
        return instance;
    }

    public static synchronized OrderStatusCache getInstance(Long shelfLife) {
        if (instance == null) {
            instance = new OrderStatusCache(shelfLife);
        } else {
            logger.debug("changing shelfLife from " + instance.getShelfLifeInMilliseconds() + " to " + shelfLife);
            instance.setShelfLifeInMilliseconds(shelfLife);
        }
        return instance;

    }

    protected OrderStatusCache() {
        super();
        long shelfLife = (4 * 60 * 60 * 1000);
        setShelfLifeInMilliseconds(shelfLife);
    }

    
    @Override
    protected Callable<?> getCacheLoader() {
        Callable<?> cacheLoader = new Callable<Object>() {

            public Boolean call() {
                return Boolean.TRUE;
            }
        };
        return cacheLoader;
    }


}
