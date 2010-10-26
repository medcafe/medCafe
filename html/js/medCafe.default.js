function addDefaultTable(callObj, widgetInfo, data)
{
	
	// creates the widget display inside an iNettuts container
	var html = v2js_inettutsHead(widgetInfo) +window["v2js_" + widgetInfo.template](data) + v2js_inettutsTail(widgetInfo);

// check for invalid tab and column numbers and use defaults if they don't exist;
	if (!widgetInfo.tab_num)
		widgetInfo.tab_num = "1";
	if (!widgetInfo.column)
		widgetInfo.column = "1";
	// attach widget to page at correct tab and column location
	$("#tabs-" + widgetInfo.tab_num + " #column" + widgetInfo.column).append(html);	
	
	$(callObj).delay(500, function ()    //attach handlers for more/less functionality
													 // if data exists from additional repositories
	{ 
		$('#collapseInfo' + widgetInfo.id).find('a.collapse').toggle(function () {
			$(this).text("Less");
     		$(this).css({backgroundPosition: '-52px 0'});
        	$(this).parent().find("#add_repos" +widgetInfo.id).show();
     		return false;
   	},function () {
   		$(this).text("More");
        	$(this).css({backgroundPosition: ''});
        	$(this).parent().find("#add_repos" +widgetInfo.id).hide();
        	return false;
   	});
   });
}
