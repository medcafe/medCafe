<%@ page import="org.mitre.medcafe.util.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%
	
	String listWidgets =   "c/widgets";
%>

<tags:IncludeRestlet relurl="<%=listWidgets%>" mediatype="json"/>
