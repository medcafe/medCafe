<%@ page import="org.mitre.medcafe.util.*" %><%
    session.invalidate();
    response.sendRedirect( Config.getWebapp() );
%>
