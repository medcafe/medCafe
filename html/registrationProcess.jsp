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
