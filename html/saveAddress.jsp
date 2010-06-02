<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import = "java.util.*"%>
<%@ page import = "java.net.URLEncoder"%>
<%@ page import = "java.io.UnsupportedEncodingException"%>
<%@ page import="org.json.JSONObject" %>
<%@ page import = "org.mitre.medcafe.util.*, org.mitre.medcafe.model.*"%>
<%
	
  	String user =  request.getRemoteUser();
  	//Use the user login to save the text
	String street = request.getParameter("street_address") ;
	String street2 = request.getParameter("street2_address") ;
	String city = request.getParameter("city_address") ;
	String state = request.getParameter("state_address") ;
	String zip = request.getParameter("zip_address") ;
	String country = request.getParameter("country_address") ;
	System.out.println("saveAddress: address : street " + street);
					
  	String patientId = request.getParameter(Constants.PATIENT_ID);
	if (patientId == null)
	{
	  	Object patientIdObj = session.getAttribute("patient");
		if (patientIdObj != null)
			patientId = patientIdObj.toString();
	}
		
	if (patientId == null)
	{
		return;
	}

	JSONObject addressObj = new JSONObject();
	JSONObject o = new JSONObject();
	o.put(Address.STREET, street);
	if (street2 != null)
		o.put(Address.STREET2, street);
			      
	o.put(Address.CITY, city);
	o.put(Address.STATE, state);
	o.put(Address.ZIP, zip);
	o.put(Address.COUNTRY , country);
					      
	addressObj.append("address", o);	
	Address address = new Address();
	
	address.saveAddress(patientId, addressObj);
	
	response.sendRedirect("listAddress.jsp");
%>