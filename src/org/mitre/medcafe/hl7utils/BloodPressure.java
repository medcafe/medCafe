package org.mitre.medcafe.hl7utils;

public class BloodPressure {

	private String type = "";
	private String date = "";
	private String value = "";
	private String units = "";
	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public static String SYSTOLIC_NAME = "Systolic";
	public static String DIASTOLIC_NAME = "Diastolic";
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public BloodPressure()
	{
		
	}
	
	
}