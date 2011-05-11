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

import com.medsphere.fileman.FMAnnoteFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class FMPatient extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization 
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, Field> domainJavaFields;
    private static Map<String, String> domainNumbers;
    
    static {
        domainJavaFields = getDomainJavaFields(FMPatient.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMPatient.class);
        fileInfo = new FMFile("PATIENT") {

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

    @FMAnnoteFieldInfo(name = "NAME", number = ".01", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    private String name;
    @FMAnnoteFieldInfo(name = "SEX", number = ".02", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    private String sex;
    @FMAnnoteFieldInfo(name = "DATE OF BIRTH", number = ".03", fieldType = FMField.FIELDTYPE.DATE)
    private Date dob;
    @FMAnnoteFieldInfo(name = "AGE", number = ".033", fieldType = FMField.FIELDTYPE.NUMERIC)
    private Double age;
    @FMAnnoteFieldInfo(name = "DISPLAY AGE", number = "21400.033", fieldType = FMField.FIELDTYPE.COMPUTED)
    private String displayAge;
    @FMAnnoteFieldInfo(name = "ENTERPRISE PATIENT IDENTIFIER", number = "21400", fieldType = FMField.FIELDTYPE.COMPUTED)
    private String enterprisePatientIdentifier;
    @FMAnnoteFieldInfo(name = "ID", number = "21400.99", fieldType = FMField.FIELDTYPE.COMPUTED)
    private String id;
    @FMAnnoteFieldInfo(name = "ADMITTING DIAGNOSIS", number = "21405.1", fieldType = FMField.FIELDTYPE.COMPUTED)
    private String admittingDiagnosis;
    @FMAnnoteFieldInfo(name = "ADMITTING PHYSICIAN", number = "21405.8", fieldType = FMField.FIELDTYPE.COMPUTED)
    private String admittingPhysician;
    @FMAnnoteFieldInfo(name = "SOCIAL SECURITY NUMBER", number = ".09", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    private String ssn;
    @FMAnnoteFieldInfo(name = "ROOM-BED", number = ".101", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    private String roomBed;
    @FMAnnoteFieldInfo(name = "ATTENDING PHYSICIAN", number = ".1041", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    private Integer attendingPhysician;
    @FMAnnoteFieldInfo(name = "CURRENT ROOM", number = ".108", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    private Integer currentRoom;
    @FMAnnoteFieldInfo(name = "CURRENT ADMISSION", number = ".105", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    private Integer currentAdmission;
    @FMAnnoteFieldInfo(name = "WARD LOCATION", number = ".1", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    private String wardLocation;
    @FMAnnoteFieldInfo(name = "CURRENT MOVEMENT", number = ".102", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    private Integer currentMovement;
    @FMAnnoteFieldInfo(name = "PROVIDER", number = ".104", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    private Integer provider;
    
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

}
