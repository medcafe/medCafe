<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %><%@
    page import = "org.mitre.medcafe.util.*"%><%@
    page import = "org.mitre.medcafe.model.*"%><%

	String userName =  request.getRemoteUser();
	String patientId = request.getParameter("patient_id");
	//System.out.println("PatientID XXX" + patientId + "XXX");
	if( patientId == null || patientId.equals("undefined"))
    {  //this should ONLY be the case when the "Save" button is used on the index page.
        PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
     	 // System.out.println("Cache check");
        if( cache == null )
        {  //nobody is logged in
            System.out.println("No patient selected");
            response.sendRedirect("introPage.jsp");
            return;
        }
        patientId = cache.getDatabasePatientId();
        System.out.println(patientId);
    }

	System.out.println("deleteWidget.jsp about to Delete Widget for patient " + patientId );

	Widget.deleteWidgets(patientId, userName);

%>
