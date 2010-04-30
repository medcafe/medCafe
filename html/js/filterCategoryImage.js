function filterCategoryImage(filter, tab_num)
{
	jQuery.each($("#iframe" + tab_num), function() {
				
				var source = $(this).attr("src");
				var pos = source.indexOf("?");
				if (pos > 0)
				{
					source = source.substring(0,pos);
				}
				source = source + "&filterCat=" + filter;
				
				//source = "test.jsp";
				//$('#tabs').tabs('select', "#tabs-" + tab_num);
			
				
				$(this).attr("src", source);
				/*$(this).delay(500,function()
				{
					//document.getElementById("iframe" + tab_num).contentDocument.location.reload(true);
			
				});*/
	});
	
}
