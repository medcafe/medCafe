<%@ page import="org.mitre.medcafe.util.*" %>
<%@ page import="org.mitre.medcafe.model.*" %>
<%@ page import="org.json.JSONObject, org.json.JSONArray" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%

	System.out.println("listHistoryTemplateJSON: url start");
	String jspUrl =  "/history/templates";

	String repositoryName = "local";
	String patientRepId = request.getParameter("patient_rep_id");
	JSONObject repositoryIds = new JSONObject();
	if (patientRepId == null)
	{
		PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
	    if( cache == null )
	    {  //nobody is logged in
	        //log.warning("No patient selected");
	        response.sendRedirect("introPage.jsp");
	        return;
	    }
	    repositoryIds = cache.getRepositories();
    
		System.out.println("listHistoryTemplateJSON: got repository ID  " + repositoryIds.toString() );
		JSONArray repArray = repositoryIds.getJSONArray("repositories");
		if (repArray != null)
		{
			for (int i= 0; i < repArray.length(); i++)
			{
				JSONObject rep = (JSONObject)repArray.get(i);
				if (rep != null)
				{
					String repName = rep.getString("repository");
					if (repName.equals(repositoryName))
					{
							patientRepId =  rep.getString("id");;
					}
				}
			}
		}
		
	}
	
	System.out.println("listHistoryTemplateJSON: got patient rep Id " + patientRepId );
	
	String patientId = null;
	
	Object patientObj = session.getAttribute("patient");
	System.out.println("listHistoryTemplateJSON: got patient from session object " + patientObj );
	
	if (patientObj != null)
		 	patientId = patientObj.toString();
	
	if (patientId != null)
		 jspUrl =  "/history/templates/patients/" + patientRepId;
	
	String user =  request.getRemoteUser();
	jspUrl = jspUrl + "?user=" + user;
	
	System.out.println("listHistoryTemplateJSON: url " + jspUrl);
%>

<tags:IncludeRestlet relurl="<%=jspUrl%>" mediatype="json"/>



