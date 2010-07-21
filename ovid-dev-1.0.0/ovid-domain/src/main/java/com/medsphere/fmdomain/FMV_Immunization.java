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
 * container class of fileman V Immunization information
 */


import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.Date;
import java.util.ArrayList;

import java.util.Map;
import java.util.Set;

import com.medsphere.fileman.FMAnnotateFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;

public class FMV_Immunization extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/

    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMV_Immunization.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMV_Immunization.class);
        fileInfo = new FMFile("V IMMUNIZATION") { //

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

    @FMAnnotateFieldInfo(name = "IMMUNIZATION", number = ".01", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer immunization;
    @FMAnnotateFieldInfo(name = "PATIENT NAME", number = ".02", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer patient;
    @FMAnnotateFieldInfo(name = "VISIT", number = ".03", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer visit;
    @FMAnnotateFieldInfo(name = "SERIES", number = ".04", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String seriesCode;
    @FMAnnotateFieldInfo(name = "REACTION", number = ".06", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String reaction;
    @FMAnnotateFieldInfo(name = "CONTRAINDICATED", number = ".07", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String contraindicated;
    @FMAnnotateFieldInfo(name = "DIAGNOSIS", number = ".08", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer diagnosis;
    @FMAnnotateFieldInfo(name = "DIAGNOSIS 2", number = ".09", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer diag2;
    @FMAnnotateFieldInfo(name = "DIAGNOSIS 3", number = ".1", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer diag3;
    @FMAnnotateFieldInfo(name = "DIAGNOSIS 4", number = ".11", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer diag4;
    @FMAnnotateFieldInfo(name = "DIAGNOSIS 5", number = ".12", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer diag5;
    @FMAnnotateFieldInfo(name = "DIAGNOSIS 6", number = ".13", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer diag6;
    @FMAnnotateFieldInfo(name = "DIAGNOSIS 7", number = ".14", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer diag7;
    @FMAnnotateFieldInfo(name = "DIAGNOSIS 8", number = ".15", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer diag8;
    @FMAnnotateFieldInfo(name = "REMARKS", number = "1101", fieldType = FMField.FIELDTYPE.WORD_PROCESSING)
    protected String remarks;
    @FMAnnotateFieldInfo(name = "EVENT DATE AND TIME", number = "1201", fieldType = FMField.FIELDTYPE.DATE)
    protected Date eventDate;
    @FMAnnotateFieldInfo(name = "ORDERING PROVIDER", number = "1202", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer orderProvider;
    @FMAnnotateFieldInfo(name = "ENCOUNTER PROVIDER", number = "1204", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer encounterProvider;
	 private ArrayList<FMICD_Diagnosis> diagnoses = null;



    public FMV_Immunization() {
        super(fileInfo.getFileName());


    }

    public FMV_Immunization(FMResultSet results) {
        super(results);
    }
   
    /*  This method works based on the setInternal being false for Diagnosis 1 - 8
     *   returning ICD9 code. If they are not set to false, then it returns IENs into
     *   the ICD DIAGNOSIS file.
     */
    public Collection<String> getDiagnosesKeyList() {
        ArrayList<String> diagList = new ArrayList<String>();
        if (getDiagnosisValue() != null) {
            diagList.add(getDiagnosisValue());
        }
        if (getDiagnosis2Value() != null) {
            diagList.add(getDiagnosis2Value());
        }
        if (getDiagnosis3Value() != null) {
            diagList.add(getDiagnosis3Value());
        }
        if (getDiagnosis4Value() != null) {
            diagList.add(getDiagnosis4Value());
        }
        if (getDiagnosis5Value() != null) {
            diagList.add(getDiagnosis5Value());
        }
        if (getDiagnosis6Value() != null) {
            diagList.add(getDiagnosis6Value());
        }
        if (getDiagnosis7Value() != null) {
            diagList.add(getDiagnosis7Value());
        }
        if (getDiagnosis8Value() != null) {
            diagList.add(getDiagnosis8Value());
        }
        return diagList;
    }

    public void setDiagnoses(ArrayList<FMICD_Diagnosis> diagArray)
    {
        diagnoses = diagArray;
    }
    /* to use this method, you must first populate it by calling setDiagnoses  */
    public ArrayList<FMICD_Diagnosis> getDiagnoses()
    {
        return diagnoses;
    }
    public String getImmunizationValue (){
        return getValue(".01");
    }
    public Integer getImmunization()
    {
        return immunization;
    }

    public Integer getPatientId() {
        return patient;
    }

    public String getPatientName(){
        return getValue(".02");
    }

    public Integer getVisitIEN(){
       return visit;
    }
    public String getSeries()
    {

        return seriesCode;
    }
    public String getReaction()
    {
        return reaction;
    }
    public String getContraindicated()
    {
        return contraindicated;
    }
    public Integer getDiagnosis()
    {
        return diagnosis;
    }
    public String getDiagnosisValue()
    {
        return getValue(".08");
    }
    public Integer getDiagnosis2()
    {
	return diag2;
    }
    public String getDiagnosis2Value()
    {
        return getValue(".09");
    }
    public Integer getDiagnosis3()
    {
        return diag3;
    }
    public String getDiagnosis3Value()
    {
        return getValue(".1");
    }
    public Integer getDiagnosis4()
    {
        return diag4;
    }
    public String getDiagnosis4Value()
    {
        return getValue(".11");
    }
    public Integer getDiagnosis5()
    {
	return diag5;
    }
    public String getDiagnosis5Value()
    {
        return getValue(".12");
    }
    public Integer getDiagnosis6()
    {
        return diag6;
    }
    public String getDiagnosis6Value()
    {
        return getValue(".13");
    }
    public Integer getDiagnosis7()
    {
	return diag7;
    }
    public String getDiagnosis7Value()
    {
        return getValue(".14");
    }
    public Integer getDiagnosis8()
    {
        return diag8;
    }
    public String getDiagnosis8Value()
    {
        return getValue(".15");
    }
    public String getRemarks()
    {
        return remarks;
    }
    public Date getEventDate()
    {
        return eventDate;
    }
    public Integer getOrderingProvider()
    {
        return orderProvider;
    }
    public String getOrderingProviderValue()
    {
         return getValue("1202");
    }
    public Integer getEncounterProvider()
    {
            return encounterProvider;
    }
    public String getEncounterProviderValue()
    {
           return getValue("1204");
    }



    @Override
    public String toString() {
        return " Visit=[" + getVisitIEN() + "]" +
        " patient=["+getPatientName()+"]"
        + " immunization =["+getImmunizationValue()+"]"
        + " series=["+ getSeries() + "]"
        + " reaction=[" + getReaction() + "]"
        + " contraindicated=[" + getContraindicated() + "]"
        + " date=["+getEventDate() +"]"+
                " diagnosis =[" + getDiagnosisValue() + "]"
                + " remarks = [" + getRemarks() + "]"
                + " ordering provider=[" + getOrderingProviderValue() + "]"
                + " encounter provider=["+ getEncounterProviderValue()+ "]";

    }

}
