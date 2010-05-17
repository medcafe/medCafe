function filterCategoryImage(filter, tab_num)
{

	jQuery.each($("#iframe" + tab_num), function() {	
				
				var source = $(this).attr("src");
				
				var pos = source.indexOf("?");
				var append = "?";
	
				//If there is no filter specified then this is the UNFILTER event
				if (filter == "")
				{
						var posFilter = source.indexOf("filterCat=");
						if (posFilter > 0)
						{
							source = source.substring(0,posFilter);
						}
						
				}	
				else
				{		
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
				
				}
				$(this).attr("src", source);
				
	});
	
}
