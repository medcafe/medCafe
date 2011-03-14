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
package org.medsphere.datasource.pool;

import java.util.Date;
import org.medsphere.pool.PoolEventCallback;

class TimeoutCallback implements PoolEventCallback<PooledConnectionProxy> {

    final private long interval;

    public TimeoutCallback(long interval) {
        this.interval = interval;
    }

    public boolean event(PooledConnectionProxy connection) {
        boolean retVal = new Date().getTime() - connection.getLastExecuteTime() < interval;
        if (!retVal) {
            connection.expire("Connection expired due to inactivity");
        }
        return retVal;
    }
}
