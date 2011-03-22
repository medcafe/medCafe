/*
 *  Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.medsphere.fmdomain;
/*
 * container class of fileman V Skin Test information
 */


import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.Date;
import java.util.ArrayList;

import java.util.Map;
import java.util.Set;

import com.medsphere.fileman.FMAnnotateFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;

public class FMV_SkinTest extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/

    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMV_SkinTest.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMV_SkinTest.class);
        fileInfo = new FMFile("V SKIN TEST") { //

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

    @FMAnnotateFieldInfo(name = "SKIN TEST", number = ".01", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer skinTest;
    @FMAnnotateFieldInfo(name = "PATIENT NAME", number = ".02", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer patient;
    @FMAnnotateFieldInfo(name = "VISIT", number = ".03", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer visit;
    @FMAnnotateFieldInfo(name = "RESULTS", number = ".04", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String result;
    @FMAnnotateFieldInfo(name = "READING", number = ".05", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected Double reading;
    @FMAnnotateFieldInfo(name = "DATE READ", number = ".06", fieldType = FMField.FIELDTYPE.DATE)
    protected Date dateRead;
    @FMAnnotateFieldInfo(name = "DIAGNOSIS", number = ".08", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer diagnosis;
    @FMAnnotateFieldInfo(name = "DIAGNOSIS 2", number = ".09", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer diag2;
    @FMAnnotateFieldInfo(name = "DIAGNOSIS 3", number = ".1", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer diag3;
    @FMAnnotateFieldInfo(name = "DIAGNOSIS 4", number = ".11", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer diag4;
    @FMAnnotateFieldInfo(name = "DIAGNOSIS 5", number = ".12", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer diag5;
    @FMAnnotateFieldInfo(name = "DIAGNOSIS 6", number = ".13", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer diag6;
    @FMAnnotateFieldInfo(name = "DIAGNOSIS 7", number = ".14", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer diag7;
    @FMAnnotateFieldInfo(name = "DIAGNOSIS 8", number = ".15", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer diag8;
    @FMAnnotateFieldInfo(name = "EVENT DATE AND TIME", number = "1201", fieldType = FMField.FIELDTYPE.DATE)
    protected Date eventDate;
    @FMAnnotateFieldInfo(name = "ORDERING PROVIDER", number = "1202", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer orderingProvider;
    @FMAnnotateFieldInfo(name = "ENCOUNTER PROVIDER", number = "1204", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer encounterProvider;
    @FMAnnotateFieldInfo(name = "COMMENTS", number = "81101", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String comments;
    private ArrayList<FMICD_Diagnosis> diagnoses = null;

    public FMV_SkinTest() {
        super(fileInfo.getFileName());


    }

    public FMV_SkinTest(FMResultSet results) {
        super(results);

    }
    public Integer getSkinTest (){
        return skinTest;
    }
    public String getSkinTestValue()
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
    public String getResults(){
        return result;
    }
    public Double getReading(){
        return reading;
    }
    public Date getDateRead(){
        return dateRead;
    }
    public Integer getDiagnosis()
    {
        return diagnosis;
    }
    public String getDiagnosisValue()
    {
        return getValue(".08");
    }
    public Integer getDiagnosis2()
    {
	return diag2;
    }
    public String getDiagnosis2Value()
    {
        return getValue(".09");
    }
    public Integer getDiagnosis3()
    {
        return diag3;
    }
    public String getDiagnosis3Value()
    {
        return getValue(".1");
    }
    public Integer getDiagnosis4()
    {
        return diag4;
    }
    public String getDiagnosis4Value()
    {
        return getValue(".11");
    }
    public Integer getDiagnosis5()
    {
	return diag5;
    }
    public String getDiagnosis5Value()
    {
        return getValue(".12");
    }
    public Integer getDiagnosis6()
    {
        return diag6;
    }
    public String getDiagnosis6Value()
    {
        return getValue(".13");
    }
    public Integer getDiagnosis7()
    {
	return diag7;
    }
    public String getDiagnosis7Value()
    {
        return getValue(".14");
    }
    public Integer getDiagnosis8()
    {
        return diag8;
    }
    public String getDiagnosis8Value()
    {
        return getValue(".15");
    }
 public Collection<String> getDiagnosesKeyList() {
        ArrayList<String> diagList = new ArrayList<String>();
        if (getDiagnosisValue() != null) {
            diagList.add(getDiagnosisValue());
        }
        if (getDiagnosis2Value() != null) {
            diagList.add(getDiagnosis2Value());
        }
        if (getDiagnosis3Value() != null) {
            diagList.add(getDiagnosis3Value());
        }
        if (getDiagnosis4Value() != null) {
            diagList.add(getDiagnosis4Value());
        }
        if (getDiagnosis5Value() != null) {
            diagList.add(getDiagnosis5Value());
        }
        if (getDiagnosis6Value() != null) {
            diagList.add(getDiagnosis6Value());
        }
        if (getDiagnosis7Value() != null) {
            diagList.add(getDiagnosis7Value());
        }
        if (getDiagnosis8Value() != null) {
            diagList.add(getDiagnosis8Value());
        }
        return diagList;
    }

    public void setDiagnoses(ArrayList<FMICD_Diagnosis> diagArray)
    {
        diagnoses = diagArray;
    }
    /* to use this method, you must first populate it by calling setDiagnoses  */
    public ArrayList<FMICD_Diagnosis> getDiagnoses()
    {
        return diagnoses;
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
        + " skin test =["+getSkinTestValue()+"]"
        + " result =["+ getResults() + "]"
        + " reading =[" + getReading() + "]"
        + " date read =[" + getDateRead() + "]"
        + " diagnosis =[" + getDiagnosisValue() + "]"
        + " diagnosis =[" + getDiagnosis2Value() + "]"
        + " diagnosis =[" + getDiagnosis3Value() + "]"
        + " diagnosis =[" + getDiagnosis4Value() + "]"
        + " diagnosis =[" + getDiagnosis5Value() + "]"
        + " diagnosis =[" + getDiagnosis6Value() + "]"
        + " diagnosis =[" + getDiagnosis7Value() + "]"
        + " diagnosis =[" + getDiagnosis8Value() + "]"
        + " visit IEN =[" + getVisitIEN() + "]"
        + " ordering provider=[" + getOrderingProviderValue() + "]"
        + " encounter provider =["+getEncounterProviderValue() +"]"+
                " comments =[" + getComments() + "]";
    }

}

