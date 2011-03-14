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

import com.medsphere.resource.Resource;

public class FMScreenIsNull implements FMScreen {

    private final String field;
    private final boolean isNull;

    public FMScreenIsNull(String field, boolean isNull) {
       this.field = field;
       this.isNull = isNull;
    }

    @Override
    public void writeResource(Resource res) {
        res.addCompound("ISNULL");
        {
            res.addProperty("FIELD", field);
            res.addProperty("BOOL", isNull ? "1" : "0");
        }
        res.endCompound(); // ISNULL
    }

}
