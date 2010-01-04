<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%
	String patientId = request.getParameter("patient_id");
	if (patientId == null)
		patientId = "1";
%>
<tags:IncludeRestlet relurl="c/repositories/medcafe/patients/<%=patientId%>/charts/temperature" mediatype="json"/>
