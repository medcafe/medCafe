// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2011  Medsphere Systems Corporation
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
import com.medsphere.fileman.FMField.FIELDTYPE;

public class FMPersonClass extends FMRecord {
    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMPersonClass.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMPersonClass.class);
        fileInfo = new FMFile("PERSON CLASS") {

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
    @FMAnnotateFieldInfo(name="PROVIDER TYPE", number=".01", fieldType=FIELDTYPE.FREE_TEXT)
    protected String providerType;
    @FMAnnotateFieldInfo(name="PROVIDER TYPE CODE", number=".011", fieldType=FIELDTYPE.COMPUTED)
    protected String providerTypeCode;
    @FMAnnotateFieldInfo(name="CLASSIFICATION", number="1", fieldType=FIELDTYPE.FREE_TEXT)
    protected String classification;
    @FMAnnotateFieldInfo(name="CLASSIFICATION CODE", number="1.1", fieldType=FIELDTYPE.COMPUTED)
    protected String classificationCode;
    @FMAnnotateFieldInfo(name="AREA OF SPECIALIZATION", number="2", fieldType=FIELDTYPE.FREE_TEXT)
    protected String areaOfSpecialization;
    @FMAnnotateFieldInfo(name="AREA OF SPECIALIZATION CODE", number="2.1", fieldType=FIELDTYPE.COMPUTED)
    protected String areaOfSpecializationCode;
    @FMAnnotateFieldInfo(name="STATUS", number="3", fieldType=FIELDTYPE.SET_OF_CODES)
    protected String status;
    @FMAnnotateFieldInfo(name="DATE INACTIVATED", number="4", fieldType=FIELDTYPE.DATE)
    protected Date dateInactivated;
    @FMAnnotateFieldInfo(name="VA CODE", number="5", fieldType=FIELDTYPE.FREE_TEXT)
    protected String vaCode;
    @FMAnnotateFieldInfo(name="X12 CODE", number="6", fieldType=FIELDTYPE.FREE_TEXT)
    protected String x12Code;
    @FMAnnotateFieldInfo(name="SPECIALTY CODE", number="8", fieldType=FIELDTYPE.FREE_TEXT)
    protected String specialtyCode;

    public FMPersonClass() {
        super("PERSON CLASS");
    }

    public FMPersonClass(FMResultSet results) {
        super("PERSON CLASS");
        processResults(results);
    }

    public String getProviderType() {
        return providerType;
    }

    public String getProviderTypeCode() {
        return providerTypeCode;
    }

    public String getClassification() {
        return classification;
    }

    public String getClassificationCode() {
        return classificationCode;
    }

    public String getAreaOfSpecialization() {
        return areaOfSpecialization;
    }

    public String getAreaOfSpecializationCode() {
        return areaOfSpecializationCode;
    }

    public String getStatus() {
        return status;
    }

    public Date getDateInactivated() {
        return dateInactivated;
    }

    public String getVaCode() {
        return vaCode;
    }

    public String getX12Code() {
        return x12Code;
    }

    public String getSpecialtyCode() {
        return specialtyCode;
    }

    @Override
    public String toString() {
    return
        "providerType=["+providerType+"]"
        + " providerTypeCode=["+providerTypeCode+"]"
        + " classification=["+classification+"]"
        + " classificationCode=["+classificationCode+"]"
        + " areaOfSpecialization=["+areaOfSpecialization+"]"
        + " areaOfSpecializationCode=["+areaOfSpecializationCode+"]"
        + " status=["+status+"]"
        + " dateInactivated=["+dateInactivated+"]"
        + " vaCode=["+vaCode+"]"
        + " x12Code=["+x12Code+"]"
        + " specialtyCode=["+specialtyCode+"]"
        ;
    }
}
