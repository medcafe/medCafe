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

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 */
@XmlType(name="PatientAllergy")
@XmlRootElement(name="PatientAllergy")
public class PatientAllergy extends PatientItem {
    private PatientAllergy() {}
    public PatientAllergy(String id, String message) {
        super(id, message, null, null, PatientItemType.Allergy);
    }

  /**
   * Reaction property.
   */
  protected String reaction = null;

  /**
   * Get reaction property.
   *
   *@return Reaction property.
   */
  public String getReaction() {
  	return this.reaction;
  }

  /**
   * Set reaction property.
   *
   *@param reaction New reaction property.
   */
  public void setReaction(String reaction) {
  	this.reaction = reaction;
  }


}
