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

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.mitre.medcafe.model.Event;
import org.mitre.medcafe.model.MedCafeFile;
import org.mitre.medcafe.model.Patient;
import org.mitre.medcafe.util.Config;
import org.mitre.medcafe.util.DbConnection;
import org.mitre.medcafe.util.Repository;
import org.mitre.medcafe.util.Text;
import org.restlet.Application;
import org.restlet.data.Form;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class PatientListEventResource extends ServerResource {

    public final static String KEY = PatientListEventResource.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}
    private final static String PATIENT_ID = "id";
    private final static String USER_ID = "user";
    private String id;
    protected Date startDate = new Date();
    protected Date endDate =  new Date();
    private String userName;
    private String[] eventTypes = new String[]{};

    /** The sequence of characters that identifies the resource. */

    /**
     *  Grab the information from the url
     */
    @Override
    protected void doInit() throws ResourceException {
       log.finer("Found Patient List Event Resource: " );
       // Get the "type" attribute value taken from the URI template
       Form form = getRequest().getResourceRef().getQueryAsForm();
       id = (String)getRequest().getAttributes().get(PATIENT_ID);
       log.finer("PatientListEventResource JSON init patientId " +  id );

       String startDateStr = form.getFirstValue("start_date");
       if (startDateStr == null)
       	startDateStr = "2004,2,15";

       String endDateStr = form.getFirstValue("end_date");
       if (endDateStr == null)
       	endDateStr = "2012,2,15";

       log.finer("PatientListEventResource JSON init startDate " +  startDateStr + " endDate " + endDateStr );
       userName = form.getFirstValue(USER_ID);

       eventTypes = form.getValuesArray("event");

    }


    @Get("json")
    public JsonRepresentation toJson(){

    	/* Required JSON format
    	 * {
				'wikiURL': "http://simile.mit.edu/shelf/",
				'wikiSection': "Simile Cubism Timeline",

				'events' : [
				       {'start':  new Date(2006,2,15),
				        'title': 'Still Life with a White Dish',
				        'description': 'by Gino Severini, Italian Painter, 1883-1966',
				        'image': 'http://images.allposters.com/images/MCG/FS1254_b.jpg',
				        'link': 'http://www.allposters.com/-sp/Still-Life-with-a-White-Dish-1916-Posters_i366823_.htm'
				        }
				]
				}

    	 */
        //convert to JSON
        try
        {
        	DateFormat df = new SimpleDateFormat(MedCafeFile.DATE_FORMAT);

            String startDateStr = df.format(startDate);
            log.finer("PatientListEventResource toJSON start date " + startDateStr );

            String endDateStr = df.format(endDate);
            log.finer("PatientListEventResource toJSON end date " + endDateStr );

            Application app = this.getApplication();

            Patient patient = new Patient();
            JSONObject repositories = patient.listRepositories(id);

        	ArrayList<Event> events = Event.retrieveEvents(userName, id, startDateStr, endDateStr, eventTypes, app, repositories);

        	log.finer("PatientListEventResource : toJSON: event list " + events.size());
        	ArrayList<String> dates = new ArrayList<String>();
            JSONObject obj = new JSONObject();
            String server = Config.getServerUrl() ;
            //obj.put("wikiURL", "Patient data ");
            //obj.put("wikiSection", "Patient Data");

        	String dir = "patients/" + this.id + "/";
        	String imageDir = "images/" + dir;

        	int i=0;
            DateFormat eventDf = new SimpleDateFormat(Event.DATE_FORMAT);

        	for(Event event: events)
            {
        		JSONObject inner_obj = new JSONObject ();
                inner_obj.put("start", "<:startDate" + i + ":>");
                if (!event.getIcon().equals(""))
                	inner_obj.put("icon", "http://" + server + "/images/" + event.getIcon());
                inner_obj.put("title", event.getTitle());
                if (!event.getFileUrl().equals(""))
                	inner_obj.put("image", "http://" + server + "/" + imageDir +  event.getFileUrl());
                if (!event.getLink().equals(""))
                	inner_obj.put("link", "http://" + server + "/images/" + event.getLink());
                inner_obj.put("type", event.getType());
                inner_obj.put("repository", event.getRepository());
                inner_obj.put("rep_patient_id", event.getRepPatientId());
                inner_obj.put("description", event.getDescription());
                obj.append("events", inner_obj);
                Date date = event.getEventDate();
                String dateStr = eventDf.format(date);
                dates.add(dateStr);
                i++;
            }
        	String jsonStr = obj.toString();
        	jsonStr = putInDates(jsonStr, dates);
        	log.finer("PatientListEventResource : toJSON: " + jsonStr);
        	JsonRepresentation json = new JsonRepresentation(jsonStr);
            return json;
        }
        catch(org.json.JSONException e)
        {
            log.throwing(KEY, "toJson()", e);
            return new JsonRepresentation("{\"error\": \""+e.getMessage()+"\"}");
        }
        catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new JsonRepresentation("{\"SQL error\": \""+e.getMessage()+"\"}");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new JsonRepresentation("{\"error\": \""+e.getMessage()+"\"}");
		}

    }

    /*Workaround to stop insertion of quotes*/
    private String putInDates(String jsonStr, ArrayList<String> dates)
    {
    	int i=0;
    	//new Date (" +  dates[i] + ")
    	for (String date: dates)
    	{
    		jsonStr = jsonStr.replaceAll("\"<:startDate" + i + ":>\"", "new Date (" +  date + ")");
    		i++;
    	}
    	return jsonStr;
    }
    /**
     *  Html representation - this will likely have to changes once full integration is done
     */
    @Get("html")
    public Representation toHtml()
    {


    	StringBuffer buf = new StringBuffer();
    	StringBuffer endBuf = new StringBuffer();
    	endBuf.append("</tbody></table>");

    	StringBuilder ret = new StringBuilder( "Available Patients:<br/>\n<ul>" );

        return new StringRepresentation( buf.toString()
                + endBuf.toString());
    }
}
