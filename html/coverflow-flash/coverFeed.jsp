<%@ page import="org.restlet.data.*, org.restlet.representation.*, org.restlet.resource.*,org.json.*, java.io.*,org.mitre.medcafe.util.*" %><%@
    taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%
	String coverflowFile = "c/repositories/medcafe/patients/1/images";
	//String coverflowFile = "c/repositories/medcafe/patients/1/images";
	String startDate = request.getParameter("start_date");

	String endDate =  request.getParameter("end_date");

	String url = coverflowFile;

	String append = "?";
	if (startDate != null)
	{
			url += append + "start_date=" + startDate;
			append="&";
	}

	if (endDate != null)
	{
			url += append + "end_date=" + endDate;

	}
	System.out.println("coverFeed.jsp url " + url );
    url += "&type=link";

    ClientResource resource = new ClientResource( "http://127.0.0.1:8080/medcafe/" + url );
    // Prints the list of registered items.
    resource.get(MediaType.APPLICATION_JSON );
    StringWriter writer = new StringWriter();
    if (resource.getStatus().isSuccess() && resource.getResponseEntity().isAvailable()) {
        resource.getResponseEntity().write(writer);
    }
    JSONObject obj = new JSONObject( String.valueOf(writer));
    VelocityUtil.applyTemplate( obj, "listImages.vm", out);
%>

