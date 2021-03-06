<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ page import="org.mitre.medcafe.util.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%

	String patient_id = request.getParameter("patient_id");
	if (patient_id == null)
		patient_id = "1";
	String repository = request.getParameter("repository");
	if (repository == null)
		repository = Constants.DEFAULT_REPOSITORY;

	String jspUrl =  "/repositories/" + repository + "/patients/" + patient_id + "/bookmarks";
	String user =  request.getRemoteUser();
	jspUrl = jspUrl + "?user=" + user;
%>

<tags:IncludeRestlet relurl="<%=jspUrl%>" mediatype="json"/>
