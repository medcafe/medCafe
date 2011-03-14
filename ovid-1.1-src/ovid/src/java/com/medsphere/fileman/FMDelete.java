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

public class FMDelete implements FMStatement {

    protected final ResAdapter adapter;
    protected FMRecord entry;

    public FMDelete(ResAdapter adapter) {
        this.adapter = adapter;
    }

    public void setEntry(FMRecord entry) {
        this.entry = entry;
    }

    public FMResultSet execute() throws ResException {
        Resource insertRes = new Resource("FILEMAN");
        insertRes.addCompound("DELETE");
        insertRes.addProperty("FILE", entry.getFileName());
        insertRes.addProperty("IENS", entry.getIENS());
        insertRes.endCompound(); // DELETE
        adapter.writeResource( insertRes );
        Resource resultRes = adapter.readResource();
        ResWalker walker = new ResWalker(resultRes);
        while( walker.nextProperty() ) {
            String prop = walker.getProperty();
            if (prop.equals("RESULTS")) {
                return processResults(walker);
            } else {
                walker.skip();
            }
        }
        return null;
    }

    private FMResultSet processResults(ResWalker walker) {
        FMResultSet results = new FMResultSet();
        results.setColumns( new String[]{"ERROR NUMBER", "ERROR TEXT"} );
        boolean hasErrors = false;
        while( walker.nextProperty() ) {
            String prop = walker.getProperty();
            if (prop.equals("ERROR")) {
                String row[] = new String[2];
                while( walker.nextProperty() ) {
                    String fieldProp = walker.getProperty();
                    if (fieldProp.equals("ERROR NUMBER")) {
                        row[0] = walker.getValue() ;
                    } else if (fieldProp.equals("ERROR TEXT")) {
                        row[1] = walker.getValue();
                        results.setError(row[1]);
                    } else {
                        walker.skip();
                    }
                }
                results.addRow( row );
                hasErrors = true;
            } else {
                walker.skip();
            }
        }
        if (!hasErrors) {
            entry.setIEN( null );
        }
        return results;
    }
}
