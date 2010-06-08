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

public class FMMedicationLog extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
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

    @FMAnnotateFieldInfo(name = "PATIENT NAME", number = ".01", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String patientName;
    @FMAnnotateFieldInfo(name = "ACTION DATE/TIME", number = ".06", fieldType = FMField.FIELDTYPE.DATE)
    protected Date actionDateTime;
    @FMAnnotateFieldInfo(name = "ACTION BY", number = ".07", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String actionBy;
    @FMAnnotateFieldInfo(name = "ADMINISTRATION MEDICATION", number = ".08", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String medication;
    @FMAnnotateFieldInfo(name = "ACTION STATUS", number = ".09", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String actionStatus;
    @FMAnnotateFieldInfo(name = "ORDER DOSAGE", number = ".15", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String orderDosage;
    @FMAnnotateFieldInfo(name = "ORDER SCHEDULE", number = ".12", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String orderSchedule;
    @FMAnnotateFieldInfo(name = "PRN REASON", number = ".21", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String prnReason;

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
