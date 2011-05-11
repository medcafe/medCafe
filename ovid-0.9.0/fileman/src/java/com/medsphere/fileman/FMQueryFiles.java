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

import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.resource.ResWalker;
import com.medsphere.resource.Resource;

public class FMQueryFiles implements FMStatement {
    private ResAdapter adapter;
    final static String[] colNames = new String[]{"NAME", "NUMBER", "PARENT" };

    public FMQueryFiles( ResAdapter adapter ) {
        this.adapter = adapter;
    }

    public FMResultSet execute() throws ResException {
        Resource infoRes = new Resource("FILEMAN");
        infoRes.addCompound("FILES");
        infoRes.endCompound(); // FILES
        adapter.writeResource( infoRes );
        Resource resultRes = adapter.readResource();
        FMResultSet results = new FMResultSet();
        results.setColumns( colNames );
        if (resultRes!=null) {
            ResWalker walker = new ResWalker(resultRes);
            while( walker.nextProperty() ) {
                if (walker.getProperty().equals("RESULTS")) {
                    while( walker.nextProperty() ) {
                        String prop = walker.getProperty();
                        if (prop.equals("R")) {
                            processRow( walker, results );
                        } else {
                            walker.skip();
                        }
                    }
                } else {
                    walker.skip();
                }
            }
        }
        return results;
    }

    private void processRow(ResWalker walker, FMResultSet results) {
        String[] values = new String[3];
        while( walker.nextProperty() ) {
            String prop = walker.getProperty();
            if (prop.equals("T")) {
                values[0] = walker.getValue();
            } else if (prop.equals("N")) {
                values[1] = walker.getValue();
            } else if (prop.equals("P")) {
                values[2] = walker.getValue();
            } else {
                walker.skip();
            }
        }
        results.addRow( values );
    }
}
