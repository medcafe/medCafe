// <editor-fold defaultstate="collapsed">
/*
 * Copyright (C) 2011  Medsphere Systems Corporation
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
package com.medsphere.ovid.domain.ov;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class VitalSign implements IsAVitalSign {
    private String ien;
    private String id;
    private String name;
    private String value;
    private Date entered;
    private Date taken;
    private String units;
    private String referenceRange;
    private Collection<String> qualifiers;

    VitalSign() {}

    public VitalSign(String ien, String id, String name, String value, Date entered) {
        this(ien, id, name, value, entered, entered);
    }
    public VitalSign(String ien, String id, String name, String value, Date taken, Date entered) {
        this.ien = ien;
        this.id = id;
        this.name = name;
        this.value = value;
        this.entered = entered;
        this.taken = taken;
    }

    public VitalSign(String ien, String id, String name, String value, Date entered,
                     String units, String referenceRange,
                     String[] qualifiers) {
        this(ien, id, name, value, entered);
        this.units = units;
        this.referenceRange = referenceRange;
        setQualifiers(qualifiers);
    }

    @Override
    public String getIEN() {
        return ien;
    }
    @Override
    public String getType() {
        return id;
    }
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Date getEntered() {
        return entered;
    }

    @Override
    public Date getDateTaken() {
        return taken;
    }
    @Override
    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    @Override
    public String getReferenceRange() {
        return referenceRange;
    }

    public void setReferenceRange(String referenceRange) {
        this.referenceRange = referenceRange;
    }

    @Override
    public Collection<String> getQualifiers() {
        return qualifiers;
    }

    public void setQualifiers(Collection<String> qualifiers) {
        this.qualifiers = qualifiers;
    }

    @Override
    public String getVisit() {
        return null;
    }
    public void setQualifiers(String[] qualifiers) {
        if (qualifiers != null && qualifiers.length > 0) {
            this.qualifiers = new ArrayList<String>();
            for (String q : qualifiers) {
                if (q.length() > 0) {
                    this.qualifiers.add(q);
                }
            }
        }
    }

    @Override
    public String toString() {
        return
        ((ien != null) ? "ien=["+ien+"]" : "")
        + ((id != null) ? " id=["+id+"]" : "")
        + ((name != null) ? " name=["+name+"]" : "")
        + ((value != null) ? " value=["+value+"]" : "")
        + ((entered != null) ? " entered=["+entered+"]" : "")
        + ((taken != null) ? " taken=["+taken+"]" : "")
        + ((units != null) ? " units=["+units+"]" : "")
        + ((referenceRange != null) ? " referenceRange=["+referenceRange+"]" : "")
           + ((qualifiers != null) ? " qualifiers=["+qualifiers+"]" : "")
        ;
    }


    @Override
    public Date getDateEntered() {
        return getEntered();
    }

 }
