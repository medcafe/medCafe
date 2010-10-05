<%@ page import="org.mitre.medcafe.util.*,java.util.*,org.mitre.medcafe.model.*" %><%

	String patientId = WebUtils.getRequiredParameter( request, "patient_id" );
	String primaryRepos = WebUtils.getRequiredParameter(request, "repository");
   // String primaryRepos = "OurVista";
    PatientCache cache = new PatientCache( patientId, application, primaryRepos );
    Thread t = new Thread( cache );
    t.start();
    // cache.run();
    session.setAttribute(PatientCache.KEY, cache);
    System.out.println("cachePatient.jsp getting patient id " + patientId  );

    response.sendRedirect("index.jsp");
%>
