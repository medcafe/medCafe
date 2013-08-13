<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ page import="org.mitre.medcafe.util.*" %>
<%@ page import="org.mitre.medcafe.model.*" %>
<%@ page import="java.util.logging.*" %>
<%@ page import="java.util.HashMap" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" >
<html>
<%!
    public final static String KEY = "/index.jsp";
    public final static Logger log = Logger.getLogger( KEY );
    //static{log.setLevel(Level.FINER);}%><%
    PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    if( cache == null )
    {  //nobody is logged in
        log.warning("No patient selected");
        response.sendRedirect("introPage.jsp");
        return;
    }


    /*
	String patientId = request.getParameter("patient_id");
	if (patientId == null)
	{
		Object patientObj = session.getAttribute("patient");
		if (patientObj != null)
		 	patientId = patientObj.toString();
	}
	if (patientId == null)
		patientId = "1";
    */
    
	// Object repositoryIdObjs = 	session.getAttribute("repPatientIds");
	String cssLink = "<!-- link type=\"text/css\" href=\"css/custom-theme/jquery-ui-1.8.6.custom.css\" rel=\"stylesheet\" />";
%>
<head>

	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />

	<title>medCafe</title>
	<!-- link type="text/css" href="${css}/custom-theme/jquery-ui-1.8.9.custom.css" rel="stylesheet" /-->
	<link type="text/css" href="${css_theme}" rel="stylesheet" />
	<link type="text/css" href="${css_widget}" rel="stylesheet" />
	
	<link type="text/css" href="${css}/demo_page.css" rel="stylesheet" />
	<link type="text/css" href="${css}/demo_table.css" rel="stylesheet" />
  	<link type="text/css" rel="stylesheet" href="${css}/treeview/jquery.treeview.css" />
	<link type="text/css" rel="stylesheet" href="${css}/treeview/screen.css" />
	<link type="text/css" rel="stylesheet" href="${css}/jqzoom.css" />
	<link type="text/css" rel="stylesheet" href="${css}/jquery.iviewer.css" />

	<link type="text/css" rel="stylesheet" href="${css}/fg.menu.css" />
	<link type="text/css" rel="stylesheet" href="${css}/fullcalendar.css" />
	<link rel="Stylesheet" href="${css}/ui.slider.extras.css" type="text/css" />
	<link type="text/css" rel="stylesheet" href="css/editor/jquery.rte.css" />

	<script type="text/javascript" src="${js}/jquery-1.4.4.js"></script>
	<script type="text/javascript" src="${js}/jquery.layout-latest.js"></script>

	<link href="css/inettuts.js.css" rel="stylesheet" type="text/css"/>

    <link href="css/inettuts.css" rel="stylesheet" type="text/css" />

	<script type="text/javascript" src="${js}/medCafe.js"></script>
	<script type="text/javascript" src="${js}/medCafeTabs.js"></script>
	<script type="text/javascript" src="${js}/jquery.highlight.js"></script>
	<script type="text/javascript" language="javascript" src="${js}/jquery.dataTables.js"></script>
	<script type="text/javascript" language="javascript" src="${js}/jquery.delay.js"></script>
	<script src="${js}/treeview/jquery.treeview.js" type="text/javascript"></script>
	<!--[if IE]><script type="text/javascript" src="${js}/excanvas.js"></script><![endif]-->
	<script language="javascript" type="text/javascript" src="${js}/jquery.flot.js"></script>
 	<script language="javascript" type="text/javascript" src="${js}/jquery.flot.selection.js"></script>
    <script language="javascript" type="text/javascript" src="${js}/datagrid/js/jquery.jqGrid.min.js"></script>

 	<script type="text/javascript" src="${js}/jquery.mousewheel.js" ></script>
 	<script src="${js}/jqzoom.pack.1.0.1.js" type="text/javascript"></script>
	 	<script type="text/javascript" src="${js}/jScrollTouch.js"></script>
	<script type="text/javascript" src="${js}/vel2jstools.js"></script>
	<script type="text/javascript" src="${js}/vel2js.js"></script>
	<script type="text/javascript" src="${js}/jquery.jeditable.js"></script>
	<script type="text/javascript" src="${js}/jquery-ui-1.8.6.custom.min.js"></script>
	<script type="text/javascript" src="${js}/jquery.spinner.js"></script>
	<script type="text/javascript" src="js/widgets/inettuts.js"></script>
	<script type="text/javascript" src="${js}/menu/fg.menu.js"></script>
	<script type="text/javascript" src="${js}/menus.js"></script>
	<script type="text/javascript" src="${js}/fullcalendar.js"></script>
	<script type="text/javascript" src="${js}/medCafe.widget.js"></script>
	<script type="text/javascript" src="${js}/medCafe.calendar.js"></script>
    <script type="text/javascript" src="${js}/medCafe.patients.js"></script>
	<script type="text/javascript" src="${js}/medCafe.repository.js"></script>
	<script type="text/javascript" src="${js}/medCafeSouthTabs.js"></script>
	<script type="text/javascript" src="${js}/medCafe.touch.js"></script>
 	<script type="text/javascript" src="${js}/medCafe.images.js"></script>
 	<script type="text/javascript" src="${js}/medCafe.templates.js"></script>
 	<script type="text/javascript" src="${js}/medCafe.preferences.js"></script>
 	<script type="text/javascript" src="${js}/jquery.ui.autocomplete.js"></script>
 	<script type="text/javascript" src="${js}/medCafe.insert.js"></script>

 	<script type="text/javascript" src="${js}/selectToUISlider.jQuery.js"></script>

 	<script language="JavaScript" type="text/javascript" src="contentflow/contentflow_src.js" load="white medCafe"></script>
    <script type="text/javascript" src="${js}/jquery.iviewer.js" ></script>

    <script type="text/javascript" src="${js}/editor/jquery.rte.js"></script>
	<script type="text/javascript" src="${js}/editor/jquery.rte.tb.js"></script>
 	<script type="text/javascript" src="${js}/jquery.qtip-1.0.0-rc3.min.js"></script>
 	<script type="text/javascript" src="js/jquery.annotate.js"></script>

    <link rel="stylesheet" media="screen" type="text/css" href="js/datagrid/css/ui.jqgrid.css" />
    <script src="js/datagrid/js/ui.multiselect.js" type="text/javascript"></script>
    <script src="js/datagrid/js/jquery.tablednd.js" type="text/javascript"></script>
    <script src="js/datagrid/js/jquery.contextmenu.js" type="text/javascript"></script>
    <script src="js/datagrid/js/grid.locale-en.js" type="text/javascript"></script>
    <script src="js/datagrid/js/jquery.jqGrid.min.js" type="text/javascript"></script>
   
 	<script language="javascript" type="text/javascript" src="js/jquery.flot.js"></script>
 	<script language="javascript" type="text/javascript" src="js/jquery.flot.selection.js"></script>
	<script type="text/javascript">
 	   Timeline_ajax_url="http://${server}/js/timeline/simile-ajax-api.js";
	   Timeline_urlPrefix='http://${server}/js/timeline/';
       Timeline_parameters='bundle=true';
    </script>
    <script src="http://${server}/js/timeline/timeline-api.js?bundle=true" type="text/javascript">
    </script>
    <script src="js/timeline/examples.js" type="text/javascript"></script>
    <!-- script src="http://127.0.0.1:8080/medcafe/js/timeline/timeline-api.js?bundle=true" type="text/javascript">
    </script>
    <script src="http://127.0.0.1:8080/medcafe/js/timeline/simile-ajax-api.js" type="text/javascript"></script>
    <script src="http://127.0.0.1:8080/medcafe/js/timeline/simile-ajax-bundle.js" onerror="" type="text/javascript"></script>
    <script src="http://127.0.0.1:8080/medcafe/js/timeline/timeline-bundle.js" onerror="" type="text/javascript"></script>
    <link rel="stylesheet" href="http://127.0.0.1:8080/medcafe/js/timeline/timeline-bundle.css" type="text/css">
    <script src="http://127.0.0.1:8080/medcafe/js/timeline/scripts/l10n/en/timeline.js" onerror="" type="text/javascript"></script>
    <script src="http://127.0.0.1:8080/medcafe/js/timeline/scripts/l10n/en/labellers.js" onerror="" type="text/javascript"></script>
    <link rel="stylesheet" href="http://127.0.0.1:8080/medcafe/js/timeline/styles/graphics.css" type="text/css"-->
  	<link type="text/css" href="${css}/custom.css" rel="stylesheet" />

	<script type="text/javascript">
        var outerLayout;
		var repositoryPatientJSON = {};

        var tabID;
        /*
        *#######################
        *		 ON PAGE LOAD
        *#######################
        */

        $(function(){
           refresh("<%=cache.getDatabasePatientId()%>");
        });


        initialize();
		getHeader();

		function getHeader()
		{

		}

		function initialize(repositoryJSON)
		{
			repositoryPatientJSON = getAssocPatientRepositories("<%=cache.getDatabasePatientId()%>");
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
<body ontouchmove="BlockMove(event);" class="ui-widget-content">


    <div id="head"></div>
    <div id="dialog" >Are you sure you want to close?</div>
	<div id="dialogTemplate" ></div>
   <div id="dialogInsert"></div>
	<div id="saveDialog" style="visibility:hidden;" >You are about to close all tabs for this patient. Would you like to save changes?</div>
    <div id="preferencesDialog" ></div>

    <div id="copyTemplateDialog" style="visibility:hidden;">You are about to override all tabs for this patient. Would you like to continue?</div>
    <div id="associatePatientDialog"></div>

<div class="ui-layout-center ui-corner-all no-copy">
	<div id="announcements"></div>
	<div id="tabs" >
	    <ul class="tabs" id ="test">
	    <!-- add wrapper that Layout will auto-size to 'fill space' -->
    	</ul>

    </div>
</div>


<div class="ui-layout-east  ui-corner-all">
	<div id="accordion">
    	<h3><a href="#">General Widgets</a></h3>
    	<div>

				<div id="generalWidgets"></div>
				<!-- iframe height="400" frameborder="0" width="155" name="widgetframe" id="general_widgetsFrame" src="http://${server}/widgets-list.jsp"></iframe-->

		</div>
		<h3><a href="#">Patient Specific</a></h3>
    	<div>

    			<div id="patientWidgets"></div>
				<!--iframe height="400" frameborder="0" width="155" name="patientWidgetframe" id="patient_widgetsFrame" src="http://${server}/widgets-list.jsp?type=patient_widgets"></iframe-->

    	</div>
	</div>
</div>

<div class="ui-layout-west  ui-corner-all">
    <div id="west-sections" class="ui-layout-content">
    	<h6 id="patient-search"><center><a href="#">Patient Search</a></center></h6>
		<div class="widget" id="intro" >
			<div class="widget-content">
         	<p>
           		<object  height="200" width="240" id="patient_searchFrame" data="http://${server}/searchPatients.jsp"><PARAME name-frameborder value=0></object>
            </p>
			</div>
		</div>
      <h6><center><a href="#" >Schedule</a></center></h6>
      <div id="calendar"  class="widget-content"></div>
      <h6 id="#"><center><a href="#">Options</a></center></h6>
		<div id="OptionWindow">
			<a tabindex="0" href="#search-engines" class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all" id="flat">
         <span class="ui-icon ui-icon-triangle-1-s"></span>Tabs
         </a>
         <div id="search-engines" class="hidden"></div>
          	<%-- <button id="addTabBtn">Add Tab</button> --%>

       	<a tabindex="1" href="#template_list" class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all" id="template">
         <span class="ui-icon ui-icon-triangle-1-s"></span>Templates
        	</a>
         <div id="template_list" class="hidden"></div>
       	<a tabindex="2" href="logout.jsp" class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all" id="flat">Logout</a>
		<a tabindex="3" class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all" id="prefs">Preferences</a>
		<a tabindex="4" class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all" id="refreshCache">Refresh Patient Cache</a>
		</div>
		</div>
	</div>
</div>
<div class="ui-layout-north" id="banner2">
		<jsp:include page="banner.jsp"/>
</div>
<div class="ui-layout-south">
    <div id="south-tabs">

    </div>
</div>
<div id="clone" class="copy"></div>
<jsp:include page="schedulePopup.html"/>


</body>



</html>
