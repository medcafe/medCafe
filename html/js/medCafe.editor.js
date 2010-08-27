function processEditor(widgetInfo, data)
{

	setUpEditActions(widgetInfo.tab_num, widgetInfo.patient_id);
}

function setUpEditActions(tab_num, patientId)
{
	
	var titleNew = $(this).val();
	loadNotes(titleNew, tab_num, patientId);
		
	$("select#title").change(function()
	{
		var title =$(this).val(); 
				
		$("#changeTextDialog").dialog({
			autoOpen: false,					
			modal:true,
			resizable: true,
			title: "Change Text",
			buttons : {
				"Yes" : function() {          
				//Have to Destroy as otherwise 
				//the Dialog will not be reinitialized on open    
				 $(this).dialog("destroy");
				 loadNotes(title, tab_num, patientId);
				 //Put in code to goto saveText.jsp Delete
			},
				"No" : function() {
				 $(this).dialog("destroy");
				}  
			}
	 	}); 						
	 	$("#changeTextDialog").dialog("open");
		
    });
	
	$("select#template").change(function()
	{
		var title =$(this).val();
    			
    	$("#changeTextDialog").dialog({
			autoOpen: false,					
			modal:true,
			resizable: true,
			title: "Change Text",
			buttons : {
				"Yes" : function() {          
				//Have to Destroy as otherwise 
				//the Dialog will not be reinitialized on open    
				 $(this).dialog("destroy");
				 loadTemplate(title, tab_num, patientId);	
				 //Put in code to goto saveText.jsp Delete
			},
				"No" : function() {
				 $(this).dialog("destroy");
				}  
			}
	 	}); 						
	 	$("#changeTextDialog").dialog("open");
    
    });
    
   
}
function loadTemplate(title, tab_num, patientId)
{
	$('#editNote').load('editorNotes.jsp?title=' + title + '&tab_num=' + tab_num + '&action=copyTemplate',
    	
    		function() {
    			
		       var arr = $('.rte1').rte({
				css: ['default.css'],
				controls_rte: rte_toolbar,
				controls_html: html_toolbar
				});
				$("select#title").val(0);
				addDeleteClick(title, tab_num, patientId);
				addSaveClick(arr);
			});

}

function loadNotes(title, tab_num, patientId)
{

	$('#editNote').load('editorNotes.jsp?patient_id=' + patientId + '&title=' + title + '&tab_num=' + tab_num,
    	
    		function() {
    			
		       var arr = $('.rte1').rte({
				css: ['default.css'],
				controls_rte: rte_toolbar,
				controls_html: html_toolbar
				});
				//Make sure that the template is not selected
				$("select#template").val(0);
				
				addDeleteClick(title, tab_num, patientId);
				addSaveClick(arr);
				setDroppable();
			});

}
function addDeleteClick(title, tab_num, patientId)
{
		$('#deleteEditsButton').bind("click",{},
			
			function(e){
				
				$("#editorDialog").dialog({
					 autoOpen: false,					
					 modal:true,
					 resizable: true,
					 title: "Close Tab",
					 buttons : {
					    "Yes" : function() {          
					     //Have to Destroy as otherwise 
					     //the Dialog will not be reinitialized on open    
					     $(this).dialog("destroy");
					     deleteText(title, tab_num, patientId);
					      //Put in code to goto saveText.jsp Delete
					   },
					   "No" : function() {
					      $(this).dialog("destroy");
					     }  
					}
				}); 						
				$("#editorDialog").dialog("open");
							
		} );

}

function addSaveClick(arr)
{
	$('#saveEditsButton').click(function() 
	{
  						
	  		var saveLink = "saveText.jsp?";
	  		var enterTitle = $('#enterTitle').val();
	  		//var notes = $('form[name="editorNotesForm"] input[name="form[info1]"]').text();
	  		var notes = arr["notes"].get_content();
	  		
	  		saveLink = saveLink + "title=" + enterTitle + "&form[info1]=" + notes + "&action=Save";
	  		//$('div#myDiv form[name="myForm"] fieldset.myField input[name="myInput"]')
			$.get(saveLink, function(data)
			{
				$("#editorDialog").dialog("destroy");		
			});
	});

}

function setDroppable()
{
	$("#enterTitle").draggable({helper: 'clone'});
	
	$("#textDrop").droppable(
	{
		drop: function(ev, ui)
		{
			alert("dragged text " + ui.draggable.text());
		}
	});
}

function deleteText(title, tab_num, patientId)
{
	var content = "saveText.jsp?action=Delete&title=" + title;
					
	$.get(content, function(data)
	{						  
		$("#editorDialog").dialog("destroy");
	});
}
