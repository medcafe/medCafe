<%@ page import = "org.mitre.medcafe.util.*, org.mitre.medcafe.model.*"%>
<%
	String tabNum = WebUtils.getOptionalParameter(request, "tab_num", "2");
	String title = WebUtils.getOptionalParameter(request, "title", "Title");
	String type = WebUtils.getOptionalParameter(request, "type", "Chart");
	String location = WebUtils.getOptionalParameter(request, "location", "center");

	PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    if( cache == null )
    {  //nobody is logged in
        //log.warning("No patient selected");
        response.sendRedirect("introPage.jsp");
        return;
    }
    String patientId = cache.getDatabasePatientId();

	System.out.println("tabs-template.jsp type " + type + " patient id " + patientId );

%>
<script>

var hasContent = false;

$(function(){

	initialize();
	filterType();
	bindClose();

	$(".inettuts-container").droppable({
      drop: function(event, ui)
      {

      		var hasContentObj = $(this).find("#hasContent");
      		var hasContent = $(hasContentObj).attr("custom:hasContent");
      		var img = $(dragObj).find('img');

			if (img.length == 0)
			{
				alert("this is not a draggable object");
				//This is not a droppable object
				return;
			}

			var dragObj = $(ui.draggable)
       		var widgetId = $(ui.draggable).html();

       		//Make
			var link = $(dragObj).find('img').attr("custom:url")
			//alert("tabs-template.jsp drag Obj " + $(dragObj).html());
			if (typeof(link) == "undefined")
			{
				return;
			}

     
			var serverLink = "retrievePatientRepositoryAssoc.jsp";
			var widgetInfo = {
			
				"patient_id" : "<%=patientId%>",
				"rep_patient_id" : "",
				"location" : "<%=location%>",
				"repository" : $(dragObj).find('img').attr("custom:repository"),
				"type" : $(dragObj).find('img').attr("custom:type"),
				"name" : $(dragObj).find('p').text(),
				"clickUrl" : $(dragObj).find('img').attr("custom:url"),
				"server" : $(dragObj).find('img').attr("custom:server"),
				"tab_num": "<%=tabNum%>",
				"params" : $(dragObj).find('img').attr("custom:params"),
				"column" : "1",
				"script" : $(dragObj).find('img').attr("custom:script"),
				"script_file" : $(dragObj).find('img').attr("custom:script_file"),
				"template" : $(dragObj).find('img').attr("custom:template"),
				"jsonProcess" : $(dragObj).find('img').attr("custom:jsonProcess"),
				"iNettuts" : $(dragObj).find('img').attr("custom:iNettuts")
			};
			//	var tabObject = $(this).closest('.tabContent');
			widgetInfo.column = $(this).attr('id').substring(6);
			widgetInfo.collapsed = 'false';
			widgetInfo.label = widgetInfo.name;
			widgetInfo.color_num = 2;
	
			var repPatientJSON;

			$.getJSON(serverLink,function(data)
			{
					repPatientJSON = data;
					var len = repPatientJSON.repositories.length;
					var x;

					for (x in repPatientJSON.repositories)
					{
						test = repPatientJSON.repositories[x].repository;
						if (test == widgetInfo.repository)
						{
							  widgetInfo.rep_patient_id = repPatientJSON.repositories[x].id;
							  //repPatientId = repPatientJSON.repositories[x].id;
							  //alert("tabs-template.jsp getRepId rep id: " + repPatientId);
						}

					}
					var new_id = addWidgetNum(widgetInfo)
					if (new_id > 0)
					{
						widgetInfo.id = new_id;

					// if (hasContent == "false")
					// {
					// 	//No content : Use the current Tab
						//addChart(this, link, "<%=tabNum%>");
						if (widgetInfo.iNettuts == false || widgetInfo.iNettuts == "false")
						{
							widgetInfo.tab_num = -1;
							
						}

						
						createWidgetContent(widgetInfo);
						$(hasContentObj).attr("custom:hasContent",true);

						// renameTab("<%=tabNum%>",text);
					// }
					// else
					// {
					// 	//Tab already has content Create a new Tab
					// 	createLink(patientId,link, text, type ,params, repository, repPatientId);
					// }

		   	}
		   });
		   
		   }
    });

});

//Take the Hashmap of the repositories and the associated patient rep id and put into JSON object

function filterType()
{
	//alert("tabs_template.jsp : filterType try to bind the FILTERS for type <%=type%> looking for javascript function filterDate<%=type%>");

	var srcName = "js/filterDate<%=type%>.js";
	if (typeof filterDate<%=type%> == 'undefined')
	{
		$.getScript(srcName, function(){

			//alert("tabs_template.jsp : binding the FILTER_DATE filterDate<%=type%>");

			$(document).bind('FILTER_DATE', function(event, startDate, endDate, filterCat)
			{
				var tabNum = "<%=tabNum%>";
				filterDate<%=type%>(startDate, endDate,filterCat, tabNum );

			});

		});
	}
	else
	{
		//alert("tabs_template.jsp : binding the FILTER_DATE filterDate<%=type%>");

		$(document).bind('FILTER_DATE', function(event, startDate, endDate, filterCat)
		{
				var tabNum = "<%=tabNum%>";
				filterDate<%=type%>(startDate, endDate, filterCat, tabNum );

		});
	}
}

function bindClose()
{

			$(document).bind('CLOSE_TAB', function(event, tabSelected)
			{

				var tabNum = "<%=tabNum%>";

				if (tabNum == tabSelected)
				{
					//alert("close tab " + tabNum + "  " + tabSelected);
					//removeWidgetsFromTab(tabNum);
					removeWidgetTab(tabNum);
				}

			});

}
</script>



<!--<div class="id" id="<%=title%>"></div>-->
<div id="columns">
	<div id="column1" class="column inettuts-container"></div>
	<div id="column2" class="column inettuts-container"></div>
	<div style="clear:both"/>
</div>

