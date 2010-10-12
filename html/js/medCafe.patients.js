
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
		    		var cacheServer = server + "/cachePatient.jsp?patient_id=" + src+"&repository=" + repository;
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
	 		
			//alert(JSON.stringify(data));
	 	   //If no tabs are defined then just return.
		   if (!data.tabs)
		   {
		   		var tab_num = parent.addTab("New", "chart", true);

		   	//	parent.iNettuts.refresh("yellow-widget-" + tab_num);
				// parent.iNettuts.makeSortable();

		   		return;
		   }
		  // alert(JSON.stringify(data));
		   //put the new tabs in
		
		   for(i=0; i< data.tabs.length; i++)
		   {
                var label = data.tabs[i].name;
		        // alert("adding tab " + i);
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
		   	                  // var link = "";
                // var label = data.widgets[i].name;
                // //var label = "Label" + i;
                // var type =  data.widgets[i].type;
                // var widgetInfo.repository =  data.widgets[i].repository;
                // var tab_num =  data.widgets[i].order;
                // var location =  data.widgets[i].location;
                // var server =  data.widgets[i].server;
                // var repPatientId =  data.widgets[i].rep_patient_id;
                // var params = "";

                // parent.createWidgetContent(patient_id, server, label, type ,tab_num, params, widgetInfo.repository, repPatientId);
                // alert("about to run createWidgetContent for a widget of type " + data.widgets[i].type );
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
				
             //   parent.createWidgetContent( data.widgets[i], true );
					 previous_id = data.widgets[i].id;
					 previous_tab = data.widgets[i].tab_num;
					 previous_col = data.widgets[i].column;

		   	}
		  // 		$(parent).delay(700* data.widgets.length,function()
		//{
				for (i=0; i<data.widgets.length; i++)
				{
					//alert(i + "  : " + data.widgets[i].id);
					//iNettuts.refresh("yellow-widget" + data.widgets[i].id);
		 			refreshYellowWidget($(parent), data.widgets[i], 1, true)
		   	}

				$(parent).delay(2000, function()
				{
				$('#tabs').tabs('select', "#tabs-" + focusedTab);
				iNettuts.makeSortable();
				});
	//	});
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
function addPatientDetail(obj, widgetInfo, data)
{
		var html = "<div class=\"" + widgetInfo.type +  widgetInfo.patient_id + "\"></div>";
/*	var link =  "repository-listJSON.jsp?repository=" + widgetInfo.repository  +"&patient_id="  + widgetInfo.rep_patient_id;

	$.getJSON(link, function(data)
	{
	*/

		//	var dataObject = eval('(' + data + ')');
	/*		if (data.announce)
            {
              	  //alert("announce");
                  updateAnnouncements(data);
                  return;
            }  */
		//	var html = v2js_listPatientTable( data );

			var html = v2js_inettutsHead(widgetInfo) +window["v2js_" + widgetInfo.template](data) + v2js_inettutsTail(widgetInfo);
	  		// $("#aaa" + tab_num).append(html);
	  		if (!widgetInfo.tab_num)
				widgetInfo.tab_num = "2";
			if (!widgetInfo.column)
				widgetInfo.column = "1";

			$("#tabs-" + widgetInfo.tab_num + " #column" + widgetInfo.column).append(html);
			$(obj).delay(500, function ()
			{ 
	
			$('#collapseInfo' + widgetInfo.id).find('a.collapse').toggle(function () {
						  $(this).text("Less");
                    $(this).css({backgroundPosition: '-52px 0'});
                     $(this).parent().find("#add_repos" +widgetInfo.id).show();
                    return false;
                },function () {
                		$(this).text("More");
                      $(this).css({backgroundPosition: ''});
                     $(this).parent().find("#add_repos" +widgetInfo.id).hide();
                    return false;
                });
            });
	  		//$("#tabs-2 #column2").append(html);

			//Delay to let DOM refresh before adding table styling
		//	$(obj).delay(500,function()
		//	{
					//alert( $("#example" + patientId).text());

				// $("#"+ widgetInfo.type + widgetInfo.rep_patient_id).dataTable( {
				// 	"bJQueryUI": true,
				// 	"aaSortingFixed": [[ 0, 'asc' ]],
				// 	"aoColumns": [
				// 			{ "bVisible": false },
				// 				null,
				// 				null ]
				// } );
				setHasContent(widgetInfo.tab_num);
/*			} );
	});  */
}
