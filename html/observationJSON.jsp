<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ page import="org.mitre.medcafe.util.*, org.json.*, org.mitre.medcafe.model.*, org.mitre.medcafe.hl7utils.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%
		  String type = request.getParameter("type");
		  PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    if( cache == null )
    {  //nobody is logged in
       // log.warning("No patient selected");
        response.sendRedirect("introPage.jsp");
        return;
    }

        System.out.println("observationJson type " + type);

        SortObservations sortObs = new SortObservations();
       
        JSONObject jsonObj = cache.retrieveObjectList("observationVitals");
        JSONObject rtnObj = sortObs.processObservations(jsonObj);
        out.print(jsonObj);
       
%>
