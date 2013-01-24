<%@ page import="org.mitre.medcafe.util.*" %>    
<%
	GreenCDAFeedParser parser = new GreenCDAFeedParser();
	//parser.parseAtom("http://1.1.22.110:3000/records");
	parser.parseDom("http://1.1.22.110:3000/records/1");
	
%>    

