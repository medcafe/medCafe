<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%
	String url = "listDates.jsp";
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
	<meta http-equiv="content-type"  name = "viewport" content = "user-scalable = no, width =device-width"/>
	<style>
	.scroll {
	  height:400px;
	  width:1000px;
	  overflow:auto;
	}
	</style>
	<title>Demo Page: Using Progressive Enhancement to Convert a Select Box Into an Accessible jQuery UI Slider</title>
	<script type="text/javascript" src="js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="js/ui.all-1.7.1.js"></script>
	<script type="text/javascript" src="js/selectToUISlider.jQuery.js"></script>
 	<script type="text/javascript" src="js/vel2js.js"></script>
    <script type="text/javascript" src="js/vel2jstools.js"></script>
    <link type="text/css" href="css/custom.css" rel="stylesheet" />
    
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
		
			$.getJSON("<%=url%>", function(data)
			{
  			
	  			var html = v2js_listDates( data );  
	  			
	  			$("#valueAA").append(html);
	    		$("#valueBB").append(html);
				
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
				 	//add a day to the date
				 	
				 	var pos = valueA.indexOf("/");
				 	startDate =  valueA.substring(0,pos) + "/01" +  valueA.substring(pos);
				 	
				 	
				 	var valueB = $('select#valueBB').val();
				 	pos = valueB.indexOf("/");
				 	endDate =  valueB.substring(0,pos) + "/01" +  valueB.substring(pos);
				 	
				    parent.triggerFilter(startDate, endDate);
				   
				});
			
				
				
				$('#slider_button_unfilter').click(function()
				 {
				 	//for some reason cannot call trigger('FILTER_DATE') directly
				 	var startDate = "";
				 	var endDate = "";
				 
				    parent.triggerFilter(startDate, endDate);
				   
				});
			});
		
			
		});
		
	</script>
</head>

<body>
	<div id="container" class="scroll">
		<fieldset><label for="valueAA">From:</label><select name="valueAA" id="valueAA"></select>
		<label for="valueBB">To: </label><select name="valueBB" id="valueBB"></select></fieldset>
	
		
		<button value="Filter" id="slider_button">Filter</button>
		<button value="Remove Filter" id="slider_button_unfilter">Remove Filter</button>
	</div>
</body>
</html>