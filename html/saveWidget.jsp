<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import = "java.util.*"%>
<%@ page import = "java.net.URLEncoder"%>
<%@ page import = "java.io.UnsupportedEncodingException"%>
<%@ page import = "org.mitre.medcafe.util.*"%>
<%@ page import = "org.mitre.medcafe.model.*"%>
<%@ page import = "org.json.JSONObject" %>
<%@ page import = "org.json.JSONException" %>
<%
	//Web utils get the widgetSettings
	//Put the String to JSON data
	//Save the JSON Object to the database	
	System.out.println("saveWidget.jsp in Save Widget start " );
	Enumeration e = request.getParameterNames();
	/**
	key id value 1
	patient_id value 1
	server value http://127.0.0.1
	clickUrl value http://127.0.0.1:8080
	repository value OurVista
	type value images
	location value center
	tab_num value 1
	**/
	String userName =  request.getRemoteUser();
	String patientId = request.getParameter("patient_id");
	//System.out.println("PatientID XXX" + patientId + "XXX");
	if( patientId == null || patientId.equals("undefined"))
    {  //this should ONLY be the case when the "Save" button is used on the index page.
        PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
     	 // System.out.println("Cache check");
        if( cache == null )
        {  //nobody is logged in
            System.out.println("No patient selected");
            response.sendRedirect("introPage.jsp");
            return;
        }
        patientId = cache.getDatabasePatientId();
    }    
		if(e != null)
		{
	   	try
	   	{
	   	 
	   	   JSONObject jsonobj = new JSONObject();
	   	   while(e.hasMoreElements())
			  {
			     String key = "";
			  	 Object keyObj = e.nextElement();
			  	 if (keyObj != null)
			  	 	key = keyObj.toString();
			  	 	System.out.println("saveWidget.jsp key  " +key + " value " + request.getParameter(key));
	
				 	jsonobj.put( key, request.getParameter(key));
			  }
			 
			  jsonobj.put(Widget.ID, patientId);
			  System.out.println("saveWidget.jsp about to Save Widget for jsonObj  " +jsonobj.toString() );
	
			  Widget.saveWidgets(userName, jsonobj);
	   	 }
	   	 catch(JSONException je) {
				System.out.println("Error in creating JSON " + je.getMessage() );
	
	   	 }
  		}

	
%>
