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

import java.util.ArrayList;
import java.util.HashMap;

import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.resource.ResWalker;
import com.medsphere.resource.Resource;
import com.medsphere.fileman.FMField.FIELDTYPE;

public abstract class FMQuery implements FMStatement {

    protected final ResAdapter adapter;
    protected boolean packed;
    protected FMFieldSet fields = new FMFieldSet();
    protected HashMap<String,Integer> fieldPositions = new HashMap<String,Integer>();
    private String fileName;
    private FMScreen screen;
    private boolean htmlEncoded = false;
    private String IENS;
    private String number;
    private GlobalNode from = null;
    private String fieldNames[] = null;
    private boolean safe = false;

    public FMQuery( ResAdapter sourceAdapter, FMFile fileInfo ) {
        this.adapter = sourceAdapter;
        setPacked( fileInfo.shouldPack() );
        fields.addAll( fileInfo.getFields() );
        fileName = fileInfo.getNumber();
        if (fileName==null) {
            fileName = fileInfo.getFileName();
        }
        screen = null;
        IENS = fileInfo.getIENS();
        if (IENS!=null) {
            String[] IENSArray = IENS.split(",",-1);
            if (IENSArray.length<=2) {
                // Simple file
                IENS = null;
            } else if (!IENSArray[0].equals("")) {
                // LIST^DIC wants the first comma piece to be empty
                StringBuffer stringBuff = new StringBuffer(",");
                for (int idx=1; idx<IENSArray.length-1; ++idx) {
                    stringBuff.append(IENSArray[idx]);
                    stringBuff.append(",");
                }
                IENS = stringBuff.toString();
            }
        }
    }

    public void setScreen(FMScreen screen) {
        this.screen = screen;
    }

    public void setPacked(boolean packed) {
        this.packed = packed;
    }

    public void removeField(String field) {
        fields.remove( field );
    }

    public void addField(String name, FIELDTYPE type) {
        fields.add( new FMField(name, type) );
    }

    public FMField getField(String name) {
        return fields.get(name);
    }

    public void setNumber( int number ) {
        if (number>0) {
            this.number = Integer.toString( number );
        } else {
            this.number = null;
        }
    }

    protected void addSearchProperties(Resource queryRes) {
        queryRes.addProperty("FILE", fileName);
        if (IENS!=null) {
            queryRes.addProperty("IENS", IENS);
        }
        queryRes.addProperty("PACK", packed ? "1" : "0");
        if (screen != null ) {
            queryRes.addCompound("SCREEN");
            screen.writeResource(queryRes);
            queryRes.endCompound(); // SCREEN
        }
        if (number!=null) {
            queryRes.addProperty("NUMBER", number);
        }
        if (from!=null) {
            queryRes.addCompound("FROM");
            from.writeResource(queryRes);
            queryRes.endCompound(); // FROM
        }
        if (safe) {
            queryRes.addProperty("SAFE", safe?"1":"0");
        }
        for ( FMField field : fields ) {
            if (field.getType()==FIELDTYPE.SUBFILE || field.getType()==FIELDTYPE.WORD_PROCESSING) {
                continue;
            }
            queryRes.addCompound("FIELD");
            String name = field.getNumber();
            if (name==null) {
                name = field.getName();
            }
            queryRes.addProperty("NAME", name);
            if (field.useInternal()) {
                queryRes.addProperty("INTERNAL","1");
            }
            if (field.getType()==FIELDTYPE.VARIABLE_POINTER) {
                queryRes.addProperty("TYPE","VP");
            }
            queryRes.endCompound(); // FIELD
        }
        if (!packed) {
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
        }
    }

    protected FMResultSet getResult( Resource queryRes ) throws ResException {
        adapter.writeResource( queryRes );
        Resource resultRes = adapter.readResource();
        FMResultSet retVal = null;
        from = null;
        if (resultRes!=null) {
            ResWalker walker = new ResWalker(resultRes);
            while( walker.nextProperty() ) {
                String prop = walker.getProperty();
                if (prop.equals("RESULTS")) {
                    retVal = processResults(walker);
                } else if (prop.equals("FROM")) {
                    from = new GlobalNode( walker ).getChild(0);
                } else if (prop.equals("ERROR")) {
                    retVal = new FMResultSet();
                    retVal.setError(walker.getValue());
                } else {
                    walker.skip();
                }
            }
        }
        if (retVal!=null && from!=null) {
            retVal.setStartFrom(from);
        }
        return retVal;
    }

    private FMResultSet processResults(ResWalker walker) {
        FMResultSet results = new FMResultSet();
        if (fieldNames!=null) {
            results.setColumns( fieldNames );
        }
        while( walker.nextProperty() ) {
            String prop = walker.getProperty();
            if (prop.equals("RD")) { // packed
                String values[] = walker.getValue().split("\\^",-1);
                if (htmlEncoded) {
                    for (int idx=values.length-1; idx>=0; --idx) {
                        values[idx]=values[idx].replace( "&amp", "&" ).replace("&#94", "^");
                    }
                }
                results.addRow( values );
            } else if (prop.equals("ROW")) {
                processRow( walker, results ); // unpacked
            } else if (prop.equals("MAP")) {
                readMap(walker, results);
            } else if (prop.equals("HTML")) {
                htmlEncoded = !walker.getValue().equals("0");
            } else {
                walker.skip();
            }
        }
        return results;
    }

    private void readMap( ResWalker walker, FMResultSet results ) {
        ArrayList<String> arrayList = new ArrayList<String>();
        int columnCount = 0;
        while( walker.nextProperty() ) {
            String val = walker.getValue();

            if (val.matches("(\\d+\\.?\\d+I)|(\\.?\\d+I)")) { // Internal numeric?
                val = val.substring(0, val.length()-1); // Strip off "I"
            }
            fieldPositions.put(val, columnCount);
            ++columnCount;
            arrayList.add( val );
        }
        results.setColumns( arrayList.toArray( new String[arrayList.size()] ) );
    }

    private void processRow(ResWalker walker, FMResultSet results) {
        // Unpacked data
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
                        fieldValue = walker.getValue();
                    } else {
                        walker.skip();
                    }
                }
                if (fieldPos>=0 && fieldValue!=null) {
                    row[fieldPos] = fieldValue;
                }
            } else {
                walker.skip();
            }
        }
        results.addRow( row );
    }

    protected Resource getQueryResource() {
        return new Resource("FILEMAN");
    }

    public GlobalNode getFrom() {
        return from;
    }

    public void setFrom(GlobalNode from) {
        this.from = from;
    }

    public void setSafe(boolean safe) {
        this.safe = safe;
    }
}
