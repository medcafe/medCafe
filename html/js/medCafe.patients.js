$(function(){
    
    	var serverLink =  "searchPatientsJSON.jsp?";
		setOnSelect();	
		initialize(serverLink);
});
    
function initialize(serverLink)
{
    	var emptyVal = '';
		$("#last_name").blur(function(){
			serverLink =  "searchPatientsJSON.jsp?";
			var lastNameVal = $(this).val();
			serverLink = serverLink + "search_str_last=" + lastNameVal;
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
			 serverLink =  "searchPatientsJSON.jsp?";
			var firstNameVal = $(this).val();
			serverLink = serverLink + "search_str_first=" + firstNameVal;
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
    
function setOnSelect()
{
    	$("#list_names").change(function() {
    	
    		var src = $("option:selected", this).val();
    		//Get details for this patient
    		populate("retrievePatient.jsp", src);	
    		
    	});
    	
}

//Function to save and close all open Tabs
function removeTabs()
{
	 //Put in a warning that this will close tabs - Ok, Cancel, Save and Close
	 
}

function populate(url, patient_id)
{
	 var server = url + "?patient_id=" + patient_id;
	
	 $.getJSON(server, function(data)
	 {
		   
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