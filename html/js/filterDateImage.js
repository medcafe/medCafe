function filterDateImage(startDate, endDate, tab_num)
{
	
	//alert("filterDateImage : About to filter Images according to dates " + startDate + " " + endDate);
	if (typeof filterImages != 'undefined')
	{
		filterImages("","","");
	}
	
	/*jQuery.each($("#iframe" + tab_num), function() {
				
				var source = $(this).attr("src");
				//http://127.0.0.1:8080/medcafe/coverflow-flash/index.jsp?start_date=02/6/2008&end_date=02/11/2008
				
				//alert("filterDateImage source " + source);
				var pos = source.indexOf("&start_date");
				if (pos > 0)
				{
					source = source.substring(0,pos);
				}
				else
				{
					pos = source.indexOf("&end_date");
					if (pos > 0)
					{
						source = source.substring(0,pos);
					}
				}
				source = source + "&start_date=" + startDate + "&end_date=" + endDate;
				
				//source = "test.jsp";
				//$('#tabs').tabs('select', "#tabs-" + tab_num);
			
				
				$(this).attr("src", source);
				/*$(this).delay(500,function()
				{
					//document.getElementById("iframe" + tab_num).contentDocument.location.reload(true);
			
				});*/
	//});
	
}
