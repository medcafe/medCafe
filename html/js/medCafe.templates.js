function copyAndRetrieve(server, patient, template_id)
{
		
		//var url = "index.jsp";
		//alert(server + " " + patient + " " + oldPatient);
		parent.$("#copyTemplateDialog").dialog({
				autoOpen: false,
				modal:true,
				resizable: true,
				title: "Close Tab",
				buttons : {
					"Yes" : function() {
						parent.$("#copyTemplateDialog").dialog("destroy");
						parent.closeAllTabs("tabs");
						//populate(url, patient);
						var templateServer = "copyTemplate.jsp?patient_id=" + patient+"&template_id=" + template_id;
		    			alert("medCafe.templates.js copyAndRetrieve about to call " + templateServer);
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
