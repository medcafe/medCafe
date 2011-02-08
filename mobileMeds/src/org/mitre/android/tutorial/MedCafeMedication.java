package org.mitre.android.tutorial;


import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TableLayout.LayoutParams;


public class MedCafeMedication extends ListActivity {
    /** Called when the activity is first created. */
	 private static final String TAG = "MedicationApp";
	private static final String MEDCAFE = "http://medcafe.org/medcafe/c/repositories/OurVista/patients/7/medications";
	private static final String MEDCAFE_MEDS = "http://medcafe.mitre.org:8081/medcafe/c/repositories/OurVista/patients/7/medications";
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    
   
	@Override
	protected void onResume() {
		super.onResume();
		 new DownloadMedications().execute();
	}
    
    
    private class DownloadMedications extends  AsyncTask<URL, Integer, Long>{

    	private String[] jsonStrings= null;
    	private ProgressDialog dialog;
    	ArrayList<String> nameStrings = new ArrayList<String>();
    	ArrayList<Medication> medArray = new ArrayList<Medication>();
		private Medication medObj =  null;
		private String repository;
		private String patient_name;
		private String patient_id;
		public static final String OBJ_NAME = "medications";
		
    	@Override
		protected Long doInBackground(URL... params) {
			// TODO Auto-generated method stub
			long resultSet = 0; 
			process(MEDCAFE);
			return resultSet;
		}
        
		protected void onPreExecute() 
		{
				// TODO Auto-generated method stub
				super.onPreExecute();

				dialog = ProgressDialog.show(MedCafeMedication.this, "Getting Medications",
						"Fetching medications!", false);
			
		}
		
		protected void onProgressUpdate(Integer...progress )
		{
			if (progress[0] == 0) {
			  dialog.hide();
			  dialog = new ProgressDialog(MedCafeMedication.this);
		      dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		      dialog.setMessage("Processing meds...");
		      dialog.setCancelable(true);
		      dialog.setMax(progress[1]);
		      dialog.show();
			}
			dialog.setProgress(progress[0]);
		}
		
		protected void onPostExecute(Long result)
		{
			//showDialog("Downloaded medications");
			super.onPostExecute(result);
			dialog.dismiss();
			dialog = null;
			

			Log.d(TAG,"number of medications " + medArray.size());
			setContentView(R.layout.main);
			TextView patientView = (TextView)findViewById(R.id.patient_id);
			TextView repositoryView = (TextView)findViewById(R.id.repository);

			MedicationAdapter ma = new MedicationAdapter(MedCafeMedication.this, medArray);
			if (patient_name != null)
				patientView.setText(patient_name);
			if (repository !=null)
				repositoryView.setText(repository);
			setListAdapter(ma);
			
			
		}
		
