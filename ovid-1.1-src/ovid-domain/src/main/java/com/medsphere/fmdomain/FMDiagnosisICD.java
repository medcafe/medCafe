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

public class FMDiagnosisICD extends FMRecord {
    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMDiagnosisICD.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMDiagnosisICD.class);
        fileInfo = new FMFile("ICD DIAGNOSIS") {

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

    @FMAnnotateFieldInfo(name = "CODE NUMBER", number = ".01", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String codeNumber;

    @FMAnnotateFieldInfo(name = "IDENTIFIER", number = "2", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String identifier;

    @FMAnnotateFieldInfo(name = "DIAGNOSIS", number = "3", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String diagnosis;

    @FMAnnotateFieldInfo(name = "MAJOR DIAGNOSTIC CATEGORY", number = "5", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(80.3)*/)
    protected Integer majorDiagnosticCategory;

    @FMAnnotateFieldInfo(name = "MDC13", number = "5.5", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected Double mdc13;

    @FMAnnotateFieldInfo(name = "MDC24", number = "5.7", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String mdc24;

    @FMAnnotateFieldInfo(name = "MDC25", number = "5.9", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String mdc25;

    @FMAnnotateFieldInfo(name = "ICD EXPANDED", number = "8", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String icdExpanded;

    @FMAnnotateFieldInfo(name = "SEX", number = "9.5", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String sex;

    @FMAnnotateFieldInfo(name = "DESCRIPTION", number = "10", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String description;

//    @FMAnnotateFieldInfo(name = "AGE LOW", number = "14", fieldType = FMField.FIELDTYPE.NUMERIC)
//    protected Double ageLow;
//
//    @FMAnnotateFieldInfo(name = "AGE HIGH", number = "15", fieldType = FMField.FIELDTYPE.NUMERIC)
//    protected Double ageHigh;
//
//    @FMAnnotateFieldInfo(name = "ACTIVATION DATE", number = "16", fieldType = FMField.FIELDTYPE.DATE)
//    protected Date activationDate;
//
    @FMAnnotateFieldInfo(name = "ICD9 CODES NOT TO USE WITH", number = "20", fieldType = FMField.FIELDTYPE.SUBFILE)
    protected String icd9CodesNotToUseWith;

    @FMAnnotateFieldInfo(name = "ICD9 CODES REQUIRED WITH", number = "30", fieldType = FMField.FIELDTYPE.SUBFILE)
    protected String icd9CodesRequiredWith;

    @FMAnnotateFieldInfo(name = "CODE NOT CC WITH", number = "40", fieldType = FMField.FIELDTYPE.SUBFILE)
    protected String codeNotCcWith;

    @FMAnnotateFieldInfo(name = "DRGa", number = "60", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(80.2)*/)
    protected Integer drga;

    @FMAnnotateFieldInfo(name = "DRGb", number = "61", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(80.2)*/)
    protected Integer drgb;

    @FMAnnotateFieldInfo(name = "DRGc", number = "62", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(80.2)*/)
    protected Integer drgc;

    @FMAnnotateFieldInfo(name = "DRGd", number = "63", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(80.2)*/)
    protected Integer drgd;

    @FMAnnotateFieldInfo(name = "DRGe", number = "64", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(80.2)*/)
    protected Integer drge;

    @FMAnnotateFieldInfo(name = "DRGf", number = "65", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE /*(80.2)*/)
    protected Integer drgf;

    @FMAnnotateFieldInfo(name = "EFFECTIVE DATE", number = "66", fieldType = FMField.FIELDTYPE.SUBFILE)
    protected String effectiveDate;

    @FMAnnotateFieldInfo(name = "DIAGNOSIS (VERSIONED)", number = "67", fieldType = FMField.FIELDTYPE.SUBFILE)
    protected String diagnosisVersioned;

    @FMAnnotateFieldInfo(name = "DESCRIPTION (VERSIONED)", number = "68", fieldType = FMField.FIELDTYPE.SUBFILE)
    protected String descriptionVersioned;

    @FMAnnotateFieldInfo(name = "COMPLICATION/COMORBIDITY", number = "70", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String complicationOrComorbidity;

    @FMAnnotateFieldInfo(name = "DRG GROUPER EFFECTIVE DATE", number = "71", fieldType = FMField.FIELDTYPE.SUBFILE)
    protected String drgGrouperEffectiveDate;

    @FMAnnotateFieldInfo(name = "MDC EFFECTIVE DATE", number = "72", fieldType = FMField.FIELDTYPE.SUBFILE)
    protected String mdcEffectiveDate;

    @FMAnnotateFieldInfo(name = "INACTIVE FLAG", number = "100", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String inactiveFlag;

    @FMAnnotateFieldInfo(name = "UNACCEPTABLE AS PRINCIPAL DX", number = "101", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String unacceptableAsPrincipalDx;

    @FMAnnotateFieldInfo(name = "INACTIVE DATE", number = "102", fieldType = FMField.FIELDTYPE.DATE)
    protected Date inactiveDate;

    public FMDiagnosisICD() {
        super("ICD DIAGNOSIS");
    }

    public FMDiagnosisICD(FMResultSet results) {
        super("ICD DIAGNOSIS");
        processResults(results);
    }

    public String getCodeNumber() {
        return codeNumber;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getMajorDiagnosticCategory() {
        return getValue("5");
    }

    public Double getMdc13() {
        return mdc13;
    }

    public String getMdc24() {
        return mdc24;
    }

    public String getMdc25() {
        return mdc25;
    }

    public String getIcdExpanded() {
        return icdExpanded;
    }

    public String getSex() {
        return sex;
    }

    public String getDescription() {
        return description;
    }

//    public Double getAgeLow() {
//        return ageLow;
//    }
//
//    public Double getAgeHigh() {
//        return ageHigh;
//    }
//
//    public Date getActivationDate() {
//        return activationDate;
//    }

    public String getIcd9CodesNotToUseWith() {
        return icd9CodesNotToUseWith;
    }

    public String getIcd9CodesRequiredWith() {
        return icd9CodesRequiredWith;
    }

    public String getCodeNotCcWith() {
        return codeNotCcWith;
    }

    public String getDrga() {
        return getValue("60");
    }

    public String getDrgb() {
        return getValue("61");
    }

    public String getDrgc() {
        return getValue("62");
    }

    public String getDrgd() {
        return getValue("63");
    }

    public String getDrge() {
        return getValue("64");
    }

    public String getDrgf() {
        return getValue("65");
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public String getDiagnosisVersioned() {
        return diagnosisVersioned;
    }

    public String getDescriptionVersioned() {
        return descriptionVersioned;
    }

    public String getComplicationOrComorbidity() {
        return complicationOrComorbidity;
    }

    public String getDrgGrouperEffectiveDate() {
        return drgGrouperEffectiveDate;
    }

    public String getMdcEffectiveDate() {
        return mdcEffectiveDate;
    }

    public String getInactiveFlag() {
        return inactiveFlag;
    }

    public String getUnacceptableAsPrincipalDx() {
        return unacceptableAsPrincipalDx;
    }

    public Date getInactiveDate() {
        return inactiveDate;
    }

    @Override
    public String toString() {
    return "ien=["+getIEN()+"]"

        + ((getCodeNumber() != null) ? " codeNumber=["+ getCodeNumber() +"]" : "")

        + ((getIdentifier() != null) ? " identifier=["+ getIdentifier() +"]" : "")

        + ((getDiagnosis() != null) ? " diagnosis=["+ getDiagnosis() +"]" : "")

        + ((getMajorDiagnosticCategory() != null) ? " majorDiagnosticCategory=["+ getMajorDiagnosticCategory() +"]" : "")

        + ((getMdc13() != null) ? " mdc13=["+ getMdc13() +"]" : "")

        + ((getMdc24() != null) ? " mdc24=["+ getMdc24() +"]" : "")

        + ((getMdc25() != null) ? " mdc25=["+ getMdc25() +"]" : "")

        + ((getIcdExpanded() != null) ? " icdExpanded=["+ getIcdExpanded() +"]" : "")

        + ((getSex() != null) ? " sex=["+ getSex() +"]" : "")

        + ((getDescription() != null) ? " description=["+ getDescription() +"]" : "")

//        + ((getAgeLow() != null) ? " ageLow=["+ getAgeLow() +"]" : "")
//
//        + ((getAgeHigh() != null) ? " ageHigh=["+ getAgeHigh() +"]" : "")
//
//        + ((getActivationDate() != null) ? " activationDate=["+ getActivationDate() +"]" : "")

        + ((getIcd9CodesNotToUseWith() != null) ? " icd9CodesNotToUseWith=["+ getIcd9CodesNotToUseWith() +"]" : "")

        + ((getIcd9CodesRequiredWith() != null) ? " icd9CodesRequiredWith=["+ getIcd9CodesRequiredWith() +"]" : "")

        + ((getCodeNotCcWith() != null) ? " codeNotCcWith=["+ getCodeNotCcWith() +"]" : "")

        + ((getDrga() != null) ? " drga=["+ getDrga() +"]" : "")

        + ((getDrgb() != null) ? " drgb=["+ getDrgb() +"]" : "")

        + ((getDrgc() != null) ? " drgc=["+ getDrgc() +"]" : "")

        + ((getDrgd() != null) ? " drgd=["+ getDrgd() +"]" : "")

        + ((getDrge() != null) ? " drge=["+ getDrge() +"]" : "")

        + ((getDrgf() != null) ? " drgf=["+ getDrgf() +"]" : "")

        + ((getEffectiveDate() != null) ? " effectiveDate=["+ getEffectiveDate() +"]" : "")

        + ((getDiagnosisVersioned() != null) ? " diagnosisVersioned=["+ getDiagnosisVersioned() +"]" : "")

        + ((getDescriptionVersioned() != null) ? " descriptionVersioned=["+ getDescriptionVersioned() +"]" : "")

        + ((getComplicationOrComorbidity() != null) ? " complicationOrComorbidity=["+ getComplicationOrComorbidity() +"]" : "")

        + ((getDrgGrouperEffectiveDate() != null) ? " drgGrouperEffectiveDate=["+ getDrgGrouperEffectiveDate() +"]" : "")

        + ((getMdcEffectiveDate() != null) ? " mdcEffectiveDate=["+ getMdcEffectiveDate() +"]" : "")

        + ((getInactiveFlag() != null) ? " inactiveFlag=["+ getInactiveFlag() +"]" : "")

        + ((getUnacceptableAsPrincipalDx() != null) ? " unacceptableAsPrincipalDx=["+ getUnacceptableAsPrincipalDx() +"]" : "")

        + ((getInactiveDate() != null) ? " inactiveDate=["+ getInactiveDate() +"]" : "")

        ;
    }

}
