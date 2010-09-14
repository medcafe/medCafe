/* Copyright 2010 The MITRE Corporation.  ALL RIGHTS RESERVED. */
package org.mitre.medcafe.model;

import java.sql.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        log.setLevel(Level.FINER);
    }
    //{{{ Members
    protected JSONObject medicalHistory = null;
    protected JSONObject familyHistory = null;
    protected JSONObject medicineList = null;
    protected JSONObject alertList = null;
    protected JSONObject problemList = null;
    protected JSONObject vitalsList = null;
    protected JSONObject images = null;
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

    //}}}
    //{{{ Constuctors
    public PatientCache(String databasePatientId, javax.servlet.ServletContext application) {

        this.databasePatientId = databasePatientId;
        this.application = application;
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
        repositories = patient.listRepositories(databasePatientId);
        //System.out.println("PatientCache loadRepositoryInfo JSONObject " + repositories.toString());

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
                    patientList.append("patient", patient);
                }
                //System.out.println("PatientCache generating patient list line 141 JSONObject " + patientList.toString());
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
                    vitalsList.append("vitalRecords", vitals);
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
                    medicineList.append("medicines", meds);
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
                    alertList.append("alerts", alerts);
                }
                //System.out.println("PatientCache generating alert/allergy list line 175 JSONObject " + alertList.toString());

            } catch (JSONException e) {
                log.throwing(KEY, "constructor", e);
                alertList = WebUtils.buildErrorJson("Problem retrieving alert list from source." + e.getMessage());
            }
            log.finer("Done retrieving alert list");
            log.finer(alertList.toString());
            try {
                images = new JSONObject();
                for (int i = 0; i < reps.length(); i++) {
                    JSONObject repObj = (JSONObject) reps.get(i);
                    String repository = repObj.getString("repository");
                    String repoPatientId = repObj.getString("id");

                    results = getJsonContent(app, "/repositories/" + repository + "/patients/" + repoPatientId + "/images");

                    JSONObject imageObj = new JSONObject(results);
                    imageObj.put("repository", repository);
                    images.append("imageList", imageObj);
                }
                //System.out.println("PatientCache generating image list line 198 JSONObject " + images.toString());

            } catch (JSONException e) {
                log.throwing(KEY, "constructor", e);
                images = WebUtils.buildErrorJson("Problem retrieving image list from source." + e.getMessage());
            }
            log.finer("Done retrieving image list");
            log.finer(images.toString());
            try {
                medicalHistory = new JSONObject();
                for (int i = 0; i < reps.length(); i++) {
                    JSONObject repObj = (JSONObject) reps.get(i);
                    String repository = repObj.getString("repository");
                    String repoPatientId = repObj.getString("id");

                    results = getJsonContent(app, "/repositories/" + repository + "/patients/" + repoPatientId + "/history");

                    try {
                        JSONObject history = new JSONObject(results);
                        history.put("repository", repository);
                        medicalHistory.append("medicalHistory", history);
                    } catch (JSONException e) {
                        log.log(Level.SEVERE, "Error converting the results to JSON.  Raw string was [" + results + "]", e);
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
                    problemList.append("problems", problems);
                }
                //System.out.println("PatientCache generating problem list line 239 JSONObject " + problemList.toString());

            } catch (JSONException e) {
                log.throwing(KEY, "constructor", e);
                problemList = WebUtils.buildErrorJson("Problem retrieving problem list from source." + e.getMessage());
            }
            log.finer("Done retrieving problem list");
            //log.finer(problemList.toString());
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
        mediaTypes.add(new Preference(MediaType.APPLICATION_JSON, 1.0F));
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
            while (!finished) {
                Thread.sleep(500);
            }
        } catch (InterruptedException intE) {
            log.severe("Interrupted while getting allergies from patient cache.");
        }
        return this.alertList;
    }

    public JSONObject getFamilyHistory() {
        try {
            while (!finished) {
                Thread.sleep(500);
            }
        } catch (InterruptedException intE) {
            log.severe("Interrupted while getting family history from patient cache.");
        }
        return this.familyHistory;
    }

    public JSONObject getImages() {
        try {
            while (!finished) {
                Thread.sleep(500);
            }
        } catch (InterruptedException intE) {
            log.severe("Interrupted while getting images from patient cache.");
        }
        return this.images;
    }

    public JSONObject getMedicalHistory() {
        try {
            while (!finished) {
                Thread.sleep(500);
            }
        } catch (InterruptedException intE) {
            log.severe("Interrupted while getting medical history from patient cache.");
        }
        return this.medicalHistory;
    }

    public JSONObject getMedicineList() {
        try {
            while (!finished) {
                Thread.sleep(500);
            }
        } catch (InterruptedException intE) {
            log.severe("Interrupted while getting medicine list from patient cache.");
        }
        return this.medicineList;
    }

    public JSONObject getProblemList() {
        try {
            while (!finished) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException intE) {
            log.severe("Interrupted while getting problem list from patient cache.");
        }
        return this.problemList;
    }

    public JSONObject getVitalsList() {
        try {
            while (!finished) {
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

    public void setMedicalHistory(JSONObject medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public void setMedicineList(JSONObject medicineList) {
        this.medicineList = medicineList;
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

    public JSONObject getRepositories() {
        return this.repositories;
    }

    public void setRepository(JSONObject repositories) {
        this.repositories = repositories;
    }

    public void setPatientList(JSONObject patientList) {
        this.patientList = patientList;
    }

    public JSONObject getPatientList() {  
    try {
            while (!finished) {
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