		 private  void process(String url)
		 {
		    	Log.d(TAG,url);
		   	 
		        HttpClient httpclient = new DefaultHttpClient();

		 
		        // Prepare a request object
		        HttpGet httpget = new HttpGet(url); 
		 
		        // Execute the request
		        HttpResponse response;
		        try {
		            response = httpclient.execute(httpget);
	
		            InputStreamReader reader = new InputStreamReader (response.getEntity().getContent());
		            Log.d(TAG,"Responses :" + response.getStatusLine().getReasonPhrase() + " " + response.getStatusLine().getStatusCode() );
			        int jsonChar= 0;
			        StringBuilder builder = new StringBuilder();
			       try { 
			        while( (jsonChar = reader.read()) != -1){
			        	builder.append(String.valueOf((char)jsonChar)); 
			        	//Log.d(TAG, builder.toString());
			        }
			       }
			       catch(Exception err){
			    	   Log.d(TAG, " " + err.getMessage() + " caught ");
			    	//   String result = builder.toString();
			    	/*   for (int i=0; i < result.length(); i=i+80)
			    		   if (i+80 <result.length())
			    			   Log.d(TAG,result.substring(i, i+80));
			    		   else
			    			   Log.d(TAG,result.substring(i)); */
			       }
			        
			        //Log.d(TAG, builder.toString());
			        JSONObject json=new JSONObject(builder.toString());
			        parseJSON(json);
			        //jsonStr = parseJSON(json);
			        
			        //
			    }
		        catch (ClientProtocolException e) 
			    {
		        	Log.d(TAG, e.getMessage());
			        // TODO Auto-generated catch block
			        e.printStackTrace();
			    }
		        catch (IOException e) {
		        	Log.d(TAG, e.getMessage());
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					Log.d(TAG, e.getMessage());
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 }
    
   
    
		 	/**
		     *  recursively converts a JSONObject into a hierarchical series of Maps and Lists
		     *  @param ret StringBuilder to be appended to
		     *  @param format format the field is to be inserted into
		     *  @param parent JSONObject or JSONArray that is the starting point
		     *  @param tokens array of field names yet to be processed
		     *  @param otherLeafKeys List of other leaf node keys.  E.g., if I want repositories.name as the main, also pass "type" to get repositories.type as well.  Only works with items of the same depth
		     */
		    public void process(String nodeKey, Object node) throws JSONException
		    {
		    	List<String> medKeyList = Arrays.asList(Medication.MED_KEYS); 
		        if( node instanceof JSONObject )
		        {
		            JSONObject jobj = (JSONObject) node;
		            
		            Iterator<String> keys = jobj.keys();
		            
		            while(keys.hasNext())
		            {
		                Object key = keys.next();
		                
		                if (medKeyList.contains (key))
			            {
		                	processMeds((String)key, jobj.get((String)key));
		                	//Log.d(TAG, key + " : " + jobj.getString((String) key));
			            }
		                process( (String)key, jobj.get((String)key) );
		        	    
		            }
		         
		        }
		        else if( node instanceof JSONArray )
		        {

		            //leaf node is an array of JSONObjects which better have a property matching tokens[0]
		            JSONArray leaf = (JSONArray)node;
		            if (leaf.length()> 1 || !leaf.getString(0).equals("{}"))
		            {
		            	for(int i = 0; i < leaf.length(); i++)
		            	{
		            		if (nodeKey.equals(OBJ_NAME))
		            		{

		            			medObj = new Medication(); 
		            		}
			        	
		            		process(nodeKey, leaf.get(i));
		            		if (nodeKey.equals(OBJ_NAME))
		            		{
		            			medArray.add(medObj);
		            		}
		            	}
		            }

		        }
		        else
		        {
		        	
	                processMeds((String)nodeKey, (Object) node);
	                	
		        }
		        
		        
		    }
	   
		    
		private String[] parseJSON(JSONObject json) throws JSONException
	    {
			Iterator<String> keys = json.keys();
	        while(keys.hasNext())
	        {
	            String key = (String)keys.next();
	            Log.d(TAG, key);
	            process( key, json.get(key));
	        }
	        
	        String[] rtnStrings = new String[nameStrings.size()];
	        for (int i=0; i< nameStrings.size(); i++)
	        {
	        	rtnStrings[i] = nameStrings.get(i);
	        }
	        return rtnStrings;
	    }
	   
		public void processMeds(String key, Object valueObject)
		{
			JSONObject jobj = null;
			JSONArray jarray = null;
			String value = "";
			if (valueObject instanceof JSONObject)
			{
				jobj = (JSONObject)valueObject;
				value = jobj.toString();
			}
			else if (valueObject instanceof JSONArray)
			{
				jarray = (JSONArray) valueObject;
				value = jarray.toString();
			}
			else
			{
				value = (String) valueObject;
			}

			Log.d(TAG, key + " " + value);
			try {
			if (key.equals(Medication.REPOSITORY_TYPE))
			{

				repository = value;
			}
			else if (key.equals(Medication.PATIENT_NAME_TYPE))
			{

				patient_name = "Clinical M Patient";
				patient_id = value;
			}
			else if (key.equals(Medication.DELIVERY_TYPE))
			{
				Log.d(TAG, jobj.toString() + " " + jobj.getString("value"));
				if (jobj!=null && !jobj.getString("value").equals(""))
					medObj.setDeliveryMethod(jobj.getString("value"));
			}
			else if (key.equals(Medication.EFFECTIVE_TIME_TYPE))
			{
				if (jobj!= null)
				{
					String year = jobj.getString("year");
					String month = jobj.getString("month");
					String day = jobj.getString("day");
					medObj.setEffectiveTime(month + "/" + day + "/"+year);
				}
			}
			else if (key.equals(Medication.INSTRUCTIONS_TYPE))
			{
				medObj.setPatientInstructions(value);
			}
			else if (key.equals(Medication.MEDICATION_TYPE))
			{
				medObj.setMedication(value);
			}
			else if (key.equals(Medication.NARRATIVE_TYPE))
			{

				if (medObj.getMedication() == null || medObj.getMedication().equals("") )
					medObj.setMedication(value);
			}
			}
			catch (JSONException jsonE)
			{
				Log.d(TAG, "JSON Exception: " + jsonE.getMessage() + " parsing " + key + ":" + value);
			}
		}
		
    
    }

}