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
package com.medsphere.fileman;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.medsphere.fileman.FMField.FIELDTYPE;
import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.resource.ResWalker;
import com.medsphere.resource.Resource;

public class FMQueryByIENS implements FMStatement {
    private Logger logger = LoggerFactory.getLogger(FMQueryByIENS.class);
    protected Collection<String> IENS;
    protected final ResAdapter adapter;
    protected String fileName;
    protected FMFieldSet fields = new FMFieldSet();
    private String fieldNames[] = null;
    protected HashMap<String,Integer> fieldPositions = new HashMap<String,Integer>();

    public FMQueryByIENS(ResAdapter sourceAdapter, FMFile fileInfo) {
        adapter = sourceAdapter;
        this.fields.addAll(fileInfo.getFields());
        fileName = fileInfo.getNumber();
        if (fileName==null) {
            fileName = fileInfo.getFileName();
        }
    }

    public FMField getField(String name) {
        return fields.get(name);
    }

    public void addIEN(String ien) {
        if (this.IENS == null) {
            this.IENS = new HashSet<String>();
        }
        this.IENS.add(ien);
    }
    public void setIENS(Collection<String> iens) {
        this.IENS = iens;
    }

    public void setIENS(String[] iens) {
        this.IENS = new HashSet<String>();
        this.IENS.addAll(Arrays.asList(iens));
    }

    public Collection<String> getIENS() {
        return IENS;
    }

    public void addField(String name, FIELDTYPE type) {
        fields.add( new FMField(name, type) );
    }

    public FMResultSet execute() throws ResException {
        Resource res = new Resource("FILEMAN");
        res.addCompound("QIEN");
        res.addProperty("FILE", fileName);
        for (String ien : IENS) {
            res.addProperty("IENS", ien + (ien.endsWith(",") ? "" : ",") );
        }
        if (IENS == null || IENS.isEmpty()) {
            throw new ResException("No IENS specified.");
        }

        for ( FMField field : fields ) {
            if (field.getType()==FIELDTYPE.SUBFILE || field.getType()==FIELDTYPE.WORD_PROCESSING) {
                continue;
            }
            res.addCompound("FIELD");
            String name = field.getNumber();
            if (name==null) {
                name = field.getName();
            }
            res.addProperty("NAME", name);
            if (field.useInternal()) {
                res.addProperty("INTERNAL","INTERNAL(\"I\")");
            }
            if (field.getType()==FIELDTYPE.VARIABLE_POINTER) {
                res.addProperty("TYPE","VP");
            }
            res.endCompound(); // FIELD
        }

        res.endCompound(); // QIEN

        fieldNames = new String[fields.size()+1];
        int i=0;
        String fieldName = "IEN";
        fieldNames[i] = fieldName;
        fieldPositions.put(fieldName, i);
        i++;
        for ( FMField field : fields ) {
            String name = field.getNumber();
            if (name==null) {
                name = field.getName();
            }
            fieldNames[i] = name;
            fieldPositions.put(name, i);
            i++;
        }

        adapter.writeResource(res);
        Resource resultRes = adapter.readResource();
        if (resultRes!=null) {
            FMResultSet results = new FMResultSet();
            if (fieldNames!=null) {
                results.setColumns( fieldNames );
            }

            ResWalker walker = new ResWalker(resultRes);
            while( walker.nextProperty() ) {
                if (walker.getProperty().equals("RESULTS")) {
                    while( walker.nextProperty() ) {
                        if (walker.getProperty().equals("RECORD")) {
                            processRow(walker, results);
                        }
                    }
                }

            }
            return results;
        }
        return null;
    }

    private void processRow(ResWalker walker, FMResultSet results) {
        String[] row = new String[fieldNames.length];
        while( walker.nextProperty() ) {
            String prop = walker.getProperty();
            if (prop.equals("FIELD")) {
                int fieldPos = -1;
                String fieldValue = null;
                while( walker.nextProperty() ) {
                    String fieldProp = walker.getProperty();
                    if (fieldProp.equals("NAME")) {
                        fieldPos = fieldPositions.get( walker.getValue() );
                    } else if (fieldProp.equals("VALUE")) {
                        if (fieldValue != null) {
                            fieldValue = fieldValue + "\n" + walker.getValue();
                        } else {
                            fieldValue = walker.getValue();
                        }
                    } else {
                        walker.skip();
                    }
                }
                if (fieldPos>=0 && fieldValue!=null) {
                    row[fieldPos] = fieldValue;
                }
            } else if (prop.equals("IENS")) {
                int fieldPos = fieldPositions.get("IEN");
                String fieldValue = walker.getValue().replaceFirst(",$", "");
                row[fieldPos] = fieldValue;
            } else {
                walker.skip();
            }
        }
        results.addRow( row );
    }
}
