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

package org.medsphere.pool;

/**
 * Interface used by {@link GenericPool} for callbacks.
 */
public interface PoolEventCallback<T> {
    /**
     * An callback fired by the pool. One call to this method is made for every
     * applicable object in the pool.
     * @param arg The pooled object.
     * @return A <b>false</b> value indicates that the object should be removed
     * from the pool.
     */
    boolean event(T arg);
}
