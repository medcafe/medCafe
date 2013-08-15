package test.org.mitre.medcafe.fhir.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import org.hl7.fhir.instance.formats.JsonParser;
import org.hl7.fhir.instance.formats.ParserBase.ResourceOrFeed;
import org.hl7.fhir.instance.model.AtomEntry;
import org.hl7.fhir.instance.model.Coding;
import org.hl7.fhir.instance.model.Observation;
import org.hl7.fhir.instance.model.Observation.ObservationComponentComponent;
import org.hl7.fhir.instance.model.Observation.ObservationReferenceRangeComponent;
import org.hl7.fhir.instance.model.Period;
import org.hl7.fhir.instance.model.Quantity;
import org.hl7.fhir.instance.model.Range;
import org.hl7.fhir.instance.model.Resource;

public class ParseBP {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
	
		ParseBP parseBP = new ParseBP();
		parseBP.getObservations();
	}
	
	public List<Observation> getObservations() throws FileNotFoundException, Exception
	{
		// TODO Auto-generated method stub
		JsonParser jsonParser = new JsonParser();
		File file = new File("/home/dev/mmrc/data/bp.json");

		List<Observation> obsList = new ArrayList<Observation>();
		
	    ResourceOrFeed rf =jsonParser.parseGeneral(new FileInputStream(file));
	    if (rf.getResource() != null)
	    {
	    	System.out.println(rf.getResource().getClass().toString());
	    }
	    else
	    {
	    	System.out.println(rf.getFeed().getEntryList());
	    	for (AtomEntry entry : rf.getFeed().getEntryList())
	    	{
	    		Resource resource = entry.getResource();
	    		System.out.println(resource.getClass().toString());
	    		if (resource instanceof Observation)
	    		{
	    			Observation obs = (Observation) resource;
	    			obsList.add(obs);
	    			
	    			for (Coding code : obs.getName().getCoding())
	    			{
	    				if (code.getSystem()!= null)
	    				{
	    					System.out.println("Code system uri: " + code.getSystem().getValue());
	    					System.out.println("Code system simple: " + code.getSystemSimple());
	    				}
	    				if (code.getCode()!= null)
	    				{
	    					System.out.println("Code: " + code.getCode().getValue());
	    					System.out.println("Code simple: " + code.getCodeSimple());
	    				}
	    				if (code.getDisplay() != null)
	    				{
	    					System.out.println("Display name: " + code.getDisplay().getValue());
	    					System.out.println("Display simple: " + code.getDisplaySimple());
	    				}
	    			}
	    	
	    			if (obs.getPerformer() != null)
	    			{
	    			System.out.println("Performer: " +obs.getPerformer().getDisplaySimple());
	    			}
	    			if (obs.getSubject() != null)
	    			{
	    				System.out.println("Subject: " + obs.getSubject().getDisplaySimple());
	    			}
	    			if (obs.getInterpretation()!= null)
	    			{
	    				System.out.println("INTERPRETATION: ");
	    				for (Coding code : obs.getInterpretation().getCoding())
	    				{
		    				if (code.getSystem()!= null)
		    				{
		    					
		    					System.out.println("Code system: " + code.getSystemSimple());
		    				}
		    				if (code.getCode()!= null)
		    				{
		    				
		    					System.out.println("Code: " + code.getCodeSimple());
		    				}
		    				if (code.getDisplay() != null)
		    				{
		    					
		    					System.out.println("Display: " + code.getDisplaySimple());
		    				}
	    				}
	    				if (obs.getValue()!= null && obs.getValue() instanceof Quantity)
	    				{
	    					System.out.println("VALUE");
	    					Quantity quantity = (Quantity) obs.getValue();
	    					System.out.println("Value: " + quantity.getValueSimple() + " " + quantity.getUnitsSimple());
	    				}
	    				for (ObservationComponentComponent comp : obs.getComponent())
	    				{
	    					System.out.println(comp.getName() + ": " + ((Quantity)comp.getValue()).getValueSimple() + " " + ((Quantity)comp.getValue()).getUnitsSimple());  
	    				}
	    				if (obs.getReferenceRange()!= null)
	    				{
	    					System.out.println("Range:");
	    					for (ObservationReferenceRangeComponent range : obs.getReferenceRange())
	    					{
	    						if (range.getRange() instanceof Range)
	    						{
	    							Range r = (Range) range.getRange();
	    							System.out.println(r.getLow().getValueSimple() +" " + r.getLow().getUnitsSimple() + " - " +
	    									r.getHigh().getValueSimple() +" " + r.getHigh().getUnitsSimple() );
	    						}
	    					}
	    				}
	    				if (obs.getReliability() != null)
	    				{
	    					System.out.println("Reliability: " +obs.getReliabilitySimple());
	    				}
	    				if (obs.getApplies()!= null)
	    				{
	    					if (obs.getApplies() instanceof Period)
	    					{
	    						System.out.print("Date: ");
	    						Period period = (Period) obs.getApplies();
	    						System.out.print(period.getStartSimple());
	    						if (!period.getEndSimple().equals(period.getStartSimple()))
	    						{
	    							System.out.print(" - " + period.getEndSimple());
	    						}
	    						System.out.println();
	    						
	    					}
	    				}
	    				if (obs.getBodySite()!= null)
	    				{
	    					System.out.print("Body Site:");
	    					for (Coding code : obs.getBodySite().getCoding())
	    	    			{
	    	    				if (code.getSystem()!= null)
	    	    				{
	    	    					
	    	    					System.out.println("Code system: " + code.getSystemSimple());
	    	    				}
	    	    				if (code.getCode()!= null)
	    	    				{
	    	    				
	    	    					System.out.println("Code: " + code.getCodeSimple());
	    	    				}
	    	    				if (code.getDisplay() != null)
	    	    				{
	    	    					
	    	    					System.out.println("Display: " + code.getDisplaySimple());
	    	    				}
	    	    			}
	    					
	    				}
	    				System.out.println("\n\n\n******\n\n");
	    			}
	    		}
	    	}
	    }
	    	

	    URL data_server = new URL("http://hl7connect.healthintersections.com.au/svc/fhir/observation/search?_format=json&subject=example");
        
        HttpURLConnection connection = (HttpURLConnection)data_server.openConnection();
        connection.setRequestProperty("Accept","*/*");
        connection.setRequestMethod("GET");

        connection.connect();
       
        System.out.println("*****Querying the web");
	  rf =jsonParser.parseGeneral(connection.getInputStream());
	    if (rf.getResource() != null)
	    {
	    	System.out.println(rf.getResource().getClass().toString());
	    }
	    else
	    {
	    	System.out.println(rf.getFeed().getEntryList());
	    	for (AtomEntry entry : rf.getFeed().getEntryList())
	    	{
	    		Resource resource = entry.getResource();
	    		System.out.println(resource.getClass().toString());
	    		if (resource instanceof Observation)
	    		{
	    			Observation obs = (Observation) resource;
	    			obsList.add(obs);
	    			
	    			for (Coding code : obs.getName().getCoding())
	    			{
	    				if (code.getSystem()!= null)
	    				{
	    					System.out.println("Code system uri: " + code.getSystem().getValue());
	    					System.out.println("Code system simple: " + code.getSystemSimple());
	    				}
	    				if (code.getCode()!= null)
	    				{
	    					System.out.println("Code: " + code.getCode().getValue());
	    					System.out.println("Code simple: " + code.getCodeSimple());
	    				}
	    				if (code.getDisplay() != null)
	    				{
	    					System.out.println("Display name: " + code.getDisplay().getValue());
	    					System.out.println("Display simple: " + code.getDisplaySimple());
	    				}
	    			}
	    	
	    			if (obs.getPerformer() != null)
	    			{
	    			System.out.println("Performer: " +obs.getPerformer().getDisplaySimple());
	    			}
	    			if (obs.getSubject() != null)
	    			{
	    				System.out.println("Subject: " + obs.getSubject().getDisplaySimple());
	    			}
	    			if (obs.getInterpretation()!= null)
	    			{
	    				System.out.println("INTERPRETATION: ");
	    				for (Coding code : obs.getInterpretation().getCoding())
	    				{
		    				if (code.getSystem()!= null)
		    				{
		    					
		    					System.out.println("Code system: " + code.getSystemSimple());
		    				}
		    				if (code.getCode()!= null)
		    				{
		    				
		    					System.out.println("Code: " + code.getCodeSimple());
		    				}
		    				if (code.getDisplay() != null)
		    				{
		    					
		    					System.out.println("Display: " + code.getDisplaySimple());
		    				}
	    				}
	    				if (obs.getValue()!= null && obs.getValue() instanceof Quantity)
	    				{
	    					System.out.println("VALUE");
	    					Quantity quantity = (Quantity) obs.getValue();
	    					System.out.println("Value: " + quantity.getValueSimple() + " " + quantity.getUnitsSimple());
	    				}
	    				for (ObservationComponentComponent comp : obs.getComponent())
	    				{
	    					System.out.println(comp.getName() + ": " + ((Quantity)comp.getValue()).getValueSimple() + " " + ((Quantity)comp.getValue()).getUnitsSimple());  
	    				}
	    				if (obs.getReferenceRange()!= null)
	    				{
	    					System.out.println("Range:");
	    					for (ObservationReferenceRangeComponent range : obs.getReferenceRange())
	    					{
	    						if (range.getRange() instanceof Range)
	    						{
	    							Range r = (Range) range.getRange();
	    							System.out.println(r.getLow().getValueSimple() +" " + r.getLow().getUnitsSimple() + " - " +
	    									r.getHigh().getValueSimple() +" " + r.getHigh().getUnitsSimple() );
	    						}
	    					}
	    				}
	    				if (obs.getReliability() != null)
	    				{
	    					System.out.println("Reliability: " +obs.getReliabilitySimple());
	    				}
	    				if (obs.getApplies()!= null)
	    				{
	    					if (obs.getApplies() instanceof Period)
	    					{
	    						System.out.print("Date: ");
	    						Period period = (Period) obs.getApplies();
	    						System.out.print(period.getStartSimple());
	    						if (!period.getEndSimple().equals(period.getStartSimple()))
	    						{
	    							System.out.print(" - " + period.getEndSimple());
	    						}
	    						System.out.println();
	    						
	    					}
	    				}
	    				if (obs.getBodySite()!= null)
	    				{
	    					System.out.print("Body Site:");
	    					for (Coding code : obs.getBodySite().getCoding())
	    	    			{
	    	    				if (code.getSystem()!= null)
	    	    				{
	    	    					
	    	    					System.out.println("Code system: " + code.getSystemSimple());
	    	    				}
	    	    				if (code.getCode()!= null)
	    	    				{
	    	    				
	    	    					System.out.println("Code: " + code.getCodeSimple());
	    	    				}
	    	    				if (code.getDisplay() != null)
	    	    				{
	    	    					
	    	    					System.out.println("Display: " + code.getDisplaySimple());
	    	    				}
	    	    			}
	    					
	    				}
	    				System.out.println("\n\n\n******\n\n");
	    			}
	    		}
	    	}
	    }
		return obsList;
	}
	
}

