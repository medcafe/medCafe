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

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.lang.reflect.Field;

import com.medsphere.fileman.FMField.FIELDTYPE;

public class FMRecord {

    HashMap<String,String> values = null; // For non-domain members
    protected String IEN;
    protected String IENS;
    protected final String fileName;
    protected HashSet<String> modifiedFields = new HashSet<String>();
    protected FMFile file;
    protected FMRecord parent;

    public FMRecord() {
        this.fileName = null;
    }

    public FMRecord( String fileName ) {
        this.fileName = fileName;
    }

    public FMRecord( FMFile file ) {
        fileName = file.getFileName();
        this.file = file;
        values = new HashMap<String,String>();
        for (FMField field : file.getFields() ) {
            values.put(field.getNumber(), null);
        }
    }

    public FMRecord( FMResultSet results ) {
        fileName = results.getFileName();
        processResults( results);
    }

    protected void processResults( FMResultSet results ) {
        Map<String, Field> fieldMap = getDomainJavaFields();
        if (fieldMap!=null) {
            for (int idx=results.getColumnCount()-1; idx>=0; --idx ) {
                String value = results.getValue(idx);
                if (value==null) {
                    continue;
                }
                boolean valueSet = false;
                Field f = fieldMap.get(results.getName(idx));
                if (f!=null) {
                    f.setAccessible(true);
                    valueSet = setValue( f, value );
                }
                if (!valueSet) {
                    acceptField(results.getName(idx), results.getValue(idx));
                }
            }
        } else {
            for (int idx=results.getColumnCount()-1; idx>=0; --idx ) {
                acceptField( results.getName(idx), results.getValue(idx) );
            }
        }
        clearModifiedFields();
    }

    public void erase() {
        // Wipe out previous results
        Map<String, Field> fieldMap = getDomainJavaFields();
        if (fieldMap!=null) {
            for ( Map.Entry<String, Field> entry : fieldMap.entrySet() ) {
                Field f = entry.getValue();
                f.setAccessible( true );
                try {
                    f.set( this, null );
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        values = null;
    }

    public void setValue( String fieldName, String value ) {
        String numericFieldName = getNumericFieldName( fieldName );
        if (numericFieldName!=null) {
            fieldName = numericFieldName;
        }
        boolean valueSet = false;
        Map<String, Field> fieldMap = getDomainJavaFields();
        if (fieldMap!=null) {
            Field f = fieldMap.get(fieldName);
            if (f!=null) {
                f.setAccessible(true);
                valueSet = setValue( f, value );
            }
        }
        if (!valueSet) {
            acceptField( fieldName, value );
        }
    }

    private static boolean valuesChanged( Object obj1, Object obj2 ) {
        return (obj1==null)!=(obj2==null) || !(obj1==null || obj1.equals(obj2));
    }

    private boolean setValue(Field field, String value) {
        FMAnnoteFieldInfo annote = field.getAnnotation(FMAnnoteFieldInfo.class);
        FIELDTYPE fieldType = annote.fieldType();
        boolean retVal = true;
        Object obj = null;
        try {
            if (!value.equals("")) {
                switch( fieldType ) {
                case DATE:
                {
                    obj = FMUtil.fmDateToDate( value );
                    break;
                }
                case POINTER_TO_FILE:
                {
                    obj = Integer.valueOf( value );
                    break;
                }
                case NUMERIC:
                {
                    obj = Double.valueOf( value );
                    break;
                }
                default:
                {
                    obj = value;
                    break;
                }
                }
            }

            if (valuesChanged(field.get(this), obj)) {
                field.set(this, obj );
                addModifiedField( annote.number() );
            }
        } catch (IllegalAccessException e) {
            // Bad but not overall fatal, so keep going.
            retVal = false;
        } catch (ParseException e) {
            // Bad but not overall fatal, so keep going.
            retVal = false;
        } catch (NumberFormatException e) {
            // Bad but not overall fatal, so keep going.
            retVal = false;
        } finally {
            // Empty block. Don't throw exceptions out of here.
        }
        return retVal;
    }

    protected void addModifiedField(String fieldName) {
        modifiedFields.add( fieldName );
    }

    public void clearModifiedFields() {
        modifiedFields = new HashSet<String>();
    }

    protected void setDomainValue(String fieldName, Object value)  {
        boolean didFieldSet = false;
        try {
            Class thisClass = getClass();
            Field field = thisClass.getDeclaredField(fieldName);
            field.setAccessible( true );
            Object currentValue = field.get(this);
            if (valuesChanged(currentValue,value)) {
                field.set(this, value);
                addModifiedField(field.getAnnotation(FMAnnoteFieldInfo.class).number());
            }
            didFieldSet = true;
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (!didFieldSet) {
            acceptField( fieldName, value.toString() );
        }
    }

    private void acceptField(String fieldName, String value) {
        if (fieldName.equals("IEN")) {
            if (valuesChanged(getIEN(), value)) {
                setIEN( value );
            }
        } else {
            if (values==null) {
                values = new HashMap<String,String>();
            }
            String currentValue = values.get(fieldName);
            if (valuesChanged(currentValue, value)) {
                values.put( fieldName, value );
                addModifiedField( fieldName );
            }
        }
    }

    protected String getNumericFieldName( String fieldName ) {
        String numericFieldName = null;
        if (fieldName.matches("(\\d+\\.?\\d+)|(\\.?\\d+)")) { // Numeric?
            numericFieldName = fieldName;
        } else {
            Map<String, String> mapping = getNumericMapping();
            if (mapping!=null) {
                numericFieldName = mapping.get( fieldName );
            }
        }
        return numericFieldName;
    }


    public String getValue( String fieldName ) {
        Object obj = getObject( fieldName );
        if (obj==null) {
            return null;
        }
        if (obj instanceof Date) {
            return FMUtil.dateToFMDate((Date) obj);
        }
        return obj.toString();
    }

    public Object getObject( String fieldName ) {
        Map<String, Field> domainFields = getDomainJavaFields();
        if (domainFields!=null) {
            String numericFieldName = getNumericFieldName( fieldName );
            if (numericFieldName!=null) {
                Field classField = domainFields.get( numericFieldName );
                if (classField!=null) {
                    try {
                        classField.setAccessible( true );
                        Object retVal =  classField.get(this);
                        if (retVal!=null) {
                            return retVal;
                        }
                    } catch (IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        // Couldn't get from domain fields.
        if (values==null) {
            return null;
        }
        return values.get( fieldName );
    }

    protected Map<String, String> getNumericMapping() {
        // Provides a mapping of English names to field numbers
        // Domain classes will want to override this and probably return a static member
        return null;
    }

    protected static Map<String, String> getNumericMapping( Class<? extends FMRecord> domainClass ) {
        // Helper function called by child classes
        Map<String, String> numberMap = new HashMap<String, String>();
        for (Field f : domainClass.getDeclaredFields()) {
            FMAnnoteFieldInfo annote = f.getAnnotation( FMAnnoteFieldInfo.class );
            if ( annote != null) {
                numberMap.put(annote.name(), annote.number());
            }
        }
        return numberMap;
    }

    protected Map<String, Field> getDomainJavaFields() {
        // Domain classes will want to override this and probably return a static member
        // Provides a mapping of numeric fields to Java fields
        return null;
    }

    static protected Map<String, Field> getDomainJavaFields( Class<? extends FMRecord> domainClass ) {
        // Helper function called by child classes
        Map<String, Field> domainFields = new HashMap<String, Field>();
        for (Field f : domainClass.getDeclaredFields()) {
            FMAnnoteFieldInfo annote = f.getAnnotation( FMAnnoteFieldInfo.class );
            if ( annote != null) {
                domainFields.put(annote.number(), f);
            }
        }
        return domainFields;
    }

    protected Set<FMField> getDomainFields() {
        // Domain classes will want to override this
        return getFieldsInDomain( getDomainJavaFields() );
    }

    public Set<String> getModifiedFields() {
        return modifiedFields;
    }

    public Set<FMField> getFields() {
        HashSet<FMField> retVal = new HashSet<FMField>();
        Set<FMField> domainFields = getDomainFields();
        if (domainFields.size()!=0) {
            retVal.addAll( domainFields );
        }
        if (values!=null) {
            for ( Map.Entry<String, String> entry : values.entrySet() ) {
                retVal.add( new FMField(entry.getKey(), FIELDTYPE.FREE_TEXT) );
            }
        }
        return retVal;
    }

    static protected Set<FMField> getFieldsInDomain( Map<String, Field> fieldMap ) {
        // Helper function called by child classes
        Set<FMField> fields = new HashSet<FMField>();
        if (fieldMap!=null) {
            for ( Map.Entry<String, Field> entry : fieldMap.entrySet() ) {
                FMAnnoteFieldInfo annote = entry.getValue().getAnnotation(FMAnnoteFieldInfo.class);
                fields.add( new FMField(annote.name(), annote.number(), annote.fieldType()) );
            }
        }
        return fields;
    }

    public String getIEN() {
        return IEN;
    }

    public void setIEN(String ien) {
        IEN = ien;
    }

    public String getFileName() {
        if (parent==null) {
            return fileName;
        }
        return parent.getFileName()+"^"+fileName;
    }

    public String getIENS() {
        if (IENS!=null) {
            return IENS;
        }
        StringBuffer retVal = new StringBuffer();
        if (IEN!=null) {
            retVal.append( IEN );
        } else {
            retVal.append( "+1" );
        }
        retVal.append( "," );
        if (getParent()!=null) {
            retVal.append(getParent().getIENS());
        }

        return retVal.toString();
    }

    public FMFile getFile() {
        if (file==null) {
            final FMRecord tmp = this;
            file = new FMFile(getFileName()) {
                public Collection<FMField> getFields() {
                    return tmp.getFields();
                }
                public boolean shouldPack() {
                    return true;
                }
                public String getIENS() {
                    return tmp.getIENS();
                }
            };
        }
        return file;
    }

    public FMRecord getParent() {
        return parent;
    }

    public void setParent(FMRecord parent) {
        this.parent = parent;
    }

    public void setIENS(String iens) {
        if (iens!=null) {
            String parts[] = iens.split(",");
            IEN = parts[0];
        }
        IENS = iens;
    }

}
