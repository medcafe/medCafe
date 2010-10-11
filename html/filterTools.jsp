<%@ page import="org.mitre.medcafe.util.*, org.mitre.medcafe.model.*, java.util.ArrayList " %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%
	MedCafeFilter filter = null;
	Object filterObj = session.getAttribute("filter");
		
	String startDate = request.getParameter("start_date");
	
	String endDate = request.getParameter("end_date");
	
	String filterStartDate ="";
	String filterEndDate ="";
	
	String intervalType = request.getParameter("interval_type");
	
	String categories = "";
	if (filterObj != null)
	{
		filter = (MedCafeFilter)filterObj;
		filterStartDate = filter.getStartDate();
		filterEndDate = filter.getEndDate();
		categories = filter.catToString();
		
		System.out.println("filterTools filter " + filter.toJSON());
		 
	}	
		
	String url = "listDates.jsp";
	String append = "?";
	
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
	System.out.println("filterTools url " + url);
%>  
    <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
	<head>
	<title>Category Filter</title>
	
	
	<style type="text/css">
		body {font-size: 62.5%; font-family:"Segoe UI","Helvetica Neue",Helvetica,Arial,sans-serif; }
		fieldset { border:0; margin: 2em; height: 10em;}	
		label {font-weight: normal; float: left; margin-right: .5em; font-size: 1.1em;}
		select {margin-right: 1em; float: left;}
		.ui-slider {clear: both; top: 5em;}
	</style>
	
	<script type="text/javascript">
	
		var startDate="<%=filterStartDate%>";
		var endDate = "<%=filterEndDate%>";
		var category = "<%=categories%>";
		var url = "<%=url%>";
		
		function getFilterStartDate()
		{
			return startDate;
		}	
		
		function getFilterEndDate()
		{
			return endDate;
		}
		
		function getFilterCategory()
		{
			return category;
		}
		
		function getFilterURL()
		{
			return url;
		}
		
		$(function(){
		
			var startDate="<%=filterStartDate%>";
			var endDate = "<%=filterEndDate%>";
			var category = "<%=categories%>";
			//filterInitialize("<%=url%>", startDate, endDate, category);

		});

	</script>
</head>

<body>
	<div id="holder">
	<div id="dateFilter" class="ui-widget ui-corner-all">
		<div style="height:180px;" class="ui-state-highlight ui-corner-all" style="padding: .7em;">
	
			<fieldset><label for="valueAA">From:</label><select name="valueAA" id="valueAA"></select>
			<label for="valueBB">To: </label><select name="valueBB" id="valueBB"></select></fieldset>
			
			<div id="filterStartDate" style="display:none"><%=filterStartDate%></div>
			<div id="filterEndDate" style="display:none"><%=filterEndDate%></div>
		</div>
		<br/>
		<div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
			
			<button value="Filter" id="slider_button">Filter</button>
			<button value="Remove Filter" id="slider_button_unfilter">Remove Filter</button>
			<br/>
			
		</div>
	</div>
	<br/>

	<div id="categoryFilter" class="ui-widget ui-corner-all">
		<div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
		
			
			<input type="checkbox" class="filter_checkbox" value="Smoker" id="smoking">Smoking</input>
			<br/>
			<input type="checkbox" class="filter_checkbox" value="NonSmoking" id="nonsmoking">Non Smoking</input>
			<br/>
			<br/>
			<button value="Filter" id="filter_button">Filter</button>
			<button value="Remove Filter" id="unfilter_button">Remove Filter</button>
			<div id="categoryList" style="display:none"><%=categories%></div>
		</div>
	</div>
	</div>
</body>
</html>
