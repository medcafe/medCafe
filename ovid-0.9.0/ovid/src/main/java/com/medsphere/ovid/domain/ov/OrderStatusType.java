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

package com.medsphere.ovid.domain.ov;

import java.util.HashMap;
import java.util.Map;

public enum OrderStatusType {

    DISCONTINUED("DISCONTINUED"),
    COMPLETE("COMPLETE"),
    HOLD("HOLD"),
    FLAGGED("FLAGGED"),
    PENDING("PENDING"),
    ACTIVE("ACTIVE"),
    EXPIRED("EXPIRED"),
    SCHEDULED("SCHEDULED"),
    PARTIAL_RESULTS("PARTIAL RESULTS"),
    DELAYED("DELAYED"),
    UNRELEASED("UNRELEASED"),
    DISCONTINUED_EDI("DISCONTINUED/EDI"),
    CANCELLED("CANCELLED"),
    LAPSED("LAPSED"),
    RENEWED("RENEWED"),
    NO_STATUS("NO STATUS"),
    PRINTED("PRINTED"),
    ;
    
    private final String displayString;

    OrderStatusType(String type) {
        this.displayString = type;
    }
    
    @Override
    public String toString() {
        return displayString;
    }
    
    /**
     * here is where you map incoming type strings to known types.  For example,
     * if "char*" needs to be mapped to a STRING, then add it to the list of
     * STRING datatype mappings.  Note that mappings are caseless.
     */
    private static Map<OrderStatusType, String[]> typeMap = new HashMap<OrderStatusType, String[]>()
    {
        {
	    put(OrderStatusType.DISCONTINUED, new String[] {"discontinued", "dc", "dc"});
	    put(OrderStatusType.COMPLETE, new String[] {"complete", "comp", "c"});
	    put(OrderStatusType.HOLD, new String[] {"hold", "hold", "h"});
	    put(OrderStatusType.FLAGGED, new String[] {"flagged", "flag", "?"});
	    put(OrderStatusType.PENDING, new String[] {"pending", "pend", "p"});
	    put(OrderStatusType.ACTIVE, new String[] {"active", "actv", "a"});
	    put(OrderStatusType.EXPIRED, new String[] {"expired", "exp", "e"});
	    put(OrderStatusType.SCHEDULED, new String[] {"scheduled", "schd", "s"});
	    put(OrderStatusType.PARTIAL_RESULTS, new String[] {"partial results", "part", "pr"});
	    put(OrderStatusType.DELAYED, new String[] {"delayed", "dlay", "dly"});
	    put(OrderStatusType.UNRELEASED, new String[] {"unreleased", "unr", "u"});
	    put(OrderStatusType.DISCONTINUED_EDI, new String[] {"discontinued/edi", "dc/e", "dce"});
	    put(OrderStatusType.CANCELLED, new String[] {"cancelled", "canc", "x"});
	    put(OrderStatusType.LAPSED, new String[] {"lapsed", "laps", "l"});
	    put(OrderStatusType.RENEWED, new String[] {"renewed", "rnew", "rn"});
	    put(OrderStatusType.NO_STATUS, new String[] {"no status", "none"});
	    put(OrderStatusType.PRINTED, new String[] {"printed", "prnt", "pt"});
        }

    };
    
    public static OrderStatusType fromString(String type) {
        
        OrderStatusType returnType = NO_STATUS;
        
        for (OrderStatusType key : typeMap.keySet()) {
            for (String value : typeMap.get(key)) {
                if (value.equalsIgnoreCase(type)) {
                    returnType = key;
                    break;
                }
            }
        }
        
        return returnType;
    }

}
