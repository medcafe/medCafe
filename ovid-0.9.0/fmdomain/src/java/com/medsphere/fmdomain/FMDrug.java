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

import java.util.Date;

import com.medsphere.fileman.FMAnnoteFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class FMDrug extends FMRecord { // extends FMDomainObject {

    /*-------------------------------------------------------------
     * begin static initialization 
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, Field> domainJavaFields;
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
    protected Map<String, Field> getDomainJavaFields() {
        return domainJavaFields;
    }

    @Override
    protected Map<String, String> getNumericMapping() {
        return domainNumbers;
    }
    
    /*-------------------------------------------------------------
     * end static initialization 
     *-------------------------------------------------------------*/
    
    @FMAnnoteFieldInfo( name="GENERIC NAME", number=".01", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    private String genericName;
    @FMAnnoteFieldInfo( name="AHFS NUMBER", number="1", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    private String ahfsNumber;
    @FMAnnoteFieldInfo( name="ORDER UNIT", number="12", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    private Integer orderUnit;
    @FMAnnoteFieldInfo( name="PRICE PER ORDER UNIT", number="13", fieldType=FMField.FIELDTYPE.NUMERIC)
    private Double pricePerOrderUnit;
    @FMAnnoteFieldInfo( name="DISPENSE UNIT", number="14.5", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    private String dispenseUnit;
    @FMAnnoteFieldInfo( name="EXPIRATION DATE", number="17.1", fieldType=FMField.FIELDTYPE.DATE)
    private Date expirationDate;
    @FMAnnoteFieldInfo( name="VA PRODUCT NAME", number="21", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    private String vaProductName;
    @FMAnnoteFieldInfo( name="PACKAGE SIZE", number="23", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    private String packageSize;
    @FMAnnoteFieldInfo( name="PACKAGE TYPE", number="24", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    private String packageType;
    @FMAnnoteFieldInfo( name="NDC", number="31", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    private String ndc;
    @FMAnnoteFieldInfo( name="INACTIVE DATE", number="100", fieldType=FMField.FIELDTYPE.DATE)
    private Date inactiveDate;
    @FMAnnoteFieldInfo( name="STRENGTH", number="901", fieldType=FMField.FIELDTYPE.NUMERIC)
    private Double strength;
    @FMAnnoteFieldInfo( name="UNIT", number="902", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    private Integer unit;
    @FMAnnoteFieldInfo( name="SYNONYM", number="9", fieldType=FMField.FIELDTYPE.SUBFILE)
    private FMDrugSynonym synonym;

    public FMDrug() {
        super( fileInfo.getFileName() );
    }

    public FMDrug(FMResultSet results) {
        super( results );
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

    public void setStrength(Double strength) {
        setDomainValue("strength", strength);
    }

    public Integer getUnit() {
        return unit;
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

}
