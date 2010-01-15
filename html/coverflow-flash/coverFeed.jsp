<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%
	String coverflowFile = "c/treenode?relurl=/repositories/medcafe/patients/1/images";
	
	
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
%>
<?xml version="1.0" encoding="utf-8"?>
<covers xcurrent="1">
	<tags:IncludeRestlet relurl="<%=url%>"/>
</covers>
