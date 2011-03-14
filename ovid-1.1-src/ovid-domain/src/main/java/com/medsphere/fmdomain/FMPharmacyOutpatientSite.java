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
public class FMPharmacyOutpatientSite extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMPharmacyOutpatientSite.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMPharmacyOutpatientSite.class);
        fileInfo = new FMFile("OUTPATIENT SITE") {

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
    @FMAnnotateFieldInfo( name="PROFILE 'SORT BY' DEFAULT", number=".114", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String profileSortByDefault;
    @FMAnnotateFieldInfo( name="COPIES ON NEW", number=".115", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String copiesOnNewFlag;
    @FMAnnotateFieldInfo( name="VERIFICATION", number=".12", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String verificationFlag;
    @FMAnnotateFieldInfo( name="EDIT DRUG", number=".13", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String editDrugFlag;
    @FMAnnotateFieldInfo( name="RENEWING RX'S ALLOWED", number=".14", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String renewAllowedFlag;
    @FMAnnotateFieldInfo( name="RELATED INSTITUTION", number="100", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer relatedInstitution;

    public FMPharmacyOutpatientSite() {
        super("OUTPATIENT SITE");
    }

    public FMPharmacyOutpatientSite(FMResultSet results) {
        super("OUTPATIENT SITE");
        processResults(results);
    }

    public String getName() {
        return name;
    }

    public String getCopiesOnNewFlag() {
        return copiesOnNewFlag;
    }

    public String getEditDrugFlag() {
        return editDrugFlag;
    }

    public String getProfileSortByDefault() {
        return profileSortByDefault;
    }

    public String getVerificationFlag() {
        return verificationFlag;
    }

    public Integer getRelatedInstitution() {
        return relatedInstitution;
    }

    public String getRenewAllowedFlag() {
        return renewAllowedFlag;
    }

    @Override
    public String toString() {
        return
        "name=["+name+"]"
        + " profileSortByDefault=["+profileSortByDefault+"]"
        + " verificationFlag=["+verificationFlag+"]"
        + " copiesOnNewFlag=["+copiesOnNewFlag+"]"
        + " editDrugFlag=["+editDrugFlag+"]"
        + " renewAllowedFlag=["+renewAllowedFlag+"]"
        + " relatedInstitution["+relatedInstitution+"]"
        + " IEN=["+getIEN()+"]"
        ;
    }
}
