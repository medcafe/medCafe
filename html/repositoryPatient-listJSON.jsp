<%@ page import="org.mitre.medcafe.util.*,org.mitre.medcafe.model.*" %>
<%@ page import="java.util.logging.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %><%!
    public final static String KEY = "/repositoryPatient-listJSON.jsp";
    public final static Logger log = Logger.getLogger( KEY );
    static{log.setLevel(Level.FINER);}%>
    <%
    String repository = request.getParameter("repository");
	
	String listRep =  "/repositories/" + repository + "/patients/";
	String append = "?";
	String firstName = request.getParameter("first_name");
	String lastName = request.getParameter("last_name");
	String middleInitial = request.getParameter("middle_initial");
	
	if (firstName != null)
	{
		listRep = listRep + append + "first_name="+ firstName;
		append ="&";
	}
	
	if (lastName != null)
	{
		listRep = listRep + append + "last_name="+ lastName;
		append ="&";
	}
	
	if (middleInitial != null)
	{
		listRep = listRep + append + "middleInitial="+ middleInitial;
	}
	
	System.out.println("repositoryPatient-listJSON.jsp: list Rep  " + listRep);
%>

<tags:IncludeRestlet relurl="<%=listRep%>" mediatype="json"/>
