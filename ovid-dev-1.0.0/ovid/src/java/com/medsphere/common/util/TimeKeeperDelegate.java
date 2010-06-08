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

package com.medsphere.common.util;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TimeKeeperDelegate {
    private static Logger logger = Logger.getLogger(TimeKeeperDelegate.class);
    private static Level logLevel = Level.DEBUG;
    private static TimeKeeper tk = new TimeKeeper();

    public static void setLogLevel(Level level) {
        logLevel = level;
    }

    public static void start(String tag) {
        if (canLog()) {
            try {
                tk.start(tag);
            } catch (TimeKeeperException e) {
                logger.debug(e);
                tk.clear(tag);
            }
        }
    }

    public static void stop(String tag) {
        if (canLog()) {
            try {
                tk.stop(tag);
            } catch (TimeKeeperException e) {
                logger.debug(e);
                tk.clear(tag);
            }
        }
    }

    public static void clear(String tag) {
        tk.clear(tag);
    }

    public static long getElapsedTime(String tag) {
        try {
            if (canLog()) {
                return tk.getElapsedTime(tag);
            }
        } catch (TimeKeeperException e) {
            logger.debug(e);
            tk.clear(tag);
        }
        return -1L;
    }

    public static String getDisplayString(String tag) {
        try {
            if (canLog()) {
                return tk.getDisplayString(tag);
            }
        } catch (TimeKeeperException e) {
            logger.debug(e);
            TimeKeeperDelegate.clear(tag);
        }
        return "Error in TimeKeeper";
    }

    public static void stopAndLog(String tag, Level level) {
        try {
            if (canLog()) {
                tk.stop(tag);
                if (level != Level.OFF) {
                    logger.log(level, tk.getDisplayString(tag));
                }
            }
        } catch (TimeKeeperException e) {
            tk.clear(tag);
        }
    }

    public static void log(String msg) {
        if (canLog()) {
            logger.log(logLevel, msg);
        }
    }

    public static void stopAndLog(String tag) {
        stopAndLog(tag, logLevel);
    }

    private static boolean canLog() {
        return logLevel != Level.OFF;
    }

    @Test public void testDelegate() throws InterruptedException {
        TimeKeeperDelegate.start("TestDelegate");
        Thread.sleep(100);
        TimeKeeperDelegate.stop("TestDelegate");
        System.out.println(TimeKeeperDelegate.getDisplayString("TestDelegate"));
    }

    @Test public void testLogging() throws InterruptedException {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.DEBUG);
        TimeKeeperDelegate.setLogLevel(Level.DEBUG);
        String tag = "logging test";
        TimeKeeperDelegate.start(tag);
        Thread.sleep(100);
        TimeKeeperDelegate.stopAndLog(tag, Level.DEBUG); // this shouldn't display if level is INFO
        TimeKeeperDelegate.stopAndLog(tag); // this should display whatever the level is, unless OFF

    }

    @Test public void testNoOpLogging() {
        TimeKeeperDelegate.setLogLevel(Level.DEBUG);
        Assert.assertTrue(TimeKeeperDelegate.canLog());
        TimeKeeperDelegate.setLogLevel(Level.OFF);
        Assert.assertFalse(TimeKeeperDelegate.canLog());
    }
}


