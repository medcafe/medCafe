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
import javax.xml.bind.annotation.XmlType;

/**
 * 
 */
@XmlType(name="PatientResult")
@XmlRootElement(name="PatientResult")
public class PatientResult extends PatientItem {
    private Collection<String> text;
    private Collection<ResultDetail> details;
    private Collection<String> comments;   

    protected PatientResult() {}
    public PatientResult(String id, String message, String status, Date dateTime) {
        super(id, message, status, dateTime, PatientItemType.Result);
        text = new ArrayList<String>();
        details = new ArrayList<ResultDetail>();
        comments = new ArrayList<String>();
    }

    @XmlElement(name="Details")
    public Collection<ResultDetail> getDetails() {
        return details;
    }

    public void addDetail(ResultDetail detail) {
        this.details.add(detail);
    }

    @XmlElement(name="Text")
    public Collection<String> getText() {
        return text;
    }

    public void addText(String text) {
        this.text.add(text);
    }

    @XmlElement(name="Comments")
    public Collection<String> getComments() {
        return comments;
    }

    public void addComment(String comment) {
        this.comments.add(comment);
    }

	@Override
    public String toString() {
        StringBuffer buf = new StringBuffer(super.toString());
        for (ResultDetail detail : details) {
            buf.append("\n\t test=["+detail.getTestName()+"] value=["+detail.getValue()+"] units=["+detail.getUnits()
                       +"] range=["+detail.getReferenceRange()+"] indicator=["+detail.getIndicator()
                       +"] labTestIEN=["+detail.getLabTestIEN()+"] loincCode=["+detail.getLoincCode()+"]");
        }
        for (String textLine : this.text) {
            buf.append("\n\t" + textLine);
        }
        for (String comment : this.comments) {
            buf.append("\n\tComment: " + comment);
        }
        return buf.toString();
    }

}