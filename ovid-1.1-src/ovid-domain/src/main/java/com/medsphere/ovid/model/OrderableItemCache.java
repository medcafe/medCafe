// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2011  Medsphere Systems Corporation
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

import com.medsphere.common.cache.GenericCache;
import com.medsphere.common.cache.GenericCacheReaper;
import com.medsphere.fmdomain.FMOrderableItem;

/**
 * 
 */
public class OrderableItemCache extends GenericCache<String,FMOrderableItem> {

    private static OrderableItemCache instance = null;

    protected OrderableItemCache() {
        super();
        long shelfLife = (30 * 60 * 1000); // 30 minute cache
        setShelfLifeInMilliseconds(shelfLife);
    }

    public static synchronized OrderableItemCache getInstance() {
        if (instance == null) {
            instance = new OrderableItemCache();
            GenericCacheReaper.getInstance().addCache(instance);
        }
        return instance;
    }

}
