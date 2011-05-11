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
 * container class of fileman CPT information
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


public class FMCPT extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMCPT.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMCPT.class);
        fileInfo = new FMFile("CPT") { //

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
    @FMAnnotateFieldInfo(name = "CPT CODE", number = ".01", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String cptCode;
    @FMAnnotateFieldInfo(name = "SHORT NAME", number = "2", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String shortName;
    @FMAnnotateFieldInfo(name = "CPT CATEGORY", number = "3", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer category;
    @FMAnnotateFieldInfo(name = "SOURCE", number = "6", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    protected String source;

    public FMCPT() {
        super(fileInfo.getFileName());
    }
    public FMCPT(FMResultSet results) {
        super(results);
    }
    public String getCptCode() {
        return cptCode;
    }
    public String getShortName() {
        return shortName;
    }

    public Integer getCategory() {
        return category;
    }

    public String getCategoryValue() {
        return getValue("3");
    }

    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return " CPT Code=[" + getCptCode() + "]"
                + " short name=[" + getShortName() + "]"
                + " category =[" + getCategoryValue() + "]"
                + " source =[" + getSource() + "]";            
    }
}
