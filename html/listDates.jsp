<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%
	String url = "c/dates/";
	String append = "?";
	String startDate = request.getParameter("start_date");
	
	String endDate = request.getParameter("end_date");
	
	String intervalType = request.getParameter("interval_type");
	
	if (startDate != null)
	{
			url += append + "start_date=" + startDate;
			append= "&";
	}
	
	if (endDate != null)
	{
			url += append + "end_date=" + endDate;
			append= "&";
	}
	
	if (intervalType != null)
	{
			url += append + "interval_type=" + intervalType;
	}
	
%>    
<tags:IncludeRestlet relurl="<%=url%>" mediatype="json" />

