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

package com.medsphere.fileman.test.unit;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.medsphere.fileman.FMUtil;


/**
 *
 * @author Pete Johanson <pete.johanson@medsphere.com>
 */
@Test(groups = { "unit" })
public class FMUtilTestSuite {

    // Sets of valid data
    @DataProvider(name = "validDateData")
    public Iterator<Object[]> getValidDateData() {
        ArrayList<Object[]> formats = new ArrayList<Object[]>();

        // Date only
        formats.add(new Object[] { "3071221", getDate(2007, 11, 21) });
        // Another date only
        formats.add(new Object[] { "3100208", getDate(2010, 1, 8) });
        // Leap year
        formats.add(new Object[] { "3080229", getDate(2008, 1, 29) });
        // Date and hour only
        formats.add(new Object[] { "3100208.12", getDate(2010, 1, 8, 12, 0, 0) });
        // Date with hours and minutes
        formats.add(new Object[] { "3100208.1212", getDate(2010, 1, 8, 12, 12, 0) });
        // Date with hours, minutes, and seconds
        formats.add(new Object[] { "3100208.121212", getDate(2010, 1, 8, 12, 12, 12) });
        // Time with hour with trailing 0 dropped
        formats.add(new Object[] { "3100208.1", getDate(2010, 1, 8, 10, 0, 0) });
        // Time with minutes with trailing 0 dropped
        formats.add(new Object[] { "3100208.101", getDate(2010, 1, 8, 10, 10, 0) });
        // Time with seconds with trailing 0 dropped
        formats.add(new Object[] { "3100208.10101", getDate(2010, 1, 8, 10, 10, 10) });

        return formats.iterator();
    }

    // The FM String format is invalid and should throw an exception
    @DataProvider(name = "invalidFMString")
    public Iterator<Object[]> getInvalidFMString() {
        ArrayList<Object[]> formats = new ArrayList<Object[]>();

        // Year only
        formats.add(new Object[] { "307", getDate(2007, 0, 1) });
        // Too large
        formats.add(new Object[] { "9100208", getDate(2610, 1, 8) });
        // Too small
        formats.add(new Object[] { "0010229", getDate(1701, 1, 29) });
        // Not a number
        formats.add(new Object[] { "3O7I22I", getDate(1701, 1, 29) });

        return formats.iterator();
    }

    // The dates are out of FileMan range and should throw an exception
    @DataProvider(name = "invalidDate")
    public Iterator<Object[]> getInvalidDate() {
        ArrayList<Object[]> formats = new ArrayList<Object[]>();

        // Too large
        formats.add(new Object[] { "9100208", getDate(2610, 1, 8) });
        // Too small
        formats.add(new Object[] { "0010229", getDate(1701, 1, 29) });

        return formats.iterator();
    }

    @Test(dataProvider = "validDateData")
    public void testFMDateToDate (String fmDate, Date date) throws ParseException {
        Assert.assertEquals(FMUtil.fmDateToDate(fmDate), date);
    }

    @Test(dataProvider = "validDateData")
    public void testDateToFMDate (String fmDate, Date date) throws ParseException {
        Assert.assertEquals(FMUtil.dateToFMDate(date), fmDate);
    }

    @Test(dataProvider = "invalidFMString", expectedExceptions=ParseException.class)
    public void testInvalidFMDateToDate (String fmDate, Date date) throws ParseException {
        Assert.assertEquals(FMUtil.fmDateToDate(fmDate), date);
    }

    @Test(dataProvider = "invalidDate", expectedExceptions=IllegalArgumentException.class)
    public void testInvalidDateToFMDate (String fmDate, Date date) throws ParseException {
        Assert.assertEquals(FMUtil.dateToFMDate(date), fmDate);
    }

    private Date getDate (int year, int month, int day) {
        return getDate(year, month, day, 0, 0, 0);
    }

    private Date getDate (int year, int month, int day, int hour, int minute, int second) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);

        cal.set(year, month, day, hour, minute, second);

        return cal.getTime();
    }
}
