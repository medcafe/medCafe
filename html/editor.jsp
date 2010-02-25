<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import = "java.util.*"%>
<%@ page import = "org.mitre.medcafe.util.*"%>
<%
	String patientId = request.getParameter("patient_id");
	if (patientId == null)
		patientId = "1";
		
	String user =  request.getRemoteUser();
  	
	TextProcesses processText = new TextProcesses();	
	HashMap<String,Text> textObjs =  processText.populateTextObjects(user, patientId);
	String note = "";
	String title = "";
	Text textObj = null;
	System.out.println("Editor.jsp number of textObjects " + textObjs.size() ); 
  	
  	StringBuffer sbuff= new StringBuffer("");
	for (String titleNew: textObjs.keySet())
	{
		  sbuff.append("<option value=\"" + titleNew + "\">" + titleNew + "</option>");
	}
	sbuff.append("");
	
	System.out.println("Editor.jsp note " + note + " title " + title) ; 
	
  	
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
</style>
</head>
<body>

<div id="main" style="width:800px;">
 <form action="saveText.jsp">
    <p>                      
    	Select Title : <select name="title" id="title"><option></option>
    		<%=sbuff.toString() %>
    	</select>
    </p>
     <noscript>
    	<input type="submit" name="action" value="Populate Note" />
  	</noscript>
    
</form>
<div id="editNote">Test</div>
    
<script type="text/javascript" src="js/jquery-1.3.2.js"></script>
<script type="text/javascript" src="js/editor/jquery.rte.js"></script>
<script type="text/javascript" src="js/editor/jquery.rte.tb.js"></script>
<script type="text/javascript">
$(document).ready(function() {

	$('#editNote').load('editorNotes.jsp?title=' + $(this).val(),
    	
    		function() {
    			
		       var arr = $('.rte1').rte({
				css: ['default.css'],
				controls_rte: rte_toolbar,
				controls_html: html_toolbar
				});
			});
		
	$("select#title").change(function()
	{
    	$('#editNote').load('editorNotes.jsp?title=' + $(this).val(),
    	
    		function() {
    			
		       var arr = $('.rte1').rte({
				css: ['default.css'],
				controls_rte: rte_toolbar,
				controls_html: html_toolbar
				});
			});
		
    		
    });
		
});
</script>

<hr>
</body>
</html>
