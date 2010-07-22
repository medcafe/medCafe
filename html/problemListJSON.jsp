<%@ page import="org.mitre.medcafe.util.*, org.mitre.medcafe.model.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%

	System.out.println("problemListJSON: url start");
	String patient_id = request.getParameter(Constants.PATIENT_ID);
	if (patient_id == null)
		patient_id = Constants.DEFAULT_PATIENT;
	String repository = Constants.DEFAULT_REPOSITORY;
	PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    if( cache == null )
    {  //nobody is logged in
        response.sendRedirect("introPage.jsp");
        return;
    }
	
	String[] repositories = request.getParameterValues("repository");
	if (repositories != null)
	{
		repository = repositories[0];
		if (repository == null)
			repository = Constants.DEFAULT_REPOSITORY;
		patient_id = cache.getRepoPatientId(repository);
	}
	
	String jspUrl =  "/repositories/" + repository + "/patients/" + patient_id + "/problems";
	
	String user =  request.getRemoteUser();
	jspUrl = jspUrl + "?user=" + user;
	
	System.out.println("problemListJSON: url " + jspUrl);
%>

<tags:IncludeRestlet relurl="<%=jspUrl%>" mediatype="json"/>
