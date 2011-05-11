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

import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.medsphere.fileman.FMAnnotateFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author mark.taylor
 */
public class FMNonVerifiedOrder extends FMRecord {

   /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
    private static Map<String, String> domainNumbers;

    static {
        domainJavaFields = getDomainJavaFields(FMNonVerifiedOrder.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMNonVerifiedOrder.class);
        fileInfo = new FMFile("NON-VERIFIED ORDERS") {

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
    @FMAnnotateFieldInfo( name="ORDER NUMBER", number=".01", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String orderNumber;
    @FMAnnotateFieldInfo( name="PRIORITY", number=".24", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String priority;
    @FMAnnotateFieldInfo( name="PATIENT", number=".5", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer patient;
    @FMAnnotateFieldInfo( name="PROVIDER", number="1", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer provider;
    @FMAnnotateFieldInfo( name="DISPENSE DRUG", number="2", fieldType=FMField.FIELDTYPE.SUBFILE)
    protected FMNonVerifiedDispenseDrug dispenseDrug;
    @FMAnnotateFieldInfo( name="MED ROUTE", number="3", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer route;
    @FMAnnotateFieldInfo( name="ORDER TYPE", number="4", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String orderType;
    @FMAnnotateFieldInfo( name="SCHEDULE TYPE", number="7", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String scheduleType;
    @FMAnnotateFieldInfo( name="SPECIAL INSTRUCTIONS", number="8", fieldType=FMField.FIELDTYPE.WORD_PROCESSING)
    protected Collection<String> specialInstructions;
    @FMAnnotateFieldInfo( name="START DATE/TIME", number="10", fieldType=FMField.FIELDTYPE.DATE)
    protected Date startDate;
    @FMAnnotateFieldInfo( name="STOP DATE/TIME", number="25", fieldType=FMField.FIELDTYPE.DATE)
    protected Date stopDate;
    @FMAnnotateFieldInfo( name="SCHEDULE", number="26", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String schedule;
    @FMAnnotateFieldInfo( name="ORDER DATE", number="27", fieldType=FMField.FIELDTYPE.DATE)
    protected Date orderDate;
    @FMAnnotateFieldInfo( name="LOG-IN DATE", number="27.1", fieldType=FMField.FIELDTYPE.DATE)
    protected Date loginDate;
    @FMAnnotateFieldInfo( name="STATUS", number="28", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String status;
    @FMAnnotateFieldInfo( name="COMMENTS", number="40", fieldType=FMField.FIELDTYPE.WORD_PROCESSING)
    protected Collection<String> comments;
    @FMAnnotateFieldInfo( name="IV TYPE", number="53", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String IVType;
    @FMAnnotateFieldInfo( name="ORDERABLE ITEM", number="108", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer orderableItem;
    @FMAnnotateFieldInfo(name="ORDER CHECKS", number="112", fieldType=FMField.FIELDTYPE.SUBFILE)
    protected FMInpatientOrderCheck orderChecks;
    @FMAnnotateFieldInfo( name="SIG", number="47", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String sig;
    @FMAnnotateFieldInfo( name="FLAGGED", number="124", fieldType=FMField.FIELDTYPE.SET_OF_CODES)
    protected String flagged;

    protected Collection<FMNonVerifiedDispenseDrug> dispenseDrugs;

    public FMNonVerifiedOrder() {
        super("NON-VERIFIED ORDERS");
    }

    public FMNonVerifiedOrder(FMResultSet results) {
        super("NON-VERIFIED ORDERS");
        processResults(results);
    }

    public String getOrderNumber() {
        return orderNumber;
    }
    public String getPriority() {
        return priority;
    }
    public Integer getPatient() {
        return patient;
    }
    public Integer getProvider() {
        return provider;
    }
    public String getOrderType() {
        return orderType;
    }
    public String getScheduleType() {
        return scheduleType;
    }
    public Collection<String> getSpecialInstructions() {
        return specialInstructions;
    }
    public Date getStartDate() {
        return startDate;
    }
    public Date getStopDate() {
        return stopDate;
    }
    public String getSchedule() {
        return schedule;
    }
    public Date getOrderDate() {
        return orderDate;
    }
    public String getStatus() {
        return status;
    }
    public Integer getOrderableItem() {
        return orderableItem;
    }
    public Collection<String> getComments() {
        return comments;
    }
    public String getIVType() {
        return IVType;
    }

    public FMNonVerifiedDispenseDrug getDispenseDrug() {
        if (dispenseDrug==null) {
            dispenseDrug = new FMNonVerifiedDispenseDrug();
            dispenseDrug.setParent( this );
        }
        return dispenseDrug;
    }

    public void addDispenseDrug(FMNonVerifiedDispenseDrug dispenseDrug) {
        if (dispenseDrugs == null) {
            dispenseDrugs = new ArrayList<FMNonVerifiedDispenseDrug>();
        }
        dispenseDrugs.add(dispenseDrug);
    }

    public Collection<FMNonVerifiedDispenseDrug> getDispenseDrugs() {
        return dispenseDrugs;
    }

    public Date getLoginDate() {
        return loginDate;
    }
    public Integer getRoute() {
        return route;
    }
    public FMInpatientOrderCheck getOrderChecks() {
        if (orderChecks == null) {
            orderChecks = new FMInpatientOrderCheck();
            orderChecks.setParent( this );
        }
        return orderChecks;
    }
    public String getSig() {
        return sig;
    }
    public String getFlagged() {
        return flagged;
    }

    @Override
    public String toString() {
        return
        "patient IEN=["+patient+"]"
        ;
    }
}
