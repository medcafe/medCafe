function copyToTemplate()
{
	
	var templateNameObj = $( "#templateName" ),
			allFields = $( [] ).add( templateNameObj ),
			tips = $( ".validateTips" );
			
	$( "#dialogCreateTemplate" ).dialog( "destroy" );
	
	$( "#dialogCreateTemplate" ).dialog({
			autoOpen: false,
			height: 300,
			width: 350,
			modal: true,
			buttons: {
				"Create Template": function() {
					
					var bValid = true;

					var templateName = templateNameObj.val();
					bValid = bValid && checkLength( templateNameObj, "templateName", 3, 16 );

					bValid = bValid && checkRegexp( templateNameObj, /^[a-zA-Z]([0-9A-Za-z_])+$/i, "Template name may consist of a-z, 0-9, underscores, begin with a letter." );
					
					if ( bValid ) {

						var templateServer = "copyTemplate.jsp?template_id=" + templateName +"&action=CreateTemplate";
		    			//alert("medCafe.templates.js copyAndRetrieve about to call " + templateServer);
		    			$.get(templateServer, function(data)
	 					{
								//parent.window.location.replace("index.jsp");
								//Some sort of success message data is the JSON rtn Object
								
						});
						//Code to copy To Template
						$( this ).dialog( "close" );
					}
				},
				Cancel: function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				templateNameObj.val( "Template" ).removeClass( "ui-state-error" );
			}
		});
	
		
		$("#dialogCreateTemplate").dialog( "open" );
}

function copyAndRetrieve(server, patient, template_id)
{
		
		//var url = "index.jsp";
		//alert(server + " " + patient + " " + oldPatient);
		parent.$("#copyTemplateDialog").dialog({
				autoOpen: false,
				modal:true,
				resizable: true,
				title: "Close Tab",
				minWidth: 600,
				width : 800,
				buttons : {
					"Yes" : function() {
						parent.$("#copyTemplateDialog").dialog("destroy");
						parent.closeAllTabs("tabs");
						//populate(url, patient);
						var templateServer = "copyTemplate.jsp?patient_id=" + patient+"&template_id=" + template_id + "&action=CopyTemplate";
		    			//alert("medCafe.templates.js copyAndRetrieve about to call " + templateServer);
		    			$.get(templateServer, function(data)
	 					{
								parent.window.location.replace("index.jsp");
						});

					}
					,
					"No" : function() {
						parent.$("#copyTemplateDialog").dialog("destroy");
					}
				}
		});
		parent.$("#copyTemplateDialog").dialog("open");
}

function displayTemplate(patient_id, template_id)
{

	$('#dialogTemplate').html('');
	$("#dialogTemplate").append("<div id='templateTabs'><ul class=\"tabs\" id =\"templateTabHolder\"></ul></div>");
	
	var $tabs = $('#templateTabs').tabs();
	
	populateTemplate(patient_id, template_id);
	
	var marginHDialog = 25; marginWDialog  = 25;
	marginHDialog = $(window).height()-marginHDialog;
		
	var marginWDialog = $(window.body).width()-marginWDialog;
	$("#dialogTemplate").dialog({
						 autoOpen: false,
						 modal:true,
						 resizable: true,
						 title: "Templates",
						 height: marginHDialog,
						 width: marginWDialog,
						 maxWidth: 600,
						 minWidth: 600,
						 buttons : {
						    "Close" : function() {
						   
						   	 //Put in code to make sure that template code is removed
						     $(this).dialog("destroy");
						     $('#dialogTemplate').html('');
						     
						   }
						},
						close: function() {
                    		 $(this).dialog("destroy");						     
                    		 $('#dialogTemplate').html('');
						     
              			}
					});

		$("#dialogTemplate").dialog("open");
}

     