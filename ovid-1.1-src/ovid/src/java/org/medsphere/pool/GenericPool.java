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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A generic pool of objects. The object returned by
 * {@link #getObject getObject} is the one that has been in the pool the
 * longest, resulting in a FIFO retrieval of pooled objects.<P/>
 * Shutdown callbacks may be added so that objects can be properly disposed when
 * the pool is shut down. </P>
 * Callbacks may be registered to be run a periodic intervals. See
 * {@link #addTimerCallback addTimerCallback} for more information.
 */
public class GenericPool<T> {

    private Queue<T> available = new LinkedList<T>();
    private Collection<T> used = new ArrayList<T>();
    private Collection<PoolEventCallback<T>> shutdownCallbacks = new ArrayList<PoolEventCallback<T>>();
    Timer timer = null;

    /**
     * Get an object from the pool.
     * @return Returns the object that has been in the pool the longest. If the
     * pool is empty, then <b>null</b> is returned.
     */
    synchronized public T getObject() {
        T retVal = available.poll();
        if (retVal !=null) {
            used.add(retVal);
        }
        return retVal;
    }

    /**
     * Adds an object to the pool. It is assumed that the object is already in
     * use, so to make it available for {@link #getObject getObject}, it must
     * be passed supplied in a {@link #returnObject returnObject} call.
     * @param object The object to add to the pool. It is considered in use
     * until {@link #returnObject returnObject} is called.
     */
    synchronized public void addObject(T object) {
        used.add(object);
    }

    synchronized private void removeObject(T object) {
        used.remove(object);
        available.remove(object);
    }

    /**
     * Returns an object to the pool and makes it available for
     * {@link #getObject getObject}.
     * @param object The object to return. If it has not been used in an
     * {@link #addObject addObject} call, then it is not added to the pool.
     */
    synchronized public void returnObject(T object) {
        if (used.remove(object)) {
            available.offer(object);
        }
    }

    /**
     * Shuts down the pool. This causes all shutdown callbacks to be called for
     * each object in the pool, whether the object is in use or not.
     */
    synchronized public void shutdown() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        Collection<T> objects = getAllObjects();
        for (PoolEventCallback<T> callback : shutdownCallbacks) {
            for (T object : objects) {
                callback.event(object);
            }
        }
        available.clear();
        used.clear();
    }

    /**
     * Add a shutdown callback. These callbacks will be fired on each object in
     * the pool when the pool is shut down, whether the object is in use or not.
     * @param poolEventCallback Callback to fire on shutdown.
     */
    synchronized public void addShutdownCallback(PoolEventCallback<T> poolEventCallback) {
        shutdownCallbacks.add(poolEventCallback);
    }

    private Collection<T> getAllObjects() {
        Collection<T> objects = new ArrayList<T>();
        objects.addAll(available);
        objects.addAll(used);
        return objects;
    }

    /**
     * Adds a callback that will be fired at the given interval. A callback will
     * be made using each applicable object in the pool. If the callback returns
     * <b>false</b>, the object will be removed from the pool.
     * @param callback The callback to fire.
     * @param interval Time between calls, in milliseconds.
     * @param allObjects If <b>true</b>, the callback will be fired for each
     * object in the pool. If <b>false</b>, the callback will be fired only for
     * the available objects.
     */
    public void addTimerCallback(PoolEventCallback<T> callback, long interval, boolean allObjects) {
        getTimer().schedule(new PeriodicEvent(callback, allObjects), interval, interval);
    }

    private synchronized Timer getTimer() {
        if (timer == null) {
            timer = new Timer(true);
        }
        return timer;
    }

    class PeriodicEvent extends TimerTask {

        private final boolean allObjects;
        private final PoolEventCallback<T> callback;

        public PeriodicEvent(PoolEventCallback<T> callback, boolean allObjects) {
            this.allObjects = allObjects;
            this.callback = callback;
        }

        /**
         * Runs the periodic callback on the objects in the pool. If the
         * callback returns <b>false</b>, it is removed from the pool.
         */
        @Override
        public void run() {
            synchronized (GenericPool.this) {
                Collection<T> objects;
                if (allObjects) {
                    objects = getAllObjects();
                } else {
                    objects = new ArrayList<T>();
                    objects.addAll(available);
                }
                for (T object : objects) {
                    if (!callback.event(object)) {
                        removeObject(object);
                    }
                }
            }
        }
    }
}
