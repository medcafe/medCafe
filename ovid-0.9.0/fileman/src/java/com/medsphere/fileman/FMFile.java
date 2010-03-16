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

import java.util.Collection;

import com.medsphere.fileman.FMField.FIELDTYPE;

public class FMFile {
    private String location;
    private String number;
    protected FMFieldSet fields;
    protected final String fileName;
    protected boolean pack;
    FMFile parentFile;
    protected FMRecord parentRecord; // for listing subfiles, since a subfile doesn't exist without a parent record
    String sqlProjection;
    String[] description;

    public FMFile( String fileName ) {
        pack = true;
        this.fileName = fileName;
        fields = new FMFieldSet();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    protected void setFields() {
    }

    public FMField addField( String fieldName ) {
        FMField newField = new FMField(fieldName, FIELDTYPE.FREE_TEXT);
        getFields().add( newField );
        return newField;
    }

    public FMField addField( String fieldName, FIELDTYPE type ) {
        FMField newField = new FMField(fieldName, type);
        getFields().add( newField );
        return newField;
    }

    public String getFileName() {
        if (parentRecord!=null) {
            return parentRecord.getFileName() + "^" + fileName;
        }
        return fileName;
    }

    public void removeField( String name ) {
        getFields().remove( name );
    }

    public Collection<FMField> getFields() {
        return fields;
    }

    public boolean shouldPack() {
        return pack;
    }

    public String getIENS() {
        String IENS;
        if (parentRecord!=null) {
            IENS = "," + parentRecord.getIENS();
        } else {
            IENS = ",";
        }
        return IENS;
    }

    public void setParentRecord(FMRecord parentRecord) {
        setParentFile( parentRecord.getFile() );
        this.parentRecord = parentRecord;
    }

    public void setPack(boolean pack) {
        this.pack = pack;
    }

    public FMFile getParentFile() {
        return parentFile;
    }

    public void setParentFile(FMFile parentFile) {
        this.parentFile = parentFile;
    }

    public String getSqlProjection() {
        return sqlProjection;
    }

    public void setSqlProjection(String sqlProjection) {
        this.sqlProjection = sqlProjection;
    }

    public String[] getDescription() {
        return description;
    }

    public void setDescription( String[] description ) {
        this.description = description;
    }
    
    public FMField getField( String fieldID ) {
        return fields.get(fieldID);
    }
}
