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
 * container class of fileman NameComponents information
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

public class FMNameComponents extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    public static final double PATIENT_FILE = 2;
    public static final double USER_FILE = 200;
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMNameComponents.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMNameComponents.class);
        fileInfo = new FMFile("NAME COMPONENTS") { //

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

    @FMAnnotateFieldInfo(name = "FILE", number = ".01", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected double originatingFile;
    @FMAnnotateFieldInfo(name = "FIELD", number = ".02", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected double field;
    @FMAnnotateFieldInfo(name = "IENS", number = ".03", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String iens;
    @FMAnnotateFieldInfo(name = "FAMILY (LAST) NAME", number = "1", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String familyName;
    @FMAnnotateFieldInfo(name = "GIVEN (FIRST) NAME", number = "2", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String givenName;
    @FMAnnotateFieldInfo(name = "MIDDLE NAME", number = "3", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String middleName;
    @FMAnnotateFieldInfo(name = "PREFIX", number = "4", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String prefix;
    @FMAnnotateFieldInfo(name = "SUFFIX", number = "5", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String suffix;
    @FMAnnotateFieldInfo(name = "DEGREE", number = "6", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String degree;
    @FMAnnotateFieldInfo(name = "SOURCE NAME FORMAT FLAGS", number = "7", fieldType = FMField.FIELDTYPE.FREE_TEXT)
    protected String code;



    public FMNameComponents(double originatingFile) {
        super(fileInfo.getFileName());
        field = .01;
        this.originatingFile = originatingFile;
        iens = "";
        code = "CL30";

    }

    public FMNameComponents(FMResultSet results) {
        super(results);
    }

    public String getName() {
        String name = "";
        if (familyName != null && !familyName.equals(""))
            name = name + familyName + ", ";
        if (givenName != null && !givenName.equals(""))
            name = name + givenName + " ";
        if (middleName != null && !middleName.equals(""))
            name = name + middleName + " ";
        if (suffix != null && !suffix.equals(""))
            name = name + suffix;
        return name.trim();
    }
    public String getFamilyName()
    {
        return familyName;
    }

    public String getGiven() {
        return givenName;
    }

    public String getMiddleName() {
        return middleName;
    }
    public String getSuffix()
    {
        return suffix;
    }
    public String getDegree()
    {
        return degree;
    }
    public String getPrefix()
    {
        return prefix;
    }
    public boolean readyToWrite()
    {
        return (iens.length()>0);
    }
    public String getOriginatingFileIEN()
    {
	return iens;
    }
    public void setIENS(String originatingFileIEN)
    {
        iens = originatingFileIEN + ",";
    }
    public void setGivenName(String given) throws FieldLengthException
    {
        if (given.length() > 25)
        {
            throw new FieldLengthException("Given name must be 25 characters or less!");
        }
        givenName = given;
    }
    public void setFamilyName(String family) throws FieldLengthException
    {
        if (family.length() > 35)
        {
            throw new FieldLengthException("Family name must be 35 characters or less!");
        }
        familyName = family;
    }
    public void setMiddleName(String middle) throws FieldLengthException
    {
        if (middle.length() > 25)
        {
            throw new FieldLengthException("Middle name must be 25 characters or less!");
        }
        middleName = middle;
    }
    public void setSuffix(String suf) throws FieldLengthException
    {
            if (suf.length() > 10)
        {
            throw new FieldLengthException("Suffix must be 10 characters or less!");
        }
        suffix = suf;
    }
    public void setPrefix(String pre) throws FieldLengthException
    {
            if (pre.length() > 10)
        {
            throw new FieldLengthException("Prefix must be 10 characters or less!");
        }
        prefix = pre;
    }
    public void setDegree(String deg) throws FieldLengthException
    {
            if (deg.length() > 10)
        {
            throw new FieldLengthException("Degree must be 10 characters or less!");
        }
        degree = deg;
    }

    @Override
    public String toString() {
        return " name=["+getName()+"]"
        + " degree =["+getDegree()+"]"
        + " prefix=["+getPrefix() +"]";

    }

}
