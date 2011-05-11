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

public class FMPatientAllergyComment extends FMRecord  {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMPatientAllergyComment.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMPatientAllergyComment.class);
        fileInfo = new FMFile("COMMENTS") {

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

    @FMAnnotateFieldInfo( name="DATE/TIME COMMENT ENTERED", number=".01", fieldType=FMField.FIELDTYPE.DATE)
    protected Date commentDate;
    @FMAnnotateFieldInfo( name="USER ENTERING", number="1", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer userEnteringIEN;
    @FMAnnotateFieldInfo( name="COMMENT TYPE", number="1.5", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String commentType;

   @FMAnnotateFieldInfo( name="COMMENTS", number="2", fieldType=FMField.FIELDTYPE.WORD_PROCESSING)
    protected String comments;


    public FMPatientAllergyComment() {
        super("COMMENTS");
    }

    public FMPatientAllergyComment(FMResultSet results) {
        super("COMMENTS");
        processResults(results);
      
    }

    public Date getCommentDate() {
        return commentDate;
    }
    public void setCommentDate(Date commDate)
    {
        setDomainValue("commentDate", commDate);
    }

    public Integer getUserEnteringIEN() {
        return userEnteringIEN;
    }
    public void setUserEnteringIEN(Integer ien)
    {
        setDomainValue("userEnteringIEN", ien);
    }
    public String getUserEntering() {
        return getValue("1");
    }
    public String getCommentType() {
        return commentType;
    }
    public void setCommentType(String commType) throws InvalidFileManFieldValueException
    {
        if (commType.equals("V")||commType.equals("O")||commType.equals("E"))
        {
            setDomainValue("commentType", commType);
        }
        else
        {
            throw new InvalidFileManFieldValueException("Invalid FileMan Field Value for Comment Type:  Enter 'V' for verified;"+
                    " 'O' for observed; or 'E' for errored");
        }
    }

    public String getComments() {
      return comments;
    }
    public void setComments(String comment)
    {
        setDomainValue("comments", comment);
    }



    @Override
    public String toString() {
        return ""
            + ((getCommentDate() != null) ? "date=[" + getCommentDate() + "]" : "")
            + ((getUserEntering() != null) ? " user Entering=[" + getUserEntering()  + "]" : "")
            + ((getCommentType() != null) ? " comment type=[" + getCommentType()  + "]" : "")
            + ((getComments() != null)? " comments = [" + getComments() + "]":"")
           ;

        

    }
}
