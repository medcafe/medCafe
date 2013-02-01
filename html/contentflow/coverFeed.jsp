<%@ page import="org.restlet.*, org.restlet.data.*, org.restlet.representation.*, org.restlet.resource.*,org.json.*, java.io.*,org.mitre.medcafe.util.*,org.mitre.medcafe.restlet.*, org.mitre.medcafe.model.*, java.util.*, java.text.*" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="org.mitre.medcafe.model.Image" %>
<%@ page import="com.google.gson.JsonObject" %>
<%@ page import="com.google.gson.JsonParser" %>

<%

	
    MedCafeFilter filter = null;
    Object filterObj = session.getAttribute("filter");

    String startDate="";
    String endDate = "";
    String filterCat ="";
    if (filterObj != null)
    {
        filter = (MedCafeFilter)filterObj;
        startDate = filter.getStartDate();
        endDate = filter.getEndDate();
        filterCat = filter.catToString();
        System.out.println("coverFeed.jsp filter " + filter.toJSON());
    }
	
        //check for a patientId in the session

    String patientId = request.getParameter("patient_id");
    PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    if( cache == null )
    {  //nobody is logged in
        //log.warning("No patient selected");
        response.sendRedirect("introPage.jsp");
        return;
    }
    
    String dates = null;
    String rep = "local";
    patientId = cache.getRepoPatientId(rep);
    String user =  request.getRemoteUser();

    JSONObject obj = cache.retrieveObjectList("images");
   
    StringWriter imageDivs = new StringWriter();
    System.out.println("coverFeed get object " +  obj.toString() );
    
     StringBuffer strBuf = new StringBuffer();
     Gson gson = new Gson();
     JsonParser parser = new JsonParser();
    ArrayList<Image> arrayImageList = new ArrayList<Image>();
    
    JSONArray objArray = obj.getJSONArray("repositoryList");
    for (int i = 0; i < objArray.length(); i++)
    {
                JSONObject reposObject = objArray.getJSONObject(i);
                System.out.println("coverFeed reposObject " +  reposObject.toString() );
 
               if (!reposObject.has("images"))
                continue;
                
                JSONArray imageArray = reposObject.getJSONArray("images");
                for (int j = 0; j < imageArray.length(); j++)
                {
                    JSONObject singleimageObject = imageArray.getJSONObject(j);
                              
                    String strImageObject = singleimageObject.toString();
                    JsonObject o = parser.parse(strImageObject).getAsJsonObject();
                    Image image = gson.fromJson(o,  Image.class);
                    arrayImageList.add(image);
                    strBuf.append(Image.getDivs(image));    
                }
                
     }           
     out.write( strBuf.toString() );

%>

