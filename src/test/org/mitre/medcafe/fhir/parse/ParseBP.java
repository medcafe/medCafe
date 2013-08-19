package test.org.mitre.medcafe.fhir.parse;

import java.io.File;
import java.io.FileInputStream;
<<<<<<< Updated upstream
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
=======
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
>>>>>>> Stashed changes
import java.util.List;

import org.hl7.fhir.instance.formats.JsonComposer;
import org.hl7.fhir.instance.formats.JsonParser;
import org.hl7.fhir.instance.formats.JsonWriter;
import org.hl7.fhir.instance.formats.ParserBase.ResourceOrFeed;
import org.hl7.fhir.instance.model.AtomEntry;
import org.hl7.fhir.instance.model.CodeableConcept;
import org.hl7.fhir.instance.model.Coding;
import org.hl7.fhir.instance.model.DateTime;
import org.hl7.fhir.instance.model.Narrative;
import org.hl7.fhir.instance.model.Observation;
import org.hl7.fhir.instance.model.Observation.ObservationComponentComponent;
import org.hl7.fhir.instance.model.Observation.ObservationReferenceRangeComponent;
import org.hl7.fhir.instance.model.Observation.ObservationReliability;
import org.hl7.fhir.instance.model.Observation.ObservationStatus;
import org.hl7.fhir.instance.model.Period;
import org.hl7.fhir.instance.model.Quantity;
import org.hl7.fhir.instance.model.Range;
import org.hl7.fhir.instance.model.Resource;
import org.hl7.fhir.instance.model.ResourceFactory;
import org.hl7.fhir.instance.model.ResourceReference;
import org.hl7.fhir.utilities.xhtml.NodeType;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

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
<<<<<<< Updated upstream
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
=======
	    		processResource(resource);

