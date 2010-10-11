/* Copyright 2010 The MITRE Corporation.  ALL RIGHTS RESERVED. */
package org.mitre.medcafe.model;

//import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.json.*;
import org.mitre.medcafe.restlet.*;
import org.mitre.medcafe.util.*;
import org.restlet.*;
import org.restlet.data.*;
import org.restlet.representation.*;
import org.restlet.resource.*;

public class PatientCache extends TimerTask {

    public final static String KEY = PatientCache.class.getName();
    public final static Logger log = Logger.getLogger(KEY);

    static {
       // log.setLevel(Level.FINER);
    }
    //{{{ Members
    protected JSONObject medicalHistory = null;
    protected JSONObject familyHistory = null;
    protected JSONObject medicineList = null;
    protected JSONObject alertList = null;
    protected JSONObject problemList = null;
    protected JSONObject supportList = null;
    protected JSONObject vitalsList = null;
    protected JSONObject images = null;
    protected JSONObject immuneList = null;
    protected JSONObject encounterList = null;
    protected String databasePatientId = null;
    protected javax.servlet.ServletContext application = null;
    public final static String NA = "Resource not available";
    protected JSONObject repositories = null;
    //protected String repoPatientId = null;
    protected JSONObject patientList = null;
    protected String firstName = null;
    protected String lastName = null;
    protected String photo = null;
    protected boolean finished = false;
    protected boolean bannerFinished = false;
    private String primaryRepos = "";
    protected HashMap<String, JSONObject> historyMap = null;

    //}}}
    //{{{ Constuctors
    public PatientCache(String databasePatientId, javax.servlet.ServletContext application, String primaryRepos) {

        this.databasePatientId = databasePatientId;
        this.application = application;
        this.primaryRepos = primaryRepos;
        loadLocalInfo();
    }
    //}}}

    //{{{ Methods
    public void loadLocalInfo() {
        //get repository/local IDs
        String query = "select * from patient where id=?";
        //String query = "select a.rep_patient_id, p.first_name, p.last_name, a.repository from patient p join patient_repository_assoc a on p.id=a.patient_id where p.id=?";
        DbConnection conn = null;
        PreparedStatement ps = null;

        ResultSet rs = null;
        try {
            conn = new DbConnection();
            loadRepositoryInfo(conn);

            rs = conn.psExecuteQuery(query, "Looking up repository info", new Integer(databasePatientId));
            while (rs.next()) {
                firstName = rs.getString("first_name");
                lastName = rs.getString("last_name");
                photo = rs.getString("photo");
            }

        } catch (SQLException e) {
            log.throwing(KEY, "Error looking up patient repository details", e);
            throw new RuntimeException(e);
        } finally {
            //If this is closed - causes issues later
            //DatabaseUtility.close(rs);
            //conn.close();
        }
    }

    public void loadRepositoryInfo(DbConnection conn) {
        //get repository/local IDs
        //String query = "select * from patient where id=?";
        Patient patient = new Patient(conn);
        JSONObject repositoryList = patient.listRepositories(databasePatientId);
        ArrayList<JSONObject> repos = new ArrayList<JSONObject>();
        try {
       	JSONArray reps = new JSONArray();
       	reps = repositoryList.getJSONArray("repositories");
       	int length = reps.length();
        	for (int i = 0; i< length; i++)
         {
       
        /* cycle through to put primary repository first in the list  */
       		JSONObject repository = reps.getJSONObject(i);
        
        		String repositoryName = repository.getString("repository");
        		System.out.println(repositoryName + " " + primaryRepos);
        		System.out.println(repository.toString());
        		if (repositoryName.equals(primaryRepos))
        		{
        			
        			repos.add(0, repository);
        		}
        		else
        		{
        			repos.add(repository);
        		}
        	}
        	repositories = new JSONObject();
        	for (JSONObject repository: repos)
        	{
        		repositories.append("repositories", repository);
        	}
        	System.out.println(repositories.toString());
        }
        catch (JSONException jsonE)
        {
        System.out.println("Error accessing JSON Array to order repository");
        }
        System.out.println("PatientCache loadRepositoryInfo JSONObject " + repositories.toString());

    }

