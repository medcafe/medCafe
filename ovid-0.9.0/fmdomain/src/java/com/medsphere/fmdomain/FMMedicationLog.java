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
 * Log of administered medications
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

public class FMMedicationLog extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization 
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, Field> domainJavaFields;
    private static Map<String, String> domainNumbers;
    
    static {
        domainJavaFields = getDomainJavaFields(FMMedicationLog.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMMedicationLog.class);
        fileInfo = new FMFile("53.79") {

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
    
    @FMAnnoteFieldInfo(name = "PATIENT NAME", number = ".01", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    private String patientName;
    @FMAnnoteFieldInfo(name = "ACTION DATE/TIME", number = ".06", fieldType = FMField.FIELDTYPE.DATE)
    private Date actionDateTime;
    @FMAnnoteFieldInfo(name = "ACTION BY", number = ".07", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    private String actionBy;
    @FMAnnoteFieldInfo(name = "ADMINISTRATION MEDICATION", number = ".08", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    private String medication;
    @FMAnnoteFieldInfo(name = "ACTION STATUS", number = ".09", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    private String actionStatus;
    @FMAnnoteFieldInfo(name = "ORDER DOSAGE", number = ".15", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    private String orderDosage;
    @FMAnnoteFieldInfo(name = "ORDER SCHEDULE", number = ".12", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    private String orderSchedule;
    @FMAnnoteFieldInfo(name = "PRN REASON", number = ".21", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    private String prnReason;
  
    public FMMedicationLog() {
        super(fileInfo.getFileName());        
    }

    public FMMedicationLog(FMResultSet results) {
        super(results);
    }

    public String getActionBy() {
        return actionBy;
    }

    public Date getActionDateTime() {
        return actionDateTime;
    }

    public String getMedication() {
        return medication;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public String getOrderDosage() {
        return orderDosage;
    }

    public String getOrderSchedule() {
        return orderSchedule;
    }

    public String getPrnReason() {
        return prnReason;
    }

    
}
