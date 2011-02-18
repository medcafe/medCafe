<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ page import="org.mitre.medcafe.util.*" %><%
String loginResult = null;
try {
    loginResult = AuthenticationUtils.registerUser( request );
}
catch ( NullPointerException e) {
    loginResult = e.getMessage();
}
if( loginResult.equals( AuthenticationUtils.OK ))
{
    response.sendRedirect( "index.jsp" );
    return;
}
%>
<jsp:forward page="login.jsp" >
    <jsp:param name="message" value="<%=loginResult%>" />
</jsp:forward>
