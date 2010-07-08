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
/*
 * container class of fileman Visit information
 */


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

public class FMVisit extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/

    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMVisit.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMVisit.class);
        fileInfo = new FMFile("VISIT") { //

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

    @FMAnnotateFieldInfo(name = "VISIT/ADMIT DATE&TIME", number = ".01", fieldType = FMField.FIELDTYPE.DATE)
    protected Date visitDate;
    @FMAnnotateFieldInfo(name = "TYPE", number = ".03", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String type;
    @FMAnnotateFieldInfo(name = "PATIENT NAME", number = ".05", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer patient;
    @FMAnnotateFieldInfo(name = "LOC. OF ENCOUNTER", number = ".06", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer location;
    @FMAnnotateFieldInfo(name = "SERVICE CATEGORY", number = ".07", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String serviceCategory;
    @FMAnnotateFieldInfo(name = "PARENT VISIT LINK", number = ".12", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer parentVisit;
    @FMAnnotateFieldInfo(name = "HOSPITAL LOCATION", number = ".22", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer hospitalLocation;
    @FMAnnotateFieldInfo(name = "OUTSIDE LOCATION", number = "2101", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String outsideLocation;
    @FMAnnotateFieldInfo(name = "VISIT ID", number = "15001", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String visitId;
    @FMAnnotateFieldInfo(name= "PATIENT IN/OUT", number = "15002", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String patientInOut;
    @FMAnnotateFieldInfo(name = "ENCOUNTER TYPE", number = "15003", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String encounterType;

    public FMVisit() {
        super(fileInfo.getFileName());


    }

    public FMVisit(FMResultSet results) {
        super(results);
    }

    public Date getVisitDate (){
        return visitDate;
    }
    public String getType()
    {
        return type;
    }

    public Integer getPatient() {
        return patient;
    }

    public String getPatientName(){
        return getValue(".05");
    }

    public Integer getLocation(){
       return location;

    }
    public String getLocationValue(){
        return getValue(".06");
    }
    public String getServiceCategory()
    {
        return serviceCategory;
    }
    public Integer getParentVisitIEN()
    {
        return parentVisit;
    }
    public Integer getHospitalLocation()
    {
        return hospitalLocation;
    }
    public String getHospitalLocationValue()
    {
        return getValue(".22");
    }
    public String getOutsideLocation()
    {
        return outsideLocation;
    }
    public String getVisitID()
    {
	return visitId;
    }
    public String getPatientInOut()
    {
        return patientInOut;
    }
    public String getEncounterType()
    {
        return encounterType;
    }

    @Override
    public String toString() {
        return " Visit=[" + getVisitDate() + "]" +
        " patient=["+getPatientName()+"]"
        + " type =["+getType()+"]"
        + " location =["+ getLocationValue() + "]"
        + " service category =[" + getServiceCategory() + "]"
        + " hospital location=[" + getHospitalLocation() + "]"
        + " parent visit link =["+getParentVisitIEN() +"]"+
                " outside location =[" + getOutsideLocation() + "]"
                + " visit id = [" + getVisitID() + "]"
                + " In or Out Patient=[" + getPatientInOut() + "]"
                + " encounter type=["+ getEncounterType()+ "]";

    }

}
