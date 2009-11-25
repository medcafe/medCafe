<%@ tag import="org.restlet.data.*, org.restlet.representation.*, org.restlet.resource.*" %><%@
attribute name="relurl" required="true" %><%
System.out.println( "http://127.0.0.1:8080/medcafe/" + relurl +" as " + MediaType.TEXT_HTML);
ClientResource resource = new ClientResource( "http://127.0.0.1:8080/medcafe/" + relurl );
// Prints the list of registered items.
resource.get(MediaType.TEXT_HTML);
if (resource.getStatus().isSuccess() && resource.getResponseEntity().isAvailable()) {
    resource.getResponseEntity().write(out);
}
else
    out.write("Resource not available");

%>
