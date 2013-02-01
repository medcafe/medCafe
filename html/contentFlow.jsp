<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ page import="org.mitre.medcafe.util.*, org.mitre.medcafe.model.*" %>
<%@ page import = "java.util.logging.Logger, java.util.logging.Level"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%!
    public final static String KEY = "/contentFlow.jsp";
    public final static Logger log = Logger.getLogger( KEY );
    //static{log.setLevel(Level.FINER);}%><%
	String server = "" ;
	MedCafeFilter filter = null;
	Object filterObj = session.getAttribute("filter");

	String patientId = request.getParameter(Constants.PATIENT_ID);
	if (patientId == null)
		patientId = "1";

	String append = "~";
	String delim = "=";
	//coverFeed.jsp?filter=dates=<start_date>_<end_date>~filter=filter1,filter2
	String coverflowFile = "contentflow/coverFeed.jsp?patient_id"  + delim + patientId;
	String url = coverflowFile;
	String startDate = request.getParameter("start_date");
	String endDate = request.getParameter("end_date");
	String filterCat = request.getParameter("filterCat");
	//System.out.println("coverflow flash: index.jsp filterCat from params " +  filterCat );

	if (filterObj != null)
	{
		filter = (MedCafeFilter)filterObj;
		startDate = filter.getStartDate();
		endDate = filter.getEndDate();
		filterCat = filter.catToString();
		log.finer("contentflow/index.jsp filter " + filter.toJSON());
	}

	if (startDate != null)
			coverflowFile += append + "dates" +  delim  + startDate;
	if (endDate != null)
			coverflowFile +=  "_" + endDate;

	if ( (filterCat != null) && (!filterCat.equals("")) )
			coverflowFile +=  "~filterCat"+  delim  + filterCat;

	//System.out.println("contentflow flash: index.jsp startDate " +  startDate + " endDate " + endDate );
    //System.out.println("contentflow flash: index.jsp url " +  coverflowFile );

	//coverflowFile = "coverFeedGen.xml";
	//coverflowFile = "http://127.0.0.1:8080/medcafe/c/repositories/medcafe/patients/1/images";
%>
<html>
    <head>
        <script tyle="text/javascript">
           // var cf = new ContentFlow('contentFlow', {reflectionColor: "#000000"});
        </script>
    </head>
    <body>
        <div id="contentFlow" class="ContentFlow">
            <!-- should be place before flow so that contained images will be loaded first -->
            <div class="loadIndicator"><div class="indicator"></div></div>

            <div class="flow" id="flowFile">

            </div>
            <div class="globalCaption"></div>
            <div class="scrollbar">
                <div class="slider"><div class="position"></div></div>
            </div>

			<div style="visibility:hidden" id="cfStartDate"><%=startDate%></div>
			<div style="display :none;" id="cfEndDate"><%=endDate%></div>
			<div style="display :none;" id="cfFilterCat"><%=filterCat%></div>

        </div>
    </body>
</html>
