<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import = "org.mitre.medcafe.model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<%
	String patientId = request.getParameter("patient_id");
	PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    if( cache == null )
    {  //nobody is logged in
        //log.warning("No patient selected");
        response.sendRedirect("introPage.jsp");
        return;
    }
    patientId = cache.getDatabasePatientId();
%>
<head>

	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />

	<title>medCafe</title>

	<link type="text/css" href="${css}/custom-theme/jquery-ui-1.7.2.custom.css" rel="stylesheet" />
  	<link type="text/css" href="${css}/custom.css" rel="stylesheet" />
	<link type="text/css" href="${css}/demo_page.css" rel="stylesheet" />
	<link type="text/css" href="${css}/demo_table.css" rel="stylesheet" />
  	
	<script type="text/javascript" src="${js}/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="${js}/ui.all-1.7.1.js"></script>
	<script type="text/javascript" language="javascript" src="${js}/jquery.delay.js"></script>
	<script type="text/javascript" src="${js}/vel2jstools.js"></script>
	<script type="text/javascript" src="${js}/vel2js.js"></script>
	<style>
		td {font-size:0.8em};
	</style>
	<script>
        var outerLayout;

        var tabID;
        /*
        *#######################
        *		 ON PAGE LOAD
        *#######################
        */

        $(function(){
            //Initialize Accordion with second accordion open
		    	$("#accordion").accordion({
		       		active: 1,
		       		collapsible: true,
		       		autoHeight: false
		   		});
        });

       
	</script>
    <%--  {{{ css --%>
    <style type='text/css'>
        /* #calendar {
            width: 250px;
            margin: 0 auto;
            } */
    </style>

</head>
<body>


    <div id="head"></div>


<div class="ui-corner-all">
	<div id="accordion">
    	<h3><a href="#">Add Patient Details</a></h3>
    	<div>
			<p>
				<iframe height="400"  frameborder="0" name="associatePatient" id="general_widgetsFrame" src="http://${server}/searchRepositoryPatient.jsp?patient_id=<%=patientId%>"></iframe>
			</p>
		</div>
		<h3><a href="#">Address</a></h3>
    	<div>
			<p>
				<iframe height="400" frameborder="0" name="addAddress" id="general_widgetsFrame" src="http://${server}/listAddress.jsp?patient_id=<%=patientId%>"></iframe>
			</p>
		</div>
		<h3><a href="#">Patient History</a></h3>
    	<div>
    		<p>
				<iframe height="400"  frameborder="0" name="patientHistoryframe" id="patient_widgetsFrame" src="http://${server}/listHistoryTemplate.jsp"></iframe>
			</p>
    	</div>
	</div>
</div>

</body>

</html>