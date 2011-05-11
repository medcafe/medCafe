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
import com.medsphere.ovid.domain.ov.SupportsNameComponents;

public class FMNewPerson extends FMRecord implements SupportsNameComponents {
    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMNewPerson.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMNewPerson.class);
        fileInfo = new FMFile("NEW PERSON") {

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
    @FMAnnotateFieldInfo( name="NAME", number=".01", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String name;
    @FMAnnotateFieldInfo( name="STREET ADDRESS 1", number=".111", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String streetAddress1;
    @FMAnnotateFieldInfo( name="STREET ADDRESS 2", number=".112", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String streetAddress2;
    @FMAnnotateFieldInfo( name="STREET ADDRESS 3", number=".113", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String streetAddress3;
    @FMAnnotateFieldInfo( name="CITY", number=".114", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String city;
    @FMAnnotateFieldInfo( name="STATE", number=".115", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer state;
    @FMAnnotateFieldInfo( name="ZIP CODE", number=".116", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String zipCode;
    @FMAnnotateFieldInfo( name="PHONE (HOME)", number=".131", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String homePhone;
    @FMAnnotateFieldInfo( name="OFFICE PHONE", number=".132", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String officePhone;
    @FMAnnotateFieldInfo( name="PHONE #3", number=".133", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String phone3;
    @FMAnnotateFieldInfo( name="PHONE #4", number=".134", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String phone4;
    @FMAnnotateFieldInfo( name="COMMERCIAL PHONE", number=".135", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String commercialPhone;
    @FMAnnotateFieldInfo( name="FAX NUMBER", number=".136", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String faxNumber;
    @FMAnnotateFieldInfo( name="VOICE PAGER", number=".137", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String voicePager;
    @FMAnnotateFieldInfo( name="DIGITAL PAGER", number=".138", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String digitalPager;
    @FMAnnotateFieldInfo( name="ROOM", number=".141", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String room;
    @FMAnnotateFieldInfo( name="EMAIL ADDRESS", number=".151", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String emailAddress;
    @FMAnnotateFieldInfo( name="INITIAL", number="1", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String initial;
    @FMAnnotateFieldInfo( name="SEX", number="4", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String sex;
    @FMAnnotateFieldInfo( name="DOB", number="5", fieldType=FMField.FIELDTYPE.DATE)
    protected Date dob;
    @FMAnnotateFieldInfo( name="TITLE", number="8", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer title;
    @FMAnnotateFieldInfo( name="SSN", number="9", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String ssn;
    @FMAnnotateFieldInfo( name="TERMINATION DATE", number="9.2", fieldType=FMField.FIELDTYPE.DATE)
    protected Date terminationDate;
    @FMAnnotateFieldInfo( name="DEGREE", number="10.6", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String degree;
    @FMAnnotateFieldInfo( name="NPI", number="41.99", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String npi;
    @FMAnnotateFieldInfo( name="AUTHORIZED TO WRITE MED ORDERS", number="53.1", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String authOrderWriter;
    @FMAnnotateFieldInfo( name="DEA#", number="53.2", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String deaNumber;
    @FMAnnotateFieldInfo( name="VA#", number="53.3", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String vaNumber;
    @FMAnnotateFieldInfo( name="INACTIVE DATE", number="53.4", fieldType=FMField.FIELDTYPE.DATE)
    protected Date orderAuthInactiveDate;
    @FMAnnotateFieldInfo( name="PROVIDER CLASS", number="53.5", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer providerClass;
    @FMAnnotateFieldInfo( name="PROVIDER TYPE", number="53.6", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String providerType;
    @FMAnnotateFieldInfo( name="TAX ID", number="53.92", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String taxId;
    @FMAnnotateFieldInfo( name="LANGUAGE", number="200.07", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer language;
    @FMAnnotateFieldInfo( name="VPID", number="9000", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected String vpid;

    protected Collection<FMPersonClass> personClassList = null;

    protected String familyName;
    protected String givenName;
    protected String middleName;
    protected String suffix;
    protected String prefix;
    protected String nameComponentDegree;

    public FMNewPerson() {
        super("NEW PERSON");
    }

    public FMNewPerson(FMResultSet results) {
        super("NEW PERSON");
        processResults(results);
    }

    public String getName() {
        return name;
    }

    public String getStreetAddress1() {
        return streetAddress1;
    }

    public String getStreetAddress2() {
        return streetAddress2;
    }

    public String getStreetAddress3() {
        return streetAddress3;
    }

    public String getCity() {
        return city;
    }

    public Integer getState() {
        return state;
    }

    public String getStateValue() {
        return getValue(".115");
    }
    public String getZipCode() {
        return zipCode;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public String getPhone3() {
        return phone3;
    }

    public String getPhone4() {
        return phone4;
    }

    public String getCommercialPhone() {
        return commercialPhone;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public String getVoicePager() {
        return voicePager;
    }

    public String getDigitalPager() {
        return digitalPager;
    }

    public String getRoom() {
        return room;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getInitial() {
        return initial;
    }

    public String getSex() {
        return sex;
    }

    public Date getDob() {
        return dob;
    }

    public Integer getTitle() {
        return title;
    }

    public String getTitleValue() {
        return getValue("8");
    }

    public String getSsn() {
        return ssn;
    }

    public Date getTerminationDate() {
        return terminationDate;
    }

    public String getDegree() {
        return degree;
    }

    public String getNpi() {
        return npi;
    }

    public String getAuthOrderWriter() {
        return authOrderWriter;
    }

    public String getDeaNumber() {
        return deaNumber;
    }

    public String getVaNumber() {
        return vaNumber;
    }

    public Date getOrderAuthInactiveDate() {
        return orderAuthInactiveDate;
    }
    
    public Integer getProviderClass() {
        return providerClass;
    }

    public String getProviderClassValue() {
        return getValue("53.5");
    }
    public String getProviderType() {
        return providerType;
    }

    public String getTaxId() {
        return taxId;
    }

    public Integer getLanguage() {
        return language;
    }
    public String getLanguageValue() {
        return getValue("200.07");
    }

    public String getVpid() {
        return vpid;
    }

    public Collection<FMPersonClass> getPersonClassList() {
        return personClassList;
    }

    public void setPersonClassList(Collection<FMPersonClass> personClassList) {
        this.personClassList = personClassList;
    }

    public void addPersonClass(FMPersonClass personClass) {
        if (personClassList == null) {
            personClassList = new ArrayList<FMPersonClass>();
        }
        personClassList.add(personClass);
    }

    @Override
    public String toString() {
        return
        "name=["+name+"]"
        + ((familyName != null) ? " family name=["+familyName+"]" : "")
        + ((givenName != null) ? " given name=["+givenName+"]" : "")
        + ((middleName != null) ? " middle name=["+middleName+"]" : "")
        + ((prefix != null) ? " prefix=["+prefix+"]" : "")
        + ((suffix != null) ? " suffix=["+suffix+"]" : "")
        + ((nameComponentDegree != null) ? " nameComponentDegree=["+nameComponentDegree+"]" : "")
        + " streetAddress1=["+streetAddress1+"]"
        + " streetAddress2=["+streetAddress2+"]"
        + " streetAddress3=["+streetAddress3+"]"
        + " city=["+city+"]"
        + " state=["+getStateValue()+"]"
        + " zipCode=["+zipCode+"]"
        + " homePhone=["+homePhone+"]"
        + " officePhone=["+officePhone+"]"
        + " phone3=["+phone3+"]"
        + " phone4=["+phone4+"]"
        + " commercialPhone=["+commercialPhone+"]"
        + " faxNumber=["+faxNumber+"]"
        + " voicePager=["+voicePager+"]"
        + " digitalPager=["+digitalPager+"]"
        + " room=["+room+"]"
        + " emailAddress=["+emailAddress+"]"
        + " initial=["+initial+"]"
        + " sex=["+sex+"]"
        + " dob=["+dob+"]"
        + " title=["+getTitleValue()+"]"
        + " ssn=["+ssn+"]"
        + " terminationDate=["+terminationDate+"]"
        + " degree=["+degree+"]"
        + " npi=["+npi+"]"
        + " deaNumber=["+deaNumber+"]"
        + " vaNumber=["+vaNumber+"]"
        + " providerClass=["+getProviderClassValue()+"]"
        + " providerType=["+providerType+"]"
        + " taxId=["+taxId+"]"
        + " language=["+getLanguageValue()+"]"
        + " vpid=["+vpid+"]"
        + ((personClassList != null && personClassList.size() > 0)
          ? "\n\tperson classes=" + personClassList.toString() : "")
        ;
    }

    @Override
    public String getFieldNumber() {
        return(".01");
    }

    @Override
    public String getFileNumber() {
        return("200");
    }

    @Override
    public void setDegree(String degree) {
        this.nameComponentDegree = degree;

    }

    @Override
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    @Override
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    @Override
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public String getFamilyName() {
        return this.familyName;
    }

    @Override
    public String getGivenName() {
        return this.givenName;
    }

    @Override
    public String getMiddleName() {
        return this.middleName;
    }

}
