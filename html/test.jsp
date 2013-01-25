<%@ page import="org.mitre.medcafe.util.*"%>
<%@ page import="org.mitre.medcafe.repositories.*" %>    
<%@ page import="com.google.gson.Gson" %>   
<%@ page import="org.hl7.greencda.c32.*, java.util.*;"%>
<%
	//GreenCDAFeedParser parser = new GreenCDAFeedParser();
	//parser.parseAtom("http://1.1.22.110:3000/records");
	//parser.parseDom("http://1.1.22.110:3000/records/1");
	
	GreenCDARepository gcda = new GreenCDARepository();
	List<Medication> meds = gcda.getMedications("1");
	StringBuffer strBuf = new StringBuffer();
	Gson gson = new Gson();
	for (Medication med: meds)
	{
		if (med == null)
			strBuf.append("is null ");
			
		strBuf.append(gson.toJson(med));
	}
%>    
<%=strBuf.toString()%>

