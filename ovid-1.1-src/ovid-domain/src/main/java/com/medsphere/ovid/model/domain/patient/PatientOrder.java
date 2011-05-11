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

package com.medsphere.ovid.model.domain.patient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 */
@XmlType(name="PatientOrder")
@XmlRootElement(name="PatientOrder")
@XmlSeeAlso({PatientNursingOrder.class, PatientUnsignedOrder.class, PatientUnverifiedOrder.class})
public class PatientOrder extends PatientItem {
    private Collection<String> details;

    protected PatientOrder() {}

    public PatientOrder(String id, String message, String status, Date dateTime, PatientItemType orderType) {
        super(id, message, status, dateTime, orderType);
        details = new ArrayList<String>();
    }
    public void addDetail(String detail) {
        details.add(detail);
    }

    @XmlElement(name="Details")
    public Collection<String> getDetails() {
        return details;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer(super.toString());
        for (String detail : details) {
            buf.append("\n\t" + detail);
        }
        return buf.toString();
    }
}
