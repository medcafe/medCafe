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
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<title>Category Filter</title>
	<script type="text/javascript" src="js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="js/ui.all-1.7.1.js"></script>
	<script type="text/javascript" src="js/selectToUISlider.jQuery.js"></script>
 	<script type="text/javascript" src="js/vel2js.js"></script>
    <script type="text/javascript" src="js/vel2jstools.js"></script>
    
	<link type="text/css" href="css/custom-theme/jquery-ui-1.7.2.custom.css" rel="stylesheet" />	
	<link rel="Stylesheet" href="css/ui.slider.extras.css" type="text/css" />
	<style type="text/css">
		body {font-size: 62.5%; font-family:"Segoe UI","Helvetica Neue",Helvetica,Arial,sans-serif; }
		fieldset { border:0; margin: 2em; height: 10em;}	
		label {font-weight: normal; float: left; margin-right: .5em; font-size: 1.1em;}
		select {margin-right: 1em; float: left;}
		.ui-slider {clear: both; top: 5em;}
	</style>
	
	<script type="text/javascript">
		$(function(){
		
			var startDate="<%=filterStartDate%>";
			var endDate = "<%=filterEndDate%>";
			var category = "<%=categories%>";
			setChecked(category);
			
			$.getJSON("<%=url%>", function(data)
			{
  			
	  			var startHtml = v2js_listStartDates( data );  
	  			
	  			$("#valueAA").append(startHtml);
	    		var endHtml = v2js_listEndDates( data );  
	  			$("#valueBB").append(endHtml);
				
				$('select#valueAA, select#valueBB').selectToUISlider({
					labels: 12
				});
				
				$('#slider_button').click(function()
				 {
				 	//for some reason cannot call trigger('FILTER_DATE') directly
				    startDate = "02/02/2008";
				 	endDate = "10/02/2010";
				 	//var values = $('select#valueAA').slider('option','values');
				 	var valueA = $('select#valueAA').val();
				 	//add a day to the date
				 	
				 	var pos = valueA.indexOf("/");
				 	startDate =  valueA.substring(0,pos) + "/01" +  valueA.substring(pos);

				 	var valueB = $('select#valueBB').val();
				 	pos = valueB.indexOf("/");
				 	endDate =  valueB.substring(0,pos) + "/01" +  valueB.substring(pos);
				 	
				    parent.triggerFilter(startDate, endDate);
				   
				    var url = "setFilter.jsp?start_date=" + startDate + "&end_date=" +endDate + "&categories=" + category;	
					//Make a call to setFilter
					$.get(url, function(data)
					{						  
						  //alert('Set Filter Date was run.');
					});
				});
			
				
				
				$('#slider_button_unfilter').click(function()
				 {
				 	//for some reason cannot call trigger('FILTER_DATE') directly
				 	startDate = "";
				 	endDate = "";
				 
				 	$('select#valueBB').val("02/02/2008");
				 	$('select#valueAA').val("02/02/2008");
				 	/*selects.bind('change keyup click', function(){
					var thisIndex = jQuery(this).get(0).selectedIndex;
					var thisHandle = jQuery('#handle_'+ jQuery(this).attr('id'));
					var handleIndex = thisHandle.data('handleNum');
					thisHandle.parents('.ui-slider:eq(0)').slider("values", handleIndex, thisIndex);
					});*/
					
				 	var defIndex = 0;
					var startHandleIndex  = $('#handle_valueAA').data('handleNum');
					var endHandleIndex  = $('#handle_valueBB').data('handleNum');
					$('#handle_valueAA').parents('.ui-slider:eq(0)').slider("values", startHandleIndex, defIndex);
					$('#handle_valueBB').parents('.ui-slider:eq(0)').slider("values", endHandleIndex, defIndex);
					
				    parent.triggerFilter(startDate, endDate);
				    
				    var url = "setFilter.jsp?start_date=" + startDate + "&end_date=" +endDate;	
					//Make a call to setFilter
					$.get(url, function(data)
					{						  
						  //alert('Set Filter Date was run.');
					});
					
				});
			});
		
		
			$('#filter_button').click(function()
			{
 	
 					var comma="";
					var category = "";
				    $('.filter_checkbox').each(
						  
						  function() 
						  {
						  	
						  	if ( $(this).attr('checked') )
						  	{
						   		var id = $(this).attr('id');
								category = category + comma +  $('#' + id).val();
								comma=",";
							}
						  }
					);
					//alert("listCategory.jsp: category filter " + category);
					parent.triggerFilterCategory(category);
					var url = "setFilter.jsp?start_date=" + startDate + "&end_date=" +endDate + "&categories=" + category;
					
					$.get(url, function(data)
					{						  
						  //alert('Set Filter Date was run.');
					});
				 
			});
					
			$('#unfilter_button').click(function()
			{
				 	//for some reason cannot call trigger('FILTER_DATE') directly
				 	var category = "";

					parent.triggerFilterCategory(category);
				 	var url = "setFilter.jsp?start_date=" + startDate + "&end_date=" +endDate + "&categories=" + category;
					
					$.get(url, function(data)
					{						  
						  //alert('Set Filter Date was run.');
					});
			});
		});

		function setChecked(categories)
		{
					
			var catList = categories.split(",");
			
			for(i = 0; i < catList.length; i++)
			{
				$('#categoryFilter').find(".filter_checkbox").each(function() {
			
					var filterVal = $(this).val();
					
					if (filterVal == catList[i])
					{
						$(this).attr("checked","true");
					}
				});
			}
		}		
	</script>
</head>

<body>
	<div id="dateFilter" class="ui-widget ui-corner-all">
		<div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
	
			<fieldset><label for="valueAA">From:</label><select name="valueAA" id="valueAA"></select>
			<label for="valueBB">To: </label><select name="valueBB" id="valueBB"></select></fieldset>
			<button value="Filter" id="slider_button">Filter</button>
			<button value="Remove Filter" id="slider_button_unfilter">Remove Filter</button>
			<br/>
			
		</div>
	</div>
	<br/>
	
	
	<div id="categoryFilter" class="ui-widget ui-corner-all">
		<div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
		
			
			<input type="checkbox" class="filter_checkbox" value="Smoking" id="smoking">Smoking</input>
			<br/>
			<input type="checkbox" class="filter_checkbox" value="NonSmoking" id="nonsmoking">Non Smoking</input>
			<br/>
			<br/>
			<button value="Filter" id="filter_button">Filter</button>
			<button value="Remove Filter" id="unfilter_button">Remove Filter</button>
		</div>
	</div>
</body>
</html>