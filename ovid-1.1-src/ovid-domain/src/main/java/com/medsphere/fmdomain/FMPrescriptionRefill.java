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
import com.medsphere.fileman.FMField.FIELDTYPE;

public class FMPrescriptionRefill extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMPrescriptionRefill.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMPrescriptionRefill.class);
        fileInfo = new FMFile("REFILL") {

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

    @FMAnnotateFieldInfo(name="REFILL DATE", number=".01", fieldType=FIELDTYPE.DATE)
    protected Date refillDate;
    @FMAnnotateFieldInfo(name="QTY", number="1", fieldType=FIELDTYPE.NUMERIC)
    protected Double quantity;
    @FMAnnotateFieldInfo(name="DAYS SUPPLY", number="1.1", fieldType=FIELDTYPE.NUMERIC)
    protected Double daysSupply;
    @FMAnnotateFieldInfo(name="LOT #", number="5", fieldType=FIELDTYPE.FREE_TEXT)
    protected String lotNumber;
    @FMAnnotateFieldInfo(name="DISPENSED DATE", number="10.1", fieldType=FIELDTYPE.DATE)
    protected Date dispensedDate;
    @FMAnnotateFieldInfo(name="EXPIRATION DATE", number="13", fieldType=FIELDTYPE.DATE)
    protected Date expirationDate;
    @FMAnnotateFieldInfo(name="REMARKS", number="3", fieldType=FIELDTYPE.FREE_TEXT)
    protected String remarks;
    @FMAnnotateFieldInfo(name="PHARMACIST NAME", number="4", fieldType=FIELDTYPE.POINTER_TO_FILE)
    protected Integer pharmacist;

    public FMPrescriptionRefill() {
        super("REFILL");
    }

    public FMPrescriptionRefill(FMResultSet results) {
        super("REFILL");
        processResults(results);
    }

   public FMPrescriptionRefill(Date refillDate) {
       this();
       this.refillDate = refillDate;
   }

   public Date getRefillDate() {
        return refillDate;
    }

    public Double getQuantity() {
        return quantity;
    }

    public Double getDaysSupply() {
        return daysSupply;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public Date getDispensedDate() {
        return dispensedDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public Integer getPharmacist() {
        return pharmacist;
    }

    public String getPharmacistValue() {
        return getValue("4");
    }

    @Override
    public String toString() {
        return
        "refillDate=["+refillDate+"]"
        + " quantity=["+quantity+"]"
        + " daysSupply=["+daysSupply+"]"
        + " lotNumber=["+lotNumber+"]"
        + " dispensedDate=["+dispensedDate+"]"
        + " expirationDate=["+expirationDate+"]"
        + " remarks=["+remarks+"]"
        + " pharmacist=["+getPharmacistValue()+"]"
       ;
    }
}
