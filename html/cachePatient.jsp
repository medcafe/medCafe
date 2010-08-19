<%@ page import="org.mitre.medcafe.util.*,java.util.*,org.mitre.medcafe.model.*" %><%

	String patientId = WebUtils.getRequiredParameter( request, "patient_id" );
    PatientCache cache = new PatientCache( patientId, application );
    Thread t = new Thread( cache );
    t.start();
    // cache.run();
    session.setAttribute(PatientCache.KEY, cache);
    System.out.println("cachePatient.jsp getting patient id " + patientId  );

    response.sendRedirect("index.jsp");
%>
