
function initializePatient(server, isIntro)
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

function setOnSelect(isIntro, server, oldPatient)
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
		    		var cacheServer = server + "/cachePatient.jsp?patient_id=" + src
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
					retrieve( server, src, oldPatient);

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

function retrieve(server, patient, oldPatient)
{
		var url = "retrievePatient.jsp";
		//var url = "index.jsp";

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
							var cacheServer = "cachePatient.jsp?patient_id=" + patient
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
						var cacheServer = server + "/cachePatient.jsp?patient_id=" + patient
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

	 var server = url + "?patient_id=" + patient_id;
//	alert ("URL " + url + " id " + patient_id);
	 $.getJSON(server, function(data)
	 {
			//alert(JSON.stringify(data));
	 	   //If no tabs are defined then just return.
		   if (!data.tabs)
		   {
		   		var tab_num = parent.addTab("New", "chart");

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
                tab_num = parent.addTab(label, "Details");

		   }
			var previous_id =0;
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


                parent.createWidgetContent( data.widgets[i] );


		   	}
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

function addPatientDetail(obj, widgetInfo, data)
{
		var html = "<div class=\"" + widgetInfo.type +  widgetInfo.patient_id + "\"></div>";
/*	var link =  "repository-listJSON.jsp?repository=" + widgetInfo.repository  +"&patient_id="  + widgetInfo.rep_patient_id;

	$.getJSON(link, function(data)
	{
	*/

		//	var dataObject = eval('(' + data + ')');
			if (data.announce)
            {
              	  //alert("announce");
                  updateAnnouncements(data);
                  return;
            }
		//	var html = v2js_listPatientTable( data );

			var html = v2js_inettutsHead(widgetInfo) +window["v2js_" + widgetInfo.template](data) + v2js_inettutsTail(widgetInfo);
	  		// $("#aaa" + tab_num).append(html);
	  		if (!widgetInfo.tab_num)
				widgetInfo.tab_num = "2";
			if (!widgetInfo.column)
				widgetInfo.column = "1";

			$("#tabs-" + widgetInfo.tab_num + " #column" + widgetInfo.column).append(html);
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
