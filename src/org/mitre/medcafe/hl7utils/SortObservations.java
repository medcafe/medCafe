package org.mitre.medcafe.hl7utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hl7.fhir.instance.formats.JsonParser;
import org.hl7.fhir.instance.model.CodeableConcept;
import org.hl7.fhir.instance.model.Coding;
import org.hl7.fhir.instance.model.DateTime;
import org.hl7.fhir.instance.model.Observation;
import org.hl7.fhir.instance.model.Observation.ObservationComponentComponent;
import org.hl7.fhir.instance.model.Period;
import org.hl7.fhir.instance.model.Quantity;
import org.hl7.fhir.instance.model.String_;
import org.hl7.fhir.instance.model.Type;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mitre.medcafe.restlet.PatientObservationVitalsResource;

public class SortObservations {

	public final static String KEY = SortObservations.class.getName();
	public final static Logger log = Logger.getLogger( KEY );
	  
	/**
	 * @param args
	 * @throws Exception 
	 */
	public JSONObject processObservations(JSONObject observations) throws Exception {
	
		JsonParser jsonParser = new JsonParser();
		ArrayList<Observation> observationList = new ArrayList<Observation>();
		JSONArray repArray = observations.getJSONArray("repositoryList");
		JSONObject rtnJson = new JSONObject();
		ArrayList<BloodPressure> bloodPressures = new ArrayList<BloodPressure>();
		int failure =0;
		for (int k=0; k < repArray.length() -1;k++)
		{
			JSONObject repJson = repArray.getJSONObject(k);
		
			JSONArray obsArray = repJson.getJSONArray("observationsVitals");
			
			for ( int i = 0; i < obsArray.length(); i++ )
			{
				JSONObject jsonObservation = obsArray.getJSONObject(i);
				log.info("SortObservations resource: " + jsonObservation.toString());
				Observation javaObservation = null;
				try
				{
					javaObservation = (Observation) jsonParser.parse(jsonObservation);
				}
				catch(Exception e)
				{
					log.severe(e.getMessage());
					failure++;
					System.out.print("Failed to create resource " + failure);
					//Do nothing and continue
				}
				if (javaObservation != null)
					observationList.add(javaObservation);
			}
			
			System.out.println("Number of Observations " + observationList.size());
			for (Observation observation : observationList)
			{
				List<ObservationComponentComponent> components= observation.getComponent();
				Type obsDate = (Type)observation.getApplies();
				String time = "";
				if (obsDate instanceof Period)
				{
					Period period = (Period)obsDate;
					DateTime startTime = period.getStart();
					if (startTime != null)
					{
						time = startTime.getValue();
					}
					
				}
				else if (obsDate instanceof DateTime)
				{
					DateTime dateTime = (DateTime)obsDate;
					if (dateTime != null)
					{
						time = dateTime.getValue();
					}
				}
				for (ObservationComponentComponent comp : components)
				{
					List<Coding> codes = comp.getName().getCoding();
					for (Coding code : codes)
					{
						String codeName = code.getDisplaySimple();
						BloodPressure bloodPress = new BloodPressure();
						bloodPressures.add(bloodPress);
						
						if (codeName.contains(BloodPressure.SYSTOLIC_NAME))
						{
							Quantity type = (Quantity)comp.getValue();
							BigDecimal value = type.getValueSimple();
							String units = type.getUnitsSimple();
							bloodPress.setType(BloodPressure.SYSTOLIC_NAME);
							bloodPress.setValue(value + "");
							bloodPress.setUnits(units);
							bloodPress.setDate(time);
						}
					}
				}
			}	
		}
		
		rtnJson.put("BloodPressure", bloodPressures);	
		return rtnJson;
	}
	
}