<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ page import="org.mitre.medcafe.util.*, org.mitre.medcafe.model.*"  %>
<%@ page import = "java.util.logging.Logger, java.util.logging.Level"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %><%!
    public final static String KEY = "/defaultJSON.jsp";
    public final static Logger log = Logger.getLogger( KEY );
    //static{log.setLevel(Level.FINER);}%>
<%

	log.finer("defaultJSON: url start");
	String cacheKey = request.getParameter("cacheKey");
    PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    if( cache == null )
    {  //nobody is logged in

        response.sendRedirect("introPage.jsp");
        return;
    }



		out.print(cache.retrieveObjectList(cacheKey));


%>
