<%@ page import="org.mitre.medcafe.util.*" %>
<%@ page import="org.mitre.medcafe.model.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%	
	String server = request.getParameter("server");
	if (server == null)
		server = "noServer";
		 
	String searchStrFirst = request.getParameter("search_str_first");
	if (searchStrFirst == null)
		searchStrFirst = "";
		
	String searchStrLast = request.getParameter("search_str_last");
	if (searchStrLast == null)
		searchStrLast = "";
	
	String isPatient = request.getParameter("isPatient");
	if (isPatient == null)
		isPatient = "false";
	String userName =  request.getRemoteUser();
	
	Patient patient = new Patient();%>
	<%=patient.searchJson(isPatient, searchStrFirst, searchStrLast, userName,server).toString()%>



