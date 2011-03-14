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
 * container class of fileman V CPT information
 */

import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;

import java.util.Map;
import java.util.Set;

import com.medsphere.fileman.FMAnnotateFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;

import com.medsphere.fmdomain.FMICD_Diagnosis;

public class FMV_CPT extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMV_CPT.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMV_CPT.class);
        fileInfo = new FMFile("V CPT") { //

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
    @FMAnnotateFieldInfo(name = "CPT", number = ".01", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer cpt;
    @FMAnnotateFieldInfo(name = "PATIENT NAME", number = ".02", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer patient;
    @FMAnnotateFieldInfo(name = "VISIT", number = ".03", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer visit;
    @FMAnnotateFieldInfo(name = "PROVIDER NARRATIVE", number = ".04", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer narrative;
    @FMAnnotateFieldInfo(name = "DIAGNOSIS", number = ".05", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer diagnosis;
    @FMAnnotateFieldInfo(name = "PRINCIPAL PROCEDURE", number = ".07", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String principalProcedure;
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
    @FMAnnotateFieldInfo(name = " QUANTITY", number = ".16", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected Double quantity;
    @FMAnnotateFieldInfo(name = "EVENT DATE AND TIME", number = "1201", fieldType = FMField.FIELDTYPE.DATE)
    protected Date eventDate;
    @FMAnnotateFieldInfo(name = "ORDERING PROVIDER", number = "1202", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer orderingProvider;
    @FMAnnotateFieldInfo(name = "ENCOUNTER PROVIDER", number = "1204", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer encounterProvider;
    @FMAnnotateFieldInfo(name = "COMMENTS", number = "81101", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String comments;
    protected ArrayList<FMICD_Diagnosis> diagnoses = null;

    protected FMCPT cptRecord;


    public FMV_CPT() {
        super(fileInfo.getFileName());


    }

    public FMV_CPT(FMResultSet results) {
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
    public Integer getCpt() {
        return cpt;
    }

    public String getCptValue() {
        return getValue(".01");
    }

    public Integer getPatient() {
        return patient;
    }

    public String getPatientName() {
        return getValue(".02");
    }

    public Integer getVisitIEN() {
        return visit;
    }

    public Integer getProviderNarrative() {
        return narrative;
    }

    public String getProviderNarrativeValue() {
        return getValue(".04");
    }

    public Double getQuantity() {
        return quantity;
    }

    public String getPrincipalProcedure() {
        return principalProcedure;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public Integer getOrderingProvider() {
        return orderingProvider;
    }

    public String getOrderingProviderValue() {
        return getValue("1202");
    }

    public Integer getEncounterProvider() {
        return encounterProvider;
    }

    public String getEncounterProviderValue() {
        return getValue("1204");
    }

    public String getComments() {
        return comments;
    }

    public Integer getDiagnosis() {
        return diagnosis;
    }

    public String getDiagnosisValue() {
        return getValue(".05");
    }

    public Integer getDiagnosis2() {
        return diag2;
    }

    public String getDiagnosis2Value() {
        return getValue(".09");
    }

    public Integer getDiagnosis3() {
        return diag3;
    }

    public String getDiagnosis3Value() {
        return getValue(".1");
    }

    public Integer getDiagnosis4() {
        return diag4;
    }

    public String getDiagnosis4Value() {
        return getValue(".11");
    }

    public Integer getDiagnosis5() {
        return diag5;
    }

    public String getDiagnosis5Value() {
        return getValue(".12");
    }

    public Integer getDiagnosis6() {
        return diag6;
    }

    public String getDiagnosis6Value() {
        return getValue(".13");
    }

    public Integer getDiagnosis7() {
        return diag7;
    }

    public String getDiagnosis7Value() {
        return getValue(".14");
    }

    public Integer getDiagnosis8() {
        return diag8;
    }

    public String getDiagnosis8Value() {
        return getValue(".15");
    }

    public void setCPTRecord(FMCPT cptRec)
    {
        cptRecord = cptRec;
    }
    public FMCPT getCPTRecord()
    {
        return cptRecord;
    }

    @Override
    public String toString() {
        return " Event Date=[" + getEventDate() + "]"
                + " patient=[" + getPatientName() + "]"
                + " CPT =[" + getCptValue() + "]"
                + " quantity =[" + getQuantity() + "]"
                + " provider narrative =[" + getProviderNarrativeValue() + "]"
                + " principal procedure =[" + getPrincipalProcedure() + "]"
                + " visit IEN =[" + getVisitIEN() + "]"
                + " ordering provider=[" + getOrderingProviderValue() + "]"
                + " encounter provider =[" + getEncounterProviderValue() + "]"
                + " comments =[" + getComments() + "]"
                + " diagnosis =[" + getDiagnosisValue() + "]"
                + " diagnosis =[" + getDiagnosis2Value() + "]"
                + " diagnosis =[" + getDiagnosis3Value() + "]"
                + " diagnosis =[" + getDiagnosis4Value() + "]"
                + " diagnosis =[" + getDiagnosis5Value() + "]"
                + " diagnosis =[" + getDiagnosis6Value() + "]"
                + " diagnosis =[" + getDiagnosis7Value() + "]"
                + " diagnosis =[" + getDiagnosis8Value() + "]"
                + "CPT Record =[" + getCPTRecord() + "]";


    }
}
