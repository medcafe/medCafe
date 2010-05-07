<%@ page import="org.mitre.medcafe.util.*,org.mitre.medcafe.model.*" %>
<%@ page import="org.json.JSONObject" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%

	String user =  request.getRemoteUser();
	JSONObject recentPatients = Patient.getRecentPatients(user);
%>
<%=recentPatients.toString()%>
