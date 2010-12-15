<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import = "java.util.*"%>
<%@ page import = "org.mitre.medcafe.util.*, org.mitre.medcafe.model.*"%>
<%
	
	String title = request.getParameter("title");
	String tab_num = request.getParameter("tab_num");
	System.out.println("EditorNotes.jsp start " ) ; 
	
	String patientId = null;
	PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    if( cache == null )
    {  //nobody is logged in
        //log.warning("No patient selected");
        response.sendRedirect("introPage.jsp");
        return;
    }
    patientId = cache.getDatabasePatientId();
    System.out.println("EditorNotes.jsp patient id  "  + patientId) ; 
		 		
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
		{
			note = templateObj.getText();
			note = CharEncoding.forHTML(note);
		}
		else
			System.out.println("EditorNotes.jsp no template object found " ) ; 
	}
	else
	{
		Text textObj =  processText.populateTextObject(user, patientId, title);
		if (textObj != null)
		{
			note = textObj.getText();
			note = CharEncoding.forHTML(note);
		}
		else
			System.out.println("EditorNotes.jsp no textObj found " ) ; 
	}
  	
%>

<div id="mainNotes" style="width:800px;">

    	<br/>
    	
    	Title: <input type="text" id="enterTitle" name="title" value="<%=title%>"></input>
        <textarea name="form[info1]" id="notes" cols="100" rows="10"  class="rte1" method="post" action="#">
        <%=note%>
		</textarea>
    
    
    <input type="button" name="action" id="saveEditsButton" value="Save"></input>
    <input type="button" name="action" id="deleteEditsButton" value="Delete"></input>

</div>
