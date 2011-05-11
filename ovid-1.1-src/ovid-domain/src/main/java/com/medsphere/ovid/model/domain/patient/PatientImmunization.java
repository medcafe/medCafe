/*
 *  Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.medsphere.ovid.model.domain.patient;

import java.util.Date;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 */
@XmlType(name="PatientImmunization")
@XmlRootElement(name="PatientImmunization")
public class PatientImmunization extends PatientItem {

	
    private ArrayList<String> diagnoses;
    private String series, reaction, contraindicated, remarks, orderingProvider, encounterProvider;

    private PatientImmunization() {}
    public PatientImmunization(String id, String message) {
        super(id, message, null, null, PatientItemType.Immunization);
        diagnoses = new ArrayList<String>();
    }
    public PatientImmunization(String id, String message,  Date eventDate) {
        super(id, message, null, eventDate, PatientItemType.Immunization);
        diagnoses = new ArrayList<String>();
    }
    public PatientImmunization(String id, String message, Date eventDate, String diag1, String diag2, String diag3, 
    String diag4, String diag5, String diag6, String diag7, String diag8, String series, String react,
    String contraind, String rem, String orderProv, String encountProv)
    {
    	super(id, message, null, eventDate, PatientItemType.Immunization);
 
    	this.series = series;
    	reaction = react;
    	contraindicated = contraind;
    	remarks = rem;
    	orderingProvider = orderProv;
    	encounterProvider = encountProv;
    	diagnoses = new ArrayList<String>();
    	if (diag1 != null)
    	{
    		diagnoses.add(diag1);
    	}
    	if (diag2 != null)
    	{
    		diagnoses.add(diag2);
    	}
    	if (diag3 != null)
    	{
    		diagnoses.add(diag3);
    	}
    	if (diag4 != null)
    	{
    		diagnoses.add(diag4);
    	}
    	if (diag5 != null)
    	{
    		diagnoses.add(diag5);
    	}
    	if (diag6 != null)
    	{
    		diagnoses.add(diag6);
    	}
    	if (diag7 != null)
    	{
    		diagnoses.add(diag7);
    	}
    	if (diag8 != null)
    	{
    		diagnoses.add(diag8);
    	}
    }
    public ArrayList<String> getDiagnoses()
    {
    	return diagnoses;
    }
    public String getImmunizationName()
    {
    	return getMessage();
    }
    public String getSeries()
    {
    	return series;
    }
    public String getReaction()
    {
    	return reaction;
    }
    public String getContraindicated()
    {
    	return contraindicated;
    }
    public String getRemarks()
    {
    	return remarks;
    }
    public String getOrderingProvider()
    {
    	return orderingProvider;
    }
    public String getEncounterProvider()
    {
    	return encounterProvider;
    }
    public Date getImmunizationDate()
    {
    	return getDateTime();
    }
   

    @Override
    public String toString() {
        String str = super.toString() + " Series: [" + series + 
        "] Reaction: [" + reaction +
        "] Contraindicated?: [" + contraindicated +
        "] Remarks: [" + remarks +
        "] Ordering Provider: [" + orderingProvider +
        "] Encounter Provider: [" + encounterProvider +
        "]";
        for (String diag: diagnoses)
        {
        	   str = str + " Diagnosis: [" +  diag + "]";
        }
        return str;
    }
}
