<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ page import = "org.mitre.medcafe.util.*, org.mitre.medcafe.model.*"%>
<%@ page import = "java.util.logging.Logger, java.util.logging.Level"%>
<%!
    public final static String KEY = "/tabs-template.jsp";
    public final static Logger log = Logger.getLogger( KEY );
    //static{log.setLevel(Level.FINER);}%>
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

	//System.out.println("tabs-template.jsp type " + type + " patient id " + patientId );

%>
<script>

//var hasContent = false;

$(function(){

	initialize();
	filterType();
	bindClose();

	$(".inettuts-container").droppable({
      drop: function(event, ui)
      {


      		var dragObj = $(ui.draggable);
      		var img = $(dragObj).find('img');


			if (img.length == 0)
			{

				return;
			}


       		var widgetId = $(dragObj).html();

       		//Make
			var link = $(img).attr("custom:url")

			if (typeof(link) == "undefined")
			{
				return;
			}


			var serverLink = "retrievePatientRepositoryAssoc.jsp";
			var widgetInfo = {

				"patient_id" : "<%=patientId%>",
				"location" : "<%=location%>",

				"type" : $(img).attr("custom:type"),
				"name" : $(dragObj).find('p').text(),
				"clickUrl" : $(img).attr("custom:url"),

				"tab_num": "<%=tabNum%>",
				"params" : $(img).attr("custom:params"),
				"column" : "1",
				"script" : $(img).attr("custom:script"),
				"script_file" : $(img).attr("custom:script_file"),
				"template" : $(img).attr("custom:template"),
				"jsonProcess" : $(img).attr("custom:jsonProcess"),
				"isINettuts" : $(img).attr("custom:isINettuts"),
				"cacheKey" : $(img).attr("custom:cacheKey")
			};


			widgetInfo.column = $(this).attr('id').substring(6);
			widgetInfo.collapsed = 'false';
			widgetInfo.label = widgetInfo.name;
			widgetInfo.color_num = 2;
	widgetInfo.tab_num = $(this).closest(".tabContent").attr('id').substring(5);




					var new_id = addWidgetNum(widgetInfo)
					if (new_id > 0)
					{

						widgetInfo.id = new_id;


						if (widgetInfo.isINettuts == false || widgetInfo.isINettuts == "false")
						{
							widgetInfo.tab_num = -1;

						}


						createWidgetContent(widgetInfo);



		   			}


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
		try{
		$.getScript(srcName, function(){

			//alert("tabs_template.jsp : binding the FILTER_DATE filterDate<%=type%>");

			$(document).bind('FILTER_DATE', function(event, startDate, endDate, filterCat)
			{
				var tabNum = "<%=tabNum%>";
				filterDate<%=type%>(startDate, endDate,filterCat, tabNum );

			});

		});
		}
		catch (e)
		{
		}
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
