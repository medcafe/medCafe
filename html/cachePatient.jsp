<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ page import="org.mitre.medcafe.util.*,java.util.*,org.mitre.medcafe.model.*" %>
<%@ page import = "java.util.logging.Logger, java.util.logging.Level"%><%!
    public final static String KEY = "/cachePatient.jsp";
    public final static Logger log = Logger.getLogger( KEY );
    //static{log.setLevel(Level.FINER);}%><%

	String patientId = WebUtils.getRequiredParameter( request, "patient_id" );
	String primaryRepos = WebUtils.getRequiredParameter(request, "repository");
	String primarySet = (String) session.getAttribute("primaryRepository");
	String isIntroPage = null;

	isIntroPage= request.getParameter("isIntro");
	if (isIntroPage == null)
		isIntroPage = "false";
	if (primarySet == null || isIntroPage != "false")
		session.setAttribute("primaryRepository", primaryRepos);
	else
		primaryRepos = primarySet;
   // String primaryRepos = "OurVista";
   log.finer("cachePatient.jsp: creating new PatientCache");
    PatientCache cache = new PatientCache( patientId, application, primaryRepos );
    Thread t = new Thread( cache );
    log.finer("cachePatient.jsp: Thread started");
    t.start();
    // cache.run();
    session.setAttribute(PatientCache.KEY, cache);
    log.finer("cachePatient.jsp getting patient id " + patientId  );

    response.sendRedirect("index.jsp");
%>
