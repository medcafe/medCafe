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
import com.medsphere.fileman.FMField.FIELDTYPE;

public class FMRetrieve implements FMStatement {

    protected final ResAdapter adapter;
    protected FMRecord record;
    protected int columnCount = 0;
    protected FMFieldSet fields = new FMFieldSet();
    boolean erased;
    boolean clearOldValues;
    boolean internal = true;

    public FMRetrieve( ResAdapter adapter ) {
        this.adapter = adapter;
        clearOldValues = true;
    }

    public void setRecord(FMRecord entry) {
        this.record = entry;
        fields.addAll( entry.getFields() );
    }

    public void removeField(String field) {
        fields.remove( field );
    }

    public void removeAllFields() {
        fields.clear();
    }

    public void clearOldValues( boolean clearOldValues ) {
        this.clearOldValues = clearOldValues;
    }

    public void addField( String name, FIELDTYPE type) {
        fields.add( new FMField(name, type) );
    }

    public FMResultSet execute() throws ResException {
        erased = false;

        Resource retrieveRes = new Resource("FILEMAN");
        retrieveRes.addCompound("GETS");
        retrieveRes.addProperty("FILE", record.getFileName());
        retrieveRes.addProperty("IENS", record.getIENS());
        if (internal) {
            retrieveRes.addProperty("INTERNAL", "1");
        }
        for (FMField field : fields) {
            retrieveRes.addCompound("FIELD");
            retrieveRes.addProperty("NAME", field.getName());
            if (field.getType()==FIELDTYPE.VARIABLE_POINTER) {
                retrieveRes.addProperty("TYPE", "VP");
            }
            retrieveRes.endCompound(); // FIELD
        }
        retrieveRes.endCompound(); // GETS
        adapter.writeResource( retrieveRes );
        Resource resultRes = adapter.readResource();
        if (resultRes!=null) {
            FMResultSet results = new FMResultSet();
            results.setColumns( new String[]{"IEN", "VALUE"} );
            ResWalker walker = new ResWalker(resultRes);
            while( walker.nextProperty() ) {
                if (walker.getProperty().equals("RESULTS")) {
                    while( walker.nextProperty() ) {
                        if (walker.getProperty().equals("FIELD")) {
                            processField( walker );
                        }
                        else {
                            walker.skip();
                        }
                    }
                } else {
                    walker.skip();
                }
            }
            results.addRow(new String[]{record.getIENS(), record.getValue(".01")});
            if (erased) {
                // we must have retrieved data
                record.clearModifiedFields();
            }
            return results;
        }
        return null;
    }

    private void processField(ResWalker walker) {
        String fieldName = null;
        String value = null;
        while( walker.nextProperty() ) {
            String prop = walker.getProperty();
            if (prop.equals("NAME")) {
                fieldName = walker.getValue();
            } else if (prop.equals("VALUE")) {
                if (value != null) {
                    value = value + "\n" + walker.getValue();
                } else {
                    value = walker.getValue();
                }
            } else {
                walker.skip();
            }
        }
        if (fieldName!=null && value!=null) {
            if (!erased) {
                if (clearOldValues) {
                    record.erase();
                }
                erased = true;
            }
            record.setValue(fieldName, value);
        }
    }

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }
}
