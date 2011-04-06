<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ page import="org.mitre.medcafe.util.*, org.mitre.medcafe.model.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import = "java.util.logging.Logger, java.util.logging.Level"%>
<%!
    public final static String KEY = "/historyJSON.jsp";
    public final static Logger log = Logger.getLogger( KEY );
    //static{log.setLevel(Level.FINER);}%>
<%

	log.finer("historyJSON: url start");
		String type = request.getParameter("type");

    PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    if( cache == null )
    {  //nobody is logged in
       // log.warning("No patient selected");
        response.sendRedirect("introPage.jsp");
        return;
    }



		out.print(cache.getHistoryCategory(type));


%>
