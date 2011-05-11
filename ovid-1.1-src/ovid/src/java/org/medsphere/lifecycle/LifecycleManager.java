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

package org.medsphere.lifecycle;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * This singleton dispatches events to listeners that are interested in
 * lifecycle events. The most useful application is to receive notification that
 * the containing entity (servlet context, JVM, web service) is shutting down
 * and that resources need to be released.
 * <P/>
 * See warning in {@link #addListener(org.medsphere.lifecycle.LifecycleListener) AddListener}
 * about using anonymous listeners.
 */
public class LifecycleManager {

    private static LifecycleManager instance = null;
    Map<LifecycleListener, Object> map;

    private LifecycleManager() {
        map = new WeakHashMap<LifecycleListener, Object>();
    }

    /**
     * Gets singleton instance of this class.
     * @return Instance of <code>LifecycleManager</code>
     */
    public static synchronized LifecycleManager getInstance() {
        if (instance==null) {
            instance = new LifecycleManager();
        }
        return instance;
    }

    /**
     * Adds a listener.
     * <P/>
     * <B>Warning:</B> Listeners are held by weak references, so something other
     * than the <code>LifecycleManager</code> must hold a reference to it or
     * garbage collection will claim it. That means anonymous listeners will not
     * work. Instead of
     * <code>
     * <pre>
     * class X {
     *   void registerWithLifecycleManager() {
     *    LifecycleManager.getInstance().addListener(new LifecycleListener(){dostuff;});
     *   }
     * }
     * </pre>
     * </code>
     * make the calling object implement <code>LifecycleListener</code>
     * <code>
     * <pre>
     * class X implements LifecycleListener {
     *   void registerWithLifecycleManager() {
     *    LifecycleManager.getInstance().addListener(this);
     *   }
     *   void shutdown() {
     *    dostuff;
     *   }
     * }
     * </pre>
     * </code>
     * @param listener The listener to add.
     */
    synchronized public void addListener(LifecycleListener listener) {
        map.put(listener, null);
    }

    /**
     * Send a shutdown event to all listeners.
     */
    synchronized public void fireShutdown() {
        for (LifecycleListener listener : map.keySet()) {
            listener.shutdown();
        }
    }
}