    public void run() {
        log.entering(KEY, "run()");

        //get access point
        MedcafeApplication app = (MedcafeApplication) application.getAttribute("org.restlet.ext.servlet.ServerServlet.application");
        if (app == null) {
            log.severe("Could not connect to data restlets.");
            return;
        }
        //define and send request
        String results = "";
        //{"repositories":[{"id":2,"repository":"OurVista"},{"id":2,"repository":"local"}]}

        JSONArray reps = new JSONArray();
        try {
            System.out.println("PatientCache run repository JSONObject " + repositories.toString());

            reps = repositories.getJSONArray("repositories");
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            log.throwing(KEY, "constructor", e1);
            repositories = WebUtils.buildErrorJson("Problem retrieving list of repositories for patient." + e1.getMessage());
        }

        if (repositories.has("repositories")) {
            try {

                patientList = new JSONObject();
                for (int i = 0; i < reps.length(); i++) {
                    JSONObject repObj = (JSONObject) reps.get(i);
                    String repository = repObj.getString("repository");
                    String repoPatientId = repObj.getString("id");

                    results = getJsonContent(app, "/repositories/" + repository + "/patients/" + repoPatientId);
                    JSONObject patient = new JSONObject(results);
                    if (patient.has("patient_data")) {
                        String age = computeAge(patient);
                        patient.put("age", age);
                    }
                    patient.put("repository", repository);
                    patientList.append("repositoryList", patient);
                }
                patientList.put("displayAll", false);
              //  System.out.println("PatientCache generating patient list line 141 JSONObject " + patientList.toString());
            } catch (JSONException e) {
                log.throwing(KEY, "constructor", e);
                patientList = WebUtils.buildErrorJson("Problem retrieving patient from source." + e.getMessage());
            }

            log.finer("Done retrieving patient from repository");
            log.finer(patientList.toString());
            try {

                vitalsList = new JSONObject();
                for (int i = 0; i < reps.length(); i++) {
                    JSONObject repObj = (JSONObject) reps.get(i);
                    String repository = repObj.getString("repository");
                    String repoPatientId = repObj.getString("id");

                    results = getJsonContent(app, "/repositories/" + repository + "/patients/" + repoPatientId + "/vitals/latest");
                    JSONObject vitals = new JSONObject(results);
                    vitals.put("repository", repository);
                    vitalsList.append("repositoryList", vitals);
                }
                //System.out.println("PatientCache generating vitals list line 167 JSONObject " + vitalsList.toString());
            } catch (JSONException e) {
                log.throwing(KEY, "constructor", e);
                vitalsList = WebUtils.buildErrorJson("Problem retrieving vitals from source." + e.getMessage());
            }

            log.finer("Done retrieving vitals from repository");
            log.finer(vitalsList.toString());
            try {

                medicineList = new JSONObject();
                for (int i = 0; i < reps.length(); i++) {
                    JSONObject repObj = (JSONObject) reps.get(i);
                    String repository = repObj.getString("repository");
                    String repoPatientId = repObj.getString("id");

                    results = getJsonContent(app, "/repositories/" + repository + "/patients/" + repoPatientId + "/medications");
                    JSONObject meds = new JSONObject(results);
                    meds.put("repository", repository);
                    medicineList.append("repositoryList", meds);
                }
                //System.out.println("PatientCache generating medicine list line 141 JSONObject " + medicineList.toString());
            } catch (JSONException e) {
                log.throwing(KEY, "constructor", e);
                medicineList = WebUtils.buildErrorJson("Problem retrieving medication list from source." + e.getMessage());
            }

            log.finer("Done retrieving medication list");
            log.finer(medicineList.toString());
            try {
                alertList = new JSONObject();
                for (int i = 0; i < reps.length(); i++) {
                    JSONObject repObj = (JSONObject) reps.get(i);
                    String repository = repObj.getString("repository");
                    String repoPatientId = repObj.getString("id");

                    results = getJsonContent(app, "/repositories/" + repository + "/patients/" + repoPatientId + "/allergies");

                    JSONObject alerts = new JSONObject(results);
                    alerts.put("repository", repository);
                    alertList.append("repositoryList", alerts);
                }
                //System.out.println("PatientCache generating alert/allergy list line 175 JSONObject " + alertList.toString());

            } catch (JSONException e) {
                log.throwing(KEY, "constructor", e);
                alertList = WebUtils.buildErrorJson("Problem retrieving alert list from source." + e.getMessage());
            }
            log.finer("Done retrieving alert list");
            log.finer(alertList.toString());
            historyMap = new HashMap<String, JSONObject>();
            try {
            	 JSONObject historyObject;
            	 JSONObject categories = History.getHistoryCategories();
            	 JSONArray categoryArray = categories.getJSONArray("history_categories");
            	// System.out.println("Categories: " + categoryArray.toString());
            	 for (int j = 0; j< categoryArray.length(); j++)
            	 {
                		historyObject = new JSONObject();
                		for (int i = 0; i < reps.length(); i++) {
                    		JSONObject repObj = (JSONObject) reps.get(i);
                    		String repository = repObj.getString("repository");
                    		String repoPatientId = repObj.getString("id");

	                    results = getJsonContent(app, "/repositories/" + repository + "/patients/" + repoPatientId + "/history/" + categoryArray.getString(j));

                    try {
                        JSONObject history = new JSONObject(results);
                        history.put("repository", repository);
                        historyObject.append("repositoryList", history);
                    } catch (JSONException e) {
                        log.log(Level.SEVERE, "Error converting the results to JSON.  Raw string was [" + results + "]", e);
                    }
                    historyMap.put(categoryArray.getString(j), historyObject);
                    
                }
                }
                //System.out.println("PatientCache generating medical History list line 221 JSONObject " + medicalHistory.toString());

            } catch (JSONException e) {
                log.throwing(KEY, "constructor", e);
                images = WebUtils.buildErrorJson("Problem retrieving medical history from source." + e.getMessage());
            }
            try {
                problemList = new JSONObject();
                for (int i = 0; i < reps.length(); i++) {
                    JSONObject repObj = (JSONObject) reps.get(i);
                    String repository = repObj.getString("repository");
                    String repoPatientId = repObj.getString("id");

                    results = getJsonContent(app, "/repositories/" + repository + "/patients/" + repoPatientId + "/problems");

                    JSONObject problems = new JSONObject(results);
                    problems.put("repository", repository);
                    problemList.append("repositoryList", problems);
                }
                //System.out.println("PatientCache generating problem list line 239 JSONObject " + problemList.toString());

            } catch (JSONException e) {
                log.throwing(KEY, "constructor", e);
                problemList = WebUtils.buildErrorJson("Problem retrieving problem list from source." + e.getMessage());
            }
            log.finer("Done retrieving problem list");

            //log.finer(problemList.toString());
                    try {
                images = new JSONObject();
                for (int i = 0; i < reps.length(); i++) {
                    JSONObject repObj = (JSONObject) reps.get(i);
                    String repository = repObj.getString("repository");
                    String repoPatientId = repObj.getString("id");

                    results = getJsonContent(app, "/repositories/" + repository + "/patients/" + repoPatientId + "/images");

                    JSONObject imageObj = new JSONObject(results);
                    imageObj.put("repository", repository);
                    images.append("repositoryList", imageObj);
                }
                //System.out.println("PatientCache generating image list line 198 JSONObject " + images.toString());

            } catch (JSONException e) {
                log.throwing(KEY, "constructor", e);
                images = WebUtils.buildErrorJson("Problem retrieving image list from source." + e.getMessage());
            }
            log.finer("Done retrieving image list");
            log.finer(images.toString());
                try {
                immuneList = new JSONObject();
                for (int i = 0; i < reps.length(); i++) {
                    JSONObject repObj = (JSONObject) reps.get(i);
                    String repository = repObj.getString("repository");
                    String repoPatientId = repObj.getString("id");

                    results = getJsonContent(app, "/repositories/" + repository + "/patients/" + repoPatientId + "/immunizations");

                    JSONObject vaccines = new JSONObject(results);
                    vaccines.put("repository", repository);
                    immuneList.append("repositoryList", vaccines);
                }
                //System.out.println("PatientCache generating immunization list line 175 JSONObject " + immuneList.toString());

            } catch (JSONException e) {
                log.throwing(KEY, "constructor", e);
                immuneList = WebUtils.buildErrorJson("Problem retrieving immunization list from source." + e.getMessage());
            }
            log.finer("Done retrieving immune list");
            log.finer(immuneList.toString());
        
                 try {
                supportList = new JSONObject();
                for (int i = 0; i < reps.length(); i++) {
                    JSONObject repObj = (JSONObject) reps.get(i);
                    String repository = repObj.getString("repository");
                    String repoPatientId = repObj.getString("id");

                    results = getJsonContent(app, "/repositories/" + repository + "/patients/" + repoPatientId + "/supportList");

                    JSONObject contacts = new JSONObject(results);
                    contacts.put("repository", repository);
                    supportList.append("repositoryList", contacts);
                }
                //System.out.println("PatientCache generating support list line 175 JSONObject " + supportList.toString());

            } catch (JSONException e) {
                log.throwing(KEY, "constructor", e);
                supportList = WebUtils.buildErrorJson("Problem retrieving support list from source." + e.getMessage());
            }
            log.finer("Done retrieving support list");
            log.finer(supportList.toString());
                        bannerFinished = true;
                      try {
                encounterList = new JSONObject();
                for (int i = 0; i < reps.length(); i++) {
                    JSONObject repObj = (JSONObject) reps.get(i);
                    String repository = repObj.getString("repository");
                    String repoPatientId = repObj.getString("id");

                    results = getJsonContent(app, "/repositories/" + repository + "/patients/" + repoPatientId + "/encounters");

                    JSONObject encounter_data = new JSONObject(results);
                    encounter_data.put("repository", repository);
                    encounterList.append("repositoryList", encounter_data);
                }
                //System.out.println("PatientCache generating  list line 175 JSONObject " + encounterList.toString());

            } catch (JSONException e) {
                log.throwing(KEY, "constructor", e);
                supportList = WebUtils.buildErrorJson("Problem retrieving encounter list from source." + e.getMessage());
            }
            log.finer("Done retrieving encounter list");
            log.finer(encounterList.toString());
        }
        finished = true;
        log.exiting(KEY, "run()");

    }

