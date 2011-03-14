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
package com.medsphere.fmdomain.rpms;

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
import com.medsphere.fmdomain.FMLaboratoryTest;
import com.medsphere.ovid.domain.rpms.IsAVLAbTestResult;
import com.medsphere.ovid.model.domain.patient.ResultDetail;

public class FMVLab extends FMRecord implements IsAVLAbTestResult {
    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
    domainJavaFields = getDomainJavaFields(FMVLab.class);
    domainFields = getFieldsInDomain(domainJavaFields);
    domainNumbers = getNumericMapping(FMVLab.class);
    fileInfo = new FMFile("V LAB") {
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

    @FMAnnotateFieldInfo(name = "LAB TEST", number = ".01", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(60)*/)
    protected Integer labTest;

    @FMAnnotateFieldInfo(name = "PATIENT NAME", number = ".02", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(9000001)*/)
    protected Integer patientName;

    @FMAnnotateFieldInfo(name = "VISIT", number = ".03", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(9000010)*/)
    protected Integer visit;

    @FMAnnotateFieldInfo(name = "RESULTS", number = ".04", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String results;

    @FMAnnotateFieldInfo(name = "ABNORMAL", number = ".05", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String abnormal;

    @FMAnnotateFieldInfo(name = "LR ACCESSION NO.", number = ".06", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String lrAccessionNumber;

    @FMAnnotateFieldInfo(name = "UNITS", number = "1101", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String units;

    @FMAnnotateFieldInfo(name = "ORDER", number = "1102", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected Double order;

    @FMAnnotateFieldInfo(name = "SITE", number = "1103", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(61)*/)
    protected Integer site;

    @FMAnnotateFieldInfo(name = "REFERENCE LOW", number = "1104", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String referenceLow;

    @FMAnnotateFieldInfo(name = "REFERENCE HIGH", number = "1105", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String referenceHigh;

    @FMAnnotateFieldInfo(name = "THERAPEUTIC LOW", number = "1106", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String therapeuticLow;

    @FMAnnotateFieldInfo(name = "THERAPEUTIC HIGH", number = "1107", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String therapeuticHigh;

    @FMAnnotateFieldInfo(name = "SOURCE OF DATA INPUT", number = "1108", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String sourceOfDataInput;

    @FMAnnotateFieldInfo(name = "CURRENT STATUS FLAG", number = "1109", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String currentStatusFlag;

    @FMAnnotateFieldInfo(name = "LAB TEST COST", number = "1110", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected Double labTestCost;

    @FMAnnotateFieldInfo(name = "BILLABLE ITEM", number = "1111", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String billableItem;

    @FMAnnotateFieldInfo(name = "LOINC CODE", number = "1113", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(95.3)*/)
    protected Integer loincCode;

    @FMAnnotateFieldInfo(name = "COLLECTION SAMPLE", number = "1114", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(62)*/)
    protected Integer collectionSample;

    @FMAnnotateFieldInfo(name = "COLLECTION DATE AND TIME", number = "1201", fieldType = FMField.FIELDTYPE.DATE)
    protected Date collectionDateAndTime;

    @FMAnnotateFieldInfo(name = "ORDERING PROVIDER", number = "1202", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(200)*/)
    protected Integer orderingProvider;

    @FMAnnotateFieldInfo(name = "CLINIC", number = "1203", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(40.7)*/)
    protected Integer clinic;

    @FMAnnotateFieldInfo(name = "ENCOUNTER PROVIDER", number = "1204", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(200)*/)
    protected Integer encounterProvider;

    @FMAnnotateFieldInfo(name = "PARENT", number = "1208", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(9000010.09)*/)
    protected Integer parentRecord;

    @FMAnnotateFieldInfo(name = "EXTERNAL KEY", number = "1209", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String externalKey;

    @FMAnnotateFieldInfo(name = "OUTSIDE PROVIDER NAME", number = "1210", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String outsideProviderName;

    @FMAnnotateFieldInfo(name = "ORDERING DATE", number = "1211", fieldType = FMField.FIELDTYPE.DATE)
    protected Date orderingDate;

    @FMAnnotateFieldInfo(name = "RESULT DATE AND TIME", number = "1212", fieldType = FMField.FIELDTYPE.DATE)
    protected Date resultDateAndTime;

    @FMAnnotateFieldInfo(name = "ANCILLARY POV", number = "1213", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(80)*/)
    protected Integer ancillaryPov;

    @FMAnnotateFieldInfo(name = "ORDERING LOCATION", number = "1215", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(44)*/)
    protected Integer orderingLocation;

    @FMAnnotateFieldInfo(name = "COMMENT1", number = "1301", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String comment1;

    @FMAnnotateFieldInfo(name = "COMMENT2", number = "1302", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String comment2;

    @FMAnnotateFieldInfo(name = "COMMENT3", number = "1303", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String comment3;

    @FMAnnotateFieldInfo(name = "CPT PTR", number = "1401", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(9009021)*/)
    protected Integer cptPtr;

    @FMAnnotateFieldInfo(name = "CPT - BILLABLE ITEMS", number = "1402", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String cptBillableItems;

    @FMAnnotateFieldInfo(name = "DATE PASSED TO EXTERNAL", number = "1501", fieldType = FMField.FIELDTYPE.DATE)
    protected String datePassedToExternal;

    @FMAnnotateFieldInfo(name = "LAB POV", number = "1601", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String labPov;

    @FMAnnotateFieldInfo(name = "LAB PRINT CODE", number = "1602", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String labPrintCode;


    protected FMLaboratoryTest laboratoryTest;

    public FMVLab() {
    super("V LAB");
    }

    public FMVLab(FMResultSet results) {
    super("V LAB");
    processResults(results);
    }

    public void setLaboratoryTest(FMLaboratoryTest laboratoryTest) {
        this.laboratoryTest = laboratoryTest;
    }

    public FMLaboratoryTest getLaboratoryTest() {
        return laboratoryTest;
    }

    public String getLabTest() {
        return getValue(".01");
    }

    public String getPatientName() {
        return getValue(".02");
    }

    public String getVisit() {
        return getValue(".03");
    }

    public String getResults() {
        return results;
    }

    public String getAbnormal() {
        return abnormal;
    }

    public String getAccessionNumber() {
       return getLrAccessionNumber();
    }
    
    public String getLrAccessionNumber() {
        return lrAccessionNumber;
    }

    public String getUnits() {
        return units;
    }

    public Double getOrder() {
        return order;
    }

    public String getSite() {
        return getValue("1103");
    }

    public String getReferenceLow() {
        return referenceLow;
    }

    public String getReferenceHigh() {
        return referenceHigh;
    }

    public String getTherapeuticLow() {
        return therapeuticLow;
    }

    public String getTherapeuticHigh() {
        return therapeuticHigh;
    }

    public String getSourceOfDataInput() {
        return sourceOfDataInput;
    }

    public String getCurrentStatusFlag() {
        return currentStatusFlag;
    }

    public Double getLabTestCost() {
        return labTestCost;
    }

    public String getBillableItem() {
        return billableItem;
    }

    public String getLoincCode() {
        return getValue("1113");
    }

    public String getCollectionSample() {
        return getValue("1114");
    }

    public Date getCollectionDateAndTime() {
        return collectionDateAndTime;
    }

    public String getOrderingProvider() {
        return getValue("1202");
    }

    public String getClinic() {
        return getValue("1203");
    }

    public String getEncounterProvider() {
        return getValue("1204");
    }

    public String getParentRecord() {
        return getValue("1208");
    }

    public String getExternalKey() {
        return externalKey;
    }

    public String getOutsideProviderName() {
        return outsideProviderName;
    }

    public Date getOrderingDate() {
        return orderingDate;
    }

    public Date getResultDateAndTime() {
        return resultDateAndTime;
    }

    public String getAncillaryPov() {
        return getValue("1213");
    }

    public String getOrderingLocation() {
        return getValue("1215");
    }

    public String getComment1() {
        return comment1;
    }

    public String getComment2() {
        return comment2;
    }

    public String getComment3() {
        return comment3;
    }

    public String getCptPtr() {
        return getValue("1401");
    }

    public String getCptBillableItems() {
        return cptBillableItems;
    }

    public String getDatePassedToExternal() {
        return datePassedToExternal;
    }

    public String getLabPov() {
        return labPov;
    }

    public String getLabPrintCode() {
        return labPrintCode;
    }

    @Override
    public String toString() {

    return
        ((getLaboratoryTest() != null) ? " laboratoryTest=[" + getLaboratoryTest() + "]" : "")
        + "\n\tIEN: " + getIEN()
        + ((getLabTest() != null) ? " labTest=["+ getLabTest() +"]" : "")
        + ((getPatientName() != null) ? " patientName=["+ getPatientName() +"]" : "")
        + ((getVisit() != null) ? " visit=["+ getVisit() +"]" : "")
        + ((getResults() != null) ? " results=["+ getResults() +"]" : "")
        + ((getAbnormal() != null) ? " abnormal=["+ getAbnormal() +"]" : "")
        + ((getLrAccessionNumber() != null) ? " lrAccessionNumber=["+ getLrAccessionNumber() +"]" : "")
        + ((getUnits() != null) ? " units=["+ getUnits() +"]" : "")
        + ((getOrder() != null) ? " order=["+ getOrder() +"]" : "")
        + ((getSite() != null) ? " site=["+ getSite() +"]" : "")
        + ((getReferenceLow() != null) ? " referenceLow=["+ getReferenceLow() +"]" : "")
        + ((getReferenceHigh() != null) ? " referenceHigh=["+ getReferenceHigh() +"]" : "")
        + ((getTherapeuticLow() != null) ? " therapeuticLow=["+ getTherapeuticLow() +"]" : "")
        + ((getTherapeuticHigh() != null) ? " therapeuticHigh=["+ getTherapeuticHigh() +"]" : "")
        + ((getSourceOfDataInput() != null) ? " sourceOfDataInput=["+ getSourceOfDataInput() +"]" : "")
        + ((getCurrentStatusFlag() != null) ? " currentStatusFlag=["+ getCurrentStatusFlag() +"]" : "")
        + ((getLabTestCost() != null) ? " labTestCost=["+ getLabTestCost() +"]" : "")
        + ((getBillableItem() != null) ? " billableItem=["+ getBillableItem() +"]" : "")
        + ((getLoincCode() != null) ? " loincCode=["+ getLoincCode() +"]" : "")
        + ((getCollectionSample() != null) ? " collectionSample=["+ getCollectionSample() +"]" : "")
        + ((getCollectionDateAndTime() != null) ? " collectionDateAndTime=["+ getCollectionDateAndTime() +"]" : "")
        + ((getOrderingProvider() != null) ? " orderingProvider=["+ getOrderingProvider() +"]" : "")
        + ((getClinic() != null) ? " clinic=["+ getClinic() +"]" : "")
        + ((getEncounterProvider() != null) ? " encounterProvider=["+ getEncounterProvider() +"]" : "")
        + ((getParentRecord() != null) ? " parentRecord=["+ getParentRecord() +"]" : "")
        + ((getExternalKey() != null) ? " externalKey=["+ getExternalKey() +"]" : "")
        + ((getOutsideProviderName() != null) ? " outsideProviderName=["+ getOutsideProviderName() +"]" : "")
        + ((getOrderingDate() != null) ? " orderingDate=["+ getOrderingDate() +"]" : "")
        + ((getResultDateAndTime() != null) ? " resultDateAndTime=["+ getResultDateAndTime() +"]" : "")
        + ((getAncillaryPov() != null) ? " ancillaryPov=["+ getAncillaryPov() +"]" : "")
        + ((getOrderingLocation() != null) ? " orderingLocation=["+ getOrderingLocation() +"]" : "")
        + ((getComment1() != null) ? " comment1=["+ getComment1() +"]" : "")
        + ((getComment2() != null) ? " comment2=["+ getComment2() +"]" : "")
        + ((getComment3() != null) ? " comment3=["+ getComment3() +"]" : "")
        + ((getCptPtr() != null) ? " cptPtr=["+ getCptPtr() +"]" : "")
        + ((getCptBillableItems() != null) ? " cptBillableItems=["+ getCptBillableItems() +"]" : "")
        + ((getDatePassedToExternal() != null) ? " datePassedToExternal=["+ getDatePassedToExternal() +"]" : "")
        + ((getLabPov() != null) ? " labPov=["+ getLabPov() +"]" : "")
        + ((getLabPrintCode() != null) ? " labPrintCode=["+ getLabPrintCode() +"]" : "")
        ;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((abnormal == null) ? 0 : abnormal.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FMVLab other = (FMVLab) obj;
        if (getIEN() == null) {
            if (other.getIEN() != null)
                return false;
        } else if (!getIEN().equals(other.getIEN()))
            return false;
        return true;
    }

}
