<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import = "java.util.*"%>
<%@ page import = "org.mitre.medcafe.util.*"%>
<%@ page import = "java.net.URLEncoder"%>
<%
	String patientId = request.getParameter("patient_id");
	if (patientId == null)
		patientId = "1";
		
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

	var patientId = '<%=patientId%>';
	$('#editNote').load('editorNotes.jsp?patient_id=' + patientId + '&title=' + $(this).val(),
    	
    		function() {
    			
		       var arr = $('.rte1').rte({
				css: ['default.css'],
				controls_rte: rte_toolbar,
				controls_html: html_toolbar
				});
			});
		
	$("select#title").change(function()
	{
		var title =$(this).val();
    	$('#editNote').load('editorNotes.jsp?patient_id=' + patientId + '&title=' + title,
    	
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
