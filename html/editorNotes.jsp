<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import = "java.util.*"%>
<%@ page import = "org.mitre.medcafe.util.*"%>
<%
	
	String title = request.getParameter("title");
	String patientId = request.getParameter("patient_id");
	if (patientId == null)
		patientId = "1";
		
	String user =  request.getRemoteUser();
  	
	TextProcesses processText = new TextProcesses();	
	String note = "";
	
	Text textObj =  processText.populateTextObject(user, patientId, title);
	if (textObj != null)
		note = textObj.getText();
	else
		System.out.println("EditorNotes.jsp no textObj found " ) ; 
	
  	
%>
<div id="main" style="width:800px;">
<form action="saveText.jsp">
	<p>
    	<br/>
    	Title: <input type="text" name="title" value="<%=title%>"></input>
        <textarea name="form[info1]" cols="100" rows="10"  class="rte1" method="post" action="#">
        <%=note%>
		</textarea>
    </p>
    <input type="submit" name="action" value="Save"></input>
    <input type="submit" name="action" value="Delete"></input>
</form>
</div>
