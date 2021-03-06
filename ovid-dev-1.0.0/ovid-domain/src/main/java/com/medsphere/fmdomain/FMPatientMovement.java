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
 * container class of fileman PatientMovement information
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
import com.medsphere.fileman.FMField.FIELDTYPE;

public class FMPatientMovement extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMPatientMovement.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMPatientMovement.class);
        fileInfo = new FMFile("PATIENT MOVEMENT") {

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

    @FMAnnotateFieldInfo(name = "DATE/TIME", number = ".01", fieldType = FIELDTYPE.DATE)
    protected Date dateTime;
    @FMAnnotateFieldInfo(name = "TRANSACTION", number = ".02", fieldType = FIELDTYPE.SET_OF_CODES)
    protected String transaction;
    @FMAnnotateFieldInfo(name = "PATIENT", number = ".03", fieldType = FIELDTYPE.POINTER_TO_FILE)
    protected Integer patient;
    @FMAnnotateFieldInfo(name = "WARD LOCATION", number = ".06", fieldType = FIELDTYPE.POINTER_TO_FILE)
    protected Integer wardLocation;
    @FMAnnotateFieldInfo(name="FACILITY TREATING SPECIALTY", number=".09", fieldType = FIELDTYPE.POINTER_TO_FILE)
    protected Integer facilityTreatingSpecialty;
	 @FMAnnotateFieldInfo(name = "PTF ENTRY", number=".16", fieldType = 
	 FIELDTYPE.POINTER_TO_FILE)
	 protected Integer ptfEntry;

    public FMPatientMovement() {
        super(fileInfo.getFileName());
    }

    public FMPatientMovement(FMResultSet results) {
        super(results);
    }

    public Date getDateTime() {
        return dateTime;
    }

    public String getTransaction() {
        return transaction;
    }

    public Integer getPatient() {
        return patient;
    }

    public Integer getWardLocation() {
        return wardLocation;
    }

    public Integer getFacilityTreatingSpecialty() {
        return facilityTreatingSpecialty;
    }

    public String getFacilityTreatingSpecialtyValue() {
        return getValue(".09");
    }
    public Integer getPTFEntry(){
    	  return ptfEntry;
    	  }
    public String getPTFEntryValue() {
    	  return getValue(".16");
    	  }
}
