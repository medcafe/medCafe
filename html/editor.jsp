<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import = "java.util.*"%>
<%@ page import = "org.mitre.medcafe.util.*"%>
<%@ page import = "java.net.URLEncoder"%>
<%
	String patientId = null;
		
	Object patientIdObj = session.getAttribute("patient");
	if (patientIdObj != null)
		patientId = patientIdObj.toString();
		
	String tab_num = request.getParameter("tab_num");
		
	String user =  request.getRemoteUser();
  	
	TextProcesses processText = new TextProcesses();	
	HashMap<String,Text> textObjs =  processText.populateTextObjects(user, patientId);
	String note = "";
	String title = request.getParameter("title");
	Text textObj = null;
		
  	StringBuffer sbuff= new StringBuffer("");
	for (String titleNew: textObjs.keySet())
	{
		  String titleUrl = URLEncoder.encode(titleNew, "UTF-8"); 
		  sbuff.append("<option value=\"" + titleUrl + "\"");
		  if (titleNew.equals(title))
		  	sbuff.append(" \"selected=true\" ");
		  sbuff.append(">" + titleNew + "</option>");
	}
	sbuff.append("");

 	HashMap<String,TextTemplate> templateObjs =  processText.populateTemplateObjects(user);
	StringBuffer sbuffTemplate= new StringBuffer("");
  	for (String titleNew: templateObjs.keySet())
	{
		  String titleUrl = URLEncoder.encode(titleNew, "UTF-8"); 
		  sbuffTemplate.append("<option value=\"" + titleUrl + "\"");
		  if (titleNew.equals(title))
		  	sbuffTemplate.append(" \"selected=true\" ");
		  sbuffTemplate.append(">" + titleNew + "</option>");
	}
  	
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr-ch" lang="fr-ch">
<head>

<title>A Light weight RTE jQuery Plugin</title>
<link type="text/css" rel="stylesheet" href="css/editor/jquery.rte.css" />
<style type="text/css">
	body, textarea {
	    font-family:sans-serif;
	    font-size:12px;
	}

	.scroll {
	  height:400px;
	  width:600px;
	  overflow:auto;
	}

</style>
</head>
<body>

<div id="main" style="width:800px;" class="scroll">
 <form action="saveText.jsp?<%=Constants.PATIENT_ID%>=<%=patientId%>">
    <p>                      
    	Select Title : <select name="title" id="title"><option></option>
    		<%=sbuff.toString() %>
    	</select>
    </p>
    <p>                      
    	Select Template : <select name="template" id="template"><option></option>
    		<%=sbuffTemplate.toString() %>
    	</select>
    </p>
     <noscript>
    	<input type="submit" name="action" value="Populate Note" />
  	</noscript>
    
</form>
<div class="scroll" id="editNote">Test</div>
<div id="dialog" >Are you sure you want to delete this Note?</div>    
<div id="changeTextDialog" >Continuing will result in loss of changes. Do you wish to continue?</div>    

<script type="text/javascript" src="js/jquery-1.3.2.js"></script>
<script type="text/javascript" src="js/ui.all-1.7.1.js"></script>
<script type="text/javascript" src="js/editor/jquery.rte.js"></script>
<script type="text/javascript" src="js/editor/jquery.rte.tb.js"></script>

<link type="text/css" href="css/custom-theme/jquery-ui-1.7.2.custom.css" rel="stylesheet" />
<script type="text/javascript">
$(document).ready(function() {


	

	var patientId = '<%=patientId%>';
	var tab_num = '<%=tab_num%>';
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
});

function loadTemplate(title, tab_num, patientId)
{
	$('#editNote').load('editorNotes.jsp?title=' + title + '&tab_num=' + tab_num + '&action=copyTemplate&<%=Constants.PATIENT_ID%>='+ patientId,
    	
    		function() {
    			
		       var arr = $('.rte1').rte({
				css: ['default.css'],
				controls_rte: rte_toolbar,
				controls_html: html_toolbar
				});
				$("select#title").val(0);
				addDeleteClick();
			});

}

function loadNotes(title, tab_num, patientId)
{
	$('#editNote').load('editorNotes.jsp?<%=Constants.PATIENT_ID%>=' + patientId + '&title=' + title + '&tab_num=' + tab_num,
    	
    		function() {
    			
		       var arr = $('.rte1').rte({
				css: ['default.css'],
				controls_rte: rte_toolbar,
				controls_html: html_toolbar
				});
				//Make sure that the template is not selected
				$("select#template").val(0);
				addDeleteClick();
				setDroppable();
			});

}
function addDeleteClick()
{
		$('#deleteButton').bind("click",{},
			
			function(e){
				
				$("#dialog").dialog({
					 autoOpen: false,					
					 modal:true,
					 resizable: true,
					 title: "Close Tab",
					 buttons : {
					    "Yes" : function() {          
					     //Have to Destroy as otherwise 
					     //the Dialog will not be reinitialized on open    
					     $(this).dialog("destroy");
					     deleteText();
					      //Put in code to goto saveText.jsp Delete
					   },
					   "No" : function() {
					      $(this).dialog("destroy");
					     }  
					}
				}); 						
				$("#dialog").dialog("open");
							
		} );

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
</script>

<hr>
</body>
</html>
