<%@ page import="org.restlet.*, org.restlet.data.*, org.restlet.representation.*, org.restlet.resource.*,org.json.*, java.io.*,org.mitre.medcafe.util.*,org.mitre.medcafe.restlet.*" %><%@
    taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%

	String patientId = request.getParameter("patient_id");
	if (patientId == null)
		patientId = "1";
	// String coverflowFile = "/c/repositories/medcafe/patients/" +  patientId + "/images";
	//String coverflowFile = "c/repositories/medcafe/patients/1/images";
	String dates = request.getParameter("dates");

	String startDate ="";
	String endDate =  "";

	//As this url is part of a larger url, the '&' cannot be used
	//Use dates="start_date"_"end_date" format
	String[] startEndDate = null;
	if ( (dates != null) && (!dates.equals("_")))
	{

		if (dates.contains("_"))
		{
			startEndDate = dates.split("_");
			startDate = startEndDate[0];
			endDate = startEndDate[1];
		}
		else
		{
			startDate = dates;
		}
	}
	//System.out.println("coverFeed.jsp endDate " + endDate );
	// String url = coverflowFile;
	String url = "/repositories/medcafe/patients/" +  patientId + "/images";

	String append = "?";
	if ( (startDate != null) && (!startDate.equals("")))
	{
			url += append + "start_date=" + startDate;
			append="&";
	}

	if ((endDate != null) && (!endDate.equals("")))
	{
			url += append + "end_date=" + endDate;

	}
	//System.out.println("coverFeed.jsp url " + url );
    //url += "&type=link";

	String server = "http://" + Config.getServerUrl() +  url;
    //System.out.println("coverFeed.jsp server " + server );
    /*
    ClientResource resource = new ClientResource( server );
    // Prints the list of registered items.
    resource.get(MediaType.APPLICATION_JSON );
    StringWriter writer = new StringWriter();
    if (resource.getStatus().isSuccess() && resource.getResponseEntity().isAvailable()) {
        resource.getResponseEntity().write(writer);
    }
    */
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
    }

    try
    {
        JSONObject obj = new JSONObject( String.valueOf(writer));
        VelocityUtil.applyTemplate( obj, "listImages.vm", out);
    }
    catch(org.json.JSONException e)
    {
        System.out.println(  String.valueOf(writer) );
        throw e;
    }
%>

