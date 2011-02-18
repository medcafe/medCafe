<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import = "java.util.*, java.sql.PreparedStatement, java.sql.ResultSet, java.sql.SQLException"%>
<%@ page import = "org.mitre.medcafe.util.*"%>
<script>
$(document).ready( function() {

		var tabSelectedId;



});
</script>
<%
	String user =  request.getRemoteUser();
	String sql = "SELECT username, title, url, description FROM link where username =?";
	DbConnection dbConn = null;
	PreparedStatement prep = null;
	ResultSet rs = null;
	StringBuffer strBuf = null;
	try {
		dbConn= new DbConnection();
		prep=dbConn.prepareStatement(sql);
		prep.setString(1, user);
		rs = prep.executeQuery();
		strBuf = new StringBuffer();
%>
	<ul>
<%
		int i=0;
		while (rs.next())
		{
			String title = rs.getString("title");
			String url = rs.getString("url");
			strBuf.append("<div id='south-tabs-" + i + "'>");
			strBuf.append("<iframe src ='" + url + "' width='100%' height='700'><p>Your browser does not support iframes.</p></iframe></div>");
%>
		<li class="tabHeader"><a href="#south-tabs-<%=i%>"><%=title%></a></li>
<%
			i++;
		}
	}
	catch (SQLException e)
	{
		throw e;
	}
	finally {

		DatabaseUtility.close(rs);
		DatabaseUtility.close(prep);
		if (dbConn != null)
			dbConn.close();
	}
%>
	</ul>
<%=strBuf.toString()%>
