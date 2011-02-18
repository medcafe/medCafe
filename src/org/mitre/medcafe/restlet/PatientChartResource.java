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

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mitre.medcafe.util.Repository;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;


public class PatientChartResource extends ServerResource {

	 /** The underlying Item object. */
    //Patient item;

    /** The sequence of characters that identifies the resource. */
    String id;
    String repository;
    public final static String KEY = PatientChartResource.class.getName();
    public final static Logger log = Logger.getLogger( KEY );
    // static{log.setLevel(Level.FINER);}


    @Get("html")
    public Representation toHtml(){

    	System.out.println("Found PatientResource html ");

    	StringBuffer startBuf = new StringBuffer("Test output");
    	StringBuffer patientImages = new StringBuffer();
    	StringBuffer endBuf = new StringBuffer();

    	return new StringRepresentation( startBuf.toString() + patientImages.toString()
                 + endBuf.toString());

    }

    @Get("json")
    public JsonRepresentation toJson(){
        try
        {
        	System.out.println("PatientChartResource JSON start");

        	String[] xValues = new String[]{"1261380000000","1261385000000","1261425000000",
					"1261480000000", "1261485000000","1261490000000","1261584970731"};
        	String[] yValues = new String[]{"100.1","101.7","101.7",
					"101.5", "100.2","100.2","98.7"};
        	String[] label = new String[]{"Temperature" };
        	int i=0;

            JSONObject obj = new JSONObject();
            obj.put("label",label[0]);

            for(String xValue: xValues)
            {
            	JSONArray arrayObj=new JSONArray();

            	arrayObj.put(xValue);
            	arrayObj.put(yValues[i]);
                obj.append("data", arrayObj);  //append creates an array for you
                i++;
            }

            log.finer( obj.toString());
            System.out.println("PatientChartResource JSON " + getString( obj));
            JsonRepresentation rep = new JsonRepresentation(getString( obj));

            return rep;
        }
        catch(Exception e)
        {
            log.throwing(KEY, "toJson()", e);
            return null;
        }
    }

    public String getString(JSONObject jsonObj)
    {

            try {
                Iterator     keys = jsonObj.keys();
                StringBuffer sb = new StringBuffer();
                StringBuffer start = new StringBuffer("{");
                while (keys.hasNext())
                {

                    Object o = keys.next();
                    if (o.toString().equals("label"))
                    {
                    	start.append(JSONObject.quote(o.toString()));
                    	start.append(':');
                    	start.append(JSONObject.quote(jsonObj.get((String)o).toString() ));

                    }
                    else
                    {
                    	sb.append(',');

                    	sb.append(JSONObject.quote(o.toString()));
                    	sb.append(':');

                    	//Object jsonValue = jsonObj.get( (String)o );

                    	sb.append(jsonObj.get((String)o));
                    }
                }
                sb.append('}');
                return start.toString() +  sb.toString();

                //return "{ \"label\": 'Temperature (Patient 1)', \"data\": [[1261380000000, 100.1], [1261385000000, 101.7],[1261425000000, 101.7],[1261480000000, 101.5], [1261485000000, 100.2], [1261490000000, 100.2], [1261584970731, 98.7]]}";

            } catch (Exception e) {
                return null;
            }

    }

    private String join(JSONArray jsonArray)
    {
    	 StringBuilder sb  = new StringBuilder();

    	 String separator = ",";
    	 int len = jsonArray.length();
    	 for (int i = 0; i < len; i += 1) {
             if (i > 0) {
                 sb.append(separator);
             }

            try {
				sb.append(jsonArray.get(i));
				System.out.println("PatientChartResource: join JSON Object " + jsonArray.get(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }
         return sb.toString();

    }
}