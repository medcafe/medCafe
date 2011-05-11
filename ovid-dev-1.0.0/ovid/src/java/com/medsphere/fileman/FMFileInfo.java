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

import com.medsphere.resource.ResAdapter;
import com.medsphere.resource.ResException;
import com.medsphere.resource.ResWalker;
import com.medsphere.resource.Resource;

public class FMFileInfo {

    private final String fileName;
    private ResAdapter adapter;
    private boolean getProjections = false;

    public FMFileInfo( ResAdapter adapter, String name, boolean projections ) {
        getProjections = projections;
        fileName = name;
        this.adapter = adapter;

    }
    public FMFileInfo( ResAdapter adapter, String name ) {
        this( adapter, name, false );
    }

    public FMFile execute() throws ResException {
        Resource infoRes = new Resource("FILEMAN");
        infoRes.addCompound("FILEINFO");
        infoRes.addProperty("FILE", fileName);
        if (getProjections) {
            infoRes.addProperty("SQL", "1");
        }
        infoRes.endCompound(); // FILEINFO
        adapter.writeResource( infoRes );
        Resource resultRes = adapter.readResource();
        FMFile retVal = null;
        if (resultRes!=null) {
            ResWalker walker = new ResWalker(resultRes);
            while( walker.nextProperty() ) {
                String prop = walker.getProperty();
                if (prop.equals("ERROR")) {
                    return null;
                } else if (prop.equals("FILE")) {
                    return processFile( walker );
                } else {
                    walker.skip();
                }
            }
        }
        return retVal;
    }

    private FMFile processFile( ResWalker walker ) {
        FMFile retVal = null;
        while( walker.nextProperty() ) {
            String prop = walker.getProperty();
            if (prop.equals("F")) {
                processFields( walker, retVal );
            } else if (prop.equals("NAME")) {
                retVal = new FMFile( walker.getValue() );
            } else if (prop.equals("NUMBER")) {
                retVal.setNumber( walker.getValue() );
            } else if (prop.equals("LOCATION")) {
                retVal.setLocation( walker.getValue() );
            } else if (prop.equals("SQL")) {
                retVal.setSqlProjection( walker.getValue() );
            } else if (prop.equals("DESC")) {
                retVal.setDescription( readArray(walker) );
            } else {
                walker.skip();
            }
        }
        return retVal;
    }

    private String[] readArray(ResWalker walker) {
        ArrayList<String> array = new ArrayList<String>();
        while( walker.nextProperty() ) {
            array.add(walker.getValue());
        }
        return array.toArray(new String[array.size()]);
    }

    private void processFields(ResWalker walker, FMFile retVal) {
        String name = null;
        String location = null;
        FMField.FIELDTYPE fieldType = FMField.FIELDTYPE.UNDEFINED;
        String num = null;
        ArrayList<String> pointsTo = null;
        FMFile subfile = null;
        String sqlProj = null;
        while( walker.nextProperty() ) {
            String prop = walker.getProperty();
            if (prop.equals("N")) {
                name = walker.getValue();
            } else if (prop.equals("#")) {
                num = walker.getValue();
            } else if (prop.equals("T")) {
                fieldType = FMField.FIELDTYPE.getType( Integer.parseInt(walker.getValue()) );
            } else if (prop.equals("L")) {
                location = walker.getValue();
            } else if (prop.equals("FILE")) {
                subfile = processFile( walker );
            }else if (prop.equals("P")) {
                if (pointsTo==null) {
                    pointsTo = new ArrayList<String>();
                }
                pointsTo.add( walker.getValue() );
            } else if (prop.equals("Q")) {
                sqlProj = walker.getValue();
            } else {
                walker.skip();
            }
        }
        String[] arr = null;
        if (pointsTo!=null) {
            arr = pointsTo.toArray(new String[pointsTo.size()]);
        }
        FMField field = new FMField( name, num, fieldType, location, arr );
        if (subfile!=null) {
            subfile.setParentFile(retVal);
            field.setSubfile(subfile);
        }
        if (sqlProj!=null) {
            field.setSqlProjection( sqlProj );
        }
        retVal.getFields().add(field);
    }
}
