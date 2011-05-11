
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
    @DataProvider(name = "dateFormatsToParse")
    public Iterator<Object[]> getDateFormatsToParse() {
        ArrayList<Object[]> formats = new ArrayList<Object[]>();

        // Year only
        formats.add(new Object[] { "307", getDate(2007, 0, 1) });
        // Year and month only
        formats.add(new Object[] { "30712", getDate(2007, 11, 1) });
        // Date only
        formats.add(new Object[] { "3071221", getDate(2007, 11, 21) });
        // "Beginning" of time
        formats.add(new Object[] { "0000101", getDate(1700, 0, 1) });
        // "End" of time
        formats.add(new Object[] { "9991231.235959", getDate(2699, 11, 31, 23, 59, 59) });
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

    // Sadly, these cannot be the same data providers, as FMUtil
    // currently does not "abbreviate" if there's trailing zeros that could
    // be dropped, etc.
    @DataProvider(name = "datesToFormat")
    public Iterator<Object[]> getDatesToFormat() {
        ArrayList<Object[]> formats = new ArrayList<Object[]>();

        // Year only
        formats.add(new Object[] { "3070101", getDate(2007, 0, 1) });
        // Year and month only
        formats.add(new Object[] { "3071201", getDate(2007, 11, 1) });
        // Date only
        formats.add(new Object[] { "3071221", getDate(2007, 11, 21) });
        // "Beginning" of time
        formats.add(new Object[] { "0000101", getDate(1700, 0, 1) });
        // "End" of time
        formats.add(new Object[] { "9991231.235959", getDate(2699, 11, 31, 23, 59, 59) });
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
        formats.add(new Object[] { "3100208.10", getDate(2010, 1, 8, 10, 0, 0) });
        // Time with minutes with trailing 0 dropped
        formats.add(new Object[] { "3100208.1010", getDate(2010, 1, 8, 10, 10, 0) });
        // Time with seconds with trailing 0 dropped
        formats.add(new Object[] { "3100208.101010", getDate(2010, 1, 8, 10, 10, 10) });

        return formats.iterator();
    }



    @Test(dataProvider = "dateFormatsToParse")
    public void testFMDateToDate (String fmDate, Date date) throws ParseException {
        Assert.assertEquals(FMUtil.fmDateToDate(fmDate), date);
    }

    @Test(dataProvider = "datesToFormat")
    public void testDateToFMDate (String fmDate, Date date) throws ParseException {
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
