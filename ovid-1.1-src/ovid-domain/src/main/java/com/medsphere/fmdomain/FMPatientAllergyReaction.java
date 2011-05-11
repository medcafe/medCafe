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
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.medsphere.fileman.FMAnnotateFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import com.medsphere.fmdomain.FMNewPerson;

public class FMPatientAllergyReaction extends FMRecord  {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMPatientAllergyReaction.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMPatientAllergyReaction.class);
        fileInfo = new FMFile("REACTIONS") {

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

    @FMAnnotateFieldInfo( name="REACTION", number=".01", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer reactionIEN;
    @FMAnnotateFieldInfo( name="OTHER REACTION", number="1", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String otherReaction;
    @FMAnnotateFieldInfo( name="ENTERED BY", number="2", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer enteredBy;

    @FMAnnotateFieldInfo( name="DATE ENTERED", number="3", fieldType=FMField.FIELDTYPE.DATE)
    protected Date dateEntered;


    public FMPatientAllergyReaction() {
        super("REACTIONS");
    }

    public FMPatientAllergyReaction(FMResultSet results) {
        super("REACTIONS");
        processResults(results);
    }

    public Integer getReactionIEN() {
        return reactionIEN;
    }
    public void setReactionIEN(Integer reactionIen)
    {
        setDomainValue("reactionIEN", reactionIen);
    }
    public void setReactionIEN(FMSignSymptom sign)
    {
        setDomainValue("reactionIEN", Integer.parseInt(sign.getIEN()));
    }

    public String getReactionValue() {
        return getValue(".01");
    }
    public String getOtherReaction() {
        return otherReaction;
    }
    public void setOtherReaction(String otherReact)
    {
        setDomainValue("otherReaction", otherReact);
    }
    public Integer getEnteredByIEN() {
        return enteredBy;
    }
    public void setEnteredByIEN(Integer enterIEN)
    {
        setDomainValue("enteredBy", enterIEN);
    }
    public void setEnteredByIEN(FMNewPerson entryPerson)
    {
        setDomainValue("enteredBy", Integer.parseInt(entryPerson.getIEN()));
    }
    public String getEnteredByName(){
        return getValue("2");
    }
    public Date getDateEntered() {
        return dateEntered;
    }
    public void setDateEntered(Date enterDate)
    {
        setDomainValue("dateEntered", enterDate);
    }


    @Override
    public String toString() {
        return ""
            + ((getReactionValue() != null) ? "Reaction=[" + getReactionValue() + "]" : "")
            + ((getOtherReaction() != null) ? " Other Reaction=[" + getOtherReaction()  + "]" : "")
            + ((getEnteredByName() != null) ? " Entered By=[" + getEnteredByName()  + "]" : "")
            + ((getDateEntered() != null) ? " Date Entered=[" + getDateEntered()  + "]" : "")

        ;

    }
}
