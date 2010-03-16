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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 */
@XmlType(name="ResultDetail")
public class ResultDetail {
    private String testName;
    private String value;
    private String units;
    private String referenceRange;
    private String indicator;

    private ResultDetail() {}

    public ResultDetail(String testName, String value, String units, String referenceRange, String indicator) {
        this.testName = testName;
        this.value = value;
        this.units = units;
        this.referenceRange = referenceRange;
        this.indicator = indicator;
    }

    @XmlElement(name="Indicator")
    public String getIndicator() {
        return indicator;
    }

    @XmlElement(name="ReferenceRange")
    public String getReferenceRange() {
        return referenceRange;
    }

    @XmlElement(name="TestName")
    public String getTestName() {
        return testName;
    }

    @XmlElement(name="Units")
    public String getUnits() {
        return units;
    }

    @XmlElement(name="Value")
    public String getValue() {
        return value;
    }



}
