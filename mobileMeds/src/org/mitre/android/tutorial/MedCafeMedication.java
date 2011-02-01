package org.mitre.android.tutorial;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MedCafeMedication extends ListActivity {
    /** Called when the activity is first created. */
	 private static final String TAG = "TwitTutorial";
	private static final String TWIT_URL ="http://api.twitter.com/1/statuses/public_timeline.json";
	private static final String LOCAL = "http://employeeshare.mitre.org/e/elevine/transfer/Android/twitter.txt";
	private static final String MEDCAFE = "http://medcafe.org:8081/medcafe/c/repositories/OurVista/patients/7/medications";
	private static final String LOCAL_MEDCAFE = "medications.txt";
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
    
    private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    
    
    private class DownloadMedications extends  AsyncTask<URL, Integer, Long>{

    	private String[] jsonStrings= null;
    	private ProgressDialog dialog;
    	ArrayList<String> nameStrings = new ArrayList<String>();
    	ArrayList<Medication> medArray = new ArrayList<Medication>();
		private Medication medObj =  new Medication();
		private String repository;
		private String patient_name;
		
    	@Override
		protected Long doInBackground(URL... params) {
			// TODO Auto-generated method stub
			long resultSet = 0; 
			processFile(LOCAL_MEDCAFE);
			return resultSet;
		}
        
		protected void onPreExecute() 
		{
				// TODO Auto-generated method stub
				super.onPreExecute();

				dialog = ProgressDialog.show(MedCafeMedication.this, "Getting Medications",
						"Fetching timeline!", false);
			
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
			
			//TextView tv = (TextView)findViewById(R.id.label1);
	        //tv.setText(jsonStr);
			String[] medList = new String[medArray.size()];
			Log.d(TAG,"number of medications " + medArray.size());
			/*for (int i=0; i < medArray.size(); i++)
			{
				medArray.get(i).setPatient_name(patient_name);
				medArray.get(i).setRepository(repository);
				
			}*/
				
			MedicationAdapter ma = new MedicationAdapter(MedCafeMedication.this, medArray);
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
		            BufferedReader reader = new BufferedReader(new InputStreamReader (response.getEntity().getContent()));
			        String line= null;
			        StringBuilder builder = new StringBuilder();
			        
			        while( (line = reader.readLine()) != null){
			        	builder.append(line); 
			        }
			        Log.d(TAG,"in here too");
			        
			        Log.d(TAG, builder.toString());
			        JSONObject json=new JSONObject(builder.toString());
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
    
    
		 private  void processFile(String file)
		 {
		    	Log.d(TAG,file);
		   	 
		        
		        try {
		            
		        	InputStream is = getResources().openRawResource(R.raw.medications);
		            String line= null;
			        StringBuilder builder = new StringBuilder();

			        char[] buffer = new char[1024];
			        Reader reader = new BufferedReader(new InputStreamReader(is));
			        
			        while ((reader.read(buffer)) != -1) {
			        	builder.append(buffer); 
			        }
			        
			        Log.d(TAG,"in here too");
			        
			        Log.d(TAG, builder.toString());
			        JSONObject json=new JSONObject(builder.toString());
			        jsonStrings = parseJSON(json);
			        
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
		     *  recursively converts a JSONObject into a heirarchical series of Maps and Lists
		     *  @param ret StringBuilder to be appended to
		     *  @param format format the field is to be inserted into
		     *  @param parent JSONObject or JSONArray that is the starting point
		     *  @param tokens array of field names yet to be processed
		     *  @param otherLeafKeys List of other leaf node keys.  E.g., if I want repositories.name as the main, also pass "type" to get repositories.type as well.  Only works with items of the same depth
		     */
		    public void process(Object node) throws JSONException
		    {
		    	List<String> medKeyList = Arrays.asList(Medication.MED_KEYS); 
		        if( node instanceof JSONObject )
		        {
		            JSONObject jobj = (JSONObject) node;
		            
		            Iterator keys = jobj.keys();
		            
		            while(keys.hasNext())
		            {
		                Object key = keys.next();
		                
		                if (medKeyList.contains (key))
			            {
		                	processMeds((String)key, jobj.getString((String) key));
		                	//Log.d(TAG, key + " : " + jobj.getString((String) key));
			            }
		                process( jobj.get((String)key) );
		        	    
		            }
		         
		        }
		        else if( node instanceof JSONArray )
		        {
		            //leaf node is an array of JSONObjects which better have a property matching tokens[0]
		            JSONArray leaf = (JSONArray)node;
		            for(int i = 0; i < leaf.length(); i++)
		            {
		        
		                process(leaf.get(i));
		            }
		           
		        }
		        else
		        {
		        	
	                processMeds((String)node, (String)node);
	                	
		        }
		        
		        
		    }
	   
		    
		private String[] parseJSON(JSONObject json) throws JSONException
	    {
			Iterator keys = json.keys();
	        while(keys.hasNext())
	        {
	            String key = (String)keys.next();
	            process( json.get(key));
	        }
	        
	        String[] rtnStrings = new String[nameStrings.size()];
	        for (int i=0; i< nameStrings.size(); i++)
	        {
	        	rtnStrings[i] = nameStrings.get(i);
	        }
	        return rtnStrings;
	    }
	   
		public void processMeds(String key, String value)
		{
			
			if (key.equals(Medication.REPOSITORY_TYPE))
			{
				medObj.setRepository(value);
				repository = value;
			}
			else if (key.equals(Medication.PATIENT_NAME_TYPE))
			{
				medObj.setPatient_name(value);
				patient_name = value;
			}
			else if (key.equals(Medication.DELIVERY_TYPE))
			{
				medObj.setDeliveryMethod(value);
			}
			else if (key.equals(Medication.EFFECTIVE_TIME_TYPE))
			{
				medObj.setEffectiveTime(value);
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
				if (medObj.getNarrative() != null)
				{
					medArray.add(medObj);
					Log.d(TAG, "creating new medication "  + medObj.toString());
				}
				medObj = new Medication();
				medObj.setNarrative(value);
			}
		}
		
    
    }

}