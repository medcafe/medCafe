var tips;
		function updateTips( t ) {
			tips
				.text( t )
				.addClass( "ui-state-highlight" );
			setTimeout(function() {
				tips.removeClass( "ui-state-highlight", 1500 );
			}, 500 );
		}

		function checkLength( o, n, min, max ) {
			if ( o.val().length > max || o.val().length < min ) {
				o.addClass( "ui-state-error" );
				updateTips( "Length of " + n + " must be between " +
					min + " and " + max + "." );
				return false;
			} else {
				return true;
			}
		}

		function checkRegexp( o, regexp, n ) {
			if ( !( regexp.test( o.val() ) ) ) {
				o.addClass( "ui-state-error" );
				updateTips( n );
				return false;
			} else {
				return true;
			}
		}
				


function processMenuClick(menuLabel, patientId)
{
		
	if (menuLabel == "Add Tab")
	{
		var tabNameObj = $( "#tabName" );
			tips = $( ".validateTips" );
		
	$( "#dialog-newTab" ).dialog( "destroy" );
		$( "#dialog-newTab" ).dialog({
			autoOpen: false,
			modal: true,
			buttons: {
				"Add New Tab": function() {
					var bValid = true;

					tabName = tabNameObj.val();
					bValid = bValid && checkLength( tabNameObj, "tabName", 3, 16 );


					bValid = bValid && checkRegexp( tabNameObj, /^[a-zA-Z]([0-9A-Za-z_])+$/i, "Tab name may consist of a-z, 0-9, underscores, begin with a letter." );
					
					if ( bValid ) {

						var tab_num = addTab(tabName,"chart", true);
						$( this ).dialog( "close" );
					}
				},
				Cancel: function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				tabNameObj.removeClass( "ui-state-error" );
			}
		});
	
	

		$( "#dialog-newTab" ).dialog( "open");

		$("#dialog-newTab").keydown(function(e){

                  if (e.keyCode == 13) { 
                  	e.stopPropagation();
                  	e.preventDefault();               
                       $('.ui-dialog-buttonpane').find("button:contains('Add New Tab')").trigger('click');
                  }

		
		});
		//Code to add an empty tab
		//var tab_num = addTab("new","chart", true);
		//Make sure that tab is refreshed to add relevant scripts/ events
		//iNettuts.refresh("yellow-widget" + tab_num);
		//iNettuts.makeSortable();

		// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!

	


/*		$( "#createTab" )
			.button()
			.click(function() {
				$( "#dialog-newTab" ).dialog( "open" );
			});  */

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
		     //alert("menus.js settings val " + val  + " tab num " + settings.tab_num);
		 });
	}
	else if (menuLabel == "Test Template")
	{
		//alert("menus.js Getting test template for patient " + patientId );
		//Code to load the test template 
		copyAndRetrieve("",patientId, 1);
		
		
	}
	else if (menuLabel.indexOf("Create Template") > -1)
	{
		copyToTemplate();
	}
	else if (menuLabel.indexOf("View") > -1)
	{
		var templateName = menuLabel.split(" ")[1];
		displayTemplate(patientId, templateName);
	}
	else if (menuLabel.indexOf("Preferences") > -1)
	{
		
		showPreferences();
	}
	else
	{
		copyAndRetrieve("",patientId, menuLabel);	
	}
}

