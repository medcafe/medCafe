function filterDateImages(startDate, endDate, tab_num)
{
	jQuery.each($("#iframe" + tab_num), function() {
				
				var source = $(this).attr("src");
				
				source = source + "?start_date=" + startDate + "&endDate=" + endDate;
				//source = "test.jsp";
				//$('#tabs').tabs('select', "#tabs-" + tab_num);
			
				$(this).attr("src", source);
				/*$(this).delay(500,function()
				{
					//document.getElementById("iframe" + tab_num).contentDocument.location.reload(true);
			
				});*/
	});
	
}
