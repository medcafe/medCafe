<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<%
	String patientId = request.getParameter("patient_id");
	if (patientId == null)
		patientId = "1";

	//String dataUrl = "chartData.jsp";
	//if (dataUrl == null)
	//	dataUrl = "/repositories/medcafe/patients/" + patientId + "/charts/temperature";

	%>
 <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Flot Examples</title>
    <link href="layout.css" rel="stylesheet" type="text/css"></link>
    <script type="text/javascript" src="${js}/medCafe.chart.js"></script>
    <!--[if IE]><script language="javascript" type="text/javascript" src="../excanvas.min.js"></script><![endif]-->

 </head>
    <body>
    <h3>Chart - Patient ID: <%=patientId%></h3>

    <div id="placeholder" style="width:300px;height:220px;"></div>
	<div id="legend"></div>
	<!--<div id="miniature" style="float:left;margin-left:20px;margin-top:30px">-->
   <!-- <div id="overview" style="width:80px;height:100px"></div>-->
    <p>
    <FORM NAME="chartform">

      <input type="checkbox" name="vitalType" value="Temp."> Temperature<br>
	<input type="checkbox" name="vitalType" value="B/P" > Blood Pressure<br>
	<input type="checkbox" name="vitalType" value="Resp."> Respiration<br>
	<input type="checkbox" name="vitalType" value="Wt."> Weight<br>
	<input type="checkbox" name="vitalType" value="Ht."> Height<br>
	<input type="checkbox" name="vitalType" value="BMI" > Body Mass Index<br>
	<input type="checkbox" name="vitalType" value="Pulse Ox"> Pulse Oxygenation<br>
	<input type="checkbox" name="vitalType" value="Pulse"> Pulse<br>
	<input class="dataUpdate" type="button" value="Update graph" onClick="processChartButton(this.form)">
	<input class="dataUpdate" type="button" value="Clear checkboxes" onClick="clearCheckBoxes(this.form)">
	</FORM>
    </p>

<script id="source" language="javascript" type="text/javascript">
$(function () {
   //processChart("","<%=patientId%>","","","","");

});


</script>

 </body>
</html>
