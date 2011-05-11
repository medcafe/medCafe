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

/*
 * container class of fileman Patient information
 */
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
import com.medsphere.ovid.domain.ov.SupportsNameComponents;

public class FMPatient extends FMRecord implements SupportsNameComponents {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMPatient.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMPatient.class);
        fileInfo = new FMFile("2") { // need to use number because RPMS names file 2 'VA PATIENT'

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
    @FMAnnotateFieldInfo(name = "SEX", number = ".02", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String sex;
    @FMAnnotateFieldInfo(name = "DATE OF BIRTH", number = ".03", fieldType = FMField.FIELDTYPE.DATE)
    protected Date dob;
    @FMAnnotateFieldInfo(name = "PLACE OF BIRTH (CITY)", number = ".092", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String birthCity;
    
    @FMAnnotateFieldInfo(name = "PLACE OF BIRTH (STATE)", number = ".093", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer birthState;
    @FMAnnotateFieldInfo(name = "AGE", number = ".033", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected Double age;
    @FMAnnotateFieldInfo(name = "DISPLAY AGE", number = "21400.033", fieldType = FMField.FIELDTYPE.COMPUTED)
    protected String displayAge;
    @FMAnnotateFieldInfo(name = "ENTERPRISE PATIENT IDENTIFIER", number = "21400", fieldType = FMField.FIELDTYPE.COMPUTED)
    protected String enterprisePatientIdentifier;
    @FMAnnotateFieldInfo(name = "ID", number = "21400.99", fieldType = FMField.FIELDTYPE.COMPUTED)
    protected String id;
    @FMAnnotateFieldInfo(name = "ADMITTING DIAGNOSIS", number = "21405.1", fieldType = FMField.FIELDTYPE.COMPUTED)
    protected String admittingDiagnosis;
    @FMAnnotateFieldInfo(name = "ADMITTING PHYSICIAN", number = "21405.8", fieldType = FMField.FIELDTYPE.COMPUTED)
    protected String admittingPhysician;
    @FMAnnotateFieldInfo(name = "SOCIAL SECURITY NUMBER", number = ".09", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String ssn;
    @FMAnnotateFieldInfo(name = "ROOM-BED", number = ".101", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String roomBed;
    @FMAnnotateFieldInfo(name = "ATTENDING PHYSICIAN", number = ".1041", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer attendingPhysician;
    @FMAnnotateFieldInfo(name = "CURRENT ROOM", number = ".108", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer currentRoom;
    @FMAnnotateFieldInfo(name = "CURRENT ADMISSION", number = ".105", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer currentAdmission;
    @FMAnnotateFieldInfo(name = "WARD LOCATION", number = ".1", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String wardLocation;
    @FMAnnotateFieldInfo(name = "CURRENT MOVEMENT", number = ".102", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer currentMovement;
    @FMAnnotateFieldInfo(name = "PROVIDER", number = ".104", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer provider;
    protected String familyName;
    protected String givenName;
    protected String middleName;
    protected String suffix;
    protected String prefix;
    protected String degree;

    public FMPatient() {
        super(fileInfo.getFileName());
    }

    public FMPatient(FMResultSet results) {
        super(results);
    }

    public String getName() {
        return name;
    }

    public Date getDob() {
        return dob;
    }

    public String getBirthCity() {
        return birthCity;
    }

    public Integer getBirthState() {
        return birthState;
    }

    public String getBirthStateValue() {
        return getValue(".093");
    }
    public Double getAge() {
        return age;
    }

    public String getDisplayAge() {
        return displayAge;
    }

    public String getSex() {
        return sex;
    }

    public String getEnterprisePatientIdentifier() {
        return enterprisePatientIdentifier;
    }

    public String getId() {
        return id;
    }

    public String getSsn() {
        return ssn;
    }

    public String getAttendingPhysician() {
        return getValue(".1041");
    }
 public Integer getAttendingPhysicianId() {
        return attendingPhysician;
    }
    public String getAdmittingDiagnosis() {
        return admittingDiagnosis;
    }

    public String getAdmittingPhysician() {
        return admittingPhysician;
    }

    public String getCurrentRoom() {
        return getValue(".108");
    }

    public String getCurrentAdmission() {
        return getValue(".105");
    }

    public String getRoomBed() {
        return roomBed;
    }

    public String getWardLocation() {
        return wardLocation;
    }

    public String getCurrentMovement() {
        return getValue(".102");
    }

    public String getProvider() {
        return getValue(".104");
    }


    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    @Override
    public String toString() {
        return "DFN=["+getIEN()+"]"
        + " name=["+getName()+"]"
        + ((familyName != null) ? " family name=["+familyName+"]" : "")
        + ((givenName != null) ? " given name=["+givenName+"]" : "")
        + ((middleName != null) ? " middle name=["+middleName+"]" : "")
        + ((prefix != null) ? " prefix=["+prefix+"]" : "")
        + ((suffix != null) ? " suffix=["+suffix+"]" : "")
        + ((degree != null) ? " degree=["+degree+"]" : "")
        + " sex=["+getSex()+"]"
       + " dob=["+((dob!= null) ?getDob().toString():"null")
        +"]"
        + " eid=["+getEnterprisePatientIdentifier()+"]"
        + " id=["+getId()+"]"
        + " age=["+getAge()+"]"
        + " display age=["+getDisplayAge()+"]"
        + " room-bed=["+getRoomBed()+"]"
        + " current room=["+getCurrentRoom()+"]"
        + " attending=["+getAttendingPhysician()+"]"
        + " ward=["+getWardLocation()+"]"
        + " current movement=["+getCurrentMovement()+"]"
        + " current admission=["+getCurrentAdmission()+"]"
        + " admitting physician=["+getAdmittingPhysician()+"]"
        + " admitting diagnosis=["+getAdmittingDiagnosis()+"]";

    }

    public String getFieldNumber() {
        return ".01";
    }


    public String getFileNumber() {
        return("2");
    }
    //added
   public void setCurrentAdmission(FMPatientMovement movement)
   {
     setDomainValue("currentAdmission", movement.getIEN());
   }

     public boolean setSex(String sex ) {
         sex = sex.toUpperCase();
        if (sex.equals("F")||sex.equals("M"))
        {
             setDomainValue("sex",  sex);
            return true;
        }
         return false;
    }

    public void setEnterprisePatientIdentifier(String EPI) {
         setDomainValue("enterprisePatientIdentifier",  EPI);
    }

    public void setId(String id) {
         setDomainValue("id", id);
    }

    public void setSsn(String ssn) {
         setDomainValue("ssn",  ssn);
    }

    public void setAttendingPhysician(FMUser physician) {
        setDomainValue("attendingPhysician", physician.getIEN());
    }

    public void setCurrentRoom(FMRoom room) {
        setDomainValue("currentRoom", room.getIEN());
    }

  public void setRoomBed(String roomAndBed) {
         setDomainValue("roomBed", roomAndBed);
    }

    public void setWardLocation(String location) {
         setDomainValue("wardLocation", location);
    }

    public void setCurrentMovement(FMPatientMovement movement) {
        setDomainValue("currentMovement", movement);
    }

    public void setProvider(FMUser provide) {
        setDomainValue("provider", provide);
    }
        public void setBirthState(Integer newState) {

        setDomainValue("birthState", newState, ".093");
    }
    public void setFMBirthState(FMState fmState)
    {
         setDomainValue("birthState", Integer.parseInt(fmState.getIEN()),".093");
    }
    public void setBirthCity(String city)
    {
        setDomainValue("birthCity", city);
    }

    public boolean setDob(Date date)
    {
        if (date.after(new Date(-30,11,31)))
        {

         setDomainValue("dob",  date);
        return true;
        }
        else
            return false;

    }
   

}
