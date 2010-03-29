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
	//System.out.println("saveWidget.jsp in Save Widget start " );
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
	String patient_id = request.getParameter(Widget.ID);
		
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
			 	jsonobj.put( key, request.getParameter(key));
		  }
		  System.out.println("saveWidget.jsp about to Save Widget" );
	
		  Widget.saveWidgets(userName, jsonobj);
	    }
	    catch(JSONException je) {
			System.out.println("Error in creating JSON " + je.getMessage() );
	
	    }
  	}
	
	
%>