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
package com.medsphere.fmdomain;
/*
 * container class of fileman ICD DIAGNOSIS information
 */


import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.Date;

import java.util.Map;
import java.util.Set;

import com.medsphere.fileman.FMAnnotateFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;

public class FMICD_Diagnosis extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/

    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMICD_Diagnosis.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMICD_Diagnosis.class);
        fileInfo = new FMFile("ICD DIAGNOSIS") { //

            @Override
            public Collection<FMField> getFields() {
                return domainFields;
            }
        };
        fileInfo.setPack(true);

    }

    public static FMFile getFileInfoForClass() {
        return fileInfo;
    }

    @Override
    protected Set<FMField> getDomainFields() {
        return domainFields;
    }

    @Override
    protected Map<String, AnnotatedElement> getDomainJavaFields() {
        return domainJavaFields;
    }

    @Override
    protected Map<String, String> getNumericMapping() {
        return domainNumbers;
    }

    /*-------------------------------------------------------------
     * end static initialization
     *-------------------------------------------------------------*/

    @FMAnnotateFieldInfo(name = "CODE NUMBER", number = ".01", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String icd9;
    @FMAnnotateFieldInfo(name = "DIAGNOSIS", number = "3", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String diagnosis;
    @FMAnnotateFieldInfo(name = "MAJOR DIAGNOSTIC CATEGORY", number = "5", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer category;
    @FMAnnotateFieldInfo(name = "MDC13", number = "5.5", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected Integer mdc13;
    @FMAnnotateFieldInfo(name = "MDC24", number = "5.7", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String mdc24;
    @FMAnnotateFieldInfo(name = "MDC25", number = "5.9", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String mdc25;
    @FMAnnotateFieldInfo(name = "ICD EXPANDED", number = "8", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String expanded;
    @FMAnnotateFieldInfo(name = "SEX", number = "9.5", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String sex;
    @FMAnnotateFieldInfo(name = "DESCRIPTION", number = "10", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String description;
    @FMAnnotateFieldInfo(name = "AGE LOW", number = "14", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected Integer ageLow;
    @FMAnnotateFieldInfo(name = "AGE HIGH", number = "15", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected Integer ageHigh;
    @FMAnnotateFieldInfo(name = "ACTIVATION DATE", number = "16", fieldType = FMField.FIELDTYPE.DATE)
    protected Date activationDate;




    public FMICD_Diagnosis() {
        super(fileInfo.getFileName());
    }

    public FMICD_Diagnosis(FMResultSet results) {
        super(results);
    }

    public String getIcd9(){
        return icd9;
    }
    public String getDiagnosis(){
    	return diagnosis;
    }
    public Integer getCategory()
    {
        return category;
    }

    public String getCategoryValue() {
        return getValue("5");
    }

    public Integer getMdc13(){
        return mdc13;
    }

    public String getMdc24(){
       return mdc24;
    }
    public String getMdc25(){
        return mdc25;
    }
    public String getIcdExpanded()
    {
        return expanded;
    }
    public String getSex()
    {
        return sex;
    }
    public String getDescription()
    {
        return description;
    }
    public Integer getAgeLow()
    {
        return ageLow;
    }
    public Integer getAgeHigh()
    {
        return ageHigh;
    }
    public Date getActivationDate()
    {
	return activationDate;
    }
   

    @Override
    public String toString() {
        return " Code=[" + getIcd9() + "]" +
        " Diagnosis=[" + getDiagnosis() + "]" +
        " Major Diagnostic Category=["+getCategory()+"]"
        + " MDC13 =["+getMdc13()+"]"
        + " MDC24 =["+getMdc24()+"]"
        + " MDC25 =["+getMdc25()+"]"
        + " ICD Expanded=["+ getIcdExpanded() + "]"
        + " sex=[" + getSex() + "]"
        + " description=[" + getDescription() + "]"
        + " Low Age=["+getAgeLow() +"]"+
                " High Age  =[" + getAgeHigh() + "]"
                + " Code activation date = [" + getActivationDate() + "]";
    }

}
