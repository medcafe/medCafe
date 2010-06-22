<%@ page import="org.mitre.medcafe.util.*" %>
<%@ page import="org.mitre.medcafe.model.*" %>
<%@ page import="java.util.HashMap" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<%
	String patientId = request.getParameter("patient_id");
	if (patientId == null)
	{
		Object patientObj = session.getAttribute("patient");
		if (patientObj != null)
		 	patientId = patientObj.toString();
	}
	if (patientId == null)
		patientId = "1";
		
	Object repositoryIdObjs = 	session.getAttribute("repPatientIds");
	
%>
<head>

	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />

	<title>medCafe</title>

	<link type="text/css" href="${css}/custom-theme/jquery-ui-1.7.2.custom.css" rel="stylesheet" />
  	<link type="text/css" href="${css}/custom.css" rel="stylesheet" />
	<link type="text/css" href="${css}/demo_page.css" rel="stylesheet" />
	<link type="text/css" href="${css}/demo_table.css" rel="stylesheet" />
  	<link type="text/css" rel="stylesheet" href="${css}/treeview/jquery.treeview.css" />
	<link type="text/css" rel="stylesheet" href="${css}/treeview/screen.css" />
	<link type="text/css" rel="stylesheet" href="${css}/jqzoom.css" />
	<link type="text/css" rel="stylesheet" href="${css}/jquery.iviewer.css" />
	<link type="text/css" rel="stylesheet" href="${css}/annotation.css" />
	<link type="text/css" rel="stylesheet" href="${css}/fg.menu.css" />
	<link type="text/css" rel="stylesheet" href="${css}/fullcalendar.css" />
	<link rel="Stylesheet" href="${css}/ui.slider.extras.css" type="text/css" />
	<link type="text/css" rel="stylesheet" href="css/editor/jquery.rte.css" />
	
	<script type="text/javascript" src="${js}/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="${js}/jquery.layout.js"></script>
	<script type="text/javascript" src="${js}/ui.all-1.7.1.js"></script>
	<script type="text/javascript" src="${js}/medCafe.js"></script>
	<script type="text/javascript" src="${js}/medCafeTabs.js"></script>
	<script type="text/javascript" src="${js}/jquery.highlight.js"></script>
	<script type="text/javascript" language="javascript" src="${js}/jquery.dataTables.js"></script>
	<script type="text/javascript" language="javascript" src="${js}/jquery.delay.js"></script>
	<script src="${js}/treeview/jquery.treeview.js" type="text/javascript"></script>
	<script language="javascript" type="text/javascript" src="${js}/jquery.flot.js"></script>
 	<script language="javascript" type="text/javascript" src="${js}/jquery.flot.selection.js"></script>
 	<script type="text/javascript" src="${js}/jquery.iviewer.js" ></script>
 	<script type="text/javascript" src="${js}/jquery.mousewheel.js" ></script>
 	<script src="${js}/jqzoom.pack.1.0.1.js" type="text/javascript"></script>
 	<script type="text/javascript" src="${js}/jquery.annotate.js"></script>
	<script type="text/javascript" src="${js}/menu/fg.menu.js"></script>
	<script type="text/javascript" src="${js}/menus.js"></script>
	<script type="text/javascript" src="${js}/vel2jstools.js"></script>
	<script type="text/javascript" src="${js}/vel2js.js"></script>
	<script type="text/javascript" src="${js}/jquery.jeditable.js"></script>
	<script type="text/javascript" src="${js}/fullcalendar.js"></script>
	<script type="text/javascript" src="${js}/medCafe.widget.js"></script>
	<script type="text/javascript" src="${js}/medCafe.calendar.js"></script>
    <script type="text/javascript" src="${js}/medCafe.patients.js"></script>
	<script type="text/javascript" src="${js}/medCafe.history.js"></script>
	<script type="text/javascript" src="${js}/medCafe.problemList.js"></script>
	<script type="text/javascript" src="${js}/medCafeSouthTabs.js"></script>
	<script type="text/javascript" src="${js}/medCafe.touch.js"></script>
 	<script type="text/javascript" src="${js}/medCafe.images.js"></script>
 	<script type="text/javascript" src="${js}/jScrollTouch.js"></script>
 	<script type="text/javascript" src="${js}/selectToUISlider.jQuery.js"></script>
 	<script language="JavaScript" type="text/javascript" src="contentflow/contentflow_src.js" load="white"></script>
        
    <script type="text/javascript" src="${js}/editor/jquery.rte.js"></script>
	<script type="text/javascript" src="${js}/editor/jquery.rte.tb.js"></script>

 	<script type="text/javascript" src="${js}/jquery.qtip-1.0.0-rc3.min.js"></script>
	
	<script>
        var outerLayout;
		var repositoryPatientJSON = {};
		
        var tabID;
        /*
        *#######################
        *		 ON PAGE LOAD
        *#######################
        */

        $(function(){
            refresh("<%=patientId%>");
        });

        listHistory("listPatientHistory", "<%=patientId%>", "${server}", "Personal");
        listHistory("listFamilyHistory", "<%=patientId%>", "${server}", "Family");
        listProblemList("listProblemSummary", "<%=patientId%>", "${server}");
	    initialize();
	  	
		function initialize(repositoryJSON)
		{
			repositoryPatientJSON = getAssocPatientRepositories("<%=patientId%>");
		}      		
		var isiPad = navigator.userAgent.match(/iPad/i) != null;
	   
 		if (isiPad)
 		{  	
			document.addEventListener('touchmove', function(e){ e.preventDefault(); });
		 }
		function BlockMove(event) {
  			// Tell Safari not to move the window.
  			event.preventDefault() ;
 		}
 		
 		loadWidgetData(  "generalWidgets", "<%=Constants.GENERAL_WIDGETS%>");
 		loadWidgetData( "patientWidgets", "<%=Constants.PATIENT_WIDGETS%>");
 		
	</script>
    <%--  {{{ css --%>
    <style type='text/css'>
        /* #calendar {
            width: 250px;
            margin: 0 auto;
            } */
            	.no-copy {
	  			-webkit-user-select: none;
	  			}
  	
		fieldset { border:0; margin-top: 1em;}	
		.ui-slider {clear: both; top: 5em;}
    </style>

</head>
<body ontouchmove="BlockMove(event);">


    <div id="head"></div>
    <div id="dialog" >Are you sure you want to close?</div>
	<div id="saveDialog" >You are about to close all tabs for this patient. Would you like to save changes?</div>


<div class="ui-layout-center ui-corner-all no-copy">
	<div id="announcements"></div>
	<div id="tabs" >
	    <ul class="tabs" id ="test">
	    <!-- add wrapper that Layout will auto-size to 'fill space' -->
    	</ul>
        <div id="tabs-1" class="tabContent">

        </div>
    </div>
</div>

<div class="ui-layout-east  ui-corner-all">
	<div id="accordion">
    	<h3><a href="#">General Widgets</a></h3>
    	<div>
			<p>
				<div id="generalWidgets"></div>
				<!-- iframe height="400" frameborder="0" width="155" name="widgetframe" id="general_widgetsFrame" src="http://${server}/widgets-list.jsp"></iframe-->
			</p>
		</div>
		<h3><a href="#">Patient Specific</a></h3>
    	<div>
    		<p>
    			<div id="patientWidgets"></div>
				<!--iframe height="400" frameborder="0" width="155" name="patientWidgetframe" id="patient_widgetsFrame" src="http://${server}/widgets-list.jsp?type=patient_widgets"></iframe-->
			</p>
    	</div>
	</div>
</div>

<div class="ui-layout-west  ui-corner-all">
    <div id="west-sections" class="ui-layout-content">
        <h6 id="patient-search"><a href="#">Patient Search</a></h6>
        <div class="widget" id="intro" >
            <div class="widget-content">
                <p>
                    <iframe frameborder="0" height="200" width="240" name="patientSearchframe" id="patient_searchFrame" src="http://${server}/searchPatients.jsp"></iframe>
                </p>
            </div>
        </div>
        <h6><a href="#" >Schedule</a></h6>
        <div id="calendar"  class="widget-content"></div>
        <a href="logout.jsp">Logout</a>
	</div>
</div>

<div class="ui-layout-north">
    <span>
        <a tabindex="0" href="#search-engines" class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all" id="flat">
            <span class="ui-icon ui-icon-triangle-1-s"></span>Tabs
        </a>
        <div id="search-engines" class="hidden"></div>
        <button id="addTabBtn">Add Tab</button>
    </span>
    <div class="ui-widget top-panel" style="width:100px;padding:0px;text-align:center;">
        <div class="ui-state-highlight ui-corner-all" style="padding: 0em;">
            <p>
                <img alt="Patient photo" src="images/patients/photo_1.jpg" ></img>
            </p>
        </div>
    </div>
    <div class="ui-widget top-panel" id="patient_bio">
        <div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
            <p>
                <table border="0"><tr><th colspan="2" style="text-align:left;">Joe Patient</th></tr>
                    <tr><td colspan="2">40 year old male</td></tr>
                    <tr><td>BP</td><td>224/107</td></tr>
                    <tr><td>HR</td><td>78</td></tr>
                    <tr><td>Temp</td><td>98.5&deg;F</td></tr>
                </table>
            </p>
        </div>
    </div>
    <div class="ui-widget top-panel" id="patient_history">
        <div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
            <p><strong>Past Medical History</strong>
            <div id="listPatientHistory"></div>
            </p>
        	</div>
    </div>
    <div class="ui-widget top-panel" id="meds_list">
        <div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
            <p><strong>Medicine List</strong><br/>Repaglinide<br/>Ibuprofin<br/>Hydrochlorothiazide </p>
        </div>
    </div>
     
     <div class="ui-widget top-panel" id="problem_List">
        <div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
            <p><strong>Problem List</strong>
            <div id="listProblemSummary"></div>
            <!-->br/>Heart Disease<br/>Colon Cancer<br/>Smoking<br/>Alcohol Abuse</p-->
        </div>
    </div>
    <div class="ui-widget top-panel" id="family_history">
        <div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
            <p><strong>Family/Social History</strong>
            <div id="listFamilyHistory"></div>
            <!-->br/>Heart Disease<br/>Colon Cancer<br/>Smoking<br/>Alcohol Abuse</p-->
        </div>
    </div>
    <div class="ui-widget top-panel" id="allergies_list">
        <div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
            <p><strong>Allergies/Alerts</strong><br/>Penicillan Allergy</p>
        </div>
    </div>
</div>

<div class="ui-layout-south">
    <div id="south-tabs">

    </div>
</div>
<div id="clone" class="copy"></div>
<jsp:include page="schedulePopup.html"/>

</body>
		<script type="text/javascript" src="js/widgets/inettuts.js"></script>
     	<link href="css/inettuts.css" rel="stylesheet" type="text/css" />

	
</html>