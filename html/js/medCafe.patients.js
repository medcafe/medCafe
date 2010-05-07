
function initialize(server)
{
		var origserverLink = server;
		
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
    
function setOnSelect(isIntro, server)
{
			
			if (isIntro == "true")
			{
				//window.location.replace(server);
				//alert("medCafe.patient.js setOnSelect isIntro true ");
				$("#list_names").change(function() 
				{
		    		var src = $("option:selected", this).val();
		    		//Get details for this patient
		    		parent.window.location.replace(server + "/index.jsp?patient_id=" + src);	
	    		});
	    		
	    		
			}
			else
			{
				//alert("medCafe.patient.js setOnSelect isIntro false ");
				
				$("#list_names").change(function() 
				{
		    		var src = $("option:selected", this).val();
		    		//Get details for this patient
		    		
		    		retrieve( src);	
	    		});
    		}
}

function refresh(patient)
{
		var url = "retrievePatient.jsp";
		//parent.saveWidgets();
		parent.closeAllTabs("tabs");
		populate(url, patient);	
							
		//addScheduleButton(patient);
		//addCreateAssocButton(patient,"physician");
		
}

function retrieve(patient)
{
		var url = "retrievePatient.jsp";
		
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
							parent.saveWidgets();
							parent.closeAllTabs("tabs");
							populate(url, patient);	
							
							addScheduleButton(patient);
							addCreateAssocButton(patient,"physician");
					},
					"No" : function() {
						parent.$("#saveDialog").dialog("destroy");
						parent.closeAllTabs("tabs");
						populate(url, patient);	
						addScheduleButton(patient);
						addCreateAssocButton(patient,"physician");
					}
					,
					"Cancel" : function() {
						parent.$("#saveDialog").dialog("destroy");
					}  
				}
		}); 						
		parent.$("#saveDialog").dialog("open");
}

function populate(url, patient_id)
{
	 var server = url + "?patient_id=" + patient_id;

	 $.getJSON(server, function(data)
	 {
	 	   //If no data is retrieved then just return.
		   if (!data.widgets)
		   {
		   		parent.addTab("New Widget", "blank");
		   		return;
		   }
		   
		   //alert("medCafe.patients.js : number of widgets " + data.widgets.length);
		   for(i=0; i< data.widgets.length; i++) 
		   {
		    	
		  			var link = "";
					var label = data.widgets[i].name;
					//var label = "Label" + i;
					var type =  data.widgets[i].type;
					var repId =  data.widgets[i].repository;
					var tab_num =  data.widgets[i].tab_order;
					var location =  data.widgets[i].location;
					var server =  data.widgets[i].server;
					var params = "";
					
					tab_num = parent.addTab(label, type);					
					parent.createWidgetContent(patient_id, server, label, type ,tab_num, params, repId);
					
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

