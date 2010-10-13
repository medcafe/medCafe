<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%
	  PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    if( cache == null )
    {  //nobody is logged in
       // log.warning("No patient selected");
        response.sendRedirect("introPage.jsp");
        return;
    }


	
		out.print(cache.getVitalDataForChart("temperature"));


%>
