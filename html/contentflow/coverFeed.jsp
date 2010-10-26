<%@ page import="org.restlet.*, org.restlet.data.*, org.restlet.representation.*, org.restlet.resource.*,org.json.*, java.io.*,org.mitre.medcafe.util.*,org.mitre.medcafe.restlet.*, org.mitre.medcafe.model.*, java.util.*, java.text.*" %><%

	
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

    JSONObject retObj;
    
    if ((startDate == null ||startDate.equals(""))&&(endDate == null ||
            endDate.equals(""))&&(filterCat == null ||filterCat.equals("")))
        retObj = obj;
    else
    {
        if (startDate == null)
            startDate = "01/01/1950";
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        if (endDate == null)
        {
            Date today = new Date();

                //	endDate = "01/01/2012";
            endDate = df.format(today);
        }
        Date startDt, endDt;
        try {
            startDt = df.parse(startDate);
            endDt = df.parse(endDate);
        }
        catch (ParseException parseE)
        {
            System.out.println("Error parsing filter dates, using default dates");
            endDt = new Date();
            GregorianCalendar cal = new GregorianCalendar(1950, 1, 1);
            startDt = cal.getTime();
        }
        boolean categories = false;
        String[] catFilters = new String[0];
        if (filterCat != null && !filterCat.equals(""))
        {
            catFilters = filterCat.split(",");
            categories = true;
        }
        retObj = new JSONObject();
        SimpleDateFormat fileDateFormat = new SimpleDateFormat(MedCafeFile.DATE_FORMAT);
        try{
            JSONArray objArray = obj.getJSONArray("repositoryList");
            for (int i = 0; i < objArray.length(); i++)
            {
                JSONObject reposObject = objArray.getJSONObject(i);
                String repos = reposObject.getString("repository");
                JSONArray imageArray = reposObject.getJSONArray("images");
                JSONObject newImages = new JSONObject();
                for (int j = 0; j < imageArray.length(); j++)
                {
                    JSONObject imageObj = imageArray.getJSONObject(j);
                    String dateStr = imageObj.getString(MedCafeFile.DATE);
                    Date imageDate = null;
                    try {
                        imageDate = fileDateFormat.parse(dateStr);
                    }
                    catch (ParseException parseE)
                    {
                        System.out.println("Error parsing date of image " + imageObj.getString("name"));
                        break;
                    }
                    if (imageDate.compareTo(endDt)<= 0 && imageDate.compareTo(startDt)>=0)
                    {
                        if (categories)
                        {
                            for (String cat : catFilters)
                            {
                                if (cat.equals(imageObj.getString("category")))
                                {
                                    newImages.append("images", imageObj);
                                    break;
                                }
                            }
                        }
                        else
                        {
                            newImages.append("images", imageObj);
                        }
                    }
                }
                newImages.put("repository", repos);
                retObj.append("repositoryList", newImages);
            }
        }
			
        catch (JSONException e) {
            System.out.println(e.getMessage());
            retObj = WebUtils.buildErrorJson("Problem creating filtered image list." + e.getMessage());
        }
        finally{  
          obj = retObj;
        }
    }
    StringWriter imageDivs = new StringWriter();
    VelocityUtil.applyTemplate( obj, "listImages.vm", imageDivs);
        
    out.write( imageDivs.toString().replaceAll("<:prefix:>", "http://" + Config.getServerUrl() + "/images/patients/" + patientId + "/" ) );

%>

