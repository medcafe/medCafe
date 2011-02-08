package org.mitre.android.tutorial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Medication{

	public static String REPOSITORY_TYPE = "repository";
	public static String PATIENT_NAME_TYPE ="patient_id";
	public static String INSTRUCTIONS_TYPE = "patientInstructions";
	public static String DELIVERY_TYPE = "deliveryMethod";
	public static String NARRATIVE_TYPE = "narrative";
	public static String EFFECTIVE_TIME_TYPE = "effectiveTime";
	public static String MEDICATION_TYPE = "freeTextBrandName";
	public static String DOSE = "value";
	
	public static String[] MED_KEYS = new String[]{REPOSITORY_TYPE, PATIENT_NAME_TYPE,INSTRUCTIONS_TYPE,DELIVERY_TYPE,NARRATIVE_TYPE,EFFECTIVE_TIME_TYPE, MEDICATION_TYPE   };
	

	private String patientInstructions;
	private String deliveryMethod;
	private String narrative;
	private String effectiveTime;
	private String dose;
	private String medication;
	/*Class to contain medication information*/
	
	public String getDose() {
		return dose;
	}
	public void setDose(String dose) {
		this.dose = dose;
	}
	public String getMedication() {
		return medication;
	}
	public void setMedication(String medication) {
		this.medication = medication;
	}

	public String getPatientInstructions() {
		return patientInstructions;
	}
	public void setPatientInstructions(String patientInstructions) {
		this.patientInstructions = patientInstructions;
	}
	public String getDeliveryMethod() {
		return deliveryMethod;
	}
	public void setDeliveryMethod(String deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}
	public String getNarrative() {
		return narrative;
	}
	public void setNarrative(String narrative) {
		this.narrative = narrative;
	}
	public String getEffectiveTime() {
		return effectiveTime;
	}
	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	
	@Override
	public String toString()
	{
		return "Medication: " + medication + " Delivery Method : " + deliveryMethod +
				" Patient Instructions " + patientInstructions + " Effective Time " + effectiveTime;
		
	}
	
}