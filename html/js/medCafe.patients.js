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
    	
    		parent.closeAllTabs("tabs");
		
    		var src = $("option:selected", this).val();
    		//Get details for this patient
    		populate("retrievePatient.jsp", src);	
    		
    	});
    	
}

function populate(url, patient_id)
{
	 var server = url + "?patient_id=" + patient_id;
	 $.getJSON(server, function(data)
	 {
	 	   //If no data is retrieved then just return.
		   if (!data.widgets)
		   {
		   		return;
		   }
		   
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