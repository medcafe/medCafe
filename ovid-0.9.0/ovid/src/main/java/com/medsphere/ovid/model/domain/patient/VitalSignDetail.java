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
@XmlType(name="VitalSignDetail")
public class VitalSignDetail {

    private String name;
    private String value;
    private String units;
    private String metric;
    private String metricUnits;
    private String bmi;
    private String so2;
    private String indicator;
    private String qualifiers;

    private VitalSignDetail() {}
    
    public VitalSignDetail(String name, String value, String units, String metric, String metricUnits,
                           String bmi, String so2, String indicator, String qualifiers) {
        this.name = name;
        this.value = value;
        this.units = units;
        this.metric = metric;
        this.metricUnits = metricUnits;
        this.bmi = bmi;
        this.so2 = so2;
        this.indicator = indicator;
        this.qualifiers = qualifiers;
    }

    @XmlElement(name="BMI")
    public String getBmi() {
        return bmi;
    }

    @XmlElement(name="Indicator")
    public String getIndicator() {
        return indicator;
    }

    @XmlElement(name="Metric")
    public String getMetric() {
        return metric;
    }

    @XmlElement(name="MetricUnits")
    public String getMetricUnits() {
        return metricUnits;
    }

    @XmlElement(name="Name")
    public String getName() {
        return name;
    }

    @XmlElement(name="Qualifiers")
    public String getQualifiers() {
        return qualifiers;
    }

    @XmlElement(name="SO2")
    public String getSo2() {
        return so2;
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
