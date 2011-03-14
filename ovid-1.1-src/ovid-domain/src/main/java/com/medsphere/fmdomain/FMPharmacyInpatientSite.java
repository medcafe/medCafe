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
public class FMPharmacyInpatientSite extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMPharmacyInpatientSite.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMPharmacyInpatientSite.class);
        fileInfo = new FMFile("INPATIENT SITE") {

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
    @FMAnnotateFieldInfo( name="*RELATED INSTITUTION", number="1", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer relatedInstitution;

    public FMPharmacyInpatientSite() {
        super("INPATIENT SITE");
    }

    public FMPharmacyInpatientSite(FMResultSet results) {
        super("INPATIENT SITE");
        processResults(results);
    }

    public String getName() {
        return name;
    }

    public Integer getRelatedInstitution() {
        return relatedInstitution;
    }

    @Override
    public String toString() {
        return
        "name=["+name+"]" +
        "  relatedInstitution=[" + relatedInstitution + "]"
        ;
    }
}
