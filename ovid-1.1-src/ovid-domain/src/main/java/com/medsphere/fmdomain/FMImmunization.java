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

public class FMImmunization extends FMRecord {
    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMImmunization.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMImmunization.class);
        fileInfo = new FMFile("IMMUNIZATION") {

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

    @FMAnnotateFieldInfo(name = "NAME", number = ".01", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String name;

    @FMAnnotateFieldInfo(name = "SHORT NAME", number = ".02", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String shortName;

    @FMAnnotateFieldInfo(name = "HL7-CVX CODE", number = ".03", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected Double hl7CvxCode;

    @FMAnnotateFieldInfo(name = "DEFAULT LOT#", number = ".04", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer defaultLotNumber;

    @FMAnnotateFieldInfo(name = "MAXIMUM# DOSES IN SERIES", number = ".05", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected Double maximumNumberDosesInSeries;

    @FMAnnotateFieldInfo(name = "ACTIVE", number = ".07", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String active;

    @FMAnnotateFieldInfo(name = "SKIN TEST", number = ".08", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String skinTest;

    @FMAnnotateFieldInfo(name = "VACCINE GROUP (SERIES TYPE)", number = ".09", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer vaccineGroupSeriesType;

    @FMAnnotateFieldInfo(name = "ALTERNATE SHORT NAME", number = ".1", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String alternateShortName;

    @FMAnnotateFieldInfo(name = "CPT CODE", number = ".11", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer cptCode;

    @FMAnnotateFieldInfo(name = "RELATED CONTRAIND HL7 CODES", number = ".12", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String relatedContraindHl7Codes;

    @FMAnnotateFieldInfo(name = "VIS DEFAULT DATE", number = ".13", fieldType = FMField.FIELDTYPE.DATE)
    protected Date visDefaultDate;

    @FMAnnotateFieldInfo(name = "ICD DIAGNOSIS CODE", number = ".14", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String icdDiagnosisCode;

    @FMAnnotateFieldInfo(name = "ICD PROCEDURE CODE", number = ".15", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String icdProcedureCode;

    @FMAnnotateFieldInfo(name = "INCLUDE IN FORECAST", number = ".16", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String includeInForecast;

    @FMAnnotateFieldInfo(name = "INCLUDE IN VAC ACCOUNT REPORT", number = ".17", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String includeInVacAccountReport;

    @FMAnnotateFieldInfo(name = "DEFAULT VOLUME", number = ".18", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected Double defaultVolume;

    @FMAnnotateFieldInfo(name = "COMPONENT #1", number = ".21", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer componentNumber1;

    @FMAnnotateFieldInfo(name = "COMPONENT #2", number = ".22", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer componentNumber2;

    @FMAnnotateFieldInfo(name = "COMPONENT #3", number = ".23", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer componentNumber3;

    @FMAnnotateFieldInfo(name = "COMPONENT #4", number = ".24", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer componentNumber4;

    @FMAnnotateFieldInfo(name = "COMPONENT #5", number = ".25", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer componentNumber5;

    @FMAnnotateFieldInfo(name = "COMPONENT #6", number = ".26", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer componentNumber6;

    @FMAnnotateFieldInfo(name = "BRAND #1", number = "1.01", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String brandNumber1;

    @FMAnnotateFieldInfo(name = "BRAND #2", number = "1.03", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String brandNumber2;

    @FMAnnotateFieldInfo(name = "BRAND #3", number = "1.05", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String brandNumber3;

    @FMAnnotateFieldInfo(name = "BRAND #4", number = "1.07", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String brandNumber4;

    @FMAnnotateFieldInfo(name = "BRAND #5", number = "1.09", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String brandNumber5;

    @FMAnnotateFieldInfo(name = "FULL NAME", number = "1.14", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String fullName;

    @FMAnnotateFieldInfo(name = "CPT CODE 2ND", number = "1.15", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer cptCode2;

    public FMImmunization() {
        super("IMMUNIZATION");
    }

    public FMImmunization(FMResultSet results) {
    	this();
        processResults(results);
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public Double getHl7CvxCode() {
        return hl7CvxCode;
    }

    public String getDefaultLotNumber() {
        return getValue(".04");
    }

    public Double getMaximumNumberDosesInSeries() {
        return maximumNumberDosesInSeries;
    }

    public String getActive() {
        return active;
    }

    public String getSkinTest() {
        return skinTest;
    }

    public String getVaccineGroupSeriesType() {
        return getValue(".09");
    }

    public String getAlternateShortName() {
        return alternateShortName;
    }

    public Integer getCptCode() {
        return cptCode;
    }

    public String getRelatedContraindHl7Codes() {
        return relatedContraindHl7Codes;
    }

    public Date getVisDefaultDate() {
        return visDefaultDate;
    }

    public String getIcdDiagnosisCode() {
        return icdDiagnosisCode;
    }

    public String getIcdProcedureCode() {
        return icdProcedureCode;
    }

    public String getIncludeInForecast() {
        return includeInForecast;
    }

    public String getIncludeInVacAccountReport() {
        return includeInVacAccountReport;
    }

    public Double getDefaultVolume() {
        return defaultVolume;
    }

    public String getComponentNumber1() {
        return getValue(".21");
    }

    public String getComponentNumber2() {
        return getValue(".22");
    }

    public String getComponentNumber3() {
        return getValue(".23");
    }

    public String getComponentNumber4() {
        return getValue(".24");
    }

    public String getComponentNumber5() {
        return getValue(".25");
    }

    public String getComponentNumber6() {
        return getValue(".26");
    }

    public String getBrandNumber1() {
        return brandNumber1;
    }

    public String getBrandNumber2() {
        return brandNumber2;
    }

    public String getBrandNumber3() {
        return brandNumber3;
    }

    public String getBrandNumber4() {
        return brandNumber4;
    }

    public String getBrandNumber5() {
        return brandNumber5;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCptCode2() {
        return getValue("1.15");
    }
    @Override
    public String toString() {

	return ""
	    + ((getName() != null) ? " name=["+ getName() +"]" : "")
	    + ((getShortName() != null) ? " shortName=["+ getShortName() +"]" : "")
	    + ((getHl7CvxCode() != null) ? " hl7CvxCode=["+ getHl7CvxCode() +"]" : "")
	    + ((getDefaultLotNumber() != null) ? " defaultLotNumber=["+ getDefaultLotNumber() +"]" : "")
	    + ((getMaximumNumberDosesInSeries() != null) ? " maximumNumberDosesInSeries=["+ getMaximumNumberDosesInSeries() +"]" : "")
	    + ((getActive() != null) ? " active=["+ getActive() +"]" : "")
	    + ((getSkinTest() != null) ? " skinTest=["+ getSkinTest() +"]" : "")
	    + ((getVaccineGroupSeriesType() != null) ? " vaccineGroupSeriesType=["+ getVaccineGroupSeriesType() +"]" : "")
	    + ((getAlternateShortName() != null) ? " alternateShortName=["+ getAlternateShortName() +"]" : "")
	    + ((getCptCode() != null) ? " cptCode=["+ getCptCode() +"]" : "")
	    + ((getRelatedContraindHl7Codes() != null) ? " relatedContraindHl7Codes=["+ getRelatedContraindHl7Codes() +"]" : "")
	    + ((getVisDefaultDate() != null) ? " visDefaultDate=["+ getVisDefaultDate() +"]" : "")
	    + ((getIcdDiagnosisCode() != null) ? " icdDiagnosisCode=["+ getIcdDiagnosisCode() +"]" : "")
	    + ((getIcdProcedureCode() != null) ? " icdProcedureCode=["+ getIcdProcedureCode() +"]" : "")
	    + ((getIncludeInForecast() != null) ? " includeInForecast=["+ getIncludeInForecast() +"]" : "")
	    + ((getIncludeInVacAccountReport() != null) ? " includeInVacAccountReport=["+ getIncludeInVacAccountReport() +"]" : "")
	    + ((getDefaultVolume() != null) ? " defaultVolume=["+ getDefaultVolume() +"]" : "")
	    + ((getComponentNumber1() != null) ? " componentNumber1=["+ getComponentNumber1() +"]" : "")
	    + ((getComponentNumber2() != null) ? " componentNumber2=["+ getComponentNumber2() +"]" : "")
	    + ((getComponentNumber3() != null) ? " componentNumber3=["+ getComponentNumber3() +"]" : "")
	    + ((getComponentNumber4() != null) ? " componentNumber4=["+ getComponentNumber4() +"]" : "")
	    + ((getComponentNumber5() != null) ? " componentNumber5=["+ getComponentNumber5() +"]" : "")
	    + ((getComponentNumber6() != null) ? " componentNumber6=["+ getComponentNumber6() +"]" : "")
	    + ((getBrandNumber1() != null) ? " brandNumber1=["+ getBrandNumber1() +"]" : "")
	    + ((getBrandNumber2() != null) ? " brandNumber2=["+ getBrandNumber2() +"]" : "")
	    + ((getBrandNumber3() != null) ? " brandNumber3=["+ getBrandNumber3() +"]" : "")
	    + ((getBrandNumber4() != null) ? " brandNumber4=["+ getBrandNumber4() +"]" : "")
	    + ((getBrandNumber5() != null) ? " brandNumber5=["+ getBrandNumber5() +"]" : "")
	    + ((getFullName() != null) ? " fullName=["+ getFullName() +"]" : "")
	    + ((getCptCode2() != null) ? " cptCode2=["+ getCptCode2() +"]" : "")
	    ;
    }
}
