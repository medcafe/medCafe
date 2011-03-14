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

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;

public interface IsAVitalSign {

    public String getIEN();
    public String getType();
    public String getName();
    public String getValue();
    public String getUnits();
    public String getReferenceRange();
    public Collection<String> getQualifiers();
    public Date getDateEntered();
    public Date getDateTaken();
    public String getVisit();

    public class SortByDate implements Comparator<IsAVitalSign> {

        @Override
        public int compare(IsAVitalSign arg0, IsAVitalSign arg1) {
            if (arg0.getDateEntered().before(arg1.getDateEntered())) {
                return -1;
            } else if (arg0.equals(arg1)) {
                return 0;
            } else {
                return 1;
            }
        }
    }

}
