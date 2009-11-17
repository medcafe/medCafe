<%@ page import="org.mitre.medcafe.util.*" %><%

String loginResult = AuthenticationUtils.registerUser( request );
if( loginResult.equals( AuthenticationUtils.OK ))
{
    response.sendRedirect( "" );
    return;
}
%>
<jsp:forward page="/" >
    <jsp:param name="message" value="<%=loginResult%>" />
</jsp:forward>
