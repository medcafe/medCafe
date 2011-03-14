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

import java.util.Set;

import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.resource.ResNode;
import com.medsphere.resource.ResWalker;
import com.medsphere.resource.Resource;

public class FMUpdate implements FMStatement {

    protected final ResAdapter adapter;
    protected FMRecord entry;

    public FMUpdate(ResAdapter adapter) {
        this.adapter = adapter;
    }

    public void setEntry(FMRecord entry) {
        this.entry = entry;
    }

    public FMResultSet execute() throws ResException {
        if (entry.getModifiedFields().isEmpty()) {
            return new FMResultSet();
        }
        Resource updateRes = new Resource("FILEMAN");
        ResNode updateNode = updateRes.addCompound("UPDATE");
        updateNode.addChild("FILE", entry.getFileName());
        updateNode.addChild("IENS", entry.getIENS());
        Set<FMField> fields = entry.getFields();
        for (String field : entry.getModifiedFields() ) {
            ResNode fieldNode = updateNode.addChild("FIELD");
            fieldNode.addChild("NAME", field);
            fieldNode.addChild("VALUE", entry.getValue( field ));
            for (FMField entryField : fields) { // This is O(N^2). Perhaps fields could be a hash.
                if (entryField.getName().equals(field)) {
                    if (entryField.useInternal()) {
                        fieldNode.addChild("INTERNAL", "1");
                    }
                    break;
                }
            }
        }
        adapter.writeResource( updateRes );
        Resource resultRes = adapter.readResource();
        if (resultRes!=null) {
            ResWalker walker = new ResWalker(resultRes);
            while( walker.nextProperty() ) {
                String prop = walker.getProperty();
                if (prop.equals("RESULTS")) {
                    return processResults(walker);
                } else if (prop.equals("ERRORS")) {
                    return processError(walker);
                } else {
                    walker.skip();
                }
            }
        }
        return null;
    }

    private FMResultSet processResults(ResWalker walker) {
        return new FMResultSet(); // It should be empty if no error
    }

    private FMResultSet processError(ResWalker walker) {
        FMResultSet results = new FMResultSet();
        results.setColumns( new String[]{"ERROR NUMBER", "ERROR TEXT"} );
        boolean hasErrors = false;
        while( walker.nextProperty() ) {
            String prop = walker.getProperty();
            if (prop.equals("ERROR")) {
                hasErrors = true;
                String row[] = new String[2];
                while( walker.nextProperty() ) {
                    String fieldProp = walker.getProperty();
                    if (fieldProp.equals("NUMBER")) {
                        row[0] = walker.getValue() ;
                    } else if (fieldProp.equals("TEXT")) {
                        row[1] = walker.getValue();
                        results.setError(row[1]);
                    } else {
                        walker.skip();
                    }
                }
                results.addRow( row );
            } else {
                walker.skip();
            }
        }
        if (!hasErrors) {
            entry.clearModifiedFields();
        }
        return results;
    }
}
