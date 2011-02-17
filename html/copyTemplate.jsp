<%@ page import="org.mitre.medcafe.util.*" %>
<%@ page import="org.mitre.medcafe.model.*" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="java.util.HashMap" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%	
	String action = request.getParameter("action");
	if (action == null)
		action = "CopyTemplate";
	System.out.println("copyTemplate.jsp getting action " + action );
	
	String patientId = null;
	
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
    
	
	String templateId = request.getParameter("template_id");
	if (templateId == null)
	{	
		return;
	}
	
	String description = request.getParameter("description");
	if (description == null)
	{	
		description="";
	}	
	System.out.println("copyTemplate.jsp getting patient id " + patientId );
	
	String user_name =  request.getRemoteUser();
	//Retrieve Widgets
	JSONObject rtnObj = null;
	if (action.equals("CopyTemplate"))
		rtnObj =  Template.retrieveTemplateCopy(templateId, patientId, user_name);
	else
		rtnObj = Template.copyToTemplate(patientId,user_name,templateId,description);
		
%>