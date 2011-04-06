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

public class FMVADrugClass extends FMRecord { // extends FMDomainObject {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMVADrugClass.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMVADrugClass.class);
        fileInfo = new FMFile("VA DRUG CLASS") {

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

    @FMAnnotateFieldInfo( name="CODE", number=".01", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String code;
    @FMAnnotateFieldInfo( name="CLASSIFICATION", number="1", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String classification;
    @FMAnnotateFieldInfo( name="PARENT CLASS", number="2", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer parentClassIEN;

    @FMAnnotateFieldInfo( name="TYPE", number="3", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String type;
    @FMAnnotateFieldInfo (name="DESCRIPTION", number="4", fieldType=FMField.FIELDTYPE.WORD_PROCESSING)
    String description;

    public FMVADrugClass() {
        super("VA DRUG CLASS");
    }

    public FMVADrugClass(FMResultSet results) {
        super("VA DRUG CLASS");
        processResults(results);
    }
    public String getCode()
    {
        return code;
    }
    public String getClassification() {
        return classification;
    }

    public Integer getParentClassIEN(){
        return parentClassIEN;
    }
    public String getParentClassCode(){
        return getValue("2");
    }
    public String getType()
    {
        return type;
    }
    public String getDescription(){
        return description;
    }


    @Override
    public String toString() {
        return getIEN()
        + ((getCode() != null) ? " code=["+ getCode() +"]" : "")

        + ((getClassification() != null) ? " classification=["+ getClassification() +"]" : "")
        + ((getParentClassIEN() != null) ? " parent class code=[" + getParentClassCode() + "]" : "")
        + ((getType() != null) ? " type=[" + getType() + "]" : "")
        + ((getDescription() != null) ? " description=[" + getDescription() + "]": "")
        ;
    }
}

