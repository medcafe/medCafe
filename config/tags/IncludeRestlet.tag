<%@ tag import="org.restlet.*, org.restlet.data.*, org.restlet.representation.*, org.restlet.resource.*,org.mitre.medcafe.util.*,org.mitre.medcafe.restlet.*,java.util.*, java.util.logging.*" %><%!
    public final static String KEY = "/IncludeRestlet.tag";
    public final static Logger log = Logger.getLogger( KEY );
    //static{log.setLevel(Level.FINER);}%><%@
    attribute name="relurl" required="true" rtexprvalue="true" %><%@
    attribute name="mediatype" required="false" %><%@
    attribute name="restVerb" required="false" %><%

    MediaType mtype = null;
    if( mediatype == null )
    {
        mtype = MediaType.TEXT_HTML;
    }
    else mtype = MediaType.valueOf(mediatype);

	log.finer( "IncludeRestletTag: http://" +  Config.getServerUrl() + "/" + relurl +" method " + restVerb);
	Method method = Method.GET;
	if( restVerb == null )
    {
       method = Method.GET;
    }
	else
	{
		method = new Method(restVerb);
	}

	log.finer("IncludeRestletTag: Method " + method.getName());

    MedcafeApplication app = (MedcafeApplication)application.getAttribute("org.restlet.ext.servlet.ServerServlet.application");
    if( app == null )
    {
        out.write("Could not connect to data restlets.");
        return;
    }
    log.finer( relurl +" as " + mediatype);

    Request req = new Request( method, relurl );
    Response resp = new Response( req );

    ClientInfo clientInfo = req.getClientInfo();
    List<Preference<MediaType>> mediaTypes = clientInfo.getAcceptedMediaTypes();
    mediaTypes.add( new Preference( mtype, 1.0F) );

    mediaTypes = clientInfo.getAcceptedMediaTypes();
    for(Preference<MediaType> pref : mediaTypes)
        System.out.println( String.valueOf(pref) );

    
    // req.setClientInfo(new ClientInfo( mtype ) );
    app.handle(req, resp);

	if (method.equals(Method.GET))
	{
		log.finer( "IncludeRestlet.tag\tMethod is GET " );

	    if (resp.getStatus().isSuccess() && resp.getEntity().isAvailable() ) {
	        log.finer( "IncludeRestlet.tag success and entity available" );

	        resp.getEntity().write(out);
	    }
	    else
	    {
	        out.write("Resource not available");
	        log.severe( "IncludeRestlet.tag was not able to find the resource" );
	        log.severe( "\tFound resource?  " + resp.getStatus().isSuccess() );
	        log.severe( "\tIs the entity available?  " + resp.getEntity().isAvailable() );
	    }
	}
	else
	{
		log.finer( "IncludeRestlet.tag\tMethod is NOT a GET " );

		if (resp.getStatus().isSuccess())
		{
			//Put in a representation that signals success


		}
		else
		{
			out.write("Resource not available");
	        log.severe( "IncludeRestlet.tag was not able to find the resource" );
	        log.severe( "\tFound resource?  " + resp.getStatus().isSuccess() );
		}
	}


    /*
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
        log.severe( "IncludeRestlet.tag was not able to find the resource" );
        log.severe( "\tFound resource?  " + resource.getStatus().isSuccess() );
        log.severe( "\tIs the entity available?  " + resource.getResponseEntity().isAvailable() );
    }
     */
%>
