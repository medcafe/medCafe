function copyAndRetrieve(server, patient, template_id)
{
		
		//var url = "index.jsp";
		//alert(server + " " + patient + " " + oldPatient);
		parent.$("#copyTemplateDialog").dialog({
				autoOpen: false,
				modal:true,
				resizable: true,
				title: "Close Tab",
				maxWidth: 600,
				minWidth: 600,
				buttons : {
					"Yes" : function() {
						parent.$("#copyTemplateDialog").dialog("destroy");
						parent.closeAllTabs("tabs");
						//populate(url, patient);
						var templateServer = "copyTemplate.jsp?patient_id=" + patient+"&template_id=" + template_id;
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

     