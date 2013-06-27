<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ page import="org.mitre.medcafe.util.*, java.util.*, java.text.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import="org.mitre.medcafe.util.*" %>
<%@ page import="org.mitre.medcafe.model.*" %>
<%

	String patient_id = request.getParameter(Constants.PATIENT_ID);
	if (patient_id == null)
		patient_id = "1";
	String server = Config.getServerUrl() ;
		PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
		// String server = (String) session.getAttribute("server");

    if( cache == null )
    {  //nobody is logged in
        response.sendRedirect("introPage.jsp");
        return;
    }
	patient_id = cache.getDatabasePatientId();

	String user =  request.getRemoteUser();


	MedCafeFilter filter = null;
	GregorianCalendar cal = new GregorianCalendar();
	Date endDate = cal.getTime();

	cal.roll(Calendar.YEAR, -4);
	Date startDate= cal.getTime();

	Object filterObj = session.getAttribute("filter");
	if (filterObj != null)
	{
		filter = (MedCafeFilter)filterObj;
		SimpleDateFormat df = new SimpleDateFormat(MedCafeFilter.DATE_FORMAT);
		startDate = df.parse(filter.getStartDate(), new ParsePosition(0));
		endDate = df.parse(filter.getEndDate(), new ParsePosition(0));
	}

	String primary = "false";
	String[] eventArray  = request.getParameterValues("event");
	String[] events;

	if (eventArray == null)
		events=new String[]{};
	else
	{
		int index = -1;
		for (int i = 0; i< eventArray.length; i++)
		{
			if (eventArray[i].equals(Event.PRIMARY_REPOSITORY))
			{
				index = i;
				break;
			}
		}
		if (index != -1)
		{
			primary = "true";
			events = new String[eventArray.length - 1];
			int j= 0;
			for (int i = 0; i<eventArray.length; i++)
			{
				if (i != index)
				{
					events[j] = eventArray[i];
					j++;
				}
			}
		}
		else
		{
			events = eventArray;
		}
	}

	session.setAttribute("timelineEvents", events);


	out.print(Event.getJsonEventsFromCache(cache, primary, events, startDate, endDate, server, user));
	//System.out.println("listTimelineJSON.jsp jspUrl " + jspUrl);
%>
