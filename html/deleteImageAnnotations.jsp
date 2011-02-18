<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
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

	String userName =  request.getRemoteUser();
	String patient_id = request.getParameter("patient_id");

	PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    if( cache == null )
    {  //nobody is logged in
        //log.warning("No patient selected");
        response.sendRedirect("introPage.jsp");
        return;
    }
    patient_id = cache.getDatabasePatientId();

	String fileId = request.getParameter("file_id");
	String fileName = request.getParameter("image");

	System.out.println("deleteImageAnnotations.jsp about to Delete Annotations for patient " + patient_id  + " and file " + fileId + " image" + fileName);

	JSONObject rtnObj = ImageTag.deleteAnnotations(patient_id, userName, fileName);

%>
<%=rtnObj%>