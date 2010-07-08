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
package com.medsphere.fmdomain;
/*
 * container class of fileman V POV information
 */


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

public class FMV_POV extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/

    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMV_POV.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMV_POV.class);
        fileInfo = new FMFile("V POV") { //

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

    @FMAnnotateFieldInfo(name = "POV", number = ".01", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer pov;
    @FMAnnotateFieldInfo(name = "ICD NARRATIVE", number = ".019", fieldType = FMField.FIELDTYPE.COMPUTED)
    protected String narrative;
    @FMAnnotateFieldInfo(name = "PATIENT NAME", number = ".02", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer patient;
    @FMAnnotateFieldInfo(name = "VISIT", number = ".03", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer visit;
    @FMAnnotateFieldInfo(name = "PROVIDER NARRATIVE", number = ".04", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer providerNarrative;
    @FMAnnotateFieldInfo(name = "MODIFIER", number = ".06", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String modifier;
    @FMAnnotateFieldInfo(name="PRIMARY/SECONDARY", number = ".12", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String primarySecondary;
    @FMAnnotateFieldInfo(name="DATE OF INJURY", number = ".13", fieldType = FMField.FIELDTYPE.DATE)
    protected Date injuryDate;
    @FMAnnotateFieldInfo(name = "CLINICAL TERM", number = ".15", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer clinicalTerm;
    @FMAnnotateFieldInfo(name = "PROBLEM LIST ENTRY", number = ".16", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer problemEntry;
    @FMAnnotateFieldInfo(name = "ORDERING/RESULTING", number = ".17", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String orderingResulting;
    @FMAnnotateFieldInfo(name = "EVENT DATE AND TIME", number = "1201", fieldType = FMField.FIELDTYPE.DATE)
    protected Date eventDate;
    @FMAnnotateFieldInfo(name = "ORDERING PROVIDER", number = "1202", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer orderingProvider;
    @FMAnnotateFieldInfo(name = "ENCOUNTER PROVIDER", number = "1204", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer encounterProvider;
    @FMAnnotateFieldInfo(name = "COMMENTS", number = "81101", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String comments;
    protected FMICD_Diagnosis diagnosis;

    public FMV_POV() {
        super(fileInfo.getFileName());


    }

    public FMV_POV(FMResultSet results) {
        super(results);
    }
    /* This must be separately set in order to return the diagnosis record
     * 
     */
    public void setDiagnosis(FMICD_Diagnosis diag){
        diagnosis = diag;
    }
    public FMICD_Diagnosis getDiagnosis(){
        return diagnosis;
    }
    public Integer getPov (){
        return pov;
    }
    public String getPovValue()
    {
        return getValue(".01");
    }
    public Integer getPatient() {
        return patient;
    }
    public String getPatientName(){
        return getValue(".02");
    }
    public Integer getVisitIEN(){
       return visit;
    }
    public String getICDNarrative()
    {
        return narrative;
    }
    public Integer getProviderNarrative(){
        return providerNarrative;
    }
    public String getProviderNarrativeValue()
    {
        return getValue(".04");
    }
    public String getModifier()
    {
        return modifier;
    }
    public String getPrimarySecondary()
    {
        return primarySecondary;
    }
    public Date getInjuryDate()
    {
        return injuryDate;
    }
    public Integer getClinicalTerm()
    {
        return clinicalTerm;
    }
    public String getClinicalTermValue()
    {
        return getValue(".15");
    }
    public Integer getProblemEntry()
    {
        return problemEntry;
    }
    public String getProblemEntryValue()
    {
        return getValue(".16");
    }
    public String getOrderingResulting()
    {
        return orderingResulting;
    }

    public Date getEventDate()
    {
        return eventDate;
    }
    public Integer getOrderingProvider()
    {
        return orderingProvider;
    }
    public String getOrderingProviderValue()
    {
        return getValue("1202");
    }
     public Integer getEncounterProvider()
    {
        return encounterProvider;
    }
    public String getEncounterProviderValue()
    {
        return getValue("1204");
    }
    public String getComments()
    {
        return comments;
    }

    @Override
    public String toString() {
        return " Event Date=[" + getEventDate() + "]" +
        " patient=["+getPatientName()+"]"
        + " POV =["+getPovValue()+"]"
        + " ICD Narrative =["+ getICDNarrative() + "]"
                + " Provider Narrative =[" + getProviderNarrativeValue() + "]"
                + " modifier =[" + getModifier() + "]"
                + " Primary/Secondary =[" + getPrimarySecondary() + "]"
                + " injury date =[" + getInjuryDate() + "]"
                + " Clinical term =[" + getClinicalTermValue() + "]"
                + " problem entry =[" + getProblemEntryValue() + "]"
                + " ordering/resulting = [" + getOrderingResulting() + "]"
        + " visit IEN =[" + getVisitIEN() + "]"
        + " ordering provider=[" + getOrderingProviderValue() + "]"
        + " encounter provider =["+getEncounterProviderValue() +"]"+
                " comments =[" + getComments() + "]";
    }

}
