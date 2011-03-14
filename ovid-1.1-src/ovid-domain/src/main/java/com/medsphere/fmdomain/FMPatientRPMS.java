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

/**
 *
 * @author mark.taylor
 */
public class FMPatientRPMS extends FMRecord {
    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMPatientRPMS.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMPatientRPMS.class);
        fileInfo = new FMFile("9000001") {

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
    @FMAnnotateFieldInfo( name="DATE ESTABLISHED", number=".02", fieldType=FMField.FIELDTYPE.DATE)
    protected Date dateEstablished;
    @FMAnnotateFieldInfo( name="DATE OF LAST UPDATE", number=".03", fieldType=FMField.FIELDTYPE.DATE)
    protected Date dateUpdated;
    @FMAnnotateFieldInfo( name="HEALTH RECORD NUMBER", number="4101", fieldType=FMField.FIELDTYPE.SUBFILE)
    protected FMHealthRecordNumber healthRecordNumber;

    public FMPatientRPMS() {
        super("9000001");
    }

    public FMPatientRPMS(FMResultSet results) {
        super("9000001");
        processResults( results );
    }

    public Date getDateEstablished() {
        return dateEstablished;
    }

    public String getName() {
        return name;
    }

    public Date getDateLastUpdated() {
        return dateUpdated;
    }

    public FMHealthRecordNumber getHealthRecordNumber() {
        if (healthRecordNumber == null) {
            healthRecordNumber = new FMHealthRecordNumber();
            healthRecordNumber.setParent( this );
        }
        return healthRecordNumber;
    }

    public String getFileNumber() {
        return("9000001");
    }

}