    protected String getJsonContent(MedcafeApplication app, String endpoint) {
        log.finer("Starting retrieval of " + endpoint);
        Request req = new Request(Method.GET, endpoint);
        Response resp = new Response(req);
        ClientInfo clientInfo = req.getClientInfo();
        List<Preference<MediaType>> mediaTypes = clientInfo.getAcceptedMediaTypes();
        mediaTypes.add(new Preference<MediaType>(MediaType.APPLICATION_JSON, 1.0F));
        app.handle(req, resp);
        StringWriter out = new StringWriter();
        if (resp.getStatus().isSuccess() && resp.getEntity().isAvailable()) {
            try {
                resp.getEntity().write(out);
            } catch (IOException e) {
                log.throwing(KEY, "getJsonContent()", e);
                return WebUtils.buildErrorJson("Problem retrieving data from source." + e.getMessage()).toString();
            }
        } else {
            out.write(NA);
        }
        log.finer("Finished retrieval of " + endpoint);
        return out.toString();
    }

    //}}}
    //{{{ Getters and Setters
    public JSONObject getAlertList() {
        try {
            while (!bannerFinished) {
                Thread.sleep(500);
            }
        } catch (InterruptedException intE) {
            log.severe("Interrupted while getting allergies from patient cache.");
        }
        return this.alertList;
    }

