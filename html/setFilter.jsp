<%@ page import="org.mitre.medcafe.util.*, org.mitre.medcafe.model.*, java.util.ArrayList " %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
 "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<%

	System.out.println("setFilter.jsp start ");
	String startDate = request.getParameter("start_date");
	
	String endDate = request.getParameter("end_date");
	
	String categories = request.getParameter("categories");
	System.out.println("setFilter.jsp categories " + categories);
	
	MedCafeFilter filter = null;
	Object filterObj = session.getAttribute("filter");
	if (filterObj != null)
	{
		filter = (MedCafeFilter)filterObj;
	}	
	else
	{	
		filter = new MedCafeFilter();
		
	}
	if (startDate != null)
		filter.setStartDate(startDate);
	if (endDate != null)
		filter.setEndDate(endDate);
		
	ArrayList<String> catArray = new ArrayList<String>();
	if (categories != null)
	{
			String[] categoryList = categories.split(",");
			if (categoryList.length > 0)
			{
				for (String cat: categoryList)
				{
					catArray.add(cat);
				}
			}
	}
	filter.setCategories(catArray);
	session.setAttribute("filter", filter);
	System.out.println("setFilter.jsp filter being set  " + filter.toJSON());
	
%>

