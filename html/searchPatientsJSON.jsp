<%@ page import="org.mitre.medcafe.util.*" %>
<%@ page import="org.mitre.medcafe.model.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%	
	String searchStr = request.getParameter("search_str");
	if (searchStr == null)
		searchStr = "";
		
	String type = request.getParameter("type");
	if (type == null)
		type = Patient.LAST_NAME_TYPE;
		
	
	Patient patient = new Patient();%>
	<%=patient.searchJson(searchStr, type).toString()%>



