// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2007-2010  Medsphere Systems Corporation
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

import com.medsphere.fileman.FMAnnotateFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author mark.taylor
 */
public class FMHealthRecordNumber extends FMRecord {
    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMHealthRecordNumber.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMHealthRecordNumber.class);
        fileInfo = new FMFile("HEALTH RECORD NO.") {

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

    @FMAnnotateFieldInfo( name="HEALTH RECORD FACILITY", number=".01", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer facility;
    @FMAnnotateFieldInfo( name="HEALTH RECORD NO.", number=".02", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String healthRecordNumber;
    @FMAnnotateFieldInfo( name="DATE INACTIVATED/DELETED", number=".03", fieldType=FMField.FIELDTYPE.DATE)
    protected Date dateInactive;
    @FMAnnotateFieldInfo( name="RECORD STATUS", number=".05", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String recordStatus;

    public FMHealthRecordNumber() {
        super("HEALTH RECORD NO.");
    }

    public FMHealthRecordNumber(FMResultSet results) {
        super( results );
    }

    public Integer getFacility() {
        return facility;
    }

    public String getHealthRecordNumber() {
        return healthRecordNumber;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public Date getInactive() {
        return dateInactive;
    }

}
