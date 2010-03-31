<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<%
	String patientId = request.getParameter("patient_id");
	if (patientId == null)
		patientId = "1";
%>
<head>

	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />

	<title>Droppable Between Panes</title>


	<link type="text/css" href="${css}/custom-theme/jquery-ui-1.7.2.custom.css" rel="stylesheet" />
  	<link type="text/css" href="${css}/custom.css" rel="stylesheet" />
	<link type="text/css" href="${css}/demo_page.css" rel="stylesheet" />
	<link type="text/css" href="${css}/demo_table.css" rel="stylesheet" />
  	<link type="text/css" rel="stylesheet" href="${css}/treeview/jquery.treeview.css" />
	<link type="text/css" rel="stylesheet" href="${css}/treeview/screen.css" />
	<link type="text/css" rel="stylesheet" href="${css}/jqzoom.css" >
	<link type="text/css" rel="stylesheet" href="${css}/jquery.iviewer.css" />
	<link type="text/css" rel="stylesheet" href="${css}/annotation.css" />
	<link type="text/css" rel="stylesheet" href="${css}/fg.menu.css" />
	<link type="text/css" rel="stylesheet" href="${css}/fullcalendar.css" />

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
	<script type="text/javascript" src="${js}/fullcalendar.min.js"></script>
	<script type="text/javascript" src="${js}/medCafe.widget.js"></script>

	<script>
	var outerLayout;

    var tabID;
	/*
	*#######################
	*		 ON PAGE LOAD
	*#######################
	*/

	$(function(){
	
    	$('.fg-button').hover(
    		function(){ $(this).removeClass('ui-state-default').addClass('ui-state-focus'); },
    		function(){ $(this).removeClass('ui-state-focus').addClass('ui-state-default'); }
    	);

		$.get('menuContent.html', function(data){
			$('#flat').menu({
				content: data
			});
		});
		
		var date = new Date();
		var d = date.getDate();
		var m = date.getMonth();
		var y = date.getFullYear();


		$('#calendar').fullCalendar({
			header: {
				left: 'prev,next',
				center: '',
				right: 'today'
			},
            defaultView:'agendaDay',
            height: 500,
            theme:true,
			editable: true,
			events: "getSchedule.jsp",
            eventDrop: function(event, dayDelta, minuteDelta) {
				var url = "moveAppt.jsp?id=" + event.id + "&minutes=" + minuteDelta;
                $.getJSON(url, function(json){
                    if (json.announce)
                    {
                        updateAnnouncements(json);
                        return;
                    }
                });
			},
            eventResize: function(event, dayDelta, minuteDelta) {
				var url = "resizeAppt.jsp?id=" + event.id + "&minutes=" + minuteDelta;
                $.getJSON(url, function(json){
                    if (json.announce)
                    {
                        updateAnnouncements(json);
                        return;
                    }
                });
			}

		});


        $("#west-sections").addClass("ui-accordion ui-widget ui-helper-reset")
            .find("h6")
                .addClass("ui-accordion-header ui-helper-reset ui-state-default ui-corner-top ui-corner-bottom")
                .prepend('<span class="ui-icon ui-icon-triangle-1-e"/>')
                .click(function() {
                    $(this).toggleClass("ui-accordion-header-active").toggleClass("ui-state-active")
                        .toggleClass("ui-state-default").toggleClass("ui-corner-bottom")
                    .find("> .ui-icon").toggleClass("ui-icon-triangle-1-e").toggleClass("ui-icon-triangle-1-s")
                    .end().next().toggleClass("ui-accordion-content-active").toggle();
                    return false;
                })
                .next().addClass("ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom").hide();
        $("#patient-search").click();


    });


	</script>
    <style type='text/css'>
        /* #calendar {
            width: 250px;
            margin: 0 auto;
            } */
    </style>

</head>
<body>


<div id="head">
    </div>
    <div id="dialog" >Are you sure you want to close?</div>
	<div id="saveDialog" >You are about to close all tabs for this patient. Would you like to save changes?</div>


<div class="ui-layout-center ui-corner-all">

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
				<iframe height="400" width="155" name="widgetframe" id="general_widgetsFrame" src="http://${server}/widgets-list.jsp"></iframe>
			</p>

		</div>
		<h3><a href="#">Patient Specific</a></h3>
    	<div>
    		<p>
				<iframe height="400" width="155" name="patientWidgetframe" id="patient_widgetsFrame" src="http://${server}/widgets-list.jsp?type=patient_widgets"></iframe>
			</p>
    	</div>

	</div>
</div>


<div class="ui-layout-west  ui-corner-all">
    <div id="west-sections" class="ui-layout-content">
        <h6 id="patient-search"><a href="#">Patient Search</a></h6>
        <div class="widget color-6" id="intro" >
            <div class="widget-content">
                <p>
                    <iframe height="200" width="240" name="patientSearchframe" id="patient_searchFrame" src="http://${server}/searchPatients.jsp"></iframe>
                </p>
            </div>
        </div>
        <h6><a href="#" onclick="$('#calendar').fullCalendar('refetchEvents' );">Schedule</a></h6>
        <div id="calendar"  class="widget-content"></div>
	</div>
</div>

<div class="ui-layout-north">

    <span>

    <a tabindex="0" href="#search-engines" class="fg-button fg-button-icon-right ui-widget ui-state-default ui-corner-all" id="flat">
    <span class="ui-icon ui-icon-triangle-1-s"></span>Tabs</a>
    <div id="search-engines" class="hidden">

    </div>

    </span>
</div>

<div class="ui-layout-south">

</div>
<div id="clone" class="copy"></div>
</body>
		<script type="text/javascript" src="js/widgets/inettuts.js"></script>
     	<link href="css/inettuts.css" rel="stylesheet" type="text/css" />


</html>