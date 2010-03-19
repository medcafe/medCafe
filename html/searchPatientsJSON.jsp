<%@ page import="org.mitre.medcafe.util.*" %>
<%@ page import="org.mitre.medcafe.model.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%	
	String searchStrFirst = request.getParameter("search_str_first");
	if (searchStrFirst == null)
		searchStrFirst = "";
		
	String searchStrLast = request.getParameter("search_str_last");
	if (searchStrLast == null)
		searchStrLast = "";
	
	Patient patient = new Patient();%>
	<%=patient.searchJson(searchStrFirst, searchStrLast).toString()%>



