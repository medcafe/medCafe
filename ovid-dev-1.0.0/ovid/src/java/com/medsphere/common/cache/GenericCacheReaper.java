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

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;

/**
 * This is a singleton object that accepts GenericCaches which implement
 * IsAGenericReapableCache and maintains them on a thread safe list.
 * <p>
 * Upon instantiation, a thread is spawned that analyzes the caches on the
 * list and determines the proper time to sleep from using the getShelfLife()
 * method of each cache.  The smallest time wins, as long as it is not below
 * the minimum.  Likewise, if the shortest shelf life is longer than a preset
 * maximum, the maximum is used.
 * <p>
 * When the thread awakens, it iterates through the cache list and invokes the
 * removeExpiredEntries() method.  Effectively, cleaning the caches of expired
 * entries.
 * <p>
 * New caches can be added or removed from the reaper by calling the add or
 * removeCache methods as in:
 * <p>
 * <i>GenericCacheReaper.getInstance().addCache(anotherGenericCache);</i>
 */
public class GenericCacheReaper {

    private ConcurrentSkipListSet<GenericCache> list = null;
    private Logger logger = Logger.getLogger(GenericCacheReaper.class);
    private static GenericCacheReaper instance = null;
    private static boolean running = true;
    private static long minimumWaitTime = (60L * 1000L); // don't wait less than a minute between checks
    private static long maximumWaitTime = (5L * 60L * 1000L); // don't wait longer than 5 minutes between checks
    private static ExecutorService executor = null; //Executors.newSingleThreadExecutor();

    private GenericCacheReaper() {
        list = new ConcurrentSkipListSet<GenericCache>();
        //executor.submit(new Reaper());
    }

    public static GenericCacheReaper getInstance() {
        if (instance == null) {
            instance = new GenericCacheReaper();
        }
        return instance;
    }

    public void addCache(GenericCache cache) {
        if (executor==null) {
            executor = Executors.newSingleThreadExecutor();
            executor.submit(new Reaper());
        }
        list.add(cache);
    }

    public void removeCache(GenericCache cache) {
        if (list.contains(cache)) {
            list.remove(cache);
            if (list.size()==0) {
                executor.shutdownNow();
                executor = null;
            }
        }
    }

    /**
     * iterate through the list of caches to determine which cache has the
     * lowest shelf life and use that as the amount of time to sleep.
     * minimum and maximum waitTime keeps the wait time from being too
     * ridiculous.
     * @return
     */
    private long determineWaitTime() {
        long waitTime = maximumWaitTime;

        for (GenericCache cache : list.descendingSet()) {
            long shelfLife = cache.getShelfLifeInMilliseconds();
            if (shelfLife < waitTime) {
                if (shelfLife >= minimumWaitTime) {
                    waitTime = shelfLife;
                }
            }
        }

        logger.debug("determined the waitTime to be " + waitTime);
        return waitTime;
    }

    private class Reaper implements Callable<Object> {

        public Object call() throws Exception {
            while (running) {
                long waitTime = determineWaitTime();
                Thread.sleep(waitTime);
                logger.debug("reaping caches");
                for (GenericCache cache : list.descendingSet()) {
                    cache.removeExpiredEntries();
                }
                logger.debug("caches reaped");
            }
            return null;
        }

    }
}
