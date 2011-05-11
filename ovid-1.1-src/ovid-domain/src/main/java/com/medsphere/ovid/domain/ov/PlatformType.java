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
package com.medsphere.ovid.domain.ov;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public enum PlatformType {
    VISTA("VistA"),
    WORLDVISTA("WorldVista"),
    RPMS("RPMS"),
    OPENVISTA("OpenVista"),
    UNKNOWN("Unknown");

    private final String displayString;

    PlatformType(String type) {
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
    private static Map<PlatformType, String[]> typeMap = new HashMap<PlatformType, String[]>() {

        {
            put(PlatformType.VISTA, new String[]{"vista", "v", "va"});
            put(PlatformType.OPENVISTA, new String[]{"openvista", "ov", "medsphere"});
            put(PlatformType.WORLDVISTA, new String[]{"worldvista", "wv"});
            put(PlatformType.RPMS, new String[]{"ihs", "rpms", "ehr"});
        }
    };

    public static PlatformType fromString(String type) {

        PlatformType returnType = UNKNOWN;

        for (PlatformType key : typeMap.keySet()) {
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
