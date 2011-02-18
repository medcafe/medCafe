<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import = "java.util.*"%>
<%@ page import = "java.net.URLEncoder"%>
<%@ page import = "java.io.UnsupportedEncodingException"%>
<%@ page import = "org.mitre.medcafe.util.*, org.mitre.medcafe.model.*"%>
<%
	//String formData = request.getParameter("form[info1]");
	String action = request.getParameter("action");


  	String user =  request.getRemoteUser();
  	//Use the user login to save the text
  	String patientId =null;
  	PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    if( cache == null )
    {  //nobody is logged in
        //log.warning("No patient selected");
        response.sendRedirect("introPage.jsp");
        return;
    }
    patientId = cache.getDatabasePatientId();

	System.out.println("SaveBookmarks.jsp: patient id " + patientId);

	ArrayList<Bookmark> bookmarks = new ArrayList<Bookmark>();
	Bookmark bookmark = null;
	for (int i=0; i < 100; i++)
	{
		System.out.println("SaveBookmarks.jsp: IN LOOP  " + i);

		String name = request.getParameter("name" + i);
		if (name == null)
		{
			break;
		}
		String url = request.getParameter("url" + i);
		String desc = request.getParameter("desc" + i);
		bookmark = new Bookmark(name, url, desc);
		bookmarks.add(bookmark);

		System.out.println("saveBookmarks.jsp values : name " + name + " url " + url + " desc " + desc);

	}

	Bookmark.updateBookmarks(user, patientId, bookmarks);

	System.out.println("saveBookmarks.jsp done ");


%>