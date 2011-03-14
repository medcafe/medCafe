// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2007-2010  Medsphere Systems Corporation
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
import java.util.Date;

/**
 *
 * @author mark.taylor
 */
public class FMPendingOutpatientOrder extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMPendingOutpatientOrder.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMPendingOutpatientOrder.class);
        fileInfo = new FMFile("PENDING OUTPATIENT ORDERS") {

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
    @FMAnnotateFieldInfo( name="PLACER NUMBER", number=".01", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String placerNumber;
    @FMAnnotateFieldInfo( name="PATIENT", number="1", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer patient;
    @FMAnnotateFieldInfo( name="PATIENT LOCATION", number="1.1", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer patientLocation;
    @FMAnnotateFieldInfo( name="ORDER TYPE", number="2", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String orderType;
    @FMAnnotateFieldInfo( name="QUANTITY TIMING", number="3", fieldType=FMField.FIELDTYPE.SUBFILE)
    protected Collection<FMOrderQuantityTiming> quantityTiming;
    @FMAnnotateFieldInfo( name="ENTERED BY", number="4", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer enteredBy;
    @FMAnnotateFieldInfo( name="PROVIDER", number="5", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer provider;
    @FMAnnotateFieldInfo( name="EFFECTIVE DATE", number="6", fieldType=FMField.FIELDTYPE.DATE)
    protected Date effectiveDate;
    @FMAnnotateFieldInfo( name="PHARMACY ORDERABLE ITEM", number="8", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer pharmacyOrderableItem;
    @FMAnnotateFieldInfo( name="PROVIDER COMMENTS", number="9", fieldType=FMField.FIELDTYPE.WORD_PROCESSING)
    protected Collection<String> providerComments;
    @FMAnnotateFieldInfo( name="PHARMACY INSTRUCTIONS", number="10", fieldType=FMField.FIELDTYPE.WORD_PROCESSING)
    protected Collection<String> pharmacyInstructions;
    @FMAnnotateFieldInfo( name="DRUG", number="11", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer drug;
    @FMAnnotateFieldInfo( name="QUANTITY", number="12", fieldType=FMField.FIELDTYPE.NUMERIC)
    protected Double quantity;
    @FMAnnotateFieldInfo( name="NUMBER OF REFILLS", number="13", fieldType=FMField.FIELDTYPE.NUMERIC)
    protected Double numberOfRefills;
    @FMAnnotateFieldInfo( name="LOGIN DATE", number="15", fieldType=FMField.FIELDTYPE.DATE)
    protected Date loginDate;
    @FMAnnotateFieldInfo( name="MED ROUTE", number="16", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected String route;
    @FMAnnotateFieldInfo(name="ORDER CHECKS", number="18", fieldType=FMField.FIELDTYPE.SUBFILE)
    protected FMOutpatientOrderCheck orderChecks;
    @FMAnnotateFieldInfo( name="PICKUP ROUTING", number="19", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String pickupRouting;
    @FMAnnotateFieldInfo( name="COMMENTS", number="23", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String comments;
    @FMAnnotateFieldInfo( name="SIG", number="24", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String sig;
    @FMAnnotateFieldInfo( name="PRIORITY", number="25", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String priority;
    @FMAnnotateFieldInfo( name="POSSIBLE FILL DATE", number="26", fieldType=FMField.FIELDTYPE.DATE)
    protected Date possibleFillDate;
    @FMAnnotateFieldInfo( name="DAYS SUPPLY", number="101", fieldType=FMField.FIELDTYPE.NUMERIC)
    protected Double daysSupply;
    @FMAnnotateFieldInfo( name="FLAGGED", number="102", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String flagged;
    @FMAnnotateFieldInfo( name="DATE/TIME FLAGGED", number="33", fieldType=FMField.FIELDTYPE.DATE)
    protected Date flaggedDateTime;
    @FMAnnotateFieldInfo( name="FLAGGED BY", number="34", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer flaggedBy;
    @FMAnnotateFieldInfo( name="REASON FOR FLAG", number="35", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String flaggedReason;
    @FMAnnotateFieldInfo( name="DATE/TIME UNFLAGGED", number="36", fieldType=FMField.FIELDTYPE.DATE)
    protected Date unflaggedDateTime;
    @FMAnnotateFieldInfo( name="UNFLAGGED BY", number="37", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer unflaggedBy;
    @FMAnnotateFieldInfo( name="REASON FOR UNFLAGG", number="38", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String unflaggedReason;
    @FMAnnotateFieldInfo( name="PATIENT INSTRUCTIONS ON LABEL FLAG", number="104.1", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String ptInstructionsOnLabel;
    @FMAnnotateFieldInfo( name="PATIENT INSTRUCTIONS", number="104", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String patientInstructions;
    @FMAnnotateFieldInfo( name="EXPANDED PATIENT INSTRUCTIONS", number="105", fieldType=FMField.FIELDTYPE.WORD_PROCESSING)
    protected Collection<String> expandedPatientInstructions;
    @FMAnnotateFieldInfo( name="SIGNATURE STATUS", number="117", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String signatureStatus;

    public FMPendingOutpatientOrder() {
        super("PENDING OUTPATIENT ORDERS");
    }

    public FMPendingOutpatientOrder(FMResultSet results) {
        super("PENDING OUTPATIENT ORDERS");
        processResults(results);
    }

    public String getPlacerNumber() {
        return placerNumber;
    }
    public Integer getPatient() {
        return patient;
    }
    public Integer getPatientLocation() {
        return patientLocation;
    }
    public String getOrderType() {
        return orderType;
    }
    public Collection<FMOrderQuantityTiming> getOrderQuantityTiming() {
        return quantityTiming;
    }
    public Integer getEnteredBy() {
        return enteredBy;
    }
    public Integer getProvider() {
        return provider;
    }
    public Date getEffectiveDate() {
        return effectiveDate;
    }
    public Integer getPharmacyOrderableItem() {
        return pharmacyOrderableItem;
    }
    public Collection<String> getProviderComments() {
        return providerComments;
    }
    public Collection<String> getPharmacyInstructions() {
        return pharmacyInstructions;
    }
    public Integer getDrug() {
        return drug;
    }
    public Double getQuantity() {
        return quantity;
    }
    public Double getNumberOfRefills() {
        return numberOfRefills;
    }
    public Date getLoginDate() {
        return loginDate;
    }
    public String getRoute() {
        return route;
    }
    public FMOutpatientOrderCheck getOrderChecks() {
        if (orderChecks == null) {
            orderChecks = new FMOutpatientOrderCheck();
            orderChecks.setParent( this );
        }
        return orderChecks;
    }
    public String getPickupRouting() {
        return pickupRouting;
    }
    public String getComments() {
        return comments;
    }
    public String getSig() {
        return sig;
    }
    public String getPriority() {
        return priority;
    }
    public Date getPossibleFillDate() {
        return possibleFillDate;
    }
    public Double getDaysSupply() {
        return daysSupply;
    }
    public String getFlagged() {
        return flagged;
    }
    public Date getFlaggedDateTime() {
        return flaggedDateTime;
    }
    public Integer getFlaggedBy() {
        return flaggedBy;
    }
    public String getFlaggedReason() {
        return flaggedReason;
    }
    public Date getUnflaggedDateTime() {
        return unflaggedDateTime;
    }
    public Integer getUnflaggedBy() {
        return unflaggedBy;
    }
    public String getUnflaggedReason() {
        return unflaggedReason;
    }
    public String getPtInstructionsOnLabel() {
        return ptInstructionsOnLabel;
    }
    public String getPatientInstructions() {
        return patientInstructions;
    }
    public Collection<String> getExpandedPatientInstructions() {
        return expandedPatientInstructions;
    }
    public String getSignatureStatus(){
        return signatureStatus;
    }
    @Override
    public String toString() {
        return
        "patient IEN=["+patient+"]  " +
        "priority=["+priority+"]  " +
        "order type=["+orderType+"]  " +
        "provider=["+provider+"]  " +
        "entered by=["+enteredBy+"]  " +
        "flagged=["+flagged+"]  " +
        "login date=["+loginDate.toString()+"]  " +
        "pickup routing=["+pickupRouting+"]  " +
        "drug=["+drug+"]  " +
        "quantity=["+quantity+"]  " +
        "route=["+route+"]  " +
        "sig=["+sig+"]  " 
        ;
    }
}

