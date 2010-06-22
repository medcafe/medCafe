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
<div id="editorDialog" style="display:none">Are you sure you want to delete this Note?</div>    
<div id="changeTextDialog" style="display:none">Continuing will result in loss of changes. Do you wish to continue?</div>    


<script type="text/javascript">
$(document).ready(function() {

});


</script>

<hr>
</body>
</html>
