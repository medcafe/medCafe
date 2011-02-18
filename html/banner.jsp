<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ page import="org.mitre.medcafe.util.*" %>
<%@ page import="org.mitre.medcafe.model.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="org.json.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%
	String patientId = request.getParameter("patient_id");
	if (patientId == null)
		patientId = "1";
	PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    if( cache == null )
    {  //nobody is logged in
        response.sendRedirect("introPage.jsp");
        return;
    }
%>



    <div class="ui-widget top-panel" style="width:100px;padding:0px;text-align:center;">
        <div class="ui-state-highlight ui-corner-all" style="padding: 0em;">
            <p>
                <img alt="Patient photo" src="images/patients/<%=cache.getPhoto()%>" ></img>
            </p>
        </div>
    </div>
    <div class="ui-widget top-panel" id="patient_bio">
        <div class="ui-state-highlight ui-corner-all" style="padding: .7em;">

                <table border="0">
                 <%VelocityUtil.applyTemplate(cache.retrieveObjectList("patientList"), "listPatientBio.vm", out); %>
                 <%VelocityUtil.applyTemplate(cache.retrieveObjectList("vitalsList"), "listPatientVitals.vm", out); %>
                </table>

        </div>
    </div>
<!--    <div class="ui-widget top-panel" id="patient_history">
        <div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
            <p><strong>Past Medical History</strong>
            <div id="listPatientHistory"></div>
            </p>
        	</div>
    </div>
-->
    <div class="ui-widget top-panel" id="meds_list">
        <div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
            <p><strong>Medicine List</strong>
            <%VelocityUtil.applyTemplate(cache.retrieveObjectList("medicineList"), "listMedicineList.vm", out); %>
           <!-- <br/>Repaglinide<br/>Ibuprofin<br/>Hydrochlorothiazide -->
             </p>
        </div>
    </div>

     <div class="ui-widget top-panel">
        <div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
           <strong>Problem List</strong>

            <%VelocityUtil.applyTemplate(cache.retrieveObjectList("problemList"), "listProblemList.vm", out);%>


        </div>
    </div>
<!--
    <div class="ui-widget top-panel" id="family_history">
        <div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
            <p><strong>Family/Social History</strong>
            <div id="listFamilyHistory"></div>

        </div>
    </div>
-->
    <div class="ui-widget top-panel" id="allergies_list">
        <div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
            <p><strong>Allergies/Alerts</strong>
            	<%VelocityUtil.applyTemplate(cache.retrieveObjectList("alertList"), "listAlertList.vm", out); %>
            </p>
        </div>
    </div>


