<%@ page import="org.mitre.medcafe.util.*" %>
<%@ page import="org.mitre.medcafe.model.*" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="java.util.HashMap" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%	
	String user =  request.getRemoteUser();
	JSONObject templateObj = Template.getTemplates(user);
	JSONArray templateList =  (JSONArray)templateObj.get("Templates");
	StringBuffer sb = new StringBuffer("");
%>
<ul>
			<li><a href="#">Copy</a>
			<ul>
			<% 
				for (int i = 0; i < templateList.length(); i++)
				{
				String templateName = templateList.getString(i);
				sb.append("<li><a href=\"#\">View " + templateName + "</a></li>" );
			%>
			<li><a href="#">
			<%=templateName %>
			</a></li>
			<% 
			}
			%>
			
			</ul>
			</li>
			<li><a href="#">View</a>
			<ul>
			<%=sb.toString()%>
			</ul>
			</li>
</ul>
