<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%
	String url = "/dates/";
	String append = "?";
	String startDate = request.getParameter("start_date");
	String filterStartDate = request.getParameter("filter_start_date");
	
	String endDate = request.getParameter("end_date");
	String filterEndDate = request.getParameter("filter_end_date");
	
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
	
	if (filterStartDate != null)
	{
			url += append + "filter_start_date=" + filterStartDate;
			append= "&";
	}
	
	if (filterEndDate != null)
	{
			url += append + "filter_end_date=" + filterEndDate;
			append= "&";
	}
	
	if (intervalType != null)
	{
			url += append + "interval_type=" + intervalType;
	}
	//System.out.println("listDates url " + url);
%>    
<tags:IncludeRestlet relurl="<%=url%>" mediatype="json" />

