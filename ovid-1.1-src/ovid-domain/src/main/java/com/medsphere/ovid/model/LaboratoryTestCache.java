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
import com.medsphere.fmdomain.FMLaboratoryTest;

public class LaboratoryTestCache extends GenericCache<String, FMLaboratoryTest>{

    private static LaboratoryTestCache instance = null;

    private LaboratoryTestCache(long shelfLife) {
        super();
        setShelfLifeInMilliseconds(shelfLife);
    }

    private final static long oneHour = 60 * 60 * 1000L;
    public static synchronized LaboratoryTestCache getInstance() {
        if (instance == null) {
            instance = new LaboratoryTestCache(oneHour);
        }
        return instance;
    }

}
