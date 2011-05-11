/*
 *  Copyright 2011 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.
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
 * container class of fileman Patient Allergies information
 */


import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;
import java.util.Set;

import com.medsphere.fileman.FMAnnotateFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;

public class FMPatient_Allergies extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/

    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;
    public static final String FOOD_ALLERGY = "FOOD";
    public static final String FOOD_ALLERGY_CODE = "F";
    public static final String DRUG_ALLERGY = "DRUG";
    public static final String DRUG_ALLERGY_CODE = "D";
    public static final String FOOD_AND_DRUG_ALLERGY = "FOOD, DRUG";
    public static final String FOOD_AND_DRUG_ALLERGY_CODE = "DF";
    public static final String OTHER_ALLERGY = "OTHER";
    public static final String OTHER_ALLERGY_CODE = "O";
    public static final String OBSERVED_REACTION = "OBSERVED";
    public static final String OBSERVED_REACTION_CODE = "o";
    public static final String HISTORICAL_REACTION = "HISTORICAL";
    public static final String HISTORICAL_REACTION_CODE="h";
    public static final String ALLERGY_MECHANISM = "ALLERGY";
    public static final String ALLERGY_MECHANISM_CODE = "A";
    public static final String PHARMACOLOGIC_MECHANISM = "PHARMACOLOGIC";
    public static final String PHARMACOLOGIC_MECHANISM_CODE = "P";
    public static final String UNKNOWN_MECHANISM = "UNKNOWN";
    public static final String UNKNOWN_MECHANISM_CODE = "U";
    private static final Map<String, String> allergyCodeMap = new HashMap<String, String>();
    static {
        allergyCodeMap.put(FOOD_ALLERGY, FOOD_ALLERGY_CODE);
        allergyCodeMap.put(DRUG_ALLERGY, DRUG_ALLERGY_CODE);
        allergyCodeMap.put(FOOD_AND_DRUG_ALLERGY, FOOD_AND_DRUG_ALLERGY_CODE);
        allergyCodeMap.put(OTHER_ALLERGY,OTHER_ALLERGY_CODE);
    }
     private static final Map<String, String> obsHistMap = new HashMap<String, String>();
    static {
        obsHistMap.put(OBSERVED_REACTION, OBSERVED_REACTION_CODE);
        obsHistMap.put(HISTORICAL_REACTION, HISTORICAL_REACTION_CODE);

    }
 private static final Map<String, String> mechCodeMap = new HashMap<String, String>();
    static {
        mechCodeMap.put(ALLERGY_MECHANISM, ALLERGY_MECHANISM_CODE);
        mechCodeMap.put(PHARMACOLOGIC_MECHANISM, PHARMACOLOGIC_MECHANISM_CODE);
        mechCodeMap.put(UNKNOWN_MECHANISM, UNKNOWN_MECHANISM_CODE);

    }

    static {
        domainJavaFields = getDomainJavaFields(FMPatient_Allergies.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMPatient_Allergies.class);
        fileInfo = new FMFile("PATIENT ALLERGIES") { //

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

    @FMAnnotateFieldInfo(name = "PATIENT", number = ".01", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer patient;
    @FMAnnotateFieldInfo(name = "REACTANT", number = ".02", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String reactant;
    @FMAnnotateFieldInfo(name = "GMR ALLERGY", number = "1", fieldType = FMField.FIELDTYPE.VARIABLE_POINTER)
    protected String allergy;
    @FMAnnotateFieldInfo(name = "ALLERGY TYPE", number = "3.1", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String allergyType;
    @FMAnnotateFieldInfo(name = "ORIGINATION DATE/TIME", number = "4", fieldType = FMField.FIELDTYPE.DATE)
    protected Date originationDate;
    @FMAnnotateFieldInfo(name = "ORIGINATOR", number = "5", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer originator;
    @FMAnnotateFieldInfo(name = "OBSERVED/HISTORICAL", number = "6", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String observedOrHist;
    @FMAnnotateFieldInfo(name = "REACTIONS", number = "10", fieldType = FMField.FIELDTYPE.SUBFILE)
    protected FMPatientAllergyReaction reaction;
    protected HashMap<String, FMPatientAllergyReaction> reactions;
    @FMAnnotateFieldInfo(name = "MECHANISM", number = "17", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String mechanism;
    @FMAnnotateFieldInfo(name= "COMMENTS", number = "26", fieldType = FMField.FIELDTYPE.SUBFILE)
    protected FMPatientAllergyComment comment;
    protected HashMap<String, FMPatientAllergyComment> comments;
    protected HashMap<String, FMPatientAllergyComment> commentLookup;
    protected HashMap<String, FMPatientAllergyReaction> reactionLookup;
    protected ArrayList<FMPatientAllergyComment> newComments;
    protected ArrayList<FMPatientAllergyReaction> newReactions;


    public FMPatient_Allergies() {
        super(fileInfo.getFileName());


    }

    public FMPatient_Allergies(FMResultSet results) {
        super(fileInfo.getFileName());
        processResults(results);
    }

    public String getReactant (){
        return reactant;
    }
    public void setReactant(String reactant) throws ModifiedKeyFileManFieldException
    {
      if (getIEN()==null ||Integer.parseInt(getIEN())<=0)
      {
        setDomainValue("reactant", reactant);
      }
      else
      {
          throw new ModifiedKeyFileManFieldException("Reactant is a key field. Can't modify a key field.");
      }
    }
    public String getAllergy()
    {
        return allergy;
    }
    public void setAllergy(String file_num, String ien) throws ModifiedKeyFileManFieldException
    {
         if (getIEN()==null ||Integer.parseInt(getIEN())<=0)
      {
        setDomainValue("allergy", ien+";"+file_num);
        }
         else
         {
             throw new ModifiedKeyFileManFieldException("Allergy is a key field. Can't modify a key field.");
         }
    }
    public String getAllergyReactantFileIEN()
    {
        String[] pieces = getAllergy().split(";");
        return pieces[0];
    }
    public String getAllergyReactantFileNum()
    {
        String[] pieces = getAllergy().split(";");
        return pieces[1];
    }
    public String getAllergyValue(){
        return getValue("1");
    }

    public Integer getPatient() {
        return patient;
    }
    public void setPatient(Integer patientIEN) throws ModifiedKeyFileManFieldException
    {
        if (getIEN()==null ||Integer.parseInt(getIEN())<=0)
        {
        setDomainValue("patient", patientIEN);
        }
          else
      {
          throw new ModifiedKeyFileManFieldException("Patient is a key field. Can't modify a key field.");
      }
    }

    public String getPatientName(){
        return getValue(".01");
    }

    public String getAllergyType(){
       return allergyType;

    }
    public void setAllergyType(String allergyType) throws InvalidFileManFieldValueException
    {
        String allergyCode;
        for (String allergyC : allergyCodeMap.values())
        {
            if (allergyC.equals(allergyType.toUpperCase()))
            {
                setDomainValue("allergyType", allergyC);
                return;
            }
        }
        if ((allergyCode = allergyCodeMap.get(allergyType.toUpperCase()))!= null)
        {
        setDomainValue("allergyType", allergyCode );
        }
        else
        {
            throw new InvalidFileManFieldValueException("Invalid values for allergy type:  Enter '"+
                    DRUG_ALLERGY_CODE +"' for "+DRUG_ALLERGY+"; '"+FOOD_ALLERGY_CODE
                    +"' for "+ FOOD_ALLERGY +"; or '"+FOOD_AND_DRUG_ALLERGY_CODE+"' for " +
                    FOOD_AND_DRUG_ALLERGY + "; and, '"+OTHER_ALLERGY_CODE +"' for "
                    + OTHER_ALLERGY);
        }
    }
    public void setOriginationDateTime(Date origDate)
    {
        setDomainValue("originationDate", origDate);
    }
    public Date getOriginationDateTime(){
        return originationDate;
    }
    public Integer getOriginatorIEN() {
        return originator;
    }
    public void setOriginator(Integer origIen)
    {
        setDomainValue("originator", origIen);
    }
    public String getOriginatorName(){
        return getValue("5");
    }
    public String getObservedOrHist()
    {
        return observedOrHist;
    }
    public void setObservedOrHist(String obsHist) throws InvalidFileManFieldValueException
    {
       String obsCode;
        for (String obsC : obsHistMap.values())
        {
            if (obsHist.toLowerCase().equals(obsC))
            {
                setDomainValue("observedOrHist", obsHist);
                return;
            }
        }
        if ((obsCode = obsHistMap.get(obsHist.toUpperCase()))!= null)
        {
        setDomainValue("observedOrHist", obsCode );
        }
        else
        {
            throw new InvalidFileManFieldValueException("Invalid values for Observed or Historical: Enter '"+
                    OBSERVED_REACTION_CODE+"' for "+ OBSERVED_REACTION
                    +"; '"+ HISTORICAL_REACTION_CODE+"' for "+HISTORICAL_REACTION);
        }
    }

    public FMPatientAllergyReaction getReactionReference(){
        if (reaction == null)
        {
            reaction = new FMPatientAllergyReaction();
            reaction.setParent(this);
        }
        return reaction;
    }
    public HashMap<String, FMPatientAllergyReaction> getReactionLookup()
    {
        return reactionLookup;
    }
    public void addReactions(FMPatientAllergyReaction react)
    {
        if (reactions == null)
        {
            reactions = new HashMap<String, FMPatientAllergyReaction>();
        }
        if (reactionLookup == null)
        {
            reactionLookup = new HashMap<String, FMPatientAllergyReaction>();
        }
        if (react.getIEN() != null)
        {

        reactionLookup.put(react.getReactionValue().toLowerCase(), react);
        reactions.put(react.getIENS(), react);
        }
        else{
            if (newReactions == null)
            {
                newReactions = new ArrayList<FMPatientAllergyReaction>();
            }
            newReactions.add(react);
        }
    }
    public HashMap<String, FMPatientAllergyReaction> getReactions()
    {
        return reactions;
    }
    public List<FMPatientAllergyReaction> getNewReactions()
    {
        return newReactions;
    }
    public List<FMPatientAllergyComment> getNewComments()
    {
        return newComments;
    }

    public String getMechanism()
    {
        return mechanism;
    }
    public void setMechanism(String mech) throws InvalidFileManFieldValueException
    {
 String mechCode;
        for (String mechC : mechCodeMap.values())
        {
            if (mechC.equals(mech.toUpperCase()))
            {
                setDomainValue("mechanism", mechC);
                return;
            }
        }
        if ((mechCode = mechCodeMap.get(mech))!= null)
        {
        setDomainValue("mechanism", mechCode );
        }
         else
        {
            throw new InvalidFileManFieldValueException("Valid values for Nature of Reaction are: '"+ ALLERGY_MECHANISM_CODE+ "' for "+
                    ALLERGY_MECHANISM +"; '"+PHARMACOLOGIC_MECHANISM_CODE+"' for "+PHARMACOLOGIC_MECHANISM +"; or, "
                    + "'"+UNKNOWN_MECHANISM_CODE+"' for "+ UNKNOWN_MECHANISM);
         }

    }
 public FMPatientAllergyComment getCommentReference(){
        if (comment == null)
        {
            comment = new FMPatientAllergyComment();
            comment.setParent(this);
        }
        return comment;
    }
 public HashMap<String, FMPatientAllergyComment> getCommentLookup()
    {
     return commentLookup;
 }
    public void addComments(FMPatientAllergyComment comm)
    {
        if (comments == null)
        {
            comments = new HashMap<String, FMPatientAllergyComment>();
        }
        if (commentLookup == null)
        {
            commentLookup = new HashMap<String, FMPatientAllergyComment>();

        }
        if (comm.getIEN()!= null)
        {
        comments.put(comm.getIENS(),comm);
        commentLookup.put(comm.getComments().toLowerCase(), comm);
        }
        else
        {
            if (newComments == null)
            {
                newComments = new ArrayList<FMPatientAllergyComment>();
            }
            newComments.add(comm);
        }

    }
    public HashMap<String, FMPatientAllergyComment> getComments()
    {
        return comments;
    }


    @Override
    public String toString() {
        return " Allergy: "
                + "IEN=[" + getIEN() + "]"
                + "Patient=[" + getPatient() + "]" +
        " reactant =["+getReactant()+"]"
        + " allergy =["+getAllergy()+"]"
        + " allergy Text = ["+getAllergyValue() + "]"
        + " allergy type =["+ getAllergyType() + "]"
        + " origination date time =[" + getOriginationDateTime() + "]"
        + " originator =[" + getOriginatorName() + "]"
        + " observed or historical =["+getObservedOrHist() +"]"+
                " reaction  =[" + getReactions() + "]"
                + " mechanism = [" + getMechanism() + "]"
                + " comments =[" + getComments() + "]";


    }

}
