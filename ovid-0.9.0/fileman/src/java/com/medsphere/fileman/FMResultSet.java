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

public class FMResultSet {
    
    protected HashMap<String,Integer> fieldPositions; // Maps field names to positions
    protected String[] fieldNames; // Maps field positions to names;
    protected ArrayList<String[]> values;
    protected int currentRowNum;
    private String[] currentRow;
    private GlobalNode startFrom;
    private String error;

    FMResultSet() {
        fieldPositions = null;
        values = new ArrayList<String[]>();
        currentRowNum = -1;
        currentRow = null;
    }

    public boolean next() {
        if (currentRowNum+1==values.size()) {
            currentRow = null;
            return false;
        }
        currentRow = values.get( ++currentRowNum );
        return true;
    }

    public void addRow( String[] rowValues ) {
        for (int idx=rowValues.length-1; idx>=0; --idx) {
            if (rowValues[idx]!=null && rowValues[idx].isEmpty()) {
                rowValues[idx] = null;
            }
        }
        values.add( rowValues );
    }

    public void setColumns( String[] names ) {
        fieldPositions = new HashMap<String,Integer>();
        for (int i=names.length-1; i>=0; --i) {
            fieldPositions.put(names[i], i);
        }
        fieldNames = new String[names.length];
        System.arraycopy(names, 0, fieldNames, 0, names.length);
    }

    public String getValue(String fieldName) {
        if (currentRow==null) {
            return null;
        }
        Integer fieldPos = fieldPositions.get( fieldName );
        if (fieldPos==null) {
            return null;
        }
        return currentRow[fieldPos];
    }

    public String getName( int fieldPos ) {
        if (fieldPos<0 || fieldNames==null || fieldPos>=fieldNames.length) {
            return null;
        }
        return fieldNames[fieldPos];
    }

    public String getValue( int fieldPos ) {
        if (fieldPos<0 || currentRow==null || fieldPos>=currentRow.length) {
            return null;
        }
        return currentRow[fieldPos];
    }
    
    public int getColumnCount() {
        if (currentRow==null) {
            return 0;
        }
        return currentRow.length;
    }

    public String getFileName() {
        // TODO Auto-generated method stub
        return null;
    }

    public GlobalNode getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(GlobalNode startFrom) {
        this.startFrom = startFrom;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
