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

import java.util.HashMap;

public class FMField  implements Comparable {
    public static enum FIELDTYPE {
        DATE(1), NUMERIC(2), SET_OF_CODES(3), FREE_TEXT(4), WORD_PROCESSING(5),
        COMPUTED(6), POINTER_TO_FILE(7), VARIABLE_POINTER(8), SUBFILE(9), UNDEFINED(-1);

        private final Integer value;
        FIELDTYPE(Integer value) {
            this.value = value;
        }
        public Integer getValue() {
            return value;
        }
        static private HashMap<Integer, FIELDTYPE> fieldNums = null;

        static public FIELDTYPE getType( Integer val ) {
            if (fieldNums == null) {
                fieldNums = new HashMap<Integer, FIELDTYPE>();
                for (FIELDTYPE ft : values()) {
                    fieldNums.put( ft.getValue(), ft );
                }
            }
            return fieldNums.get( val );
        }
    }
    private final FIELDTYPE type;
    private final String name;
    private FMFile subfile;
    private String location; // global location
    private String number;
    private String[] pointsTo;
    private String sqlProjection;
    private double numeric;
    private Boolean internal = null;

    public String getLocation() {
        return location;
    }

    public String getNumber() {
        return number;
    }
    public FMField() {
        this("");
    }
    public FMField(String name) {
        this(name, FIELDTYPE.UNDEFINED);
    }
    public FMField(String name, FIELDTYPE type) {
        this(name, null, type);
    }
    public FMField(String name, String num, FIELDTYPE fieldType) {
        this(name, num, fieldType, null, null);
    }
    public FMField(String name, String num, FIELDTYPE fieldType, String location, String[] pointsTo) {
        numeric = -1;
        this.type = fieldType;
        this.name = name;
        number = num;
        this.location = location;
        this.pointsTo = pointsTo;
    }

    public String getName() {
        return name;
    }

    public FIELDTYPE getType() {
        return type;
    }

    public String[] getPointsTo() {
        return pointsTo;
    }

    public boolean useInternal() {
        if (internal!=null) {
            return internal.booleanValue();
        }
        return (type==FIELDTYPE.DATE || type==FIELDTYPE.POINTER_TO_FILE || type==FIELDTYPE.VARIABLE_POINTER);
    }

    public String toString() {
        return getName();
    }

    public FMFile getSubfile() {
        return subfile;
    }

    public void setSubfile(FMFile subfile) {
        this.subfile = subfile;
    }

    public String getSqlProjection() {
        return sqlProjection;
    }

    public void setSqlProjection(String sqlProjection) {
        this.sqlProjection = sqlProjection;
    }

    public int compareTo(Object arg0) {
        if (arg0==null || !(arg0 instanceof FMField) ) {
            return -1;
        }
        FMField other = (FMField) arg0;
        double myNumber = getDouble();
        double otherNumber = other.getDouble();
        String otherName = other.getName();
        if (name!=null && otherName!=null && (myNumber==-1.0 || otherNumber==-1.0)) {
            return name.compareTo(otherName);
        }
        return ((myNumber==otherNumber) ? 0 : ((myNumber<otherNumber) ? -1 : 1));
    }

    public double getDouble() {
        if (numeric==-1.0 && number!=null) {
            numeric = Double.parseDouble(number);
        }
        return numeric;
    }

    public boolean matches( String fieldID ) {
        if (number!=null) {
            if (number.equals(fieldID)) {
                return true;
            }
        }
        if (name!=null) {
            if (name.equals(fieldID)) {
                return true;
            }
        }
        return false;
    }

    public void setInternal(Boolean internal) {
        this.internal = internal;
    }
}
