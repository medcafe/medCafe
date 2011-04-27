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
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
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
         log.setLevel(Level.SEVERE);
    }
    //{{{ Members
    protected String databasePatientId = null;
    protected javax.servlet.ServletContext application = null;
    public final static String NA = "Resource not available";
    protected JSONObject repositories = null;
    //protected String repoPatientId = null;
    protected String firstName = null;
    protected String lastName = null;
    protected String photo = null;
    protected boolean[] finished = {false, false, false};
    private String primaryRepos = "";
    private String primaryReposId = "";
    protected HashMap<String, JSONObject> vitalsMap = null;
    protected HashMap<String, JSONObject> patientDataHash = null;
    protected HashMap<String, Integer> dataSourcePriorityMap = null;
    protected HashMap<String, MedCafeDataSource> dataSourceLookup = null;

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
            loadRepositoryInfo();

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
            DatabaseUtility.close(rs);
            conn.close();
            rs = null;
            conn = null;
        }
    }

    public void loadRepositoryInfo() {
        //get repository/local IDs
        //String query = "select * from patient where id=?";
        Patient patient = new Patient();
        JSONObject repositoryList = patient.listRepositories(databasePatientId);
        ArrayList<JSONObject> repos = new ArrayList<JSONObject>();
        try {
            JSONArray reps = new JSONArray();
            reps = repositoryList.getJSONArray("repositories");
            int length = reps.length();
            for (int i = 0; i < length; i++) {

                /* cycle through to put primary repository first in the list  */
                JSONObject repository = reps.getJSONObject(i);

                String repositoryName = repository.getString("repository");
                
                if (repositoryName.equals(primaryRepos)) {
                	  primaryReposId = repository.getString("id");
                    repos.add(0, repository);
                } else {
                    repos.add(repository);
                }
            }
            repositories = new JSONObject();
            for (JSONObject repository : repos) {
                repositories.append("repositories", repository);
            }
            log.finer(repositories.toString());
        } catch (JSONException jsonE) {
            log.severe("PatientCache: Error accessing JSON Array to order repository");
        }
        log.finer("PatientCache loadRepositoryInfo JSONObject " + repositories.toString());

    }

    public void run() {
        log.entering(KEY, "run()");
        dataSourcePriorityMap = new HashMap<String, Integer>();
			dataSourceLookup = new HashMap<String, MedCafeDataSource>();
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
            log.finer("PatientCache run repository JSONObject " + repositories.toString());

            reps = repositories.getJSONArray("repositories");
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            log.throwing(KEY, "constructor", e1);
            repositories = WebUtils.buildErrorJson("Problem retrieving list of repositories for patient." + e1.getMessage());
        }

        if (repositories.has("repositories")) {
            try {
                PriorityQueue<MedCafeDataSource> dataSourceQueue = MedCafeDataSource.retrieveDataSource(MedCafeDataSource.PATIENT_CACHE);
                patientDataHash = new HashMap<String, JSONObject>();
                int prevPriority = 0;
                while (dataSourceQueue.size() > 0) {

                    MedCafeDataSource currentSource = dataSourceQueue.poll();
                    dataSourceLookup.put(currentSource.getCacheKey(), currentSource);
                    log.finer("Starting retrieval of " + currentSource.getCacheKey());
                    while (prevPriority < currentSource.getPriority()) {
                        finished[prevPriority] = true;
                        prevPriority++;
                    }




                    if (currentSource.getRestlet().contains("{historyCategory}")) {
                        JSONObject categories = History.getHistoryCategories();
                        try {
                            JSONArray categoryArray = categories.getJSONArray("history_categories");
                            for (int k = 0; k < categoryArray.length(); k++) {
                                String histRestletString = new String(currentSource.getRestlet());
                                histRestletString = histRestletString.replaceAll("\\{historyCategory\\}", categoryArray.getString(k));
                                String historyCacheKey = new String(currentSource.getCacheKey());
                                historyCacheKey = historyCacheKey.replaceAll("\\{historyCategory\\}", categoryArray.getString(k));
                                putJSONListObject(app, histRestletString, reps, historyCacheKey);

                                dataSourcePriorityMap.put(historyCacheKey, currentSource.getPriority());
                            }
                        } catch (JSONException jsonE) {
                            log.severe("Error retrieving history categories: " + jsonE.getMessage());
                        }
                    } else {
                        putJSONListObject(app, currentSource.getRestlet(), reps, currentSource.getCacheKey());
                        dataSourcePriorityMap.put(currentSource.getCacheKey(), currentSource.getPriority());
                    }
                }



            } catch (ParseException parseE) {
                log.severe("Error parsing DataSource.xml file: " + parseE.getMessage());

            }
            catch (SQLException sqlE) {
            	log.severe("Error accessing DataSource.xml file: " + sqlE.getMessage());
            }
            catch (ParserConfigurationException configE) {
            	log.severe("Error parsing DataSource.xml file: " + configE.getMessage());
            }
            catch (SAXException saxE) {
            	log.severe("Error parsing DataSource.xml file: " + saxE.getMessage());
            }
            catch (IOException ioE) {
            	log.severe("IO exception accessing DataSource.xml file: " + ioE.getMessage());
            }


        }
		 for (int i = 0; i < finished.length; i++)
       	finished[i] = true;

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

			//log.severe("Response " + resp.getStatus() + " " + resp.getEntity());
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
    public JSONObject retrieveObjectList(String cacheKey) {
    int objectPriority;
    try{
        objectPriority = dataSourcePriorityMap.get(cacheKey);
	 }
	 catch (NullPointerException nullE)
	 {
	 	objectPriority = 2;
	 }

        int counter = 0;


        try {
            while (!finished[objectPriority] && counter < 20) {
                Thread.sleep(500);
                counter++;

            }


        } catch (InterruptedException intE) {
            log.severe("Interrupted while getting " + cacheKey + " from patient cache.");


        }

        JSONObject ret = patientDataHash.get(cacheKey);


        if (ret == null) {
        		ret = WebUtils.buildErrorJson("Timed out retrieving " + cacheKey + " from source.");
        		log.finer("PatientCache retrieve Objects - " + cacheKey + " ret is:" + ret.toString());
            return ret;

        } else {
        		log.finer("PatientCache retrieve Objects - " + cacheKey + ":  " + ret.toString());
            return ret;


        }

    }

    public String getDatabasePatientId() {
        return this.databasePatientId;


    }
    public String getPrimaryReposId() {
    	return primaryReposId;
    }

    public void setDatabasePatientId(String databasePatientId) {
        this.databasePatientId = databasePatientId;


    }

    public JSONObject getRepositories() {
        return this.repositories;


    }

    public void setRepository(JSONObject repositories) {
        this.repositories = repositories;


    }

    public String getPrimaryRepos() {
        return primaryRepos;


    }

    public String getRepoPatientId(String repositoryName) {
        //return this.repoPatientId;
        String patientRepId = "";
        JSONArray repArray;


        try {
            repArray = repositories.getJSONArray("repositories");


            if (repArray != null) {
                for (int i = 0; i
                        < repArray.length(); i++) {
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
    public String getDataSourceRestlet(String cacheKey)
    {
    	return dataSourceLookup.get(cacheKey).getRestlet();
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


    } //}}}

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

    public JSONObject getVitalDataForChart(String vitalType) {
        JSONObject dataObj;




        try {
            synchronized (this) {
                if (vitalsMap == null) {

                    vitalsMap = new HashMap<String, JSONObject>();
                    JSONArray repositoryArray = retrieveObjectList("allVitalsList").getJSONArray("repositoryList");


                    boolean noVitals = true;


                    for (int i = 0; i
                            < repositoryArray.length(); i++) {
                        String repos = repositoryArray.getJSONObject(i).getString("repository");


                        if (repos.equals(primaryRepos) && repositoryArray.getJSONObject(i).has("vitals")) {
                            JSONArray vitalArray = repositoryArray.getJSONObject(i).getJSONArray("vitals");



                            for (int j = 0; j
                                    < vitalArray.length(); j++) {
                                noVitals = false;
                                String resultType = vitalArray.getJSONObject(j).getJSONObject("resultType").getString("value");

                                JSONObject result = new JSONObject();
                                JSONArray arrayObj = new JSONArray();
                                JSONObject dateRange = vitalArray.getJSONObject(j).getJSONObject("resultDateTime");
                                JSONObject dateObj = dateRange.getJSONObject("low");


                                int mon, day, year, hour, minute;
                                mon = dateObj.getInt("month");
                                day = dateObj.getInt("day");
                                year = dateObj.getInt("year");
                                hour = dateObj.getInt("hour");
                                minute = dateObj.getInt("minute");
                                GregorianCalendar cal = new GregorianCalendar(year, mon - 1, day, hour, minute);
                                arrayObj.put(cal.getTimeInMillis());
                                String resultString = vitalArray.getJSONObject(j).getString("resultValue");
                                String unit = "";


                                if (!resultType.equals("B/P")) {
                                    String[] splitResults = resultString.split(" ");


                                    double resultVal = Double.parseDouble(splitResults[0]);
                                    arrayObj.put(resultVal);


                                    if (splitResults.length > 1) {
                                        unit = splitResults[1];


                                    }
                                    addVitalsToMap(resultType, unit, arrayObj);


                                } else {
                                    String[] splitResults = resultString.split("/");


                                    double systolic = Double.parseDouble(splitResults[0]);


                                    double diastolic = Double.parseDouble(splitResults[1]);
                                    arrayObj.put(systolic);
                                    addVitalsToMap(
                                            "Systolic", unit, arrayObj);
                                    arrayObj = new JSONArray();
                                    arrayObj.put(cal.getTimeInMillis());
                                    arrayObj.put(diastolic);
                                    addVitalsToMap(
                                            "Diastolic", unit, arrayObj);


                                }


                            }
                        }
                    }
                    if (noVitals) {
                        vitalsMap.put("Error", WebUtils.buildErrorJson("There are no vitals currently listed for this patient"));


                    }

                }

            }
            dataObj = vitalsMap.get(vitalType);


            if (dataObj == null) {
                dataObj = vitalsMap.get("Error");


            }
        } catch (JSONException jsonE) {
            vitalsMap = null;
            dataObj = WebUtils.buildErrorJson("Error retrieving JSON for vitals: " + jsonE.getMessage());


        }
        return dataObj;


    }

    private void addVitalsToMap(String resultType, String unit, JSONArray arrayObj) throws JSONException {
        try {
            JSONObject obj = vitalsMap.get(resultType);



            if (obj == null) {
                obj = new JSONObject();
                obj.put("label", resultType);


                if (!unit.equals("")) {
                    obj.put("unit", unit);


                }
                vitalsMap.put(resultType, obj);



            }
            obj.append("data", arrayObj);
            log.finer("Patient Cache: addVitalsToMap " + obj.toString());


        } catch (JSONException jsonE) {
            throw jsonE;


        }
    }

    private void putJSONListObject(MedcafeApplication app, String restlet, JSONArray reps, String cacheKey) {
        JSONObject objectList = null;


        try {

            objectList = new JSONObject();


            for (int i = 0; i
                    < reps.length(); i++) {
                JSONObject repObj = (JSONObject) reps.get(i);
                String repository = repObj.getString("repository");
                String repoPatientId = repObj.getString("id");
                String restletString = new String(restlet);
                restletString = restletString.replaceAll("\\{repository\\}", repository);
                restletString = restletString.replaceAll("\\{id\\}", repoPatientId);
                log.finer(restlet + " " + restletString + cacheKey);
                String results = getJsonContent(app, restletString);
                JSONObject dataObject = new JSONObject(results);


                if (dataObject.has("patient_data")) {
                    String age = computeAge(dataObject);
                    dataObject.put("age", age);


                }
                dataObject.put("repository", repository);
                objectList.append("repositoryList", dataObject);


            }
           
        } catch (JSONException e) {
            log.throwing(KEY, "constructor", e);
            objectList = WebUtils.buildErrorJson("Problem retrieving " + cacheKey + " from source." + e.getMessage());


        }

        log.finer("Done retrieving " + cacheKey + " list");
        log.finer(objectList.toString());


        patientDataHash.put(cacheKey, objectList);

    }
}
