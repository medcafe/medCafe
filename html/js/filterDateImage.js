function filterDateImage(startDate, endDate, tab_num)
{

	//alert("filterDateImage : About to filter Images according to dates " + startDate + " " + endDate);
	jQuery.each($("#iframe" + tab_num), function() {
				
				var source = $(this).attr("src");
				var pos = source.indexOf("?");
				if (pos > 0)
				{
					source = source.substring(0,pos);
				}
				source = source + "?start_date=" + startDate + "&end_date=" + endDate;
				
				//source = "test.jsp";
				//$('#tabs').tabs('select', "#tabs-" + tab_num);
			
				
				$(this).attr("src", source);
				/*$(this).delay(500,function()
				{
					//document.getElementById("iframe" + tab_num).contentDocument.location.reload(true);
			
				});*/
	});
	
}
