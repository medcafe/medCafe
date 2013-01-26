<%@ page import="org.mitre.medcafe.util.*"%>
<%@ page import="org.mitre.medcafe.repositories.*" %>    
<%@ page import="com.google.gson.Gson" %>   
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
    	
	String fileName =  "TestAtom.xml";
	//parser.parseAtom("http://1.1.22.110:3000/records", fileName);
	
	List<String> foundList = parser.findPatientDetails(firstName, lastName, type, fileName);
    
    StringBuffer strBuf = new StringBuffer();
    for (String foundVals: foundList)
    {
    	strBuf.append("<p>" + foundVals + "</p>" );
    }
	/*GreenCDARepository gcda = new GreenCDARepository();
	List<Medication> meds = gcda.getMedications("1");
	StringBuffer strBuf = new StringBuffer();
	Gson gson = new Gson();
	for (Medication med: meds)
	{
		if (med == null)
			strBuf.append("is null ");
			
		strBuf.append(gson.toJson(med));
	}*/
%>    
<%=strBuf.toString()%>

