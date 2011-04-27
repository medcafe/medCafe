<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ page import="org.mitre.medcafe.util.*,java.util.*,org.mitre.medcafe.model.*" %>
<%@ page import = "java.util.logging.Logger, java.util.logging.Level"%><%!
    public final static String KEY = "/refreshPatient.jsp";
    public final static Logger log = Logger.getLogger( KEY );
    //static{log.setLevel(Level.FINER);}%><%

	PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
	String primaryRepos = cache.getPrimaryRepos();
	String patientId = cache.getDatabasePatientId();
   log.finer("refreshPatient.jsp: creating new PatientCache");
    cache = new PatientCache( patientId, application, primaryRepos );
    Thread t = new Thread( cache );
    log.finer("refreshPatient.jsp: Thread started");
    t.start();
    // cache.run();
    session.setAttribute(PatientCache.KEY, cache);
    log.finer("refreshPatient.jsp getting patient id " + patientId  );

    response.sendRedirect("index.jsp");
%>
