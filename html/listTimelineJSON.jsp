<%@ page import="org.mitre.medcafe.util.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%

	String patient_id = request.getParameter("patient_id");
	if (patient_id == null)
		patient_id = "1";
	
	String jspUrl =  "/repositories/OurVista/patients/" + patient_id + "/events";
	//c/repositories/OurVista/patients/1/events

%>

<tags:IncludeRestlet relurl="<%=jspUrl%>" mediatype="json"/>
