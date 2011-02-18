<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import = "java.util.*"%>
<%@ page import = "java.net.URLEncoder"%>
<%@ page import = "java.io.UnsupportedEncodingException"%>
<%@ page import = "org.mitre.medcafe.util.*, org.mitre.medcafe.model.*"%>
<%

	String formData = request.getParameter("form[info1]");

	System.out.println("SaveText.jsp: form data " + formData);

	String action = request.getParameter("action");

  	String user =  request.getRemoteUser();
  	//Use the user login to save the text
  	String patientId = null;
  	PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    if( cache == null )
    {  //nobody is logged in
        //log.warning("No patient selected");
        response.sendRedirect("introPage.jsp");
        return;
    }
    patientId = cache.getDatabasePatientId();

	String title = request.getParameter("title");

	TextProcesses textProcesses = new TextProcesses();
	if (action.equals("Save"))
		textProcesses.saveText(user, patientId, title, formData);
	else if (action.equals("Delete"))
		textProcesses.deleteText(user, patientId, title, formData);

	title = URLEncoder.encode(title,"UTF-8");
	System.out.println("SaveText.jsp: action " + action + " patient "  + patientId + " title " + title + " text " + formData);

	//response.sendRedirect("editor.jsp?" +  Constants.PATIENT_ID + "=" + patientId + "&title=" + title);
%>