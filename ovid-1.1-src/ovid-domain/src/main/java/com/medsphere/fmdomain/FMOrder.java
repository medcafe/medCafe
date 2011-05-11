// <editor-fold defaultstate="collapsed">
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
// </editor-fold>


/*
 * FM container class of FileMan Order data
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
import java.util.ArrayList;
import java.util.TreeMap;

public class FMOrder extends FMRecord {

    /*-------------------------------------------------------------
     * begin static initialization
     *-------------------------------------------------------------*/
    private static Set<FMField> domainFields;
    private static FMFile fileInfo;
    private static Map<String, AnnotatedElement> domainJavaFields;
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

    @FMAnnotateFieldInfo(name = "ORDER #", number = ".01", fieldType = FMField.FIELDTYPE.NUMERIC)
    protected Double orderNumber;
    @FMAnnotateFieldInfo(name = "OBJECT OF ORDER", number = ".02", fieldType = FMField.FIELDTYPE.VARIABLE_POINTER)
    protected String objectOfOrder;
     @FMAnnotateFieldInfo(name = "CURRENT AGENT/PROVIDER", number = "1", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer provider;

    @FMAnnotateFieldInfo(name = "WHO ENTERED", number = "3", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer enteredBy;
 
    @FMAnnotateFieldInfo(name = "WHEN ENTERED", number = "4", fieldType = FMField.FIELDTYPE.DATE)
    protected Date whenEntered;
    
    @FMAnnotateFieldInfo(name = "DATE OF LAST ACTIVITY", number = "31", fieldType = FMField.FIELDTYPE.DATE)
    protected Date dateOfLastActivity;
    
    @FMAnnotateFieldInfo(name = "ORDERABLE ITEMS", number = ".1", fieldType = FMField.FIELDTYPE.SUBFILE)
    protected FMOrderableItem orderableItem;
    protected Map<String, FMOrderableItem> orderableItems;
    
    @FMAnnotateFieldInfo(name = "RESPONSES", number = "4.5", fieldType = FMField.FIELDTYPE.SUBFILE)
    protected FMOrderResponse response;
    protected Collection<FMOrderResponse> responses;

    @FMAnnotateFieldInfo( name="TO", number="23", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer displayGroup;
    @FMAnnotateFieldInfo( name="START DATE", number="21", fieldType=FMField.FIELDTYPE.DATE)
    protected Date startDate;
    @FMAnnotateFieldInfo( name="STOP DATE", number="22", fieldType=FMField.FIELDTYPE.DATE)
    protected Date stopDate;
    @FMAnnotateFieldInfo( name="COMPLETED", number="66", fieldType=FMField.FIELDTYPE.DATE)
    protected Date completedDate;
    @FMAnnotateFieldInfo( name="DC DATE/TIME", number="63", fieldType=FMField.FIELDTYPE.DATE)
    protected Date discontinuedDate;
    @FMAnnotateFieldInfo( name="ITEM ORDERED", number="7", fieldType=FMField.FIELDTYPE.FREE_TEXT)
    protected String itemOrdered;
    @FMAnnotateFieldInfo( name="STATUS", number="5", fieldType=FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer status;
    @FMAnnotateFieldInfo(name = "DIALOG", number = "2", fieldType = FMField.FIELDTYPE.VARIABLE_POINTER)
    protected String dialog;
    @FMAnnotateFieldInfo(name = "PARENT", number = "36", fieldType = FMField.FIELDTYPE.POINTER_TO_FILE)
    protected Integer parentOrder;

    private String patientIEN = null;

    public FMOrder() {
        super("ORDER");
    }

    public FMOrder(FMResultSet results) {
        super("ORDER");
        processResults(results);
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

    public FMOrderableItem getOrderableItemReference() {
        if (orderableItem == null) {
            orderableItem = new FMOrderableItem();
            orderableItem.setParent(this);
        }
        return orderableItem;
    }

    public void addOrderableItem(String index, FMOrderableItem item) {
        if (orderableItems == null) {
            orderableItems = new TreeMap<String, FMOrderableItem>();
        }
        orderableItems.put(index, item);
    }

    public Collection<FMOrderableItem> getOrderableItems() {
        return orderableItems.values();
    }

    public FMOrderableItem getOrderableItem(String index) {
        return orderableItems.get(index);
    }
    
    public FMOrderResponse getOrderResponseReference() {
        if (response == null) {
            response = new FMOrderResponse();
            response.setParent(this);
        }
        return response;
    }

    public void addOrderResponse(FMOrderResponse response) {
        if (responses == null) {
            responses = new ArrayList<FMOrderResponse>();
        }
        responses.add(response);
    }

    public Collection<FMOrderResponse> getOrderResponses() {
        return responses;
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

    public Date getCompletedDate() {
        return completedDate;
    }

    public Date getDiscontinuedDate() {
        return discontinuedDate;
    }

    public String getDialogPointer() {
        return dialog;
    }

    public String getParentOrder() {
        return getValue("36");
    }

    public String getProvider() {
    	return getValue("1");
    }

    public String getEnteredBy() {
    	return getValue("3");
    }
    
    public Date getWhenEntered() {
    	return whenEntered;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FMOrder other = (FMOrder) obj;
        if (this.orderNumber != other.orderNumber && (this.orderNumber == null || !this.orderNumber.equals(other.orderNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }
    
}
