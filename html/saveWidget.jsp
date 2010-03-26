<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import = "java.util.*"%>
<%@ page import = "java.net.URLEncoder"%>
<%@ page import = "java.io.UnsupportedEncodingException"%>
<%@ page import = "org.mitre.medcafe.util.*"%>
<%@ page import = "org.json.JSONObject" %>
<%@ page import = "org.json.JSONException" %>
<%
	//Web utils get the widgetSettings
	//Put the String to JSON data
	//Save the JSON Object to the database	
	System.out.println("saveWidget.jsp in Save Widget start " );
	Enumeration e = request.getParameterNames();
	
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
	    }
	    catch(JSONException je) {
			System.out.println("Error in creating JSON " + je.getMessage() );
	
	    }
  	}
	
%>