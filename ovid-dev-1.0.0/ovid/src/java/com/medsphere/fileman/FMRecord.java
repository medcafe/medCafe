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

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.iterators.EntrySetMapIterator;
import org.apache.log4j.Logger;

import com.medsphere.fileman.FMField.FIELDTYPE;

public class FMRecord {
    private Logger logger = Logger.getLogger(FMRecord.class);
    HashMap<String,String> values = null; // For non-domain members
    protected String IEN;
    protected String IENS;
    protected final String fileName;
    protected HashSet<String> modifiedFields = new HashSet<String>();
    protected FMFile file;
    private final FMFile externalFile; // passed in constructor
    protected FMRecord parent;
    private HashMap <String, FIELDTYPE> fieldTypes = new HashMap<String, FIELDTYPE>();

    public FMRecord() {
        externalFile = null;
        this.fileName = null;
    }

    public FMRecord( String fileName ) {
        externalFile = null;
        this.fileName = fileName;
    }

    public FMRecord( FMFile file ) {
        fileName = file.getFileName();
        this.file = externalFile = file;
        values = new HashMap<String,String>();
        for (FMField field : file.getFields() ) {
            values.put(field.toString(), null);
        }
    }

    public FMRecord( FMResultSet results ) {
        externalFile = null;
        fileName = results.getFileName();
        processResults( results);
    }

    protected void processResults( FMResultSet results ) {
        Map<String, AnnotatedElement> fieldMap = getDomainJavaFields();
        if (fieldMap!=null) {
            for (int idx=results.getColumnCount()-1; idx>=0; --idx ) {
                String value = results.getValue(idx);
                if (value==null) {
                    continue;
                }
                boolean valueSet = false;
                AnnotatedElement ele = fieldMap.get(results.getName(idx));
                if (ele != null) {
                    setAccessible( ele, true );
                    valueSet = setValue( ele, value );
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

    private void setAccessible(AnnotatedElement ele, boolean value) {
        if (ele != null) {
            if (ele instanceof Field) {
                Field f = (Field) ele;
                f.setAccessible(value);
            } else if (ele instanceof Method) {
                Method m = (Method) ele;
                m.setAccessible(value);
            }
        }
    }
    public void erase() {
        // Wipe out previous results
        Map<String, AnnotatedElement> fieldMap = getDomainJavaFields();
        if (fieldMap!=null) {
            for ( Map.Entry<String, AnnotatedElement> entry : fieldMap.entrySet() ) {
                AnnotatedElement ele = entry.getValue();
                setAccessible( ele, true );
                setValue( ele, null );
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
        Map<String, AnnotatedElement> fieldMap = getDomainJavaFields();
        if (fieldMap!=null) {
            AnnotatedElement ele = fieldMap.get(fieldName);
            if (ele != null) {
                setAccessible( ele, true );
                valueSet = setValue( ele, value );
            }
        }
        if (!valueSet) {
            acceptField( fieldName, value );
        }
    }

    private static boolean valuesChanged( Object obj1, Object obj2 ) {
        return (obj1==null)!=(obj2==null) || !(obj1==null || obj1.equals(obj2));
    }

    private boolean setValue(AnnotatedElement ele, String value) {
        FMAnnotateFieldInfo annote = ele.getAnnotation(FMAnnotateFieldInfo.class);
        FIELDTYPE fieldType = annote.fieldType();
        boolean retVal = true;
        Object obj = null;
        try {
            if (value != null && !value.equals("")) {
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
            if (ele instanceof Field) {
                Field field = (Field) ele;
                if (valuesChanged(field.get(this), obj)) {
                    field.set(this, obj );
                }
            } else if (ele instanceof Method) {
                Method method = (Method) ele;
                if (valuesChanged(method.getDefaultValue(), obj)) {
                    invokeSetter(ele, method, obj);
                }
            }
            addModifiedField( annote.number() );
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

    /**
     * if the annotation is a method, get the setter for the method and invoke it.
     * @param ele
     * @param method
     * @param obj
     */
    private void invokeSetter(AnnotatedElement ele, Method method, Object obj) {
        String getter = method.getName();
        String setterName = getter.replaceFirst("get", "set");
        try {
            Method setter = method.getDeclaringClass().getMethod(setterName, obj.getClass());
            setter.invoke(this, obj);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            logger.error("Unable to find setter for " + ele.toString()+ ".  Ensure that a setter exists for this field.  Expecting " + setterName);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    protected void addModifiedField(String fieldName) {
        modifiedFields.add( fieldName );
    }

    public void clearModifiedFields() {
        modifiedFields = new HashSet<String>();
    }

  protected void setDomainValue(String fieldName, Object value, String number)  {
        boolean didFieldSet = false;
        try {
            Class thisClass = getClass();
	    Field field = null;
		Class curClass = thisClass;
	while(!curClass.equals(FMRecord.class))
	{
	    try{
        	field = curClass.getDeclaredField(fieldName);
		break;
		}
	    catch(NoSuchFieldException e)
		{
		}
	    curClass = curClass.getSuperclass();

	}

         if (field != null)
         {
            field.setAccessible( true );
            Object currentValue = field.get(this);
            if (valuesChanged(currentValue,value)) {
                field.set(this, value);
                addModifiedField(number);
            }
            didFieldSet = true;
         }
         else
             throw new NoSuchFieldException();
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


    protected void setDomainValue(String fieldName, Object value)  {
        try {
            Class thisClass = getClass();

	    Field field = null;
		Class curClass = thisClass;
	while(!curClass.equals(FMRecord.class))
	{
	    try{
        	field = curClass.getDeclaredField(fieldName);
                String number = field.getAnnotation(FMAnnotateFieldInfo.class).number();
                setDomainValue(fieldName, value, number);
		break;
		}
	    catch(NoSuchFieldException e)
		{
		}
	    curClass = curClass.getSuperclass();

	}

         if (field == null)

             throw new NoSuchFieldException();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


/*

  protected void setDomainValue(String fieldName, Object value, String number)  {
        boolean didFieldSet = false;
        try {
            Class thisClass = getClass();
            Field field = thisClass.getDeclaredField(fieldName);
            field.setAccessible( true );
            Object currentValue = field.get(this);
            if (valuesChanged(currentValue,value)) {
                field.set(this, value);
                addModifiedField(number);
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

*/


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
        Map<String, AnnotatedElement> domainFields = getDomainJavaFields();
        if (domainFields!=null) {
            String numericFieldName = getNumericFieldName( fieldName );
            if (numericFieldName!=null) {
                AnnotatedElement ele = domainFields.get( numericFieldName );
                setAccessible( ele, true );
                if (ele instanceof Field) {
                    Field classField = (Field) ele;
                    if (classField!=null) {
                        try {
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
                } else if (ele instanceof Method) {
                    Method classMember = (Method) ele;
                    if (classMember!=null) {
                        try {
                            Object retVal =  classMember.invoke(this);
                            if (retVal!=null) {
                                return retVal;
                            }
                        } catch (IllegalArgumentException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
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

    private static void accumulateNumericMapping( Map<String, String> numberMap, Class<? extends FMRecord> domainClass ) {

        if ( domainClass != null && domainClass.getSuperclass() != null) {
            Class<? extends FMRecord> superClass = (Class<? extends FMRecord>) domainClass.getSuperclass();
            accumulateNumericMapping( numberMap, superClass );
        }

        for (AnnotatedElement e : domainClass.getDeclaredFields()) {
            FMAnnotateFieldInfo annote = e.getAnnotation( FMAnnotateFieldInfo.class );
            if ( annote != null) {
                numberMap.put(annote.name(), annote.number());
            }
        }
        for (AnnotatedElement e : domainClass.getDeclaredMethods()) {
            FMAnnotateFieldInfo annote = e.getAnnotation( FMAnnotateFieldInfo.class );
            if ( annote != null) {
                numberMap.put(annote.name(), annote.number());
            }
        }
    }

    protected static Map<String, String> getNumericMapping( Class<? extends FMRecord> domainClass ) {
        // Helper function called by child classes
        Map<String, String> numberMap = new HashMap<String, String>();

        accumulateNumericMapping( numberMap, domainClass );

        return numberMap;
    }

    protected Map<String, AnnotatedElement> getDomainJavaFields() {
        // Domain classes will want to override this and probably return a static member
        // Provides a mapping of numeric fields to Java fields
        return null;
    }

    static private void accumulateJavaDomainFields( Map<String, AnnotatedElement> domainFields, Class<? extends FMRecord> domainClass ) {
        if ( domainClass != null && domainClass.getSuperclass() != null) {
            Class<? extends FMRecord> superClass = (Class<? extends FMRecord>) domainClass.getSuperclass();
            accumulateJavaDomainFields( domainFields, superClass );
        }
        for (AnnotatedElement e : domainClass.getDeclaredFields()) {
            FMAnnotateFieldInfo annote = e.getAnnotation( FMAnnotateFieldInfo.class );
            if ( annote != null) {
                domainFields.put(annote.number(), e);
            }
        }
        for (AnnotatedElement e : domainClass.getDeclaredMethods()) {
            FMAnnotateFieldInfo annote = e.getAnnotation( FMAnnotateFieldInfo.class );
            if ( annote != null) {
                domainFields.put(annote.number(), e);
            }
        }


    }
    static protected Map<String, AnnotatedElement> getDomainJavaFields( Class<? extends FMRecord> domainClass ) {
        // Helper function called by child classes
        Map<String, AnnotatedElement> domainFields = new HashMap<String, AnnotatedElement>();
        accumulateJavaDomainFields( domainFields, domainClass );

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
        FMFieldSet retVal = new FMFieldSet();
        Set<FMField> domainFields = getDomainFields();
        if (domainFields.size()!=0) {
            retVal.addAll( domainFields );
        }
        // We use the externalFile here, since getFile may just return an
        // anonymous instance.
        if (externalFile!=null) {
            retVal.addAll( externalFile.getFields() );
        }
        if (values!=null) {
            for (String key : values.keySet()) {
                FIELDTYPE type = fieldTypes.get(key);
                if (type==null) {
                    type = FIELDTYPE.FREE_TEXT;
                }
                retVal.add( new FMField(key, type) );
            }
        }
        return retVal;
    }

    static protected Set<FMField> getFieldsInDomain( Map<String, AnnotatedElement> fieldMap ) {
        // Helper function called by child classes
        FMFieldSet fields = new FMFieldSet();
        if (fieldMap!=null) {
            for ( Map.Entry<String, AnnotatedElement> entry : fieldMap.entrySet() ) {
                FMAnnotateFieldInfo annote = entry.getValue().getAnnotation(FMAnnotateFieldInfo.class);
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
                @Override
                public Collection<FMField> getFields() {
                    return tmp.getFields();
                }
                @Override
                public boolean shouldPack() {
                    return true;
                }
                @Override
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

    public void setValue(String fieldName, String value, FIELDTYPE fieldType) {
        fieldTypes.put(fieldName, fieldType);
        setValue(fieldName, value);
    }

}
