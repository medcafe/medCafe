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

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 */
@XmlType(name="PatientProblem")
@XmlRootElement(name="PatientProblem")
public class PatientProblem extends PatientItem {
    private String icd;
    private Date lastModified;

    private PatientProblem() {}
    public PatientProblem(String id, String message) {
        super(id, message, null, null, PatientItemType.Problem);
    }
    public PatientProblem(String id, String message, String status, String icd, Date onset, Date lastModified) {
        super(id, message, status, onset, PatientItemType.Problem);
        this.icd = icd;
        this.lastModified = lastModified;
    }
    public String getIcd() {
        return icd;
    }
    public Date getLastModified() {
        return lastModified;
    }

    @Override
    public String toString() {
        return super.toString() +  " icd=[" + icd +"] lastModified=[" + lastModified + "]";
    }
}
