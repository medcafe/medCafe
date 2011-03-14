// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2011  Medsphere Systems Corporation
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
// </editor-fold>
package com.medsphere.fmdomain;

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

public class FMProblem extends FMRecord {
    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMProblem.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMProblem.class);
        fileInfo = new FMFile("9000011") {

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

    @FMAnnotateFieldInfo(name = "DIAGNOSIS", number = ".01", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(80)*/)
    protected Integer diagnosis;

    @FMAnnotateFieldInfo(name = "PATIENT NAME", number = ".02", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(9000001)*/)
    protected Integer patientName;

    @FMAnnotateFieldInfo(name = "DATE LAST MODIFIED", number = ".03", fieldType = FMField.FIELDTYPE.DATE)
    protected Date dateLastModified;

    @FMAnnotateFieldInfo(name = "CLASS", number = ".04", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String problemClass;

    @FMAnnotateFieldInfo(name = "PROVIDER NARRATIVE", number = ".05", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(9999999.27)*/)
    protected Integer providerNarrative;

    @FMAnnotateFieldInfo(name = "FACILITY", number = ".06", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(9999999.06)*/)
    protected Integer facility;

    @FMAnnotateFieldInfo(name = "NMBR", number = ".07", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected Double nmbr;

    @FMAnnotateFieldInfo(name = "DATE ENTERED", number = ".08", fieldType = FMField.FIELDTYPE.DATE)
    protected Date dateEntered;

    @FMAnnotateFieldInfo(name = "STATUS", number = ".12", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String status;

    @FMAnnotateFieldInfo(name = "DATE OF ONSET", number = ".13", fieldType = FMField.FIELDTYPE.DATE)
    protected Date dateOfOnset;

    @FMAnnotateFieldInfo(name = "USER LAST MODIFIED", number = ".14", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(200)*/)
    protected Integer userLastModified;

//    @FMAnnotateFieldInfo(name = "CLASSIFICATION", number = ".15", fieldType = FMField.FIELDTYPE.FREE_TEXT)
//    protected String classification;

    @FMAnnotateFieldInfo(name = "PROBLEM", number = "1.01", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(757.01)*/)
    protected Integer problem;

    @FMAnnotateFieldInfo(name = "CONDITION", number = "1.02", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String condition;

    @FMAnnotateFieldInfo(name = "ENTERED BY", number = "1.03", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(200)*/)
    protected Integer enteredBy;

    @FMAnnotateFieldInfo(name = "RECORDING PROVIDER", number = "1.04", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(200)*/)
    protected Integer recordingProvider;

    @FMAnnotateFieldInfo(name = "RESPONSIBLE PROVIDER", number = "1.05", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(200)*/)
    protected Integer responsibleProvider;

    @FMAnnotateFieldInfo(name = "SERVICE", number = "1.06", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(49)*/)
    protected Integer service;

    @FMAnnotateFieldInfo(name = "DATE RESOLVED", number = "1.07", fieldType = FMField.FIELDTYPE.DATE)
    protected Date dateResolved;

    @FMAnnotateFieldInfo(name = "CLINIC", number = "1.08", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(44)*/)
    protected Integer clinic;

    @FMAnnotateFieldInfo(name = "DATE RECORDED", number = "1.09", fieldType = FMField.FIELDTYPE.DATE)
    protected Date dateRecorded;

    @FMAnnotateFieldInfo(name = "SERVICE CONNECTED", number = "1.1", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String serviceConnected;

    @FMAnnotateFieldInfo(name = "PRIORITY", number = "1.14", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String priority;

    protected FMDiagnosisICD icdDiagnosis;

    public FMProblem() {
        super("9000011");
    }

    public FMProblem(FMResultSet results) {
        super("9000011");
        processResults(results);
    }

    public String getDiagnosis() {
        return getValue(".01");
    }

    public String getPatientName() {
        return getValue(".02");
    }

    public Date getDateLastModified() {
        return dateLastModified;
    }

    public String getProblemClass() {
        return problemClass;
    }

    public String getProviderNarrative() {
        return getValue(".05");
    }

    public String getFacility() {
        return getValue(".06");
    }

    public Double getNmbr() {
        return nmbr;
    }

    public Date getDateEntered() {
        return dateEntered;
    }

    public String getStatus() {
        return status;
    }

    public Date getDateOfOnset() {
        return dateOfOnset;
    }

    public String getUserLastModified() {
        return getValue(".14");
    }

//    public String getClassification() {
//        return classification;
//    }

    public String getProblem() {
        return getValue("1.01");
    }

    public String getCondition() {
        return condition;
    }

    public String getEnteredBy() {
        return getValue("1.03");
    }

    public String getRecordingProvider() {
        return getValue("1.04");
    }

    public String getResponsibleProvider() {
        return getValue("1.05");
    }

    public String getService() {
        return getValue("1.06");
    }

    public Date getDateResolved() {
        return dateResolved;
    }

    public String getClinic() {
        return getValue("1.08");
    }

    public Date getDateRecorded() {
        return dateRecorded;
    }

    public String getServiceConnected() {
        return serviceConnected;
    }

    public String getPriority() {
        return priority;
    }

    public FMDiagnosisICD getIcdDiagnosis() {
        return icdDiagnosis;
    }

    public void setIcdDiagnosis(FMDiagnosisICD icdDiagnosis) {
        this.icdDiagnosis = icdDiagnosis;
    }

    @Override
    public String toString() {
        return ""
        + " ien=["+getIEN()+"]"
        + ((getDiagnosis() != null) ? " diagnosis=["+ getDiagnosis() +"]" : "")
        + ((getPatientName() != null) ? " patientName=["+ getPatientName() +"]" : "")
        + ((getDateLastModified() != null) ? " dateLastModified=["+ getDateLastModified() +"]" : "")
        + ((getProblemClass() != null) ? " problemClass=["+ getProblemClass() +"]" : "")
        + ((getProviderNarrative() != null) ? " providerNarrative=["+ getProviderNarrative() +"]" : "")
        + ((getFacility() != null) ? " facility=["+ getFacility() +"]" : "")
        + ((getNmbr() != null) ? " nmbr=["+ getNmbr() +"]" : "")
        + ((getDateEntered() != null) ? " dateEntered=["+ getDateEntered() +"]" : "")
        + ((getStatus() != null) ? " status=["+ getStatus() +"]" : "")
        + ((getDateOfOnset() != null) ? " dateOfOnset=["+ getDateOfOnset() +"]" : "")
        + ((getUserLastModified() != null) ? " userLastModified=["+ getUserLastModified() +"]" : "")
//        + ((getClassification() != null) ? " classification=["+ getClassification() +"]" : "")
        + ((getProblem() != null) ? " problem=["+ getProblem() +"]" : "")
        + ((getCondition() != null) ? " condition=["+ getCondition() +"]" : "")
        + ((getEnteredBy() != null) ? " enteredBy=["+ getEnteredBy() +"]" : "")
        + ((getRecordingProvider() != null) ? " recordingProvider=["+ getRecordingProvider() +"]" : "")
        + ((getResponsibleProvider() != null) ? " responsibleProvider=["+ getResponsibleProvider() +"]" : "")
        + ((getService() != null) ? " service=["+ getService() +"]" : "")
        + ((getDateResolved() != null) ? " dateResolved=["+ getDateResolved() +"]" : "")
        + ((getClinic() != null) ? " clinic=["+ getClinic() +"]" : "")
        + ((getDateRecorded() != null) ? " dateRecorded=["+ getDateRecorded() +"]" : "")
        + ((getServiceConnected() != null) ? " serviceConnected=["+ getServiceConnected() +"]" : "")
        + ((getPriority() != null) ? " priority=["+ getPriority() +"]" : "")
        + ((getIcdDiagnosis() != null) ? "\n\tdiagnosis: " + getIcdDiagnosis() : "")
        ;
    }
}
