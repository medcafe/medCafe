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

public class FMInsert implements FMStatement {

    protected final ResAdapter adapter;
    protected FMRecord entry;

    public FMInsert(ResAdapter adapter) {
        this.adapter = adapter;
    }

    public FMResultSet execute() throws ResException {
        Resource insertRes = new Resource("FILEMAN");
        insertRes.addCompound("INSERT");
        insertRes.addProperty("FILE", entry.getFileName());
        String IENS = entry.getIENS();

        for (FMField field : entry.getFields()) {
            String value = entry.getValue( field.getName() );
            if (value==null) {
                continue;
            }
            insertRes.addCompound("FIELD");
            insertRes.addProperty("NAME", field.getName());
            insertRes.addProperty("VALUE", value);
            if (IENS!=null) {
                insertRes.addProperty("IENS", IENS);
            }
            if (field.useInternal()) {
                insertRes.addProperty("INTERNAL", "1");
            }

            insertRes.endCompound(); // FIELD
        }
        insertRes.endCompound(); // INSERT
        adapter.writeResource( insertRes );
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

    private FMResultSet processError(ResWalker walker) {
        FMResultSet results = new FMResultSet();
        results.setColumns( new String[]{"FIELD","ERROR"} );
        while( walker.nextProperty() ) {
            String prop = walker.getProperty();
            if (prop.equals("ERROR")) {
                String row[] = new String[2];
                while( walker.nextProperty() ) {
                    String fieldProp = walker.getProperty();
                    if (fieldProp.equals("NUMBER")) {
                        row[0] = walker.getValue();
                    } else if (fieldProp.equals("TEXT")) {
                        row[1] = walker.getValue();
                    } else {
                        walker.skip();
                    }
                }
                results.addRow( row );
            } else {
                walker.skip();
            }
        }
        return results;
    }

    private FMResultSet processResults(ResWalker walker) {
        FMResultSet results = new FMResultSet();
        results.setColumns( new String[]{"SEQ","IEN"} );
        while( walker.nextProperty() ) {
            String prop = walker.getProperty();
            if (prop.equals("IEN")) {
                String row[] = new String[2];
                while( walker.nextProperty() ) {
                    String fieldProp = walker.getProperty();
                    if (fieldProp.equals("VALUE")) {
                        String value = walker.getValue();
                        entry.setIEN( value );
                        row[1] = value;
                    } else if (fieldProp.equals("SEQ")) {
                        row[0] = walker.getValue();;
                    } else {
                        walker.skip();
                    }
                }
                results.addRow( row );
            } else {
                walker.skip();
            }
        }
        entry.clearModifiedFields();
        return results;
    }

    public void setEntry(FMRecord entry) {
        this.entry = entry;
    }

}