    public JSONObject getFamilyHistory() {
        try {
            while (!bannerFinished) {
                Thread.sleep(500);
            }
        } catch (InterruptedException intE) {
            log.severe("Interrupted while getting family history from patient cache.");
        }
        return this.familyHistory;
    }

    public JSONObject getImages() {
        try {
            while (!bannerFinished) {
                Thread.sleep(500);
            }
        } catch (InterruptedException intE) {
            log.severe("Interrupted while getting images from patient cache.");
        }
        return this.images;
    }
    public JSONObject getFilteredImages(String startDateStr, String endDateStr, 
    			String filterCat, String user){
    		JSONObject retObj;
    		JSONObject obj = getImages();
    		if ((startDateStr == null ||startDateStr.equals(""))&&(endDateStr == null ||
    			endDateStr.equals(""))&&(filterCat == null ||filterCat.equals("")))
    			return obj;
         if (startDateStr == null)
        	startDateStr = "01/01/1950";
        	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        if (endDateStr == null)
        {
        		Date today = new Date();

        	//	endDateStr = "01/01/2012";
        		endDateStr = df.format(today);
			}
			Date startDate, endDate;
			try {
			startDate = df.parse(startDateStr);
			endDate = df.parse(endDateStr);
			}
			catch (ParseException parseE)
			{
				log.severe("Error parsing filter dates, using default dates");
				endDate = new Date();
				GregorianCalendar cal = new GregorianCalendar(1950, 1, 1);
				startDate = cal.getTime();
			}
			boolean categories = false;
			String[] catFilters = new String[0];
			if (filterCat != null && !filterCat.equals(""))
			{
				catFilters = filterCat.split(",");
				categories = true;
			}
			retObj = new JSONObject();
			SimpleDateFormat fileDateFormat = new SimpleDateFormat(MedCafeFile.DATE_FORMAT);
			try{
			JSONArray objArray = obj.getJSONArray("repositoryList");
			for (int i = 0; i < objArray.length(); i++)
			{
				JSONObject reposObject = objArray.getJSONObject(i);
				String repos = reposObject.getString("repository");
				JSONArray imageArray = reposObject.getJSONArray("images");
				JSONObject newImages = new JSONObject();
				for (int j = 0; j < imageArray.length(); j++)
				{
					JSONObject imageObj = imageArray.getJSONObject(j);
					String dateStr = imageObj.getString(MedCafeFile.DATE);
					Date imageDate = null;
					try {
						imageDate = fileDateFormat.parse(dateStr);
						}
					catch (ParseException parseE)
					{
						log.severe("Error parsing date of image " + imageObj.getString("name"));
						break;
					}
					if (imageDate.compareTo(endDate)<= 0 && imageDate.compareTo(startDate)>=0)
					{
						if (categories)
						{	
							for (String cat : catFilters)
							{
								if (cat.equals(imageObj.getString("category")))
									newImages.append("images", imageObj);
									break;
							 }
						}
						else
						{
							newImages.append("images", imageObj);
						}
					}
					
				}
			
				newImages.put("repository", repos);
				retObj.append("repositoryList", newImages);
					
			
			}
			}
			catch (JSONException e) {
                log.severe(e.getMessage());
                retObj = WebUtils.buildErrorJson("Problem retrieving filtered image list from PatientCache." + e.getMessage());
            }
			return retObj;
    }