>>>>>>> Stashed changes
	    	}
	    }
	    	

	//    URL data_server = new URL("http://hl7connect.healthintersections.com.au/svc/fhir/observation/search?_format=json&subject=example");
	    URL data_server=new URL("http://localhost:8888/observation/search?_format=json&subject=f201");
        HttpURLConnection connection = (HttpURLConnection)data_server.openConnection();
        connection.setRequestProperty("Accept","*/*");
        connection.setRequestMethod("GET");
 //       System.setProperty("http.proxyHost", "gatekeeper-w.mitre.org");
 //       System.setProperty("http.proxyPort","80");
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
<<<<<<< Updated upstream
	    		if (resource instanceof Observation)
	    		{
	    			Observation obs = (Observation) resource;
	    			obsList.add(obs);
	    			
	    			for (Coding code : obs.getName().getCoding())
=======
	    		processResource(resource);
	    		Writer stringWriter = new StringWriter();
	    			JsonWriter jWriter = new JsonWriter(stringWriter);

	    			
	    			JsonComposer composer = new JsonComposer();
	    			composer.compose(jWriter, resource);
	    			System.out.println(stringWriter.toString());
	    			StringBufferInputStream sr = new StringBufferInputStream(stringWriter.toString());
	    			
	    			ResourceOrFeed rorf = jsonParser.parseGeneral(sr);
	    			if (rorf.getResource()!= null)
>>>>>>> Stashed changes
	    			{
	    				System.out.println("****** Printing json obtained");
	    				processResource(rorf.getResource());
	    			}
	    			
	    	
	    	}
	    }

	    Observation newObs = (Observation)ResourceFactory.createResource("Observation");
	    ResourceReference subject = new ResourceReference();
	    subject.setDisplaySimple("Roel");
	    subject.setReferenceSimple("patient/@f201");
	    subject.setTypeSimple("Patient");
	    newObs.setSubject(subject);
	    newObs.setPerformer(subject);
	    
	    DateTime date = new DateTime();
	    date.setValue("2013-08-19");
	    newObs.setApplies(date);
	    GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
	    
	    newObs.setIssuedSimple(calendar);
	    CodeableConcept test = new CodeableConcept();
	    List<Coding> coding = test.getCoding();
	    coding.add(createNewCode("http://loinc.org","55284-4","Blood pressure systolic and diastolic"));
	    newObs.setName(test);
	    CodeableConcept interpretation = new CodeableConcept();
	    coding = interpretation.getCoding();
	    coding.add(createNewCode("http://hl7.org/fhir/v2/0078", "N", "Normal (applies to non-numeric results)"));
	    newObs.setInterpretation(interpretation);
	    ObservationStatus status = ObservationStatus.final_;
	    newObs.setStatusSimple(status);
	    newObs.setReliabilitySimple(Observation.ObservationReliability.questionable);
	    List<ObservationComponentComponent>components = newObs.getComponent();
	    ObservationComponentComponent systolic = newObs.new ObservationComponentComponent();
	    CodeableConcept part = new CodeableConcept();
	    part.getCoding().add(createNewCode("http://loinc.org","8480-6","Systolic blood pressure"));
	    systolic.setName(part);
	    Quantity quantity = new Quantity();
	    quantity.setValueSimple(new BigDecimal(110));
	    quantity.setUnitsSimple("mm[Hg]");
	    systolic.setValue(quantity);
	    components.add(systolic);
	    ObservationComponentComponent diastolic = newObs.new ObservationComponentComponent();
	    part = new CodeableConcept();
	    part.getCoding().add(createNewCode("http://loinc.org","8462-4","Diastolic blood pressure"));
	    diastolic.setName(part);
	    quantity = new Quantity();
	    quantity.setValueSimple(new BigDecimal(70));
	    quantity.setUnitsSimple("mm[Hg]");
	    diastolic.setValue(quantity);
	    components.add(diastolic);
	    Narrative narrative = new Narrative();
	    narrative.setStatusSimple(Narrative.NarrativeStatus.generated);
	    XhtmlNode node = new XhtmlNode();

	    node.setNodeType(NodeType.Element);
	    node.setName("div");
	    XhtmlNode divContent = new XhtmlNode();
	    divContent.setNodeType(NodeType.Text);
	    SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
	    divContent.setContent(df.format(newObs.getIssuedSimple().getTime())+ " " + newObs.getSubject().getDisplaySimple() + " " + 
	    ((Quantity)newObs.getComponent().get(0).getValue()).getValueSimple().toString() + "/"+ 
	   ((Quantity)newObs.getComponent().get(1).getValue()).getValueSimple().toString() + "(" + newObs.getInterpretation().getCoding().get(0).getDisplaySimple().replaceAll(" [(].*[)]", "")+")");
	    node.getChildNodes().add(divContent);
	    narrative.setDiv(node);
	    newObs.setText(narrative); 
	    
	    
	    
	    StringWriter strWriter = new StringWriter();
	    JsonWriter writer = new JsonWriter(strWriter);
	    JsonComposer composer = new JsonComposer();
	    composer.compose(writer, newObs);
        data_server=new URL("http://localhost:8888/observation/@f201");
  //  data_server= new URL("http://hl7connect.healthintersections.com.au/svc/fhir/observation/@f209");
	    connection = (HttpURLConnection)data_server.openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        connection.setRequestMethod("PUT");
        
  //  System.setProperty("http.proxyHost", "gatekeeper-w.mitre.org");
  //  System.setProperty("http.proxyPort","80");
       
        connection.setRequestMethod("PUT");
        OutputStreamWriter out = new OutputStreamWriter(
           connection.getOutputStream());
        out.write(strWriter.toString());
        out.flush();
        out.close();
       int responseCode= connection.getResponseCode();

            String header = null;
            String headerValue = null;
            int index = 0;
            while ((headerValue = connection.getHeaderField(index)) != null)
            {
                header = connection.getHeaderFieldKey(index);
                
               if (header == null)
                    System.out.println(headerValue);
                else
                    System.out.println(header + ": " + headerValue);
                
               index++;
            }
            System.out.println("");


	}
	private static void processResource(Resource resource)
	{
		if (resource instanceof Observation)
		{
			Observation obs = (Observation) resource;
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
<<<<<<< Updated upstream
	    		}
	    	}
	    }
		return obsList;
=======
					
				}
				if (obs.getText() != null && obs.getText().getDiv()!= null)
					System.out.println(obs.getText().getDiv().getContent());
				System.out.println("\n\n\n******\n\n");
			}
		}
	}
	private static Coding createNewCode(String systemStr, String codeStr, String displayStr )
	{
	    Coding code = new Coding();
	    code.setSystemSimple(systemStr);
	    code.setCodeSimple(codeStr);
	    code.setDisplaySimple(displayStr);
	    return code;
>>>>>>> Stashed changes
	}
	
}

