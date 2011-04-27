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
 * container class of fileman Religion information
 */


import java.lang.reflect.AnnotatedElement;
import java.util.Collection;

import java.util.Map;
import java.util.Set;

import com.medsphere.fileman.FMAnnotateFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;

public class FMReligion extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    public static final int CATHOLIC = 1, PROTESTANT = 2, JEWISH = 3, ORTHODOX = 4;
    public static final int OTHER = 5;


    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMState.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMState.class);
        fileInfo = new FMFile("RELIGION") { //

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
    @FMAnnotateFieldInfo(name = "ABBREVIATION", number = "1", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String abbrev;
    @FMAnnotateFieldInfo(name = "CLASSIFICATION", number = "2", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected int classification;
    @FMAnnotateFieldInfo(name = "CODE", number = "3", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected int code;

    public FMReligion() {
        super(fileInfo.getFileName());
    }

    public FMReligion(FMResultSet results) {
        super(results);
    }

    public String getName() {
        return name;
    }
    public int getClassification()
    {
        return classification;
    }

    public int getCode() {
        return code;
    }

    public String getAbbrev() {
        return abbrev;
    }


    @Override
    public String toString() {
        return "IEN=["+getIEN()+"]"
        + " name=["+getName()+"]"
        + " classification =["+getClassification()+"]"
        + " abbreviation=["+getAbbrev() +"]"
        + " code =["+getCode()+"]";

    }
}