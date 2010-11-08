<%@ page import="org.mitre.medcafe.util.*,java.util.*,org.mitre.medcafe.model.*" %><%

	String patientId = WebUtils.getRequiredParameter( request, "patient_id" );
	String primaryRepos = WebUtils.getRequiredParameter(request, "repository");
	String primarySet = (String) session.getAttribute("primaryRepository");
	String isIntroPage = null;

	isIntroPage= request.getParameter("isIntro");
	if (isIntroPage == null)
		isIntroPage = "false";
	if (primarySet == null || isIntroPage != "false")
		session.setAttribute("primaryRepository", primaryRepos);
	else
		primaryRepos = primarySet;
   // String primaryRepos = "OurVista";
   System.out.println("creating new PatientCache");
    PatientCache cache = new PatientCache( patientId, application, primaryRepos );
    Thread t = new Thread( cache );
    System.out.println("Thread started");
    t.start();
    // cache.run();
    session.setAttribute(PatientCache.KEY, cache);
    System.out.println("cachePatient.jsp getting patient id " + patientId  );

    response.sendRedirect("index.jsp");
%>
