function processMenuClick(menuLabel, patientId)
{
	
	if (menuLabel == "Add Tab")
	{
		//Code to add an empty tab
		var tab_num = addTab("new","chart");
		//Make sure that tab is refreshed to add relevant scripts/ events
		iNettuts.refresh("yellow-widget" + tab_num);
		iNettuts.makeSortable();
	}
	else if (menuLabel == "Save")
	{
		 saveWidgets(patientId);
	}
	else if (menuLabel == "Close Tabs")
	{
		 initClose();
		 saveWidgets(patientId);
		 closeAllTabs("tabs");
	}
	else if (menuLabel == "Test")
	{
		//Code to cycle through the widgets and save
		
		var ids = medCafeWidget.getAllIds();
		//Cycle through each to save
		 $.each (ids, function(i, val)
		 {
		 	 
		     var settings = medCafeWidget.getExtWidgetSettings(val);
		     alert("menus.js settings val " + val  + " tab num " + settings.tab_num);
		 });
	}
}
