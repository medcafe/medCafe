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
import java.util.Map;
import java.util.Set;

import com.medsphere.fileman.FMAnnotateFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;

public class FMLaboratoryTest extends FMRecord {
    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
    domainJavaFields = getDomainJavaFields(FMLaboratoryTest.class);
    domainFields = getFieldsInDomain(domainJavaFields);
    domainNumbers = getNumericMapping(FMLaboratoryTest.class);
    fileInfo = new FMFile("LABORATORY TEST") {
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

    @FMAnnotateFieldInfo(name = "NAME", number = ".01", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String name;

    @FMAnnotateFieldInfo(name = "TEST COST", number = "1", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected Double testCost;

    @FMAnnotateFieldInfo(name = "TYPE", number = "3", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String type;

    @FMAnnotateFieldInfo(name = "SUBSCRIPT", number = "4", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String subscript;

    @FMAnnotateFieldInfo(name = "LOCATION (DATA NAME)", number = "5", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String locationDataName;

    @FMAnnotateFieldInfo(name = "UNIQUE ACCESSION #", number = "7", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String uniqueAccessionNumber;

    @FMAnnotateFieldInfo(name = "UNIQUE COLLECTION SAMPLE", number = "8", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String uniqueCollectionSample;

    @FMAnnotateFieldInfo(name = "LAB COLLECTION SAMPLE", number = "9", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(62)*/)
    protected Integer labCollectionSample;

    @FMAnnotateFieldInfo(name = "REQUIRED TEST", number = "10", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String requiredTest;

    @FMAnnotateFieldInfo(name = "PROCEDURE (SNOMED)", number = "14", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(61.5)*/)
    protected Integer procedureSnomed;

    @FMAnnotateFieldInfo(name = "EXTRA LABELS", number = "16", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected Double extraLabels;

    @FMAnnotateFieldInfo(name = "HIGHEST URGENCY ALLOWED", number = "17", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(62.05)*/)
    protected Integer highestUrgencyAllowed;

    @FMAnnotateFieldInfo(name = "FORCED URGENCY", number = "18", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(62.05)*/)
    protected Integer forcedUrgency;

    @FMAnnotateFieldInfo(name = "PRINT NAME", number = "51", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String printName;

    @FMAnnotateFieldInfo(name = "PRINT CODE", number = "53", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String printCode;

    @FMAnnotateFieldInfo(name = "PRETTY PRINT ENTRY", number = "54", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String prettyPrintEntry;

    @FMAnnotateFieldInfo(name = "PRINT ORDER", number = "56", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected Double printOrder;

    @FMAnnotateFieldInfo(name = "NATIONAL VA LAB CODE", number = "64", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(64)*/)
    protected Integer nationalVALabCode;

    @FMAnnotateFieldInfo(name = "RESULT NLT CODE", number = "64.1", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(64)*/)
    protected Integer resultNltCode;

    @FMAnnotateFieldInfo(name = "CATALOG ITEM", number = "64.2", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String catalogItem;

    @FMAnnotateFieldInfo(name = "EDIT CODE", number = "98", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(62.07)*/)
    protected Integer editCode;

    @FMAnnotateFieldInfo(name = "EXECUTE ON DATA REVIEW", number = "99.2", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(62.07)*/)
    protected Integer executeOnDataReview;

    @FMAnnotateFieldInfo(name = "REQUIRED COMMENT", number = "320", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(62.07)*/)
    protected Integer requiredComment;

    @FMAnnotateFieldInfo(name = "DATA NAME", number = "400", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(63)*/)
    protected Integer dataName;

    @FMAnnotateFieldInfo(name = "DATA TYPE", number = "411", fieldType = FMField.FIELDTYPE.COMPUTED)
    protected String dataType;

    protected String loincCode;
    protected String cptCode;

    protected String collectionSampleName;
    protected String specimenName;
    protected String specimenSnomedCode;

    public FMLaboratoryTest() {
        super("LABORATORY TEST");
    }

    public FMLaboratoryTest(FMResultSet results) {
    super("LABORATORY TEST");
    processResults(results);
    }

    public String getName() {
        return name;
    }

    public Double getTestCost() {
        return testCost;
    }

    public String getType() {
        return type;
    }

    public String getSubscript() {
        return subscript;
    }

    public String getLocationDataName() {
        return getValue("400");
    }

    public String getUniqueAccessionNumber() {
        return uniqueAccessionNumber;
    }

    public String getUniqueCollectionSample() {
        return uniqueCollectionSample;
    }

    public String getLabCollectionSample() {
        return getValue("9");
    }

    public String getRequiredTest() {
        return requiredTest;
    }

    public String getProcedureSnomed() {
        return getValue("14");
    }

    public Double getExtraLabels() {
        return extraLabels;
    }

    public String getHighestUrgencyAllowed() {
        return getValue("17");
    }

    public String getForcedUrgency() {
        return getValue("18");
    }

    public String getPrintName() {
        return printName;
    }

    public String getPrintCode() {
        return printCode;
    }

    public String getPrettyPrintEntry() {
        return prettyPrintEntry;
    }

    public Double getPrintOrder() {
        return printOrder;
    }

    public String getNationalVALabCode() {
        return getValue("64");
    }

    public String getResultNltCode() {
        return getValue("64.1");
    }

    public String getCatalogItem() {
        return catalogItem;
    }

    public String getEditCode() {
        return getValue("98");
    }

    public String getExecuteOnDataReview() {
        return getValue("99.2");
    }

    public String getRequiredComment() {
        return getValue("320");
    }

    public String getDataName() {
        return getValue("400");
    }

    public String getDataType() {
        return dataType;
    }

    public String getLoincCode() {
        return loincCode;
    }

    public void setLoincCode(String loincCode) {
        this.loincCode = loincCode;
    }

    public String getCptCode() {
        return cptCode;
    }

    public void setCptCode(String cptCode) {
        this.cptCode = cptCode;
    }

    public String getCollectionSampleName() {
        return collectionSampleName;
    }

    public void setCollectionSampleName(String collectionSampleName) {
        this.collectionSampleName = collectionSampleName;
    }

    public String getSpecimenName() {
        return specimenName;
    }

    public void setSpecimenName(String specimenName) {
        this.specimenName = specimenName;
    }

    public String getSpecimenSnomedCode() {
        return specimenSnomedCode;
    }

    public void setSpecimenSnomedCode(String specimenSnomedCode) {
        this.specimenSnomedCode = specimenSnomedCode;
    }

    @Override
    public String toString() {
    return "IEN: " + getIEN()

        + ((getName() != null) ? " name=["+ getName() +"]" : "")
        + ((getTestCost() != null) ? " testCost=["+ getTestCost() +"]" : "")
        + ((getType() != null) ? " type=["+ getType() +"]" : "")
        + ((getSubscript() != null) ? " subscript=["+ getSubscript() +"]" : "")
        + ((getLocationDataName() != null) ? " locationDataName=["+ getLocationDataName() +"]" : "")
        + ((getUniqueAccessionNumber() != null) ? " uniqueAccessionNumber=["+ getUniqueAccessionNumber() +"]" : "")
        + ((getUniqueCollectionSample() != null) ? " uniqueCollectionSample=["+ getUniqueCollectionSample() +"]" : "")
        + ((getLabCollectionSample() != null) ? " labCollectionSample=["+ getLabCollectionSample() +"]" : "")
        + ((getRequiredTest() != null) ? " requiredTest=["+ getRequiredTest() +"]" : "")
        + ((getProcedureSnomed() != null) ? " procedureSnomed=["+ getProcedureSnomed() +"]" : "")
        + ((getExtraLabels() != null) ? " extraLabels=["+ getExtraLabels() +"]" : "")
        + ((getHighestUrgencyAllowed() != null) ? " highestUrgencyAllowed=["+ getHighestUrgencyAllowed() +"]" : "")
        + ((getForcedUrgency() != null) ? " forcedUrgency=["+ getForcedUrgency() +"]" : "")
        + ((getPrintName() != null) ? " printName=["+ getPrintName() +"]" : "")
        + ((getPrintCode() != null) ? " printCode=["+ getPrintCode() +"]" : "")
        + ((getPrettyPrintEntry() != null) ? " prettyPrintEntry=["+ getPrettyPrintEntry() +"]" : "")
        + ((getPrintOrder() != null) ? " printOrder=["+ getPrintOrder() +"]" : "")
        + ((getNationalVALabCode() != null) ? " nationalVALabCode=["+ getNationalVALabCode() +"]" : "")
        + ((getResultNltCode() != null) ? " resultNltCode=["+ getResultNltCode() +"]" : "")
        + ((getCatalogItem() != null) ? " catalogItem=["+ getCatalogItem() +"]" : "")
        + ((getEditCode() != null) ? " editCode=["+ getEditCode() +"]" : "")
        + ((getExecuteOnDataReview() != null) ? " executeOnDataReview=["+ getExecuteOnDataReview() +"]" : "")
        + ((getRequiredComment() != null) ? " requiredComment=["+ getRequiredComment() +"]" : "")
        + ((getDataName() != null) ? " dataName=["+ getDataName() +"]" : "")
        + ((getDataType() != null) ? " dataType=["+ getDataType() +"]" : "")
        + ((getLoincCode() != null) ? " loincCode=["+ getLoincCode() + "]" : "")
        + ((getCptCode() != null) ? " cptCode=["+ getCptCode() + "]" : "")
        + ((getCollectionSampleName() != null) ? " collectionSampleName=["+ getCollectionSampleName() + "]" : "")
        + ((getSpecimenName() != null) ? " specimenName=["+ getSpecimenName() + "]" : "")
        + ((getSpecimenSnomedCode() != null) ? " specimenSnomedCode=["+ getSpecimenSnomedCode() + "]" : "")
       ;
    }
}
