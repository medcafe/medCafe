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

import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.resource.ResWalker;
import com.medsphere.resource.Resource;

public class FMQueryRefBy implements FMStatement {
    protected final ResAdapter adapter;
    protected final String fileName;
    final private String[] colNames = new String[]{ "FILE", "FILE#", "FIELD", "FIELD#" };

    public FMQueryRefBy(ResAdapter sourceAdapter, String fileNum) {
        adapter = sourceAdapter;
        this.fileName = fileNum;
    }

    public FMResultSet execute() throws ResException {
        FMResultSet retVal = new FMResultSet();
        retVal.setColumns( colNames );
        Resource res = new Resource("FILEMAN");
        res.addCompound("REFBY");
        res.addProperty("FILE", fileName);
        res.endCompound(); //REFBY;
        adapter.writeResource( res );
        Resource resultRes = adapter.readResource();
        if (resultRes!=null) {
            ResWalker walker = new ResWalker(resultRes);
            while (walker.nextProperty()) {
                String prop = walker.getProperty();
                if (prop.equals("R")) {
                    processRow(walker, retVal);
                } else {
                    walker.skip();
                }
            }
        }
        return retVal;
    }

    private void processRow(ResWalker walker, FMResultSet retVal) {
        String[] newRow = new String[4];
        while (walker.nextProperty()) {
            String prop = walker.getProperty();
            if (prop.equals("F")) {
                newRow[0] = walker.getValue();
            } else if (prop.equals("F#")) {
                newRow[1] = walker.getValue();
            } else if (prop.equals("C")) {
                newRow[2] = walker.getValue();
            } else if (prop.equals("C#")) {
                newRow[3] = walker.getValue();
            } else {
                walker.skip();
            }
        }
        retVal.addRow(newRow);
    }

}
