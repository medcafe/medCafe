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

public class FMVImmunization extends FMRecord {
    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMVImmunization.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMVImmunization.class);
        fileInfo = new FMFile("V IMMUNIZATION") {

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
    protected Integer patientName;

    @FMAnnotateFieldInfo(name = "VISIT", number = ".03", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer visit;

    @FMAnnotateFieldInfo(name = "SERIES", number = ".04", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String series;

    @FMAnnotateFieldInfo(name = "LOT", number = ".05", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer lot;

    @FMAnnotateFieldInfo(name = "REACTION", number = ".06", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer reaction;

    @FMAnnotateFieldInfo(name = "*CONTRAINDICATED", number = ".07", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String contraindicated;

    @FMAnnotateFieldInfo(name = "DOSE OVERRIDE", number = ".08", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String doseOverride;

    @FMAnnotateFieldInfo(name = "INJECTION SITE", number = ".09", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String injectionSite;

    @FMAnnotateFieldInfo(name = "VOLUME", number = ".11", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected Double volume;

    @FMAnnotateFieldInfo(name = "DATE OF VAC INFO STATEMENT", number = ".12", fieldType = FMField.FIELDTYPE.DATE)
    protected Date dateOfVacInfoStatement;

    @FMAnnotateFieldInfo(name = "CREATED BY V CPT ENTRY", number = ".13", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected Double createdByVCptEntry;

    @FMAnnotateFieldInfo(name = "VFC ELIGIBILITY", number = ".14", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String vfcEligibility;

    @FMAnnotateFieldInfo(name = "REMARKS", number = "1101", fieldType = FMField.FIELDTYPE.WORD_PROCESSING)
    protected String remarks;

    @FMAnnotateFieldInfo(name = "EVENT DATE AND TIME", number = "1201", fieldType = FMField.FIELDTYPE.DATE)
    protected Date eventDateAndTime;

    @FMAnnotateFieldInfo(name = "ORDERING PROVIDER", number = "1202", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer orderingProvider;

    @FMAnnotateFieldInfo(name = "CLINIC", number = "1203", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer clinic;

    @FMAnnotateFieldInfo(name = "ENCOUNTER PROVIDER", number = "1204", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer encounterProvider;

    @FMAnnotateFieldInfo(name = "PARENT", number = "1208", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer parent;

    @FMAnnotateFieldInfo(name = "EXTERNAL KEY", number = "1209", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String externalKey;

    @FMAnnotateFieldInfo(name = "OUTSIDE PROVIDER NAME", number = "1210", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String outsideProviderName;

    @FMAnnotateFieldInfo(name = "ANCILLARY POV", number = "1213", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer ancillaryPov;

    @FMAnnotateFieldInfo(name = "USER LAST UPDATE", number = "1214", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer userLastUpdate;

    @FMAnnotateFieldInfo(name = "ORDERING LOCATION", number = "1215", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer orderingLocation;

    @FMAnnotateFieldInfo(name = "EDITED FLAG", number = "80101", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String editedFlag;

    @FMAnnotateFieldInfo(name = "AUDIT TRAIL", number = "80102", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String auditTrail;

    @FMAnnotateFieldInfo(name = "COMMENTS", number = "81101", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String comments;

    @FMAnnotateFieldInfo(name = "VERIFIED", number = "81201", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String verified;

    @FMAnnotateFieldInfo(name = "PACKAGE", number = "81202", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer packageName;

    @FMAnnotateFieldInfo(name = "DATA SOURCE", number = "81203", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer dataSource;

    protected FMImmunization immunizationDetails;

    public FMVImmunization() {
        super("V IMMUNIZATION");
    }

    public FMVImmunization(FMResultSet results) {
        super("V IMMUNIZATION");
        processResults(results);
    }

    public String getImmunization() {
        return getValue(".01");
    }

    public String getPatientName() {
        return getValue(".02");
    }

    public String getVisit() {
        return getValue(".03");
    }

    public String getSeries() {
        return series;
    }

    public String getLot() {
        return getValue(".05");
    }

    public String getReaction() {
        return getValue(".06");
    }

    public String getContraindicated() {
        return contraindicated;
    }

    public String getDoseOverride() {
        return doseOverride;
    }

    public String getInjectionSite() {
        return injectionSite;
    }

    public Double getVolume() {
        return volume;
    }

    public Date getDateOfVacInfoStatement() {
        return dateOfVacInfoStatement;
    }

    public Double getCreatedByVCptEntry() {
        return createdByVCptEntry;
    }

    public String getVfcEligibility() {
        return vfcEligibility;
    }

    public String getRemarks() {
        return remarks;
    }

    public Date getEventDateAndTime() {
        return eventDateAndTime;
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

    public String getImmunizationParent() {
        return getValue("1208");
    }

    public String getExternalKey() {
        return externalKey;
    }

    public String getOutsideProviderName() {
        return outsideProviderName;
    }

    public String getAncillaryPov() {
        return getValue("1213");
    }

    public String getUserLastUpdate() {
        return getValue("1214");
    }

    public String getOrderingLocation() {
        return getValue("1215");
    }

    public String getEditedFlag() {
        return editedFlag;
    }

    public String getAuditTrail() {
        return auditTrail;
    }

    public String getComments() {
        return comments;
    }

    public String getVerified() {
        return verified;
    }

    public String getPackageName() {
        return getValue("81202");
    }

    public String getDataSource() {
        return getValue("81203");
    }

    public FMImmunization getImmunizationDetails() {
        return immunizationDetails;
    }

    public void setImmunizationDetails(FMImmunization immunizationDetails) {
        this.immunizationDetails = immunizationDetails;
    }

    @Override
    public String toString() {
    return "ien=["+ getIEN() + "]"
        + ((getImmunization() != null) ? " immunization=["+ getImmunization() +"]" : "")
        + ((getPatientName() != null) ? " patientName=["+ getPatientName() +"]" : "")
        + ((getVisit() != null) ? " visit=["+ getVisit() +"]" : "")
        + ((getSeries() != null) ? " series=["+ getSeries() +"]" : "")
        + ((getLot() != null) ? " lot=["+ getLot() +"]" : "")
        + ((getReaction() != null) ? " reaction=["+ getReaction() +"]" : "")
        + ((getContraindicated() != null) ? " contraindicated=["+ getContraindicated() +"]" : "")
        + ((getDoseOverride() != null) ? " doseOverride=["+ getDoseOverride() +"]" : "")
        + ((getInjectionSite() != null) ? " injectionSite=["+ getInjectionSite() +"]" : "")
        + ((getVolume() != null) ? " volume=["+ getVolume() +"]" : "")
        + ((getDateOfVacInfoStatement() != null) ? " dateOfVacInfoStatement=["+ getDateOfVacInfoStatement() +"]" : "")
        + ((getCreatedByVCptEntry() != null) ? " createdByVCptEntry=["+ getCreatedByVCptEntry() +"]" : "")
        + ((getVfcEligibility() != null) ? " vfcEligibility=["+ getVfcEligibility() +"]" : "")
        + ((getRemarks() != null) ? " remarks=["+ getRemarks() +"]" : "")
        + ((getEventDateAndTime() != null) ? " eventDateAndTime=["+ getEventDateAndTime() +"]" : "")
        + ((getOrderingProvider() != null) ? " orderingProvider=["+ getOrderingProvider() +"]" : "")
        + ((getClinic() != null) ? " clinic=["+ getClinic() +"]" : "")
        + ((getEncounterProvider() != null) ? " encounterProvider=["+ getEncounterProvider() +"]" : "")
        + ((getImmunizationParent() != null) ? " parent=["+ getImmunizationParent() +"]" : "")
        + ((getExternalKey() != null) ? " externalKey=["+ getExternalKey() +"]" : "")
        + ((getOutsideProviderName() != null) ? " outsideProviderName=["+ getOutsideProviderName() +"]" : "")
        + ((getAncillaryPov() != null) ? " ancillaryPov=["+ getAncillaryPov() +"]" : "")
        + ((getUserLastUpdate() != null) ? " userLastUpdate=["+ getUserLastUpdate() +"]" : "")
        + ((getOrderingLocation() != null) ? " orderingLocation=["+ getOrderingLocation() +"]" : "")
        + ((getEditedFlag() != null) ? " editedFlag=["+ getEditedFlag() +"]" : "")
        + ((getAuditTrail() != null) ? " auditTrail=["+ getAuditTrail() +"]" : "")
        + ((getComments() != null) ? " comments=["+ getComments() +"]" : "")
        + ((getVerified() != null) ? " verified=["+ getVerified() +"]" : "")
        + ((getPackageName() != null) ? " packageName=["+ getPackageName() +"]" : "")
        + ((getDataSource() != null) ? " dataSource=["+ getDataSource() +"]" : "")
        + ((getImmunizationDetails() != null) ? "\n\tImmunizationDetails: " + getImmunizationDetails() +"]" : "")
        ;
    }
}
