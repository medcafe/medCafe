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
    <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
	<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<title>Demo Page: Using Progressive Enhancement to Convert a Select Box Into an Accessible jQuery UI Slider</title>
	<script type="text/javascript" src="js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="js/ui.all-1.7.1.js"></script>
	<script type="text/javascript" src="js/selectToUISlider.jQuery.js"></script>

	<link type="text/css" href="css/custom-theme/jquery-ui-1.7.2.custom.css" rel="stylesheet" />	
	<link rel="Stylesheet" href="css/ui.slider.extras.css" type="text/css" />
	<style type="text/css">
		body {font-size: 62.5%; font-family:"Segoe UI","Helvetica Neue",Helvetica,Arial,sans-serif; }
		fieldset { border:0; margin: 6em; height: 12em;}	
		label {font-weight: normal; float: left; margin-right: .5em; font-size: 1.1em;}
		select {margin-right: 1em; float: left;}
		.ui-slider {clear: both; top: 5em;}
	</style>
	<script type="text/javascript">
		$(function(){
			$('select#valueAA, select#valueBB').selectToUISlider({
				labels: 12
			});
			
			$('#slider_button').click(function()
			 {
			 	//for some reason cannot call trigger('FILTER_DATE') directly
			 	var startDate = "02/02/2008";
			 	var endDate = "02/02/2009";
			 	//var values = $('select#valueAA').slider('option','values');
			 	var valueA = $('select#valueAA').val();
			 	startDate = "02/" + valueA;
			 	var valueB = $('select#valueBB').val();
			 	endDate = "02/" + valueB;
			 	
			    parent.triggerFilter(startDate, endDate);
			   
			});
		});
		
	</script>
</head>

<body>
	<tags:IncludeRestlet relurl="<%=url%>"/>
	<button value="Filter" id="slider_button">Filter</button>
</body>
</html>