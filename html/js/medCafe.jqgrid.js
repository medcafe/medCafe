function processJQGrid(callObj, widgetInfo, data, tab_set)
{
	
	if (tab_set === undefined)
	{
		tab_set ="tabs";
	}
	var tab_key = tab_set + "-";
	
	var html = v2js_inettutsHead(widgetInfo) +window["v2js_" + widgetInfo.template](data) + v2js_inettutsTail(widgetInfo);
	
// check for invalid tab and column numbers and use defaults if they don't exist;
	if (!widgetInfo.tab_num)
		widgetInfo.tab_num = "1";
	if (!widgetInfo.column)
		widgetInfo.column = "1";
	// attach widget to page at correct tab and column location
	$("#" + tab_key + widgetInfo.tab_num + " #column" + widgetInfo.column).append(html);	
	
	var htmlAdd = "<table id=\"list\" class=\"scroll\" cellpadding=\"0\" cellspacing=\"0\"></table>";
	
	$("#aaa" + widgetInfo.id).append(htmlAdd);	
	
	createJQGrid("list", data);
	$(callObj).delay(500, function ()    
	{ 
	  populateGrid("list", data.patientData);
	});
}

function createJQGrid (placeholder, data )
{
	
	var modelVals = "colModel:[";
	var comma = "";
	for(var i=0;i<=data.modelNames.length;i++)
	{
		//alert("data.modelNames[i]" + data.modelNames[i]);
		modelVals = modelVals + comma +  "{name:'" + data.modelNames[i] + "',index:'" + data.modelNames[i]+ "', width:100}";
		comma = ",";
		
		/*	modelVals = modelVals + comma +  "{name:'" + data.modelNames[i] + "',index:'" + data.modelNames[i]+ "', width:100, sorttype:'date'}";
		}*/
	}/*
		modelVals = modelVals + comma +  "{name:'" + data.modelNames[i] + "',index:'" + data.modelNames[i]+ "'";
		comma = ",";
		if (data.modelNames[i].indexOf("Date") > 0)
		{
			 modelVals= modelVals + ", sorttype:'date'";
		}	
	    modelVals= modelVals +  ", width:100}";
	}*/
		   
	//alert("model vals");
	$("#" + placeholder + "").jqGrid({
		datatype: "local",
		height: 250,
	   	colNames: data.titleLabels,
	   		colModel:[
	   		{name:data.modelNames[0],index:data.modelNames[0], width:100},
	   		{name:data.modelNames[1],index:data.modelNames[1], width:150, sortable:true},	
	   		{name:data.modelNames[2],index:data.modelNames[2], width:90, sorttype:"date"}	
	   	],
	   	/*colModel : modelVals,*/
	   	multiselect: true,
	   	caption: "Table Data"
	});
	
}

function populateGrid(placeholder, data)
{
	//alert("about to populate grid");

	for(var i=1;i<=data.length;i++)
	   $("#" +placeholder ).jqGrid('addRowData',i+1,data[i]);
 	
}