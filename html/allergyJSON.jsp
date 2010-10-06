<%@ page import="org.mitre.medcafe.util.*, org.mitre.medcafe.model.*"  %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%

	System.out.println("prescriptionJSON: url start");

    PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    if( cache == null )
    {  //nobody is logged in

        response.sendRedirect("introPage.jsp");
        return;
    }


	
		out.print(cache.getAlertList());


%>
