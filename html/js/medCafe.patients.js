
function initializePatient(server, isIntro, repository)
{
		var origserverLink = server;

		addAssociatePatient(isIntro);

    	var emptyVal = '';
		$("#last_name").blur(function(){

			var serverLink = origserverLink;
			var isChecked = $("#isPatientChecked").attr('checked');

	    	if (isChecked)
	    	{
	    		serverLink = serverLink + "&isPatient=" + isChecked + "&";
	    	}
			var lastNameVal = $(this).val();
			serverLink = serverLink + "&search_str_last=" + lastNameVal;
			var firstNameVal = $('#first_name').val();
		    serverLink = serverLink +  "&search_str_first=" + firstNameVal;

			 $.getJSON(serverLink,
		      function(data)
		      {

		      	  //Check to see if any error message

				  parent.updateAnnouncements(data);
				  if (data.announce)
				  {
					return;
				  }
				  var html = v2js_listSearchPatientsSelect( data );

			      $("#list_names").html(emptyVal + html);
		      });
		  });

		$("#first_name").blur(function(){
			 //serverLink =  "searchPatientsJSON.jsp?";
			 var serverLink = origserverLink;
			 var isChecked = $("#isPatientChecked").attr('checked');

	    	if (isChecked)
	    	{
	    		serverLink = serverLink + "&isPatient=" + isChecked + "&";
	    	}

			var firstNameVal = $(this).val();
			serverLink = serverLink + "&search_str_first=" + firstNameVal;
			var lastNameVal = $('#last_name').val();
		    serverLink = serverLink +  "&search_str_last=" + lastNameVal;

		    $.getJSON(serverLink,
		      function(data)
		      {
		      	  //Check to see if any error message
		      	  parent.updateAnnouncements(data);
				  if (data.announce)
				  {
					return;
				  }
				  var html = v2js_listSearchPatientsSelect( data );

			      $("#list_names").html(emptyVal + html);
		      });
		  });
}

function setOnSelect(isIntro, server, oldPatient, repository)
{
			//alert("medCafe.patient.js server is " + server);
			if (isIntro == "true")
			{
				//window.location.replace(server);
				//alert("medCafe.patient.js setOnSelect isIntro true ");
				$("#list_names").change(function()
				{
					$("#searchPatients").spinner(
					{
						img: server + '/images/ajax-loader.gif',
						position: 'center',
						height: 100,
						width: 100,
						hide: true });
		    		var src = $("option:selected", this).val();
		    		//Get details for this patient
		    		var cacheServer = server + "/cachePatient.jsp?patient_id=" + src+"&repository=" + repository + "&isIntro=" + isIntro;

		    		$.get(cacheServer, function(data)
	 				{
	 					var indexSrv = server + "/index.jsp";
	 					parent.window.location.replace(indexSrv, function()
	 					{
	 					$("#searchPatients").spinner("remove");
	 					});
	 					
	 				});

	    		});


			}
			else
			{
				//alert("medCafe.patient.js setOnSelect isIntro false ");

				$("#list_names").change(function()
				{

		    		var src = $("option:selected", this).val();
		    		//Get details for this patient
					retrieve( server, src, oldPatient, repository);

	    		});
    		}
}

function refresh(patient)
{
		var url = "retrievePatient.jsp";

		//var url = "index.jsp";
		parent.closeAllTabs("tabs");
		populate(url, patient);

		//addScheduleButton(patient);
		//addCreateAssocButton(patient,"physician");

}
function refreshCache()
{
	var url = "retrievePatient.jsp";
	var server = ""
	var oldPatient;
		//var url = "index.jsp";
		//alert(server + " " + patient + " " + oldPatient);
		parent.$("#saveDialog").css("visibility", "visible");
		parent.$("#saveDialog").dialog({
				autoOpen: false,
				modal:true,
				resizable: true,
				title: "Close Tab",
				buttons : {
					"Yes" : function() {
							//Have to Destroy as otherwise
							//the Dialog will not be reinitialized on open
							parent.$("#saveDialog").dialog("destroy");
							parent.$("#saveDialog").css("visibility", "hidden");
							//Cannot use the cached Patient as this may have been already reset
							parent.saveWidgets(oldPatient);
								$("#OptionWindow").spinner(
					{
						img: 'images/ajax-loader.gif',
						position: 'center',
						height: 100,
						width: 100,
						hide: true });
							//
							var cacheServer = "refreshPatient.jsp";
							
		    				$.get(cacheServer, function(data)
	 						{
								parent.window.location.replace("index.jsp", function()
								{
									$("#OptionWindow").spinner("remove");
								});

								//addScheduleButton(patient);
								//addCreateAssocButton(patient,"physician");
							});

					},
					"No" : function() {
						parent.$("#saveDialog").dialog("destroy");
                        parent.$("#saveDialog").css("visibility", "hidden");
						parent.closeAllTabs("tabs");
							$("#OptionWindow").spinner(
					{
						img: 'images/ajax-loader.gif',
						position: 'center',
						height: 100,
						width: 100,
						hide: true });
						//populate(url, patient);
						var cacheServer = "refreshPatient.jsp";
		    			$.get(cacheServer, function(data)
	 					{
								parent.window.location.replace("index.jsp", function()
								{
									$("#OptionWindow").spinner("remove");
								});
								//addScheduleButton(patient);
								//addCreateAssocButton(patient,"physician");
						});

					}
					,
					"Cancel" : function() {
						parent.$("#saveDialog").dialog("destroy");
                        parent.$("#saveDialog").css("visibility", "hidden");
					}
				}
		});
		parent.$("#saveDialog").dialog("open");

}


