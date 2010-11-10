
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
		    		var src = $("option:selected", this).val();
		    		//Get details for this patient
		    		var cacheServer = server + "/cachePatient.jsp?patient_id=" + src+"&repository=" + repository + "&isIntro=" + isIntro;
		    		$.get(cacheServer, function(data)
	 				{
	 					var indexSrv = server + "/index.jsp";
	 					parent.window.location.replace(indexSrv);
	 					//populate(indexSrv, src);
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

function retrieve(server, patient, oldPatient, repository)
{
		var url = "retrievePatient.jsp";
		//var url = "index.jsp";
		//alert(server + " " + patient + " " + oldPatient);
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
							//Cannot use the cached Patient as this may have been already reset
							parent.saveWidgets(oldPatient);

							//
							var cacheServer = "cachePatient.jsp?patient_id=" + patient+"&repository=" + repository;
		    				$.get(cacheServer, function(data)
	 						{
								parent.window.location.replace("index.jsp");

								//addScheduleButton(patient);
								//addCreateAssocButton(patient,"physician");
							});

					},
					"No" : function() {
						parent.$("#saveDialog").dialog("destroy");
						parent.closeAllTabs("tabs");
						//populate(url, patient);
						var cacheServer = server + "/cachePatient.jsp?patient_id=" + patient+"&repository=" + repository;
		    			$.get(cacheServer, function(data)
	 					{
								parent.window.location.replace("index.jsp");
								//addScheduleButton(patient);
								//addCreateAssocButton(patient,"physician");
						});

					}
					,
					"Cancel" : function() {
						parent.$("#saveDialog").dialog("destroy");
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
	var focusedTab = 1;
	 var server = url + "?patient_id=" + patient_id;
//	alert ("URL " + url + " id " + patient_id);
	 $.getJSON(server, function(data)
	 {
	 		

	 	   //If no tabs are defined then just return.
		   if (!data.tabs)
		   {
		   		var tab_num = parent.addTab("New", "chart", true);


		   		return;
		   }

		   //put the new tabs in
		
		   for(i=0; i< data.tabs.length; i++)
		   {
                var label = data.tabs[i].name;

		       // alert(JSON.stringify(data.tabs[i]));
		        tab_num = parent.addTab(label, "Details", data.tabs[i].iNettuts);
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

				   // This allows the widget to be added only after the widget before it
				   // has been created so that order is maintained.  The only exception
				   // is for an excessive delay of the previous widget insertion
						delayForWidgetCreation(parent, previous_id, previous_tab, previous_col, data.widgets[i], 1);
				
   
					 previous_id = data.widgets[i].id;
					 previous_tab = data.widgets[i].tab_num;
					 previous_col = data.widgets[i].column;

		   	}
	
				for (i=0; i<data.widgets.length; i++)
				{
		
		 			refreshYellowWidget($(parent), data.widgets[i], 1, true)
		   	}

				$(parent).delay(2000, function()
				{
				$('#tabs').tabs('select', "#tabs-" + focusedTab);
				iNettuts.makeSortable();
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
function delayForWidgetCreation(callObj, prev_id, prev_tab, prev_col, widgetInfo, num)
{
	if (prev_id == 0 || prev_tab != widgetInfo.tab_num || prev_col != widgetInfo.column)
	{
		 callObj.createWidgetContent( widgetInfo, true );
	}
	else
	{
		if (num<50)
		{
			if ($("#yellow-widget"+prev_id).length<=0)
			{
				//alert("#yellow-widget"+widgetInfo.id + "  length:  " + $("#yellow-widget" + widgetInfo.id).length);
				$(callObj).delay(100,function()
				{
					num++;
					delayForWidgetCreation(callObj, prev_id, prev_tab, prev_col, widgetInfo, num);
				});
			}
			else
			{
				callObj.createWidgetContent( widgetInfo, true );	
			}	
		}
		else
		{
			callObj.createWidgetContent( widgetInfo, true );
		}
	}
}

