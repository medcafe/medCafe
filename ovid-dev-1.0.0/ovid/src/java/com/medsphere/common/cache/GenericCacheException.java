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

public class GenericCacheException extends Exception {
    private static final long serialVersionUID = -674141389835151069L;
    public GenericCacheException(String message) {
        super(message);
    }
    public GenericCacheException(Throwable reason) {
        super(reason);
    }
}