function retrieve(server, patient, oldPatient, repository)
{
		var url = "retrievePatient.jsp";

		//var url = "index.jsp";
		//alert(server + " " + patient + " " + oldPatient);
		parent.$("#saveDialog").css("visibility", "visible");
		parent.$("#saveDialog").dialog({
				autoOpen: false,
				modal:true,
				resizable: true,
				title: "Close Tab",
				buttons : {
					"Yes" : function() {
							//Have to Destroy as otherwise
							//the Dialog will not be reinitialized on open
							parent.$("#saveDialog").dialog("destroy");
							parent.$("#saveDialog").css("visibility", "hidden");
							//Cannot use the cached Patient as this may have been already reset
							parent.saveWidgets(oldPatient);
								$("#searchPatients").spinner(
					{
						img: server + '/images/ajax-loader.gif',
						position: 'center',
						height: 100,
						width: 100,
						hide: true });
							//
							var cacheServer = "cachePatient.jsp?patient_id=" + patient+"&repository=" + repository;

		    				$.get(cacheServer, function(data)
	 						{
								parent.window.location.replace("index.jsp", function()
								{
									$("#searchPatients").spinner("remove");
								});

								//addScheduleButton(patient);
								//addCreateAssocButton(patient,"physician");
							});

					},
					"No" : function() {
						parent.$("#saveDialog").dialog("destroy");
                        parent.$("#saveDialog").css("visibility", "hidden");
						parent.closeAllTabs("tabs");
							$("#searchPatients").spinner(
					{
						img: server + '/images/ajax-loader.gif',
						position: 'center',
						height: 100,
						width: 100,
						hide: true });
						//populate(url, patient);
						var cacheServer = server + "/cachePatient.jsp?patient_id=" + patient+"&repository=" + repository;

		    			$.get(cacheServer, function(data)
	 					{
								parent.window.location.replace("index.jsp", function()
								{
									$("#searchPatients").spinner("remove");
								});
								//addScheduleButton(patient);
								//addCreateAssocButton(patient,"physician");
						});

					}
					,
					"Cancel" : function() {
						parent.$("#saveDialog").dialog("destroy");
                        parent.$("#saveDialog").css("visibility", "hidden");
					}
				}
		});
		parent.$("#saveDialog").dialog("open");
	
}

function addAssociatePatient(isIntro)
{
	var buttonTxt = "<center><button id='addPatientAssocBtn'>Associate Patient</button></center>";
	 $("#associatePatient").html(buttonTxt);

	 $("#addPatientAssocBtn").click(function(event,patient_id){

		parent.popUpAssociatePatient();

	});

}

function populate(url, patient_id)
{

	 var server = url + "?patient_id=" + patient_id;
	populateTabs(server,"tabs");

}

function populateTemplate(patient_id, template_id)
{
	var url = "retrieveTemplate.jsp"
	var server = url + "?template_id=" + template_id + "&patient_id=" + patient_id;
	populateTabs(server, "templateTabs");

}

