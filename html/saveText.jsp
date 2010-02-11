<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import = "java.util.*"%>
<%
	String formData = request.getParameter("form[info1]");
	
  	System.out.println("Save the following text: " + formData ); 
%>