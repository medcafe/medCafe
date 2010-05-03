function filterCategoryImage(filter, tab_num)
{

	jQuery.each($("#iframe" + tab_num), function() {				
				var source = $(this).attr("src");
				
				var pos = source.indexOf("?");
				var append = "?";
				
				if (pos > 0)
				{
					append ="&";
				}
				var posFilter = source.indexOf("filterCat=");
				if (posFilter > 0)
				{
					source = source + "," + filter;
				}
				else
				{
					source = source + append + "filterCat=" + filter;	
				}		
				$(this).attr("src", source);
				
	});
	
}
