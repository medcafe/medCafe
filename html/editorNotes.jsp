<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import = "java.util.*"%>
<%@ page import = "org.mitre.medcafe.util.*"%>
<%
	System.out.println("EditorNotes.jsp start ") ; 
	
	String title = request.getParameter("title");
	
	TextProcesses processText = new TextProcesses();	
	String note = "";
	
	Text textObj =  processText.getTextObject(title);
	if (textObj != null)
		note = textObj.getText();
	System.out.println("EditorNotes.jsp note " + note + " title " + title) ; 
	
  	
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
    <input type="submit" value="Save"></input>
</form>
</div>
