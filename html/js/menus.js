function processMenuClick(menuLabel)
{
	
	if (menuLabel == "Add Tab")
	{
		//Code to add an empty tab
	
		
		var tab_num = addTab("new");
		
		//Make sure that tab is refreshed to add relevant scripts/ events
		iNettuts.refresh("yellow-widget" + tab_num);
		iNettuts.makeSortable();
	}
	else if (menuLabel == "Save")
	{
		//Code to cycle through the widgets and save
		
		var ids = medCafeWidget.getAllIds();
		//Cycle through each to save
		
	}
}
