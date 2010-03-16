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
package com.medsphere.fileman;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FMUtil {

    public static final String FM_RPC_NAME = "MSC FILEMAN RESOURCE";
    public static final String FM_RPC_CONTEXT = "MSC FM RESOURCE USER";
    private static final Pattern fmDatePattern = Pattern.compile( "(\\d{3})(\\d{2})(\\d{2})\\.?(\\d{2})?(\\d{2})?(\\d{1,2})?");

    static public Date fmDateToDate( String FMDate ) throws ParseException {
        Matcher m = fmDatePattern.matcher(FMDate);
        if (!m.matches()) {
            throw new ParseException( "Not a Fileman date: " + FMDate, 0);
        }
        Calendar cal = Calendar.getInstance();
        Integer year = new Integer( m.group( 1 ) ) + 1700;
        Integer month = new Integer( m.group( 2 ) ) - 1;
        Integer day = new Integer( m.group( 3 ) );
        if (m.group( 4 )==null) {
            cal.set(year, month, day);
        } else {
            Integer hour = new Integer( m.group( 4 ) );
            Integer minute = ((m.group( 5 )==null) ? 0 : new Integer( m.group( 5 ) ));
            Integer second = ((m.group( 6 )==null) ? 0 : new Integer( m.group( 6 ) ));
            cal.set(year, month, day, hour, minute, second);
        }

        return cal.getTime();
    }

    static public String dateToFMDate( Date date ) {
        Calendar cal = Calendar.getInstance();
        cal.setTime( date );
        int year = cal.get( Calendar.YEAR ) - 1700;
        int month = cal.get( Calendar.MONTH ) + 1;
        int day = cal.get( Calendar.DAY_OF_MONTH );
        int hour = cal.get( Calendar.HOUR_OF_DAY );
        int minute = cal.get( Calendar.MINUTE );
        int second = cal.get( Calendar.SECOND );
        int datePart = year*10000 + month*100 + day;
        StringBuffer result = new StringBuffer( Integer.toString(datePart) );
        if (hour+minute+second!=0) {
            result.append(".");
            if (hour<10) {
                result.append("0");
            }
            result.append(Integer.toString(hour));
            if (minute+second!=0) {
                if (minute<10) {
                    result.append("0");
                }
                result.append(Integer.toString(minute));
                if (second!=0) {
                    if (second<10) {
                        result.append("0");
                    }
                    result.append(Integer.toString(second));
                }
            }
        }
        return result.toString();
    }
}
