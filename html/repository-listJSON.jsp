<%@ page import="org.mitre.medcafe.util.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%

	String patient_id = request.getParameter("patient_id");	
	String repository = request.getParameter("repository");	
			
	String listRep =  "/repositories";
	
	if (repository != null)
		listRep = listRep + "/" + repository + "/patients";
	
	if (patient_id != null)
		listRep = listRep + "/" + patient_id;
	
	System.out.println("repository-listJSON.jsp: list Rep  " + listRep);
%>

<tags:IncludeRestlet relurl="<%=listRep%>" mediatype="json"/>
