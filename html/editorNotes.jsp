<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import = "java.util.*"%>
<%@ page import = "org.mitre.medcafe.util.*"%>
<%
	
	String title = request.getParameter("title");
	String tab_num = request.getParameter("tab_num");
	
	String patientId = request.getParameter("patient_id");
	if (patientId == null)
		patientId = "1";
		
	String action  = request.getParameter("action");
	if (action == null)
		action = Constants.POPULATE_TEXT;
		
	String user =  request.getRemoteUser();
  	
	TextProcesses processText = new TextProcesses();	
	String note = "";
	if (action.equals(Constants.COPY_TEMPLATE))
	{
		TextTemplate templateObj =  processText.populateTemplateObject(user,  title);
		if (templateObj != null)
			note = templateObj.getText();
		else
			System.out.println("EditorNotes.jsp no template object found " ) ; 
	}
	else
	{
		Text textObj =  processText.populateTextObject(user, patientId, title);
		if (textObj != null)
			note = textObj.getText();
		else
			System.out.println("EditorNotes.jsp no textObj found " ) ; 
	}
  	
%>
<script>
function deleteText()
{
	var tab_num = "<%=tab_num%>";
	var content = "saveText.jsp?action=Delete&patientId=<%=patientId%>&title=<%=title%>";
	
	var iFrame = $('#iframe'+tab_num , top.document);
	
	$(iFrame).attr('src', content); 
}
</script>
<div id="main" style="width:800px;">
<form action="saveText.jsp">
	<p>
    	<br/>
    	Title: <input type="text" id="enterTitle" name="title" value="<%=title%>"></input>
        <textarea name="form[info1]" id="textDrop" cols="100" rows="10"  class="rte1" method="post" action="#">
        <%=note%>
		</textarea>
    </p>
    <input type="submit" name="action" value="Save"></input>
    <input type="button" name="action" id="deleteButton" value="Delete"></input>
</form>
</div>
