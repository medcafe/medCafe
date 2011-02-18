<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ page import="org.mitre.medcafe.util.*, java.io.*, java.util.HashMap" %><%@
    taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%
    StringBuilder strBuild = new StringBuilder();
	String cssDir = Constants.BASE_PATH + Constants.FILE_SEPARATOR + "css";
	HashMap<String,String> files = TextProcesses.getCSSFiles(new File(cssDir));

	for (String file:files.keySet())
	{
		String cssFile = "css" + Constants.FILE_SEPARATOR +files.get(file);
		strBuild.append("<option value=\"" + cssFile + "\">" + file + "</option>");
	}

%>

<body>
<div class="ui-widget ui-state-highlight ui-corner-all">
<strong>Select Theme</strong>
<br/>
<select id="themeList">
	<%=strBuild%>

	</select>
<br/>
<button id="saveTheme">Save</button>
</div>
</body>