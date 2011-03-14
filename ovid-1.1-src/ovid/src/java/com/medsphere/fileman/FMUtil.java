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

package com.medsphere.fileman;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class FMUtil {
	 
	 /* We are using the older version of the OpenVista virtual machine so we need these entries */
	// public static final String FM_RPC_NAME = "MSC FILEMAN RESOURCE";
   // public static final String FM_RPC_CONTEXT = "MSC FM RESOURCE USER";
    /* if using the newer version of the virtual machine, change to these entries */
    public static final String FM_RPC_NAME = "MSCVBFM ENTRY";
    public static final String FM_RPC_CONTEXT = "MSCV FM RESOURCE USER";
    private static DecimalFormat fmDatePartFormat = new DecimalFormat("0000000");

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
        if (datePart<1410102.0 || datePart>4141015.235959) {
            throw new IllegalArgumentException("Date out of range. Must be between 1410102.0 and 4141015.235959");
        }
        StringBuilder result = new StringBuilder( fmDatePartFormat.format(datePart) );
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
        return makeCanonical( result.toString() );
    }

    static public String makeCanonical(String str) {
        try {
            Double d = Double.parseDouble(str);
            if (d==-0.0) {
                d=0.0;
            }
            boolean isNeg = (d<0);
            if (isNeg) {
                d = -d;
            }
            String retVal = d.toString();
            if (retVal.endsWith(".0")) {
                retVal = retVal.substring(0, retVal.length() - 2);
            } else if (retVal.startsWith(("0."))) {
                retVal = retVal.substring(1);
            }
            if (isNeg) {
                retVal = "-" + retVal;
            }
            return retVal;
        } catch (NumberFormatException ex) {
            return str;
        }
    }

    static public Date fmDateToDate( String FMDate ) throws ParseException {
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MILLISECOND, 0);
            int year = 0, month = 0, day = 0, hour = 0, minute = 0, second = 0;

            String[] parts = FMDate.split("\\.");
            String dateString = parts[0];
            double dateNum = Double.parseDouble(dateString);
            if (dateNum<1410102.0 || dateNum>4141015.235959) {
                throw new ParseException("Date out of range. Must be between 1410102.0 and 4141015.235959", 0);
            }
            year = 1700 + Integer.parseInt(dateString.substring(0, 3));
            month = Integer.parseInt(dateString.substring(3, 5));
            if (month>0) {
                --month;
            }
            day = Integer.parseInt(dateString.substring(5, 7));
            if (day==0) {
                day = 1;
            }

            if (parts.length>1) {
                String timeString = parts[1] + "000000";
                hour = Integer.parseInt(timeString.substring(0, 2));
                minute = Integer.parseInt(timeString.substring(2, 4));
                second = Integer.parseInt(timeString.substring(4, 6));
            }

            cal.set(year, month, day, hour, minute, second);
            return cal.getTime();
        } catch (NumberFormatException ex) {
            throw new ParseException( ex.getMessage(), 0 );
        }
    }

}
