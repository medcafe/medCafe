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

public class FMPrescription extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMPrescription.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMPrescription.class);
        fileInfo = new FMFile("PRESCRIPTION") {

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
    @FMAnnotateFieldInfo( name="RX #", number=".01", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String rxNumber;
    @FMAnnotateFieldInfo( name="PATIENT", number="2", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer patient;
    @FMAnnotateFieldInfo( name="PATIENT STATUS", number="3", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer patientStatus;
    @FMAnnotateFieldInfo( name="PROVIDER", number="4", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer provider;
    @FMAnnotateFieldInfo( name="DRUG", number="6", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer drug;
    @FMAnnotateFieldInfo( name="DISPENSED DATE", number="25", fieldType=FMField.FIELDTYPE.DATE)
    protected Date dispensedDate;
    @FMAnnotateFieldInfo( name="EXPIRATION DATE", number="26", fieldType=FMField.FIELDTYPE.DATE)
    protected Date expirationDate;
    @FMAnnotateFieldInfo( name="ISSUE DATE", number="1", fieldType=FMField.FIELDTYPE.DATE)
    protected Date issueDate;
    @FMAnnotateFieldInfo( name="TRADE NAME", number="6.5", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String tradeName;
    @FMAnnotateFieldInfo( name="QTY", number="7", fieldType=FMField.FIELDTYPE.NUMERIC)
    protected Double quantity;
    @FMAnnotateFieldInfo( name="# OF REFILLS", number="9", fieldType=FMField.FIELDTYPE.NUMERIC)
    protected Double numberOfRefills;
    @FMAnnotateFieldInfo( name="SIG", number="10", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String sig;
    @FMAnnotateFieldInfo( name="FILL DATE", number="22", fieldType=FMField.FIELDTYPE.DATE)
    protected Date fillDate;
    @FMAnnotateFieldInfo( name="PHARMACIST", number="23", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer pharmacist;
    @FMAnnotateFieldInfo( name="LOT #", number="24", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String lotNumber;
    @FMAnnotateFieldInfo( name="CANCEL DATE", number="26.1", fieldType=FMField.FIELDTYPE.DATE)
    protected Date cancelDate;
    @FMAnnotateFieldInfo( name="NDC", number="27", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String ndc;
    @FMAnnotateFieldInfo( name="PATIENT INSTRUCTIONS", number="114", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String patientInstructions;
    @FMAnnotateFieldInfo( name="OTHER PATIENT INSTRUCTIONS", number="114.1", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String otherPatientInstructions;
    @FMAnnotateFieldInfo( name="REFILL", number="52", fieldType=FMField.FIELDTYPE.SUBFILE)
    protected FMPrescriptionRefill refill;

    public FMPrescription() {
        super("PRESCRIPTION");
    }

    public FMPrescription(FMResultSet results) {
        super("PRESCRIPTION");
        processResults(results);
    }

    public Integer getPatientStatus() {
        return patientStatus;
    }

    public String getPatientStatusValue() {
        return getValue("3");
    }
    public Integer getProvider() {
        return provider;
    }
    public String getProviderValue() {
        return getValue("4");
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public String getTradeName() {
        return tradeName;
    }

    public Double getQuantity() {
        return quantity;
    }

    public Double getNumberOfRefills() {
        return numberOfRefills;
    }

    public String getSig() {
        return sig;
    }

    public Date getFillDate() {
        return fillDate;
    }

    public Integer getPharmacist() {
        return pharmacist;
    }
    public String getPharmacistValue() {
        return getValue("23");
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public Date getCancelDate() {
        return cancelDate;
    }

    public String getNdc() {
        return ndc;
    }

    public String getPatientInstructions() {
        return patientInstructions;
    }

    public String getOtherPatientInstructions() {
        return otherPatientInstructions;
    }

    public String getRxNumber() {
        return rxNumber;
    }

    public Integer getPatient() {
        return patient;
    }

    public String getPatientValue() {
        return getValue("2");
    }

    public Integer getDrug() {
        return drug;
    }

    public String getDrugValue() {
        return getValue("6");
    }
    public Date getDispensedDate() {
        return dispensedDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public FMPrescriptionRefill getRefill() {
        if (refill==null) {
            refill = new FMPrescriptionRefill();
            refill.setParent( this );
        }
        return refill;
    }

    @Override
    public String toString() {
        return
        "rxNumber=["+rxNumber+"]"
        + " patient=["+getPatientValue()+"]"
        + " drug=["+getDrugValue()+"]"
        + " tradeName=["+tradeName+"]"
        + " issueDate=["+issueDate+"]"
        + " fillDate=["+fillDate+"]"
        + " dispensedDate=["+dispensedDate+"]"
        + " expirationDate=["+expirationDate+"]"
        + " cancelDate=["+cancelDate+"]"
        + " patientStatus=["+getPatientStatusValue()+"]"
        + " provider=["+getProviderValue()+"]"
        + " pharmacist=["+getPharmacistValue()+"]"
        + " sig=["+sig+"]"
        + " quantity=["+quantity+"]"
        + " numberOfRefills=["+numberOfRefills+"]"
        + " lotNumber=["+lotNumber+"]"
        + " ndc=["+ndc+"]"
        + " patientInstructions=["+patientInstructions+"]"
        + " otherPatientInstructions=["+otherPatientInstructions+"]"
        ;
    }

}
