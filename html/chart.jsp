<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<%
	String patientId = request.getParameter("patient_id");
	if (patientId == null)
		patientId = "1";
	String widgetId = request.getParameter("widgetId");
	if (widgetId == null)
		widgetId="";
	String tab_num = request.getParameter("tab_num");
	String tab_set = request.getParameter("tab_set");
	if (tab_set == null)
		tab_set = "tabs";
	String type =request.getParameter("type");
	String typeString = "";
	String displayValue = "none";
	boolean htwt = false;
	boolean temp = false;
	boolean triglyc = false;
	boolean basic = false;
	String buttonText = "Reset Graph";
	if (type==null || type.equals("none"))
	{

		type = "none";
		displayValue = "block";
		buttonText = "Submit/Reset";
	}
	else if (type.equals("ht/wt"))
	{
		typeString = "Height and Weight ";
		htwt = true;
	}
	else if (type.equals("temp"))
	{
		typeString ="Temperature ";
		temp = true;
	}
	else if (type.equals("triglyc"))
    {
        typeString ="Triglycerides ";
        triglyc = true;
    }
	else if (type.equals("basic"))
	{
		typeString = "Blood Pressure and Pulse ";
		basic = true;
	}
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
    <h3><%=typeString%>Chart - Patient ID: <%=patientId%></h3>

    <div id="placeholder<%=widgetId%>" style="width:300px;height:220px;"></div>
	<div id="legend<%=widgetId%>"></div>
	<!--<div id="miniature<%=widgetId%>" style="float:left;margin-left:20px;margin-top:30px">
   <div id="overview<%=widgetId%>" style="width:80px;height:100px"></div>
   </div> -->
    <p>
    <FORM NAME="chartform<%=widgetId%>" id="chartform<%=widgetId%>">
    <div style="display:<%=displayValue%>">

      <input type="checkbox" name="vitalType"
      <% if (temp)

	{%>
	 checked="true"
	 <%} %>
       value="Temp."> Temperature<br>
	
	<input type="checkbox" name="vitalType"
      <% if (triglyc)

    {%>
     checked="true"
     <%} %>
       value="Triglycerides"> Triglycerides<br>
       
	<input type="checkbox" name="vitalType"
	<% if (basic)
	{%>
	 checked="true"
	 <%} %>
	 value="B/P" > Blood Pressure<br>
	<input type="checkbox" name="vitalType" value="Resp."> Respiration<br>
	<input type="checkbox" name="vitalType"
	<% if (htwt)
	{%>
	 checked="true"
	 <%} %>
	 value="Wt."> Weight<br>
	<input type="checkbox" name="vitalType"
	<% if (htwt)
	{%>
	 checked="true"
	 <%} %>
	 value="Ht."> Height<br>
	<input type="checkbox" name="vitalType"
	<% if (htwt)
	{ %>
	 checked="true"
	 <%} %>
	 value="BMI" > Body Mass Index<br>
	<input type="checkbox" name="vitalType" value="Pulse Ox"> Pulse Oxygenation<br>
	<input type="checkbox" name="vitalType"
	<% if (basic)
	{%>
	 checked="true"
	 <%} %>
	value="Pulse"> Pulse<br>
		<input class="dataUpdate" type="button" value="Clear checkboxes" onClick="clearCheckBoxes(this.form)">
	</div>
	<input class="dataUpdate" type="button" value="<%=buttonText%>" onClick="processChartButton(this.form,<%=widgetId%>,'<%=tab_set%>-<%=tab_num%>')">

	</FORM>
    </p>

<script id="source" language="javascript" type="text/javascript">
$(function () {


});


</script>

 </body>
</html>
