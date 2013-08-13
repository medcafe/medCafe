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
package org.mitre.medcafe.restlet;

import org.restlet.data.*;
import org.mitre.medcafe.util.*;
import java.io.IOException;
import java.util.*;
import javax.xml.datatype.*;
import java.text.SimpleDateFormat;

import org.hl7.greencda.c32.Allergy;
import org.hl7.greencda.c32.Code;
import org.hl7.greencda.c32.Interval;
import org.json.JSONObject ;
import org.mitre.medcafe.util.*;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.*;
import java.util.logging.Logger;
import java.util.logging.Level;




public class PatientAllergyResource extends ServerResource {

    /** The sequence of characters that identifies the resource. */
    String id;
    String repository;
 public final static String KEY = PatientAllergyResource.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}
    @Override
    protected void doInit() throws ResourceException {
        this.id = (String) getRequest().getAttributes().get("id");
        this.repository = (String) getRequest().getAttributes().get("repository");
    }


    @Get("json")
    public JsonRepresentation toJson()
    {
        Repository r = Repositories.getRepository( repository );
        if( r == null )
        {
            return new JsonRepresentation(WebUtils.buildErrorJson( "A repository named " + repository + " does not exist."));
        }

        List<Allergy> allergies = r.getAllergies( id );

        if( allergies == null )
        {
            return new JsonRepresentation(WebUtils.buildErrorJson( "Could not establish a connection to the repository " + repository + " at this time."));
        }

        if( allergies.size() == 0)
        {
            return new JsonRepresentation(WebUtils.buildErrorJson( "There are no allergies currently listed for patient " + id + " in repository " + repository, r.canInsertAllergies()));
        }
        //convert to JSON
        return WebUtils.bundleJsonResponse( "allergies", allergies, repository, id, r.canInsertAllergies() );
    }

    @Put("form")
    public Representation acceptItem(Representation entity)
    {
        Representation ret = null;
        if( entity == null )
        {
            // PUT request with no entity.
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return new StringRepresentation("Error", MediaType.TEXT_PLAIN);
        }

        // Parse the given representation and retrieve pairs of
        // "name=value" tokens.
        Allergy allergy = new Allergy();
        Form form = new Form(entity);
        allergy.setDescription(form.getFirstValue("allergens"));
        Code code = new Code();
        code.setDisplayName(form.getFirstValue("reactions"));
        code.setOriginalText(form.getFirstValue("reactions"));
        allergy.setReaction(code);

        Interval dateRange = new Interval();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        try{
        Date eventDate = df.parse(form.getFirstValue("reactiondate"));
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(eventDate);
        DatatypeFactory factory = DatatypeFactory.newInstance();
        XMLGregorianCalendar xCal = factory.newXMLGregorianCalendar(cal);
       dateRange.setStart(xCal);
       dateRange.setValue(xCal);
        allergy.setEffectiveTime(dateRange);
        allergy.setStart_time(xCal.toXMLFormat());
        allergy.set_type("Allergy");
        }
                catch (Exception e)
        {
            log.severe("Error converting date");
        }

        allergy.setMood_code("EVN");
        Code allergyType = new Code();
        String val = form.getFirstValue("allergyType");
        String smCode;
        if (val.equals("food allergy")) {
            smCode = "414285001";

        } else if (val.equals("drug allergy")) {
            smCode = "416098002";

        } else {
            smCode = "419199007";
        }
      

        allergyType.setCode(smCode);
        allergyType.setDisplayName(val);
        allergyType.setOriginalText(val);
        allergyType.setCodeSystem("2.16.840.1.113883.6.96");
        allergyType.setCodeSystemName("SNOMED-CT");
        allergy.setType(allergyType);
        String[] severityParts = form.getFirstValue("severity").split(",");
        Code severity = new Code();
        severity.setCode(severityParts[1]);
        severity.setCodeSystem("2.16.840.1.113883.6.96");
        severity.setCodeSystemName("SNOMED-CT");
        severity.setDisplayName(severityParts[0]);
        severity.setOriginalText(severityParts[1]);
        allergy.setSeverity(severity);
        allergy.setId(UUID.randomUUID().toString());
		String comm;
		if ((comm = form.getFirstValue("comments")) == null) {
			allergy.setFreeText("");
		}
		else {
			allergy.setFreeText(comm);
		}
		Repository repo = Repositories.getRepository(repository);
		log.severe(repository);
		log.severe(repo.getType());
		Collection<Allergy> allergyColl = new ArrayList<Allergy>();
		allergyColl.add(allergy);
		try {
			boolean success = repo.insertAllergies(id, allergyColl);
			if (success) {
				ret = new JsonRepresentation(WebUtils.buildErrorJson("Allergy to "
						+ allergy.getDescription()
						+ " was inserted for patient id "
						+ id
						+ " into repository "
						+ repository
						+ ".  Choose Refresh Patient Cache from the Options to see updated information"));
			}
			else {
				ret = new JsonRepresentation(WebUtils.buildErrorJson("Error inserting "
						+ allergy.getDescription() + " into repository."));
			}
		} catch (NotImplementedException implE) {
			ret = new JsonRepresentation(WebUtils.buildErrorJson("Allergy insert not supported for repository: "
					+ repository));
		}
   /*Product product = new Product();
        product.setValue(form.getFirstValue("allergens"));
        allergy.setProduct(product);
        Reaction reaction = new Reaction();
        reaction.setValue(form.getFirstValue("reactions"));
        allergy.setReaction(reaction);
      
        String code = "";
       
                    String comm;
                    if ((comm=form.getFirstValue("comments"))== null)
                    {
                    	allergy.setNarrative("");
                    	}
                    	else
                    	{
                    		allergy.setNarrative(comm);
							}
                    Repository repo = Repositories.getRepository(repository);
                    log.severe(repository);
                    log.severe(repo.getType());
                    Collection<Allergy> allergyColl = new ArrayList<Allergy>();
                    allergyColl.add(allergy);
                    log.severe(allergy.getAdverseEventType().getValue() + " " + allergy.getReaction().getValue()+ " " + allergy.getProduct().getValue() + " " + allergy.getAdverseEventDate().getLow());
                    try
                    {
                    boolean success = repo.insertAllergies(id, allergyColl);
                    if (success)
                    {
                        ret = new JsonRepresentation(WebUtils.buildErrorJson("Allergy to " + allergy.getProduct().getValue() + " was inserted for patient id " +
                                id + " into repository " + repository+".  Choose Refresh Patient Cache from the Options to see updated information") );
                    }
                    else
                    {
                        ret = new JsonRepresentation(WebUtils.buildErrorJson("Error inserting " + allergy.getProduct().getValue() + " into repository."));
                    }
        }
                    catch(NotImplementedException implE)
                    {
                        ret = new JsonRepresentation(WebUtils.buildErrorJson("Allergy insert not supported for repository: " + repository));
                    }

        //DO INSERT HERE.  Really,we should go through the Repository Object like we do for the @Get.  And that means making an hData object first and THEN putting it
        //into VistA.  You can bypass all that, though, if you want...just leave a note there to say that'd be the "proper" way to do it.

        // See http://stackoverflow.com/questions/996819/restlet-how-to-process-multipart-form-data-requests for more info

      */

        return ret;
    }
}
