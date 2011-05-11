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

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 */
@XmlType(name="PatientMedication")
@XmlRootElement(name="PatientMedication")
public class PatientMedication extends PatientItem {
    private PatientMedication() {}

    public PatientMedication(String id, String message, String status, Date dateTime) {
        super(id, message, status, dateTime, PatientItemType.Medication);
    }

  /**
   * MedName property.
   */
  protected String medName = null;

  /**
   * Get medName property.
   *
   *@return MedName property.
   */
  public String getMedName() {
  	return this.medName;
  }

  /**
   * Set medName property.
   *
   *@param medName New medName property.
   */
  public void setMedName(String medName) {
  	this.medName = medName;
  }


  /**
   * Dose property.
   */
  protected String dose = null;

  /**
   * Get dose property.
   *
   *@return Dose property.
   */
  public String getDose() {
  	return this.dose;
  }

  /**
   * Set dose property.
   *
   *@param dose New dose property.
   */
  public void setDose(String dose) {
  	this.dose = dose;
  }


  /**
   * Delivery property.
   */
  protected String delivery = null;

  /**
   * Get delivery property.
   *
   *@return Delivery property.
   */
  public String getDelivery() {
  	return this.delivery;
  }

  /**
   * Set delivery property.
   *
   *@param delivery New delivery property.
   */
  public void setDelivery(String delivery) {
  	this.delivery = delivery;
  }


  /**
   * Frequency property.
   */
  protected String frequency = null;

  /**
   * Get frequency property.
   *
   *@return Frequency property.
   */
  public String getFrequency() {
  	return this.frequency;
  }

  /**
   * Set frequency property.
   *
   *@param frequency New frequency property.
   */
  public void setFrequency(String frequency) {
  	this.frequency = frequency;
  }


}
