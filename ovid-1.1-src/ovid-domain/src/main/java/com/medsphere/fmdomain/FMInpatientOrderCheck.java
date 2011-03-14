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
import com.medsphere.fileman.FMField.FIELDTYPE;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author mark.taylor
 *
 * It was necessary to create this separate file for IP Order checks because the
 * PENDING OUTPATIENT ORDERS file names the field "ORDERS CHECKS"
 * while the NON-VERIFIED ORDERS file names the field "ORDER CHECKS"
 */
public class FMInpatientOrderCheck extends FMRecord {
    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMInpatientOrderCheck.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMInpatientOrderCheck.class);
        fileInfo = new FMFile("ORDER CHECKS") {

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

    @FMAnnotateFieldInfo(name="ORDER CHECK NARRATIVE", number=".01", fieldType=FIELDTYPE.FREE_TEXT)
    protected String narrative;
    @FMAnnotateFieldInfo(name="OVERRIDING PROVIDER", number="1", fieldType=FIELDTYPE.POINTER_TO_FILE)
    protected Integer overridingProvider;
    @FMAnnotateFieldInfo(name="OVERRIDING REASON", number="2", fieldType=FIELDTYPE.WORD_PROCESSING)
    protected Collection<String> overridingReason;

    public FMInpatientOrderCheck() {
        super("ORDER CHECKS");
    }

    public FMInpatientOrderCheck(FMResultSet results) {
        super( results );
    }

    public String getOrderCheckNarrative() {
        return narrative;
    }

    public Integer getOverridingProvider() {
        return overridingProvider;
    }

    public Collection<String> getOverridingReason() {
        return overridingReason;
    }
    @Override
    public String toString() {
        return "\nnarrative=[" + narrative + "]\n   overriding reason=[" + getOverridingReason() + "]\n   overriding provider=[" + getOverridingProvider() + "]";
    }

}

