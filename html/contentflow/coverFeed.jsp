<%@ page import="org.restlet.*, org.restlet.data.*, org.restlet.representation.*, org.restlet.resource.*,org.json.*, java.io.*,org.mitre.medcafe.util.*,org.mitre.medcafe.restlet.*" %><%

	//url will look like this
	//?filter=dates:<start_date>_<end_date>~filter:filter1,filter2,....

	// String coverflowFile = "/c/repositories/medcafe/patients/" +  patientId + "/images";
	//String coverflowFile = "c/repositories/medcafe/patients/1/images";


	String delim ="=";

	String filterParam = request.getParameter("filter");
	System.out.println("coverFeed.jsp filterParams " + filterParam);
	String[] params = null;
	String patientId = null;

	if (filterParam != null)
		params = filterParam.split("~");

	System.out.println("coverFeed.jsp Number of filterParams " + params.length);

	String patientIdStr = params[0];
	if (patientIdStr.indexOf("patient_id" + delim) > -1)
	{
		patientId = patientIdStr.split(delim)[1];
		System.out.println("coverFeed.jsp Patient Id  " + patientId);

	}
	if (patientId == null)
	{
	    //check for a patientId in the session
        patientId = (String)session.getAttribute("patient");
	    if( patientId == null )
        {
            patientId = "5";
        }
	}

	String dates = null;
	String startDate ="";
	String endDate =  "";

	//As this url is part of a larger url, the '&' cannot be used
	//Use dates="start_date"_"end_date" format
	String[] startEndDate = null;

	String[] filterCats = null;
	String filterCat = null;

	if (params != null)
	{
		if ((params.length) > 2 )
		{
			String param1 = params[1];

			if (param1.indexOf("dates" + delim) > -1)
			{
				dates = param1.split(delim)[1];
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
			}
			else
			{
				String filter = "";
				if (param1.indexOf("filterCat" + delim) > -1)
				{
					filterCat = param1.split(delim)[1];

				}
			}

			String param2 = params[2];
			if (param2.indexOf("dates" + delim) > -1)
			{
				dates = param2.split(delim)[1];
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
			}
			else
			{
				String filter = "";
				if (param2.indexOf("filterCat" + delim) > -1)
				{
					filterCat = param2.split(delim)[1];
					System.out.println("coverFeed.jsp filterCategory " + filterCat);

				}
			}
		}
		else if ((params.length) > 1 )
		{
			String param1 = params[1];

			if (param1.indexOf("dates" + delim) > -1)
			{
				dates = param1.split(delim)[1];
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

			}
			else
			{
				String filter = "";
				if (param1.indexOf("filterCat" + delim) > -1)
				{
					filterCat = param1.split(delim)[1];
					System.out.println("coverFeed.jsp filterCategory " + filterCat);

				}
			}
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
			append="&";
	}

	if ((filterCat != null) && (!filterCat.equals("")))
	{
			url += append + "filter=" + filterCat;
			append="&";
	}

	String user =  request.getRemoteUser();
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
    }

    try
    {
        JSONObject obj = new JSONObject( String.valueOf(writer));
        StringWriter imageDivs = new StringWriter();
        VelocityUtil.applyTemplate( obj, "listImages.vm", imageDivs);
        out.write( imageDivs.toString().replaceAll("<:prefix:>", "http://127.0.0.1:8080/medcafe/images/patients/" + patientId + "/" ) );
    }
    catch(org.json.JSONException e)
    {
        System.out.println(  String.valueOf(writer) );
        throw e;
    }
%>