    public JSONObject getMedicalHistory() {
        try {
            while (!bannerFinished) {
                Thread.sleep(500);
            }
        } catch (InterruptedException intE) {
            log.severe("Interrupted while getting medical history from patient cache.");
        }
        return getHistoryCategory("Personal");
    }

    public JSONObject getMedicineList() {
        try {
            while (!bannerFinished) {
                Thread.sleep(500);
            }
        } catch (InterruptedException intE) {
            log.severe("Interrupted while getting medicine list from patient cache.");
        }
        return this.medicineList;
    }

    public JSONObject getImmuneList() {
        try {
            while (!bannerFinished) {
                Thread.sleep(500);
            }
        } catch (InterruptedException intE) {
            log.severe("Interrupted while getting immunization list from patient cache.");
        }
        return this.immuneList;
    }


    public JSONObject getProblemList() {
        try {
            while (!bannerFinished) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException intE) {
            log.severe("Interrupted while getting problem list from patient cache.");
        }
        return this.problemList;
    }

    public JSONObject getVitalsList() {
        try {
            while (!bannerFinished) {
                Thread.sleep(500);
            }
        } catch (InterruptedException intE) {
            log.severe("Interrupted while getting vitals list from patient cache.");
        }
        return this.vitalsList;
    }

    public void setAlertList(JSONObject alertList) {
        this.alertList = alertList;
    }

    public void setFamilyHistory(JSONObject familyHistory) {
        this.familyHistory = familyHistory;
    }

    public void setImages(JSONObject images) {
        this.images = images;
    }

  /*  public void setMedicalHistory(JSONObject medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
*/
    public void setMedicineList(JSONObject medicineList) {
        this.medicineList = medicineList;
    }

    public void setImmuneList(JSONObject immuneList) {
        this.immuneList = immuneList;
    }
	 public void setSupportList(JSONObject supportList) {
		  this.supportList= supportList;
	 }
	 public JSONObject getSupportList() {
	    try {
            while (!bannerFinished) {
                Thread.sleep(500);
            }
        } catch (InterruptedException intE) {
            log.severe("Interrupted while getting vitals list from patient cache.");
        }
		  return supportList;
	 }
	 public void setEncounterList(JSONObject encounterList) {
		  this.encounterList= encounterList;
	 }
	 public JSONObject getEncounterList() {
	    try {
            while (!finished) {
                Thread.sleep(500);
            }
        } catch (InterruptedException intE) {
            log.severe("Interrupted while getting vitals list from patient cache.");
        }

		  return encounterList;
	 }
    public void setProblemList(JSONObject problemList) {
        this.problemList = problemList;
    }

