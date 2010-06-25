<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<%
	String patientId = request.getParameter("patient_id");
	if (patientId == null)
		patientId = "1";

	String dataUrl = request.getParameter("data");
	if (dataUrl == null)
		dataUrl = "/repositories/medcafe/patients/" + patientId + "/charts/temperature";

	%>
 <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Flot Examples</title>
    <link href="layout.css" rel="stylesheet" type="text/css"></link>
    <!--[if IE]><script language="javascript" type="text/javascript" src="../excanvas.min.js"></script><![endif]-->

 </head>
    <body>
    <h3>Temperature Chart - Patient ID: <%=patientId%></h3>

    <div id="placeholder" style="width:600px;height:220px;"></div>

	<div id="miniature" style="float:left;margin-left:20px;margin-top:30px">
    <div id="overview" style="width:166px;height:100px"></div>
    <p>
      <input class="dataUpdate" type="button" value="Poll for data">
    </p>

<script id="source" language="javascript" type="text/javascript">
$(function () {
   //processChart("","<%=patientId%>","","","","");

});


</script>

 </body>
</html>
