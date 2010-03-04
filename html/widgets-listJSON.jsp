<%@ page import="org.mitre.medcafe.util.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%

	String type = request.getParameter("type");
	
	if (type == null)
		type = Constants.GENERAL_WIDGETS;
		
	
	String listWidgets =   "/widgets";
	
	if (type.equals(Constants.PATIENT_WIDGETS))
		listWidgets = "c/widgets/patients";
			
%>

<tags:IncludeRestlet relurl="<%=listWidgets%>" mediatype="json"/>
