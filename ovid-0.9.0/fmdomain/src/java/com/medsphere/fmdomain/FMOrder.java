/*
 * Copyright (C) 2007-2009  Medsphere Systems Corporation
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

/*
 * FM container class of FileMan Order data
 */
package com.medsphere.fmdomain;

import com.medsphere.fileman.FMAnnoteFieldInfo;
import com.medsphere.fileman.FMField;
import com.medsphere.fileman.FMFile;
import com.medsphere.fileman.FMRecord;
import com.medsphere.fileman.FMResultSet;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class FMOrder extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization 
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, Field> domainJavaFields;
    private static Map<String, String> domainNumbers;
    
    static {
        domainJavaFields = getDomainJavaFields(FMOrder.class);
        domainFields = getFieldsInDomain(domainJavaFields);
        domainNumbers = getNumericMapping(FMOrder.class);
        fileInfo = new FMFile("100") {

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
    protected Map<String, Field> getDomainJavaFields() {
        return domainJavaFields;
    }

    @Override
    protected Map<String, String> getNumericMapping() {
        return domainNumbers;
    }
    
    /*-------------------------------------------------------------
     * end static initialization 
     *-------------------------------------------------------------*/
    
    @FMAnnoteFieldInfo(name = "ORDER #", number = ".01", fieldType = FMField.FIELDTYPE.NUMERIC)
    private Double orderNumber;
    @FMAnnoteFieldInfo(name = "OBJECT OF ORDER", number = ".02", fieldType = FMField.FIELDTYPE.VARIABLE_POINTER)
    private String objectOfOrder;
    @FMAnnoteFieldInfo(name = "DATE OF LAST ACTIVITY", number = "31", fieldType = FMField.FIELDTYPE.DATE)
    private Date dateOfLastActivity;
    @FMAnnoteFieldInfo(name = "ORDERABLE ITEMS", number = ".1", fieldType = FMField.FIELDTYPE.SUBFILE)
    private FMOrderableItem orderableItem;
    @FMAnnoteFieldInfo( name="TO", number="23", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    private Integer displayGroup;
    @FMAnnoteFieldInfo( name="START DATE", number="21", fieldType=FMField.FIELDTYPE.DATE)
    private Date startDate;
    @FMAnnoteFieldInfo( name="STOP DATE", number="22", fieldType=FMField.FIELDTYPE.DATE)
    private Date stopDate;
    @FMAnnoteFieldInfo( name="ITEM ORDERED", number="7", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    private String itemOrdered;   
    @FMAnnoteFieldInfo( name="STATUS", number="5", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    private Integer status;
    @FMAnnoteFieldInfo(name = "DIALOG", number = "2", fieldType = FMField.FIELDTYPE.VARIABLE_POINTER)
    private String dialog;
    
    private String patientIEN = null;
    
    public FMOrder() {
        super(fileInfo.getFileName());
    }

    public FMOrder(FMResultSet results) {
        super(results);
    }

    public String getObjectOfOrder() {
        return objectOfOrder;
    }

    public String getObjectOfOrderString() {
        return getValue(".02");
    }
    public Double getOrderNumber() {
        return orderNumber;
    }

    public Date getDateOfLastActivity() {
        return dateOfLastActivity;
    }
    
    public FMOrderableItem getOrderableItem() {
        if (orderableItem == null) {
            orderableItem = new FMOrderableItem();
            orderableItem.setParent(this);
        }
        return orderableItem;
    }

    public String getPatientIEN() {
        if (patientIEN == null) {
            if (objectOfOrder != null && objectOfOrder.length() > 0) {
                patientIEN = objectOfOrder.split(";")[0];
            }
        }
        return patientIEN;
    }
    
    public Integer getDisplayGroup() {
        return displayGroup;
    }
    public String getDisplayGroupString() {
        return getValue("23");
    }

    public String getItemOrdered() {
        return itemOrdered;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Integer getStatus() {
        return status;
    }
    
    public String getStatusString() {
        return getValue("5");
    }

    public Date getStopDate() {
        return stopDate;
    }

    public String getDialogPointer() {
        return dialog;
    }
    
}
