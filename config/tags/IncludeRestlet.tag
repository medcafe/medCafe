<%@ tag import="org.restlet.data.*, org.restlet.representation.*, org.restlet.resource.*" %><%@
attribute name="relurl" required="true" %><%@
attribute name="mediatype" required="false" %><%

MediaType mtype = null;
if( mediatype == null )
{
    mtype = MediaType.TEXT_HTML;
}
else mtype = MediaType.valueOf(mediatype);
System.out.println( "http://127.0.0.1:8080/medcafe/" + relurl +" as " + mtype.toString());

ClientResource resource = new ClientResource( "http://127.0.0.1:8080/medcafe/" + relurl );
// Prints the list of registered items.
resource.get(mtype);
if (resource.getStatus().isSuccess() && resource.getResponseEntity().isAvailable()) {
    resource.getResponseEntity().write(out);
}
else
{
    out.write("Resource not available");
    System.out.println( "IncludeRestlet.tag was not able to find the resource" );
    System.out.println( "\tFound resource?  " + resource.getStatus().isSuccess() );
    System.out.println( "\tIs the entity available?  " + resource.getResponseEntity().isAvailable() );
}

%>
