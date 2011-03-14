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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.medsphere.fileman.FMAnnotateFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.ovid.domain.ov.IsAVitalSign;

public class FMVMeasurement extends FMRecord implements IsAVitalSign {
    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMVMeasurement.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMVMeasurement.class);
        fileInfo = new FMFile("V MEASUREMENT") {

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

    @FMAnnotateFieldInfo(name = "TYPE", number = ".01", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(9999999.07)*/)
    protected Integer type;

    @FMAnnotateFieldInfo(name = "PATIENT NAME", number = ".02", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(9000001)*/)
    protected Integer patientName;

    @FMAnnotateFieldInfo(name = "VISIT", number = ".03", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(9000010)*/)
    protected Integer visit;

    @FMAnnotateFieldInfo(name="VALUE", number = ".04", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String value;

    @FMAnnotateFieldInfo(name = "PERCENTILE", number = ".05", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected Double percentile;

    @FMAnnotateFieldInfo(name = "NUMERATOR ON VC/VU", number = ".06", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected Double numeratorOnVCVU;

    @FMAnnotateFieldInfo(name = "DATE/TIME VITALS TAKEN", number = ".07", fieldType = FMField.FIELDTYPE.DATE)
    protected Date dateTimeVitalsTaken;

    @FMAnnotateFieldInfo(name = "ENTERED BY", number = ".08", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(200)*/)
    protected Integer enteredBy;

    @FMAnnotateFieldInfo(name = "SUPPLEMENTAL O2", number = "1.4", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String supplementalO2;

    @FMAnnotateFieldInfo(name = "ENTERED IN ERROR", number = "2", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String enteredInError;

    @FMAnnotateFieldInfo(name = "ERROR ENTERED BY", number = "3", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(200)*/)
    protected Integer errorEnteredBy;

    @FMAnnotateFieldInfo(name = "REASON ENTERED IN ERROR", number = "4", fieldType = FMField.FIELDTYPE.SUBFILE)
    protected String reasonEnteredInError;

    @FMAnnotateFieldInfo(name = "QUALIFIER", number = "5", fieldType = FMField.FIELDTYPE.SUBFILE)
    protected FMFile qualifier;

    @FMAnnotateFieldInfo(name = "DATE/TIME ENTERED", number = "1201", fieldType = FMField.FIELDTYPE.DATE)
    protected Date dateTimeEntered;

    @FMAnnotateFieldInfo(name = "ORDERING PROVIDER", number = "1202", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(200)*/)
    protected Integer orderingProvider;

    @FMAnnotateFieldInfo(name = "CLINIC", number = "1203", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(40.7)*/)
    protected Integer clinic;

    @FMAnnotateFieldInfo(name = "ENCOUNTER PROVIDER", number = "1204", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(200)*/)
    protected Integer encounterProvider;

    @FMAnnotateFieldInfo(name = "PARENT", number = "1208", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(9000010.01)*/)
    protected Integer parentRecord;

    @FMAnnotateFieldInfo(name = "EXTERNAL KEY", number = "1209", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String externalKey;

    @FMAnnotateFieldInfo(name = "OUTSIDE PROVIDER NAME", number = "1210", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String outsideProviderName;

    @FMAnnotateFieldInfo(name = "ORDERING LOCATION", number = "1215", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(44)*/)
    protected Integer orderingLocation;

    @FMAnnotateFieldInfo(name = "EDITED FLAG", number = "80101", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String editedFlag;

    @FMAnnotateFieldInfo(name = "DATA SOURCE", number = "80102", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String dataSource;

    @FMAnnotateFieldInfo(name = "COMMENTS", number = "81101", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String comments;

    @FMAnnotateFieldInfo(name = "%RW", number = "9999999", fieldType = FMField.FIELDTYPE.COMPUTED)
    protected String percentRW;

    protected Collection<String> qualifiers = new HashSet<String>();

    public FMVMeasurement() {
        super("V MEASUREMENT");
    }

    public FMVMeasurement(FMResultSet results) {
        super("V MEASUREMENT");
        processResults(results);
    }

    @Override
    public String getType() {
        return getValue(".01");
    }

    public String getPatientName() {
        return getValue(".02");
    }

    public String getVisit() {
        return getValue(".03");
    }

    @Override
    public String getValue() {
        return value;
    }

    public Double getPercentile() {
        return percentile;
    }

    public Double getNumeratorOnVCVU() {
        return numeratorOnVCVU;
    }

    public Date getDateTimeVitalsTaken() {
        return dateTimeVitalsTaken;
    }

    public String getEnteredBy() {
        return getValue(".08");
    }

    public String getSupplementalO2() {
        return supplementalO2;
    }

    public String getEnteredInError() {
        return enteredInError;
    }

    public String getErrorEnteredBy() {
        return getValue("3");
    }

    public String getReasonEnteredInError() {
        return reasonEnteredInError;
    }

    public FMFile getQualifierSubfile() {
        if (qualifier == null) {
            qualifier = new FMFile("5");
            qualifier.addField(".01");
            qualifier.setParentRecord(this);
        }
        return qualifier;
    }

    public void addQualifier(String qualifier) {
        qualifiers.add(qualifier);
    }

    @Override
    public Collection<String> getQualifiers() {
        return qualifiers;
    }

    public Date getDateTimeEntered() {
        return dateTimeEntered;
    }

    @Override
    public Date getDateTaken() {
        return dateTimeVitalsTaken;
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

    public String getOrderingLocation() {
        return getValue("1215");
    }

    public String getEditedFlag() {
        return editedFlag;
    }

    public String getDataSource() {
        return dataSource;
    }

    public String getComments() {
        return comments;
    }

    public String getPercentRW() {
        return percentRW;
    }


    @Override
    public String toString() {

    return ""

        + ((getIEN() != null) ? "ien=["+getIEN()+"]" : "")
        + ((getType() != null) ? " type=["+ getType() +"]" : "")
        + ((getPatientName() != null) ? " patientName=["+ getPatientName() +"]" : "")
        + ((getVisit() != null) ? " visit=["+ getVisit() +"]" : "")
        + ((getValue() != null) ? " value=["+ getValue() +"]" : "")
        + ((getPercentile() != null) ? " percentile=["+ getPercentile() +"]" : "")
        + ((getNumeratorOnVCVU() != null) ? " numeratorOnVCVU=["+ getNumeratorOnVCVU() +"]" : "")
        + ((getDateTimeVitalsTaken() != null) ? " dateTimeVitalsTaken=["+ getDateTimeVitalsTaken() +"]" : "")
        + ((getEnteredBy() != null) ? " enteredBy=["+ getEnteredBy() +"]" : "")
        + ((getSupplementalO2() != null) ? " supplementalO2=["+ getSupplementalO2() +"]" : "")
        + ((getEnteredInError() != null) ? " enteredInError=["+ getEnteredInError() +"]" : "")
        + ((getErrorEnteredBy() != null) ? " errorEnteredBy=["+ getErrorEnteredBy() +"]" : "")
        + ((getReasonEnteredInError() != null) ? " reasonEnteredInError=["+ getReasonEnteredInError() +"]" : "")
        + ((getQualifiers() != null) ? " qualifiers=["+ getQualifiers() +"]" : "")
        + ((getDateTimeEntered() != null) ? " dateTimeEntered=["+ getDateTimeEntered() +"]" : "")
        + ((getOrderingProvider() != null) ? " orderingProvider=["+ getOrderingProvider() +"]" : "")
        + ((getClinic() != null) ? " clinic=["+ getClinic() +"]" : "")
        + ((getEncounterProvider() != null) ? " encounterProvider=["+ getEncounterProvider() +"]" : "")
        + ((getParentRecord() != null) ? " parentRecord=["+ getParentRecord() +"]" : "")
        + ((getExternalKey() != null) ? " externalKey=["+ getExternalKey() +"]" : "")
        + ((getOutsideProviderName() != null) ? " outsideProviderName=["+ getOutsideProviderName() +"]" : "")
        + ((getOrderingLocation() != null) ? " orderingLocation=["+ getOrderingLocation() +"]" : "")
        + ((getEditedFlag() != null) ? " editedFlag=["+ getEditedFlag() +"]" : "")
        + ((getDataSource() != null) ? " dataSource=["+ getDataSource() +"]" : "")
        + ((getComments() != null) ? " comments=["+ getComments() +"]" : "")
        + ((getPercentRW() != null) ? " percentRW=["+ getPercentRW() +"]" : "")
        ;

    }

    @Override
    public Date getDateEntered() {
        return getDateTimeVitalsTaken();
    }

    @Override
    public String getName() {
        return getType();
    }

    @Override
    public String getReferenceRange() {
        return null;
    }

    @Override
    public String getUnits() {
        return null;
    }
}
