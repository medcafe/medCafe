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

public class FMSignSymptom extends FMRecord implements Comparable<FMSignSymptom>{ // extends FMDomainObject {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMSignSymptom.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMSignSymptom.class);
        fileInfo = new FMFile("SIGN/SYMPTOMS") {

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
    @FMAnnotateFieldInfo(name="NATIONAL SIGN/SYMPTOM", number="1", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String nationalSignSymptom;
    @FMAnnotateFieldInfo(name="SYNONYM", number="2", fieldType=FMField.FIELDTYPE.SUBFILE)
    protected FMRecord synonym;
    protected Collection<String> synonyms = null;


    public FMSignSymptom() {
        super("SIGN/SYMPTOMS");
    }

    public FMSignSymptom(FMResultSet results) {
        super("SIGN/SYMPTOMS");
        processResults(results);
    }
    public FMSignSymptom(String name)
    {
        super("SIGN/SYMPTOMS");
        this.name = name;
    }
    public String getName()
    {
        return name;
    }
    public boolean isNationalSignSymptom()
    {
        return (nationalSignSymptom.equals("1")
                ||nationalSignSymptom.toLowerCase().startsWith("national"));
    }
       public FMRecord getSynonymReference() {
        if (synonym==null) {
            synonym = new FMRecord("SYNONYM");
            synonym.setParent( this );

        }
        return synonym;
    }

    public void addSynonym(String syn) {
        if (synonyms == null) {
            synonyms = new ArrayList<String>();
        }
        synonyms.add(syn);
    }

    public Collection<String> getSynonyms() {
        return synonyms;
    }
    public int compareTo(FMSignSymptom b)
    {
        return name.compareToIgnoreCase(b.getName());
    }


    @Override
    public String toString() {
        return getIEN()
        + ((name != null) ? " name=["+ getName() +"]" : "")
        + (( synonyms != null) ? " synonym subfile=["+ getSynonyms() +"]" : "")
        + ( " is National Sign/symptom = [" + isNationalSignSymptom() + "]")
                 ;
    }
}
