<%@ page import="org.mitre.medcafe.util.*"%>
<%@ page import="org.mitre.medcafe.repositories.*" %>    
<%@ page import="com.google.gson.Gson" %> 
<%@ page import="java.net.URL" %> 
<%@ page import="org.hl7.greencda.c32.*, java.util.*;"%>
<%
	GreenCDAFeedParser parser = new GreenCDAFeedParser();
	String firstName = request.getParameter("first_name");
   String lastName = request.getParameter("last_name");
   String type = request.getParameter("type");
    if (firstName == null)
    	firstName = "Tom";
    if (lastName == null)
    	lastName = "";
    if (type == null)
    	type = "medications";
    	
    StringBuffer strBuf = new StringBuffer();
    String url = "http://127.0.0.1:3000/records";
	String baseUrl = "http://localhost:3000/";
	//GreenCDAFeedParser.parseAtom("http://localhost:3000/records");

	List<String> foundList = GreenCDAFeedParser.findPatientDetails(firstName, lastName,
				type, url, true);
		
	GreenCDARepository gcda = new GreenCDARepository();
        
    for (String foundVals: foundList)
    {
    	//strBuf.append("<p>" + foundVals + "</p>" );
   
    	
	}
	
	List<Medication> meds = gcda.getMedications("1");
     Gson gson = new Gson();
     for (Medication med: meds)
     {
          if (med == null)
              strBuf.append("is null ");
                
          strBuf.append(gson.toJson(med));
      }
%>    
<%=strBuf.toString()%>

