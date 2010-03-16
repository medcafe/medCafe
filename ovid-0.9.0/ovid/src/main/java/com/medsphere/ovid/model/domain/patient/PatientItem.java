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

package com.medsphere.ovid.model.domain.patient;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 *
 */
@XmlSeeAlso({PatientAlert.class, PatientAllergy.class, PatientEvent.class, PatientMedication.class, PatientNursingOrder.class, PatientNursingTask.class,
             PatientOrder.class, PatientProblem.class, PatientResult.class, PatientUnsignedOrder.class, PatientUnverifiedOrder.class, PatientVitalEvent.class})
@XmlType(name = "PatientItem")
public class PatientItem implements IsAPatientItem {
    private String id;
    private String message;
    private String status;
    private Date dateTime;
    private boolean timeRangeDependent;
    private PatientItemType type;

    protected PatientItem() {}

    protected PatientItem(String id, String message, String status, Date dateTime, PatientItemType type) {
        this.id = id;
        this.message = message;
        this.status = status;
        this.dateTime = dateTime;
        this.timeRangeDependent = (dateTime != null);
        this.type = type;
    }

    @XmlElement(name="DateTime")
    public Date getDateTime() {
        return dateTime;
    }

    @XmlElement(name="DateTimeString")
    public String getDateTimeString() {

        if (getDateTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            return sdf.format(getDateTime());
        } else {
            return "";
        }
    }

    @XmlElement(name="Id")
    public String getId() {
        return id;
    }

    @XmlElement(name="Message")
    public String getMessage() {
        return message;
    }

    @XmlElement(name="Status")
    public String getStatus() {
        return status;
    }

    @XmlElement(name="IsTimeRangeDependent")
    public boolean getIsTimeRangeDependent() {
        return isTimeRangeIndependent();
    }
    public boolean isTimeRangeIndependent() {
        return timeRangeDependent;
    }

    @XmlAttribute(name="type")
    public PatientItemType getType() {
        return type;
    }

    public void setDateTime( Date d )
    {
        this.dateTime = d;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PatientItem other = (PatientItem) obj;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 71 * hash + (this.message != null ? this.message.hashCode() : 0);
        hash = 71 * hash + (this.status != null ? this.status.hashCode() : 0);
        hash = 71 * hash + (this.dateTime != null ? this.dateTime.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "type=[" + type + "] id=[" + id + "] message=[" + message + "] status=[" + status + "] date=[" + dateTime +"]";
    }


}
