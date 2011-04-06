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

public class FMDrugIngredients extends FMRecord { // extends FMDomainObject {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMDrugIngredients.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMDrugIngredients.class);
        fileInfo = new FMFile("DRUG INGREDIENTS") {

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
    @FMAnnotateFieldInfo(name="DRUG IDENTIFIER", number="1", fieldType=FMField.FIELDTYPE.SUBFILE)
    protected FMIngredientsDrugIdentifier drugIdentifier;
    protected Collection<FMIngredientsDrugIdentifier> drugIdentifiers = null;
    @FMAnnotateFieldInfo( name="PRIMARY INGREDIENT", number="2", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer primaryIngredientIEN;

    public FMDrugIngredients() {
        super("DRUG INGREDIENTS");
    }

    public FMDrugIngredients(FMResultSet results) {
        super("DRUG INGREDIENTS");
        processResults(results);
    }
    public String getName()
    {
        return name;
    }
       public FMIngredientsDrugIdentifier getDrugIdentifier() {
        if (drugIdentifier==null) {
            drugIdentifier = new FMIngredientsDrugIdentifier();
            drugIdentifier.setParent( this );

        }
        return drugIdentifier;
    }

    public void addIdentifier(FMIngredientsDrugIdentifier ident) {
        if (drugIdentifiers == null) {
            drugIdentifiers = new ArrayList<FMIngredientsDrugIdentifier>();
        }
        drugIdentifiers.add(ident);
    }

    public Collection<FMIngredientsDrugIdentifier> getDrugIdentifiers() {
        return drugIdentifiers;
    }
    public Integer getPrimaryIngredientIEN()
    {
        return primaryIngredientIEN;
    }
    public String getPrimaryIngredientName()
    {
        return getValue("2");
    }


    @Override
    public String toString() {
        return getIEN()
        + ((name != null) ? " name=["+ getName() +"]" : "")





        + (( drugIdentifiers != null) ? " identifier subfile=["+ getDrugIdentifiers() +"]" : "")
        + ((getPrimaryIngredientIEN() != null)? " primary ingredient = [" + getPrimaryIngredientName() + "]" : "")
        ;
    }
}
