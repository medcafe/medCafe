<%@ page import="org.restlet.*, org.restlet.data.*, org.restlet.representation.*, org.restlet.resource.*,org.json.*, java.io.*,org.mitre.medcafe.util.*,org.mitre.medcafe.restlet.*, org.mitre.medcafe.model.*" %><%

	
	MedCafeFilter filter = null;
	Object filterObj = session.getAttribute("filter");

	String startDate="";
	String endDate = "";
	String filterCat ="";
	if (filterObj != null)
	{
		filter = (MedCafeFilter)filterObj;
		startDate = filter.getStartDate();
		endDate = filter.getEndDate();
		filterCat = filter.catToString();
		System.out.println("coverFeed.jsp filter " + filter.toJSON());
	}
	
	//check for a patientId in the session

	String patientId = request.getParameter("patient_id");
	PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    if( cache == null )
    {  //nobody is logged in
        //log.warning("No patient selected");
        response.sendRedirect("introPage.jsp");
        return;
    }
    
	String dates = null;
	String rep = "local";
	patientId = cache.getRepoPatientId(rep);
	String user =  request.getRemoteUser();
 	/*
	System.out.println("coverFeed.jsp patient_id " + patientId );
	// String url = coverflowFile;
	String url = "/repositories/" + rep +"/patients/" +  patientId + "/images";

	String append = "?";
	if ( (startDate != null) && (!startDate.equals("")))
	{
			url += append + "start_date=" + startDate;
			append="&";
	}

	if ((endDate != null) && (!endDate.equals("")))
	{
			url += append + "end_date=" + endDate;
			append="&";
	}

	System.out.println("coverFeed.jsp filterCat " + filterCat );
	if ((filterCat != null) && (!filterCat.equals("")))
	{
			url += append + "filter=" + filterCat;
			append="&";
	}

	//String user =  request.getRemoteUser();
	url += append + "user=" + user;

	System.out.println("coverFeed.jsp url " + url );
    //url += "&type=link";

	String server = "http://" + Config.getServerUrl() +  url;
    //System.out.println("coverFeed.jsp server " + server );
    MedcafeApplication app = (MedcafeApplication)application.getAttribute("org.restlet.ext.servlet.ServerServlet.application");
    if( app == null )
    {
        out.write("Could not connect to data restlets.");
        return;
    }
    Request req = new Request( Method.GET, url );
    Response resp = new Response( req );
    // request.setEntity(, mtype);
    app.handle(req, resp);
    StringWriter writer = new StringWriter();

    if (resp.getStatus().isSuccess() && resp.getEntity().isAvailable()) {
        resp.getEntity().write(writer);
    } */

    //try
   // {
    	  JSONObject obj = cache.getFilteredImages(startDate, endDate, filterCat, user);
    	 // System.out.println(obj.toString());
     // JSONObject obj = new JSONObject( String.valueOf(writer));
     //   System.out.println("Image Object: " + obj.toString());
     //   System.out.println(" Cache Object: " + cache.getImages().toString());
        StringWriter imageDivs = new StringWriter();
        VelocityUtil.applyTemplate( obj, "listImages.vm", imageDivs);
       // System.out.println(imageDivs.toString().replaceAll("<:prefix:>", "http://" + Config.getServerUrl() + "/images/patients/" + patientId + "/" ));
        
        out.write( imageDivs.toString().replaceAll("<:prefix:>", "http://" + Config.getServerUrl() + "/images/patients/" + patientId + "/" ) );
   // }
   // catch(org.json.JSONException e)
   // {
    //    System.out.println(  String.valueOf(writer) );
    //    throw e;
   // }
%>