function populateTabs(server, tab_set)
{
	 var focusedTab = 1;
	  $.getJSON(server, function(data)
	 {
	 		if (tab_set === undefined) tab_set="tabs";

			//If no tabs are defined then just return.
		   if (!data.tabs)
		   {

		   		var tab_num = parent.addAnyTab("New", "chart", true, tab_set );

		   		return;
		   }

		   //put the new tabs in

		   for(i=0; i< data.tabs.length; i++)
		   {

                var label = data.tabs[i].name;

		       // alert(JSON.stringify(data.tabs[i]));
		        tab_num = parent.addAnyTab(label, "Details", data.tabs[i].isINettuts,tab_set);
					if (data.tabs[i].inFocus  && data.tabs[i].inFocus=="true")
					{
						focusedTab = data.tabs[i].tab_num;
						//alert ("focused tab is " +focusedTab);
					}
		   }

			var previous_id =0;
			var previous_col = 0;
			var previous_tab = 0;
			if (data.widgets){

		   	//next put the widgets on the tabs
		   	for(i=0; i< data.widgets.length; i++)
		   	{


					if (!data.widgets[i].label || data.widgets[i].label == "")
						data.widgets[i].label = data.widgets[i].name;
					if (!data.widgets[i].color_num || data.widgets[i].color_num == "")
						data.widgets[i].color_num == 2;
					if (!data.widgets[i].collapsed || data.widgets[i].collapsed == "")
						data.widgets[i].collapsed == 'false';
					if (data.widgets[i].tab_set === undefined)
						data.widgets[i].tab_set = tab_set;
						data.widgets[i].id = nextWidgetNum++;
				   // This allows the widget to be added only after the widget before it
				   // has been created so that order is maintained.  The only exception
				   // is for an excessive delay of the previous widget insertion

					delayForWidgetCreation(parent, previous_id, previous_tab, previous_col, data.widgets[i], 1, tab_set);


					 previous_id = data.widgets[i].id;
					 previous_tab = data.widgets[i].tab_num;
					 previous_col = data.widgets[i].column;

		   	}

				for (i=0; i<data.widgets.length; i++)
				{

		 			refreshYellowWidget($(parent), data.widgets[i], 1, true, tab_set);
		   		}

				$(parent).delay(2000, function()
				{
					$('#'+ tab_set).tabs('select', "#" + tab_set+ "-" + focusedTab);
					if (tab_set != "templateTabs")
					{
						iNettuts.makeSortable();
					}
				});

		   }
	});
}
function addScheduleButton( patient_id)
{

	 var buttonTxt = "<button id='addScheduleBtn'>Add To Schedule</button>";
	 $("#addSchedule").html("");
	 $("#addSchedule").append(buttonTxt);
	 var url="setSchedule.jsp";
	 var server = url + "?patient_id=" + patient_id;

	 $("#addScheduleBtn").click(function(event,patient_id){

		 $.getJSON(server, function(json){

              if (json.announce)
              {
              	  //alert("announce");
                  parent.updateAnnouncements(json);
                  return;
              }
              else
              {
				  //alert("no announce");
              }
        });

	});
}

function addCreateAssocButton( patient_id, role)
{


	 var buttonTxt = "<button id='createAssocBtn'>Add To My List</button>";
	 $("#addPatient").html("");
	 $("#addPatient").append(buttonTxt);
	 var url="addPatientAssoc.jsp";
	 var server = url + "?patient_id=" + patient_id + "&role=" +  role;

	 $("#createAssocBtn").click(function(event,patient_id){

		 $.getJSON(server, function(json){

              if (json.announce)
              {
              	  //alert("announce");
                  parent.updateAnnouncements(json);
                  return;
              }
              else
              {
				  //alert("no announce");
              }
        });

	});
}
function delayForWidgetCreation(callObj, prev_id, prev_tab, prev_col, widgetInfo, num, tab_set)
{
	 //alert("medCafe.patient.js delayFor WidgetCreation prev_id " + prev_id);

	if (prev_id == 0 || prev_tab != widgetInfo.tab_num || prev_col != widgetInfo.column)
	{
		 callObj.createWidgetContent( widgetInfo, true, tab_set );
	}
	else
	{
		if (num<50)
		{
			if ($("#medCafeWidget-"+tab_set+prev_id).length<=0)
			{
				//alert("#yellow-widget"+widgetInfo.id + "  length:  " + $("#yellow-widget" + widgetInfo.id).length);
				$(callObj).delay(100,function()
				{
					num++;
					delayForWidgetCreation(callObj, prev_id, prev_tab, prev_col, widgetInfo, num, tab_set);
				});
			}
			else
			{
				callObj.createWidgetContent( widgetInfo, true , tab_set);
			}
		}
		else
		{
			callObj.createWidgetContent( widgetInfo, true, tab_set );
		}
	}
}

