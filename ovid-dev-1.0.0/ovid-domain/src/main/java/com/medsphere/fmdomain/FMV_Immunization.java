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
    public static final String FEVER = "1";
    public static final String IRRITABILITY = "2";
    public static final String LOCAL_REACTION = "3";
    public static final String VOMITING = "4";
    public static final String RASH = "5";
    public static final String LETHARGY = "6";
    public static final String CONVULSIONS = "7";
    public static final String ARTHRITIS = "8";
    public static final String ANAPHYLAXIS = "9";
    public static final String RESPIRATORY_DISTRESS = "10";
    public static final String OTHER = "11";
    public static final String NONE = "0";
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
    protected Integer visitDate;
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
    @FMAnnotateFieldInfo(name = "REMARKS", number = "1101", fieldType = FMField.FIELDTYPE.WORD_PROCESSING)
    protected String remarks;
    @FMAnnotateFieldInfo(name = "EVENT DATE AND TIME", number = "1201", fieldType = FMField.FIELDTYPE.DATE)
    protected Date eventDate;
    @FMAnnotateFieldInfo(name = "ORDERING PROVIDER", number = "1202", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    Integer orderProvider;
    @FMAnnotateFieldInfo(name = "ENCOUNTER PROVIDER", number = "1204", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    Integer encounterProvider;



    public FMV_Immunization() {
        super(fileInfo.getFileName());


    }

    public FMV_Immunization(FMResultSet results) {
        super(results);
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

    public Integer getVisit(){
       return visitDate;
    }
    public String getVisitDate(){
        return getValue(".03");
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
        return " Visit=[" + getVisitDate() + "]" +
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
