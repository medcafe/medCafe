// <editor-fold defaultstate="collapsed">
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
// </editor-fold>

package com.medsphere.common.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.testng.annotations.Test;

public class TimeKeeper {
    private Map<String, TimerInfo> timerMap = new ConcurrentHashMap<String, TimerInfo>();

    public void start(String tag) throws TimeKeeperException {
        if (timerMap.containsKey(tag)) {
            TimerInfo ti = timerMap.get(tag);
            if (ti.getStartTime() > 0 && ti.getEndTime() > 0) {
                timerMap.remove(tag);
            } else {
                throw new TimeKeeperException(tag + " is already started");
            }
        }

        timerMap.put(tag, new TimeKeeper().new TimerInfo(System.currentTimeMillis()));

    }

    public void stop(String tag) throws TimeKeeperException {
        if (timerMap.containsKey(tag)) {
            TimerInfo ti = timerMap.get(tag);
            if (ti.getStartTime() > 0) {
                timerMap.get(tag).setEndTime(System.currentTimeMillis());
            } else {
                throw new TimeKeeperException(tag + " has never been started");
            }
        } else {
            throw new TimeKeeperException(tag + " has never been started");
        }
    }

    public long getElapsedTime(String tag) throws TimeKeeperException {
        TimerInfo ti = timerMap.get(tag);
        if (ti != null) {
            if (ti.getStartTime() > 0 && ti.getEndTime() > 0) {
                if (ti.getStartTime() <= ti.getEndTime()) {
                    return ti.getEndTime() - ti.getStartTime();
                } else {
                    throw new TimeKeeperException("The end time for " + tag + " is less than the start time");
                }
            } else {
                throw new TimeKeeperException(tag + " has not been properly started or stopped.  Start time: " + ti.getStartTime() + ", End time: " + ti.getEndTime());
            }
        } else {
            throw new TimeKeeperException("There is no timer cataloged for " + tag);
        }
    }

    public String getDisplayString(String tag) throws TimeKeeperException {
        long elapsed = getElapsedTime(tag);
        String displayString = "[" + tag + "] took " + elapsed + " milliseconds";
        displayString += " (" + new Double(elapsed)/1000. + " seconds)";
        return displayString;
    }

    public String getDisplayStringWithTimeFirst(String tag) throws TimeKeeperException {
        long elapsed = getElapsedTime(tag);

        String displayString = elapsed + " milliseconds (" + new Double(elapsed)/1000. + " seconds)";
        displayString += " for [" + tag + "]";
        return displayString;
    }

    public void clear(String tag) {
        if (timerMap.containsKey(tag)) {
            timerMap.remove(tag);
        }
    }
    private class TimerInfo {
        private long startTime= 0;
        private long endTime = 0;

        public TimerInfo(long startTime) {
            this.startTime = startTime;
        }
        public long getEndTime() {
            return endTime;
        }
        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }
        public long getStartTime() {
            return startTime;
        }
        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }
    }


     @Test public void simpleTimer() throws InterruptedException, TimeKeeperException {
        TimeKeeper tk = new TimeKeeper();
        tk.start("Test 1");
        Thread.sleep(100);
        tk.stop("Test 1");
        System.out.println(tk.getDisplayString("Test 1"));
    }

     @Test public void multipleTimers() throws InterruptedException, TimeKeeperException {
        TimeKeeper tk = new TimeKeeper();
        tk.start("Test 1");
        Thread.sleep(100);
        tk.start("Test 2");
        Thread.sleep(100);
        tk.start("Test 3");
        Thread.sleep(100);
        tk.stop("Test 1");
        tk.stop("Test 2");
        tk.stop("Test 3");

        System.out.println(tk.getDisplayString("Test 1"));
        System.out.println(tk.getDisplayString("Test 2"));
        System.out.println(tk.getDisplayString("Test 3"));
    }

    @Test(expectedExceptions={TimeKeeperException.class}) public void makeSureThatATimeStartedTwiceWithoutStoppingThrowsAnException() throws InterruptedException, TimeKeeperException {
        TimeKeeper tk = new TimeKeeper();
        try {
            tk.start("Test 1");
            tk.start("Test 1");
        } finally {
            tk.clear("Test 1");
        }
    }

    @Test(expectedExceptions={TimeKeeperException.class}) public void makeSureThatATimerStoppedWithoutBeingStartedThrowsAnException() throws InterruptedException, TimeKeeperException {
        TimeKeeper tk = new TimeKeeper();
        tk.stop(",Thief");
    }


    @Test(expectedExceptions={TimeKeeperException.class}) public void makeSureThatRequestingElaspedTimeForATimerThatHasNotBeenStartedThrowsAnException() throws InterruptedException, TimeKeeperException {
        TimeKeeper tk = new TimeKeeper();
        tk.getElapsedTime("makeSureThatRequestingElaspedTimeForATimerThatHasNotBeenStartedThrowsAnException");
    }

    @Test(expectedExceptions={TimeKeeperException.class}) public void makeSureThatRequestingDisplayStringTimeForATimerThatHasNotBeenStartedThrowsAnException() throws InterruptedException, TimeKeeperException {
        TimeKeeper tk = new TimeKeeper();
        tk.getDisplayString("makeSureThatRequestingDisplayStringTimeForATimerThatHasNotBeenStartedThrowsAnException");
    }

}
