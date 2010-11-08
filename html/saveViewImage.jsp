<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import = "java.util.*"%>
<%@ page import = "java.net.URLEncoder"%>
<%@ page import = "java.io.UnsupportedEncodingException"%>
<%@ page import = "org.mitre.medcafe.util.*"%>
<%@ page import = "org.mitre.medcafe.model.*"%>
<%@ page import = "org.json.JSONObject" %>
<%@ page import = "org.json.JSONException" %>
<%
	//Web utils get the widgetSettings
	//Put the String to JSON data
	//Save the JSON Object to the database	
	//System.out.println("saveWidget.jsp in Save Widget start " );
	Enumeration e = request.getParameterNames();
	/*
	public static final String FILE_ID = "file_id";
	public static final String X_ORIGIN = "x_origin";
	public static final String Y_ORIGIN = "y_origin";
	public static final String SHAPE_X = "shape_x";
	public static final String SHAPE_Y = "shape_y";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String ZOOM = "zoom";
	public static final String COLOR = "color";
	public static final String TYPE = "shape_type";
	public static final String NOTE = "note";
	public static final String TRUE = "true";
	
	*/
	String userName =  request.getRemoteUser();
	String patient_id = request.getParameter(ImageTag.ID);
	String file_id = request.getParameter(ImageTag.FILE_ID);
	String origin_x = request.getParameter(ImageTag.X_ORIGIN);
	String origin_y = request.getParameter(ImageTag.Y_ORIGIN);
	String zoom = request.getParameter(ImageTag.ZOOM);
	String fileName = request.getParameter("image");


	if(e != null)
	{
	   try
	   {
	   	 
	      JSONObject jsonobj = new JSONObject();
	      while(e.hasMoreElements())
		  {
		     String key = "";
		  	 Object keyObj = e.nextElement();
		  	 if (keyObj != null)
		  	 	key = keyObj.toString();
			 	jsonobj.put( key, request.getParameter(key));
		  }
		  
		  System.out.println("saveViewImage.jsp about to Save View Image for jsonObj  " +jsonobj.toString() );
		  ImageTag imageTag = new ImageTag();
		  imageTag.saveAnnotations(userName, jsonobj);
		  
	    }
	    catch(JSONException je) {
			System.out.println("Error in creating JSON " + je.getMessage() );

	    }
  	}
	
	
%>
