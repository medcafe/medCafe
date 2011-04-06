<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ page import="org.mitre.medcafe.util.*,org.mitre.medcafe.model.*" %>
<%@ page import="java.util.logging.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %><%!
    public final static String KEY = "/repository-listJSON.jsp";
    public final static Logger log = Logger.getLogger( KEY );
    //static{log.setLevel(Level.FINER);}%><%
    PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    if( cache == null )
    {  //nobody is logged in
        log.warning("No patient selected");
        response.sendRedirect("introPage.jsp");
        return;
    }

	//String repository = cache.getRepository();
	String repository = request.getParameter("repository");
	//String patientId = cache.getRepoPatientId();
	String patientId = request.getParameter("rep_patient_id");
	String noCache = (String) request.getParameter("nocache");
	String listRep =  "/repositories/" + repository + "/patients";
	String cacheKey = (String) request.getParameter("cacheKey");
	if (repository == null)
	{
		//listRep += "/" + patientId;
		out.print(cache.retrieveObjectList("patientList"));
	}
	else
	{
	if (noCache.equals("true"))
		listRep += "/" + patientId+ "/singleRep/true";
	//System.out.println("repository-listJSON.jsp: list Rep  " + listRep);
%>

		<tags:IncludeRestlet relurl="<%=listRep%>" mediatype="json"/>
<%
	}

%>
