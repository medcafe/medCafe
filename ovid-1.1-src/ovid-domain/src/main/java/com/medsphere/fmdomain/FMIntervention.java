// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2007-2010  Medsphere Systems Corporation
 * All rights reserved.
 *
 * This source code contains the intellectual property
 * of its copyright holder(s), and is made available
 * under a license. If you do not know the terms of
 * the license, please stop and do not read further.
 *
 * Please read LICENSES for detailed information about
 * the license this source code file is available under.
 * Questions should be directed to legal@medsphere.com
 *
 */
// </editor-fold>


package com.medsphere.fmdomain;

import com.medsphere.fileman.FMAnnotateFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author mark.taylor
 */
public class FMIntervention extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMIntervention.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMIntervention.class);
        fileInfo = new FMFile("APSP INTERVENTION") {

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
    public FMIntervention() {
        super("APSP INTERVENTION");
    }

    public FMIntervention(FMResultSet results) {
        super(results);
    }
    // define annotated members
    @FMAnnotateFieldInfo(name = "INTERVENTION DATE", number = ".01", fieldType = FMField.FIELDTYPE.DATE)
    private Date interventionDate;
    @FMAnnotateFieldInfo(name = "PATIENT", number = ".02", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    private String patientIEN;
    @FMAnnotateFieldInfo(name = "PROVIDER", number = ".03", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    private String provider;
    @FMAnnotateFieldInfo(name = "PHARMACIST", number = ".04", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    private String pharmacist;
    @FMAnnotateFieldInfo(name = "DRUG", number = ".05", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    private String drug;
    @FMAnnotateFieldInfo(name = "INSTITUTED BY", number = ".06", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    private String institutedBy;
    @FMAnnotateFieldInfo(name = "INTERVENTION", number = ".07", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    private String interventionType;
    @FMAnnotateFieldInfo(name = "RECOMMENDATION", number = ".08", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    private String recommendation;
    @FMAnnotateFieldInfo(name = "WAS PROVIDER CONTACTED", number = ".09", fieldType = FMField.FIELDTYPE.SET_OF_CODES)
    private String wasProviderContacted;
    @FMAnnotateFieldInfo(name = "RX #", number = ".15", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    private String rxNumber;
    @FMAnnotateFieldInfo(name = "DIVISION", number = ".16", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    private String division;


    public static FMFile getFileInfo() {
        return fileInfo;
    }
    public Date getInterventionDate() {
        return interventionDate;
    }
    public String getPatient() {
        return patientIEN;
    }
    public String getProvider() {
        return provider;
    }
    public String getPharmacist() {
        return pharmacist;
    }
    public String getDrug() {
        return drug;
    }
    public String getInstitutedBy() {
        return institutedBy;
    }
    public String getInterventionType() {
        return interventionType;
    }
    public String getRecommendation() {
        return recommendation;
    }
    public String getWasProviderContacted() {
        return wasProviderContacted;
    }
    public String getRxNumber() {
        return rxNumber;
    }
    public String getDivision() {
        return division;
    }

    @Override
    public String toString() {
        return "interventionDate=[" + interventionDate + "]  IEN=["+ getIEN() + "]";
    }

}
