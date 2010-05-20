var tabName = "south-tabs";
var southTabs;
$(document).ready( function() {

		var tabSelectedId;

		$('#south-tabs').load('links.jsp', function(){
		
			$('#south-tabs' ).tabs();
			initSouthClose();
		});
			
});

function initSouthClose() 
{
					
	//$('#' + tabName + ' ul li .tabHeader').find('.close').bind("click",{},
	$('#' + tabName + " ul .tabHeader").each(function(i)
	{
		$(this).prepend("<div class='close'></div>");
	});
					
	$('#' + tabName).find('ul').find('li').find('.close').bind("click",{},
		function(e){
				var index = $(this).parent().attr('custom:southIndex');
				var id = $(this).parent().attr('id');
				var tab_num = id.substring(5,6);
	
				closeSouthTab(index, tab_num);
		});

}

function closeSouthTab(index, tabNum)
{
		$("#" + tabName).tabs("remove",index);
							
		var newIndex = index -1;
		if (newIndex < 0)
		{
			newIndex = 0;
		}
		//Cycle through the other tabs and reindex based on new order
		var count = 0;
		$("#" + tabName).find("li:has(a)").each(function(i)
		{

			var aObj = $(this).find('a');
			$(this).attr('custom:southIndex', count);
			var id = $(this).attr('id');''
			count = count + 1;

		});
		$("#" + tabName).tabs('select', newIndex);
}

function addSouthTabs()
{
}

function addSouthTab(title, url)
{
		var count = 0;
		//Reset the indexes of all the new tabs
		$("#" + tabName).find("li:has(a)").each(function(i)
		{
			count = count + 1;
	    });
	   		
	   	$("#" + tabName).tabs("add","#south-tabs-" + count,title);
		var li_obj;
	   	count = -1;
	   	$("#" + tabName).find("li:has(a)").each(function(i)
		{
			count = count + 1;
			var aObj = $(this).find('a');
			$(this).attr('custom:southIndex', count);
			
			li_obj = $(this);
		});
				
		$(li_obj).addClass("tabHeader");
		$(li_obj).prepend("<div class='close'></div>");

		var addFrameHtml = "<iframe src ='"+ url + "' width='100%' height='700'><p>Your browser does not support iframes.</p></iframe>";
		$("#" + tabName + "-" + count).prepend(addFrameHtml);
		initSouthClose();

}

