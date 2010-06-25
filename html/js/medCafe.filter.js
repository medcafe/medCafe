var retry = true;

function filterInitialize(url, startDate, endDate, category)
{			
			setChecked(category);
			$.getJSON(url, function(data)
			{
  			
	  			var startHtml = v2js_listStartDates( data );  
	  			
	  			$("#valueAA").append(startHtml);
	    		var endHtml = v2js_listEndDates( data );  
	  			$("#valueBB").append(endHtml);
				
				$('select#valueAA').delay(1000,function()
				{
					$('select#valueAA, select#valueBB').selectToUISlider({
						labels: 12
					});
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
    
				    var url = "setFilter.jsp?start_date=" + startDate + "&end_date=" +endDate + "&categories=" + category;	
					//Make a call to setFilter
					$.get(url, function(data)
					{						  
						  //alert('Set Filter Date was run.');
						  triggerFilter(startDate, endDate, category);
				   
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
    
				    var  url = "setFilter.jsp?start_date=" + startDate + "&end_date=" +endDate + "&categories=" + category;
					
					//Make a call to setFilter
					$.get(url, function(data)
					{						  
						  //alert('Set Filter Date was run.');
						  triggerFilter(startDate, endDate, category);
				    
					});
					
				});
				
				//$("#aaa" + tab_num).jScrollTouch({height:"400",width:"400"});
			});
		
			
			$('#filter_button').click(function()
			{
 	
 					var comma="";
					category = "";
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
					
					var url = "setFilter.jsp?start_date=" + startDate + "&end_date=" +endDate + "&categories=" + category;
					
					$.get(url, function(data)
					{						  
						  //alert('Set Filter Date was run.');
						  triggerFilter(startDate, endDate, category);
					});
	 
			});
					
			$('#unfilter_button').click(function()
			{
				 	//for some reason cannot call trigger('FILTER_DATE') directly
				 	var category = "";

					$('.filter_checkbox').each(
						  
						  function() 
						  {    
						    $(this).attr('checked',false);
						  }
					);
					var url = "setFilter.jsp?start_date=" + startDate + "&end_date=" +endDate + "&categories=" + category;
					
					$.get(url, function(data)
					{						  
						  //alert('Set Filter Date was run.');
						  triggerFilter(startDate, endDate, category);
				 	
					});
			});
}

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
//Any javascript that needs to be run after page is loaded - though thi smay not be required
function processFilter(repId, patientId, patientRepId, data, type)
{
		//alert("medCafe.filter.js: processFilter. Start");
		
		var startDate =$("#filterStartDate").text();
		var endDate =$("#filterEndDate").text();
		var category =$("#categoryList").text();
		
		var url = "listDates.jsp?filter_start_date=" + startDate + "&filter_end_date=" + endDate;
		//alert("medCafe.filter.js processFilter startDate " + startDate + " endDate " + endDate + " category " + category);
		filterInitialize(url, startDate, endDate, category);
}