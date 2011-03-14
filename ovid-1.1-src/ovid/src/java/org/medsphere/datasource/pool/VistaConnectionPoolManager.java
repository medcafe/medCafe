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

import java.util.HashMap;
import java.util.Map;
import org.medsphere.connection.VistaConnectionException;
import org.medsphere.connection.VistaConnectionProperties;
import org.medsphere.datasource.VistaDataSource;
import org.medsphere.lifecycle.LifecycleListener;
import org.medsphere.lifecycle.LifecycleManager;

/**
 * The singleton gives external entities the ability to shut down the pooling
 * system in order to release associated resources.
 */
public class VistaConnectionPoolManager implements LifecycleListener {

    private static VistaConnectionPoolManager instance = null;
    private Map<String, VistaConnectionPool> pools = new HashMap<String, VistaConnectionPool>();

    synchronized VistaConnectionPool createPool(VistaDataSource dataSource, VistaConnectionProperties properties)
            throws VistaConnectionException {
        String name = properties.get("poolName");
        if (name == null) {
            throw new VistaConnectionException("Pooled data source requested, but poolName is not specified");
        }

        VistaConnectionPool pool = getPool(name);
        if (pool == null) {
            pool = new VistaConnectionPool(dataSource, properties);
            pools.put(name, pool);
        }
        return pool;
    }

    synchronized VistaConnectionPool getPool(String name) {
        return pools.get(name);
    }

    private VistaConnectionPoolManager() {
    }

    /**
     * Gets singleton instance of this class.
     * @return Instance of <code>VistaConnectionPoolManager</code>
     */
    static synchronized public VistaConnectionPoolManager getInstance() {
        if (instance == null) {
            instance = new VistaConnectionPoolManager();
            LifecycleManager.getInstance().addListener(instance);
        }
        return instance;
    }

    /**
     * Shuts down the named pool. All connections belonging to the pool, even
     * those that have been released for use, will be closed. It's like
     * Caddyshack - everybody out!
     * @param name The name of the pool to close.
     */
    synchronized public void shutdownPool(String name) {
        VistaConnectionPool pool = getPool(name);
        if (pool != null) {
            pool.shutdown();
        }
    }

    /**
     * Shuts down all pools. All connections belonging to the pool, even
     * those that have been released for use, will be closed.
     */
    synchronized public void shutdownAll() {
        for (VistaConnectionPool pool : pools.values()) {
            pool.shutdown();
        }
    }

    public void shutdown() {
        shutdownAll();
    }
}
