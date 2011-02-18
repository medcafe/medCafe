<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>

<%@ page import="org.mitre.medcafe.util.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%

	String type = request.getParameter("type");

	if (type == null)
		type = Constants.GENERAL_WIDGETS;


	String listWidgets =   "/widgets";

	if (type.equals(Constants.PATIENT_WIDGETS))
		listWidgets = "/widgets/patients";

%>

<tags:IncludeRestlet relurl="<%=listWidgets%>" mediatype="json" restVerb="GET"/>
