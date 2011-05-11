// <editor-fold defaultstate="collapsed">
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
// </editor-fold>

package com.medsphere.fmdomain;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.medsphere.fileman.FMAnnotateFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import java.text.DecimalFormat;

public class FMDrug extends FMRecord { // extends FMDomainObject {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMDrug.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMDrug.class);
        fileInfo = new FMFile("DRUG") {

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

    @FMAnnotateFieldInfo( name="GENERIC NAME", number=".01", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String genericName;
    @FMAnnotateFieldInfo( name="AHFS NUMBER", number="1", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String ahfsNumber;
    @FMAnnotateFieldInfo( name="ORDER UNIT", number="12", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer orderUnit;
    @FMAnnotateFieldInfo( name="PRICE PER ORDER UNIT", number="13", fieldType=FMField.FIELDTYPE.NUMERIC)
    protected Double pricePerOrderUnit;
    @FMAnnotateFieldInfo( name="DISPENSE UNIT", number="14.5", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String dispenseUnit;
    @FMAnnotateFieldInfo( name="EXPIRATION DATE", number="17.1", fieldType=FMField.FIELDTYPE.DATE)
    protected Date expirationDate;
    @FMAnnotateFieldInfo( name="VA PRODUCT NAME", number="21", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String vaProductName;
    @FMAnnotateFieldInfo( name="PACKAGE SIZE", number="23", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String packageSize;
    @FMAnnotateFieldInfo( name="PACKAGE TYPE", number="24", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String packageType;
    @FMAnnotateFieldInfo( name="NDC", number="31", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String ndc;
    @FMAnnotateFieldInfo( name="INACTIVE DATE", number="100", fieldType=FMField.FIELDTYPE.DATE)
    protected Date inactiveDate;
    @FMAnnotateFieldInfo( name="STRENGTH", number="901", fieldType=FMField.FIELDTYPE.NUMERIC)
    protected Double strength;
    @FMAnnotateFieldInfo( name="UNIT", number="902", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer unit;
    @FMAnnotateFieldInfo( name="SYNONYM", number="9", fieldType=FMField.FIELDTYPE.SUBFILE)
    protected FMDrugSynonym synonym;

    protected Collection<FMDrugSynonym> synonyms = null;

    public FMDrug() {
        super("DRUG");
    }

    public FMDrug(FMResultSet results) {
        super("DRUG");
        processResults(results);
    }

    public String getAhfsNumber() {
        return ahfsNumber;
    }

    public void setAhfsNumber(String ahfsNumber) {
        setDomainValue("ahfsNumber", ahfsNumber);
    }

    public String getDispenseUnit() {
        return dispenseUnit;
    }

    public void setDispenseUnit(String dispenseUnit) {
        setDomainValue("dispenseUnit", dispenseUnit);
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        setDomainValue("expirationDate", expirationDate);
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        setDomainValue("genericName", genericName);
    }

    public Date getInactiveDate() {
        return inactiveDate;
    }

    public void setInactiveDate(Date inactiveDate) {
        setDomainValue("inactiveDate", inactiveDate);
    }

    public String getNdc() {
        return ndc;
    }

    public void setNdc(String ndc) {
        setDomainValue("ndc", ndc);
    }

    public Integer getOrderUnit() {
        return orderUnit;
    }

    public void setOrderUnit(Integer orderUnit) {
        setDomainValue("orderUnit", orderUnit);
    }

    public String getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(String packageSize) {
        setDomainValue("packageSize", packageSize);
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        setDomainValue("packageType", packageType);
    }

    public Double getPricePerOrderUnit() {
        return pricePerOrderUnit;
    }

    public void setPricePerOrderUnit(Double pricePerOrderUnit) {
        setDomainValue("pricePerOrderUnit", pricePerOrderUnit);
    }

    public Double getStrength() {
        return strength;
    }

    public String getDisplayStrength() {
        if (strength != null) {
            return new DecimalFormat("###################0.#######").format(strength);
        } else {
            return "";
        }
    }
    
    public void setStrength(Double strength) {
        setDomainValue("strength", strength);
    }

    public String getUnit() {
        return getValue("902");
    }

    public void setUnit(Integer unit) {
        setDomainValue("unit", unit);
    }

    public String getVaProductName() {
        return vaProductName;
    }

    public void setVaProductName(String vaProductName) {
        setDomainValue("vaProductName", vaProductName);
    }

    public FMDrugSynonym getSynonym() {
        if (synonym==null) {
            synonym = new FMDrugSynonym();
            synonym.setParent( this );
        }
        return synonym;
    }

    public void addSynonym(FMDrugSynonym synonym) {
        if (synonyms == null) {
            synonyms = new ArrayList<FMDrugSynonym>();
        }
        synonyms.add(synonym);
    }

    public Collection<FMDrugSynonym> getSynonyms() {
        return synonyms;
    }

    @Override
    public String toString() {
        return getIEN()
        + ((genericName != null) ? " genericName=["+ getGenericName() +"]" : "")

        + ((ahfsNumber != null) ? " ahfsNumber=["+ getAhfsNumber() +"]" : "")

        + ((orderUnit != null) ? " orderUnit=["+ getOrderUnit() +"]" : "")

        + ((pricePerOrderUnit != null) ? " pricePerOrderUnit=["+ getPricePerOrderUnit() +"]" : "")

        + ((dispenseUnit != null) ? " dispenseUnit=["+ getDispenseUnit() +"]" : "")

        + ((expirationDate != null) ? " expirationDate=["+ getExpirationDate() +"]" : "")

        + ((vaProductName != null) ? " vaProductName=["+ getVaProductName() +"]" : "")

        + ((packageSize != null) ? " packageSize=["+ getPackageSize() +"]" : "")

        + ((packageType != null) ? " packageType=["+ getPackageType() +"]" : "")

        + ((ndc != null) ? " ndc=["+ getNdc() +"]" : "")

        + ((inactiveDate != null) ? " inactiveDate=["+ getInactiveDate() +"]" : "")

        + ((strength != null) ? " strength=["+ getStrength() +"]" : "")

        + ((unit != null) ? " unit=["+ getUnit() +"]" : "")

        + (( synonyms != null) ? " synonym subfile=["+ getSynonyms() +"]" : "")

        ;
    }
}
