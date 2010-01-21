<%@ tag import="org.restlet.data.*, org.restlet.representation.*, org.restlet.resource.*,org.mitre.medcafe.util.*" %><%@
attribute name="relurl" required="true" rtexprvalue="true" %><%@
attribute name="mediatype" required="false" %><%

MediaType mtype = null;
if( mediatype == null )
{
    mtype = MediaType.TEXT_HTML;
}
else mtype = MediaType.valueOf(mediatype);
System.out.println( "http://" +  Config.getServerUrl() + "/" + relurl +" as " + mtype.toString());

String server = "http://" + Config.getServerUrl() + "/" ;
ClientResource resource = new ClientResource( server + relurl );
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
