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

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.medsphere.fileman.FMAnnotateFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import java.text.DecimalFormat;

public class FMGMRAllergies extends FMRecord { // extends FMDomainObject {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMGMRAllergies.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMGMRAllergies.class);
        fileInfo = new FMFile("GMR ALLERGIES") {

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
    @FMAnnotateFieldInfo( name="ALLERGY TYPE", number="1", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String allergyType;
    @FMAnnotateFieldInfo( name="NATIONAL ALLERGY", number="2", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String nationalAllergy;

    @FMAnnotateFieldInfo( name="SYNONYM", number="3", fieldType=FMField.FIELDTYPE.SUBFILE)
    protected FMAllergySynonym synonym;

    protected Collection<FMAllergySynonym> synonyms = null;
    @FMAnnotateFieldInfo(name="DRUG INGREDIENTS", number="4", fieldType=FMField.FIELDTYPE.SUBFILE)
    protected FMAllergyDrugIngredient drugIngredient;
    protected Collection<FMAllergyDrugIngredient> drugIngredients = null;
    @FMAnnotateFieldInfo(name="VA DRUG CLASSES", number="5", fieldType=FMField.FIELDTYPE.SUBFILE)
    protected FMAllergyDrugClass drugClass;
    protected Collection<FMAllergyDrugClass> drugClasses = null;

    public FMGMRAllergies() {
        super("GMR ALLERGIES");
    }

    public FMGMRAllergies(FMResultSet results) {
        super("GMR ALLERGIES");
        processResults(results);
    }
    public String getName()
    {
        return name;
    }
    public String getAllergyType() {
        return allergyType;
    }

    public String getNationalAllergy()
    {
        return nationalAllergy;
    }
    public boolean isNationalAllergy()
    {
        return (nationalAllergy.equals("1")||nationalAllergy.toLowerCase().startsWith("national"));
    }

    public FMAllergySynonym getSynonym() {
        if (synonym==null) {
            synonym = new FMAllergySynonym();
            synonym.setParent( this );
        }
        return synonym;
    }

    public void addSynonym(FMAllergySynonym syn) {
        if (synonyms == null) {
            synonyms = new ArrayList<FMAllergySynonym>();
        }
        synonyms.add(syn);
    }

    public Collection<FMAllergySynonym> getSynonyms() {
        return synonyms;
    }
    public FMAllergyDrugIngredient getDrugIngredient() {
        if (drugIngredient==null) {
            drugIngredient = new FMAllergyDrugIngredient();
            drugIngredient.setParent(this);
        }
        return drugIngredient;
    }
      public void addDrugIngredient(FMAllergyDrugIngredient ingred) {
        if (drugIngredients == null) {
            drugIngredients = new ArrayList<FMAllergyDrugIngredient>();
        }
        drugIngredients.add(ingred);
    }

    public Collection<FMAllergyDrugIngredient> getDrugIngredients() {
        return drugIngredients;
    }
      public FMAllergyDrugClass getDrugClass() {
        if (drugClass==null) {
            drugClass = new FMAllergyDrugClass();
            drugClass.setParent( this );
        }
        return drugClass;
    }

    public void addDrugClass(FMAllergyDrugClass drugC) {
        if (drugClasses == null) {
            drugClasses = new ArrayList<FMAllergyDrugClass>();
        }
        drugClasses.add(drugC);
    }

    public Collection<FMAllergyDrugClass> getDrugClasses() {
        return drugClasses;
    }

    @Override
    public String toString() {
        return getIEN()
        + ((name != null) ? " name=["+ getName() +"]" : "")

        + ((allergyType != null) ? " allergyType=["+ getAllergyType() +"]" : "")



        + (( synonyms != null) ? " synonym subfile=["+ getSynonyms() +"]" : "")
        + ((drugIngredients != null) ? " ingredient subfile=[" + getDrugIngredients() +"]" : "")
         +((drugClasses != null) ? " drug classes subfile=[" + getDrugClasses() + "]" : "")
        ;
    }
}
