<%@ page import="org.mitre.medcafe.util.*" %>
<%@ page import="org.mitre.medcafe.model.*" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="java.util.HashMap" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%	
	String patientId = request.getParameter(Constants.PATIENT_ID);
	if( patientId == null || patientId.equals("undefined"))
    { 
        PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
     	 // System.out.println("Cache check");
        if( cache == null )
        {  //nobody is logged in
            System.out.println("No patient selected");
            response.sendRedirect("introPage.jsp");
            return;
        }
        patientId = cache.getDatabasePatientId();
        System.out.println(patientId);
    }
	
	String templateId = request.getParameter("template_id");
	if (templateId == null)
	{	
		return;
	}
		
	System.out.println("copyTemplate.jsp getting patient id " + patientId );
	
	String user_name =  request.getRemoteUser();
	//Retrieve Widgets
	
	JSONObject widgets =  Template.retrieveTemplateCopy(templateId, patientId, user_name);
	

%>