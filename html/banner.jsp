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
                <img alt="Patient photo" src="images/patients/photo_1.jpg" ></img>
            </p>
        </div>
    </div>
    <div class="ui-widget top-panel" id="patient_bio">
        <div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
            <p>
                <table border="0">
                 <%VelocityUtil.applyTemplate(cache.getPatientList(), "listPatientBio.vm", out); %>
                 <%VelocityUtil.applyTemplate(cache.getVitalsList(), "listPatientVitals.vm", out); %>
                </table>
            </p>
        </div>
    </div>
    <div class="ui-widget top-panel" id="patient_history">
        <div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
            <p><strong>Past Medical History</strong>
            <div id="listPatientHistory"></div>
            </p>
        	</div>
    </div>
    <div class="ui-widget top-panel" id="meds_list">
        <div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
            <p><strong>Medicine List</strong>
            <%VelocityUtil.applyTemplate(cache.getMedicineList(), "listMedicineList.vm", out); %>
           <!--> <br/>Repaglinide<br/>Ibuprofin<br/>Hydrochlorothiazide <-->  
             </p>
        </div>
    </div>

     <div class="ui-widget top-panel">
        <div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
            <p><strong>Problem List</strong>
    
            <%VelocityUtil.applyTemplate(cache.getProblemList(), "listProblemList.vm", out);%>
      		</p>
            <!-->br/>Heart Disease<br/>Colon Cancer<br/>Smoking<br/>Alcohol Abuse</p-->
        </div>
    </div>
    <div class="ui-widget top-panel" id="family_history">
        <div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
            <p><strong>Family/Social History</strong>
            <div id="listFamilyHistory"></div>
            <!-->br/>Heart Disease<br/>Colon Cancer<br/>Smoking<br/>Alcohol Abuse</p-->
        </div>
    </div>
    <div class="ui-widget top-panel" id="allergies_list">
        <div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
            <p><strong>Allergies/Alerts</strong>
            	<%VelocityUtil.applyTemplate(cache.getAlertList(), "listAlertList.vm", out); %>
            </p>
        </div>
    </div>