    public void setVitalsList(JSONObject vitalsList) {
        this.vitalsList = vitalsList;
    }

    public String getDatabasePatientId() {
        return this.databasePatientId;
    }

    public void setDatabasePatientId(String databasePatientId) {
        this.databasePatientId = databasePatientId;
    }
    public void setHistoryCategory(String category, JSONObject historyObject) {
			historyMap.put(category, historyObject);
    }
    public JSONObject getHistoryCategory(String category) {
       try {
            while (!bannerFinished) {
                Thread.sleep(500);
            }
        } catch (InterruptedException intE) {
            log.severe("Interrupted while getting vitals list from patient cache.");
        }
		  return historyMap.get(category);
    }

    public JSONObject getRepositories() {
        return this.repositories;
    }

    public void setRepository(JSONObject repositories) {
        this.repositories = repositories;
    }

    public void setPatientList(JSONObject patientList) {
        this.patientList = patientList;
    }
    public String getPrimaryRepos() {
		   return primaryRepos;
    }

    public JSONObject getPatientList() {  
    try {
            while (!bannerFinished) {
                Thread.sleep(500);
            }
        } catch (InterruptedException intE) {
            log.severe("Interrupted while getting patient data from patient cache.");
        }
        return this.patientList;
    }

    public String getRepoPatientId(String repositoryName) {
        //return this.repoPatientId;
        String patientRepId = "";
        JSONArray repArray;
        try {
            repArray = repositories.getJSONArray("repositories");
            if (repArray != null) {
                for (int i = 0; i < repArray.length(); i++) {
                    JSONObject rep = (JSONObject) repArray.get(i);
                    if (rep != null) {
                        String repName = rep.getString("repository");
                        if (repName.equals(repositoryName)) {
                            patientRepId = rep.getString("id");
                            ;
                        }
                    }
                }
            }
            return patientRepId;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            return "-1";
        }
    }

    public void setRepoPatientId(String repositoryName, String repositoryId) {
        //this.repoPatientId = repoPatientId;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setPhoto(String photo) {
    	  this.photo = photo;
    }
    public String getPhoto() {
    	  return photo;
    }
    //}}}

    private String computeAge(JSONObject patient) {
        String age = "";
        try {
        		JSONObject patientData = patient.getJSONObject("patient_data");
            JSONObject birthtime = patientData.getJSONObject("birthtime");
            GregorianCalendar cal = new GregorianCalendar();
            GregorianCalendar birthday = new GregorianCalendar(birthtime.getInt("year"), birthtime.getInt("month"), birthtime.getInt("day"));
            int diffyear = cal.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
            if (cal.get(Calendar.DAY_OF_YEAR) < birthday.get(Calendar.DAY_OF_YEAR)) {
                diffyear--;
            }
            if (diffyear > 1) {
                age = String.valueOf(diffyear) + " year old";
            } else {
                int diffmonths = cal.get(Calendar.MONTH) - birthday.get(Calendar.MONTH);
                if (diffmonths < 0) {
                    diffmonths = diffmonths + 12;
                }
                diffmonths = diffmonths + diffyear * 12;
                if (cal.get(Calendar.DATE) < birthday.get(Calendar.DATE)) {
                    diffmonths--;
                }
                if (diffmonths > 1) {
                    age = String.valueOf(diffmonths) + " month old";
                } else {
                    int diffdays = cal.get(Calendar.DAY_OF_YEAR) - birthday.get(Calendar.DAY_OF_YEAR);
                    if (diffdays < 0) {
                        if (birthday.get(Calendar.YEAR) != cal.get(Calendar.YEAR) && birthday.isLeapYear(birthday.get(Calendar.YEAR))) {
                            diffdays = diffdays + 366;
                        } else {
                            diffdays = diffdays + 365;
                        }

                    }
                    int diffweeks = diffdays / 7;
                    if (diffweeks > 1) {
                        age = String.valueOf(diffweeks) + " week old";
                    } else {

                        age = String.valueOf(diffdays) + " day old";
                        
                    }
                }
            }
        } catch (JSONException jsonE) {
            log.severe("Error getting birthdate in Patient Cache");
            return "";
        }
        return age;

    }
}
