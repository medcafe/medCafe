<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import = "java.util.*, java.sql.PreparedStatement, java.sql.ResultSet"%>
<%@ page import = "org.mitre.medcafe.util.*"%>
<script>
$(document).ready( function() {

		var tabSelectedId;

    	

});
</script>
<%	
	String user =  request.getRemoteUser();
	String sql = "SELECT username, title, url, description FROM link where username =?";
	DbConnection dbConn = dbConn= new DbConnection();
	PreparedStatement prep=dbConn.prepareStatement(sql);
	prep.setString(1, user);
	ResultSet rs = prep.executeQuery();
	StringBuffer strBuf = new StringBuffer();
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
%>
	</ul>
<%=strBuf.toString()%>		
