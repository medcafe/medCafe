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
<div id="dialogBlock" style= "display:none">
<div id="dialogCreateTemplate" title="Create new template">
				<p class="validateTips">Use only alphanumeric characters with no spaces.</p>
			
				<form>
				<fieldset>
					<label for="templateName">Name for new template</label>
					<input type="text" name="templateName" id="templateName" value="New" class="text ui-widget-content ui-corner-all" />
			
				</fieldset>
				</form>
</div>
</div>
<ul>
	
			<li><a href="#">Create Template</a>
			
			<li><a href="#">Use Template</a>
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
