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
package com.medsphere.ovid.model;

import com.medsphere.common.cache.GenericCache;
import com.medsphere.common.cache.GenericCacheReaper;
import com.medsphere.fmdomain.FMOrderDialog;
import java.util.concurrent.Callable;
import org.apache.log4j.Logger;


/**
 * expiring cache of order dialog items
 */
public class OrderDialogCache extends GenericCache<String, FMOrderDialog> {
    private static OrderDialogCache instance = null;
    private static Logger logger = Logger.getLogger(OrderDialogCache.class);    
    
    protected OrderDialogCache(Long shelfLife) {
        super();
        this.setShelfLifeInMilliseconds(shelfLife);
        logger.debug("instantiated OrderDialogCache with shelf life of " + shelfLife);
    }

    public static OrderDialogCache getInstance() {
        if (instance == null) {
            instance = new OrderDialogCache();
            GenericCacheReaper.getInstance().addCache(instance);
        }
        return instance;
    }

    public static OrderDialogCache getInstance(Long shelfLife) {
        if (instance == null) {
            instance = new OrderDialogCache(shelfLife);
        } else {
            logger.debug("changing shelfLife from " + instance.getShelfLifeInMilliseconds() + " to " + shelfLife);
            instance.setShelfLifeInMilliseconds(shelfLife);
        }
        return instance;

    }

    protected OrderDialogCache() {
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
