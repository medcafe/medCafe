function showPreferences()
{

	$('#preferencesDialog').html('');
	$("#preferencesDialog").append("<div id=\"preferencesScreen\" ></div>");
	
	var marginHDialog = 25; marginWDialog  = 25;
	marginHDialog = $(window).height()-marginHDialog;
		
	var marginWDialog = $(window.body).width()-marginWDialog;
		var preferenceServer = "setPreferences.jsp";
	//alert("medCafe.templates.js copyAndRetrieve about to call " + templateServer);
	$.get(preferenceServer, function(data)
	{
		$('#preferencesScreen').html(data);
		$("#saveTheme").click(function () 
		{
				saveTheme();
        });
	});
		
					
	$("#preferencesDialog").dialog({
						 autoOpen: false,
						 modal:true,
						 resizable: true,
						 title: "Preferences",
						 height: marginHDialog,
						 width: marginWDialog,
						 minWidth: 600,
						 buttons : {
						    "Close" : function() {
						   
						   	 //Put in code to make sure that template code is removed
						     $(this).dialog("destroy");
						     $('#preferencesDialog').html('');
						     
						   }
						},
						close: function() {
                    		 $(this).dialog("destroy");						     
                    		 $('#preferencesDialog').html('');
						     
              			}
					});

		$("#preferencesDialog").dialog("open");
		
		
}

function saveTheme()
{
	var theme = $("#themeList").val();

	var savePrefs= "setColor.jsp?theme_value=" + theme;
	$.get(savePrefs, function(data)
	{
		
	});
	
}
