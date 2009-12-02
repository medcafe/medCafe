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

	<link type="text/css" href="${css}/ui.tabs.css" rel="stylesheet" />
  	<link type="text/css" href="${css}/ui.theme.css" rel="stylesheet" />
 	<link type="text/css" href="${css}/droppable-tabs.css" rel="stylesheet" />
  	<link type="text/css" href="${css}/custom.css" rel="stylesheet" />
	<link type="text/css" href="${css}/demo_page.css" rel="stylesheet" />
	<link type="text/css" href="${css}/demo_table.css" rel="stylesheet" />
  
	<script type="text/javascript" src="${js}/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="${js}/jquery.layout.js"></script>
	<script type="text/javascript" src="${js}/ui.all-1.7.1.js"></script>
	<script type="text/javascript" src="${js}/custom.js"></script>
	<script type="text/javascript" src="${js}/jquery.highlight.js"></script>
	<script type="text/javascript" language="javascript" src="${js}/jquery.dataTables.js"></script>
	<script type="text/javascript" language="javascript" src="${js}/jquery.delay.js"></script>


	<script>
	var outerLayout;

    var tabID;
	/*
	*#######################
	*		 ON PAGE LOAD
	*#######################
	*/
	
	</script>
	
</head>
<body>
<div id="head">
    </div>
<div class="ui-layout-center">
	<div id="tabs" >
	    <ul class="tabs" id ="test">

			<li custom:index="0" id="tabs-1-link" name="tabs-1" class="tabHeader"><a href="#tabs-1">Tab 1</a></li>
	        <li custom:index="1" id="tabs-2-link" name="tabs-2" class="tabHeader"><a href="#tabs-2">Tab 2</a></li>
	        <li custom:index="2" id="tabs-3-link" name="tabs-3" class="tabHeader"><a href="#tabs-3">Tab 3</a></li>
	  		<li custom:index="3" id="tabs-4-link" name="tabs-4" class="tabHeader"><a href="#tabs-4">Tab 4</a></li>

	    <!-- add wrapper that Layout will auto-size to 'fill space' -->
		    <div class="ui-layout-content" id="tabs-layout">

		        <div id="tabs-1" class="tabContent" >
		        	<p>

					<div id="columns">

					      <div id="column1" class="column">
					            <div class="widget color-green" id="green-widget">
					                <div class="widget-head">
					                    <h3>Coverflow Widget</h3>
					                </div>
					                <div class="widget-content">
					                    <p>
					                    <iframe height="400" width="680" name="framename" id="myframe" src="coverflow/coverflow.html"></iframe>
					                    </p> 
					                    </div>
					            </div>
					        </div>

					    </div>

					</p>
		        </div>
		        
		        
		        <!-- div id="tabs-2" class="tabContent">
		        </div-->
		        
		        <div id="tabs-3" class="tabContent">
		        <p>

				<div id="columns">
					<div id="column3" class="column">
			            <div class="widget color-blue" id="blue-widget">
			                <div class="widget-head">
			                    <h3>Widget title</h3>
			                </div>
			                <div class="widget-content">
						       <div id="aaa"></div>
									        	        
							</div>
			            </div>
		            </div>
	        	</div>

				</p>
		        </div>
		          <!--  div id="tabs-4" class="tabContent" >
			        <p>

						<div id="columns">
							<div id="column4" class="column">
					            <div class="widget color-yellow" id="yellow-widget">
					                <div class="widget-head">
					                    <h3>Yellow Widget</h3>
					                </div>
					                <div class="widget-content">
					                    <p>Morbi tincidunt, dui sit amet facilisis feugiat</p>
					                </div>
					            </div>
				            </div>
			        	</div>

					</p>
		        </div-->
		      

		    </div>
    	</ul>
    </div>

 	
</div>



<div class="ui-layout-east">
<h3>East Pane</h3>

	<div id="tabs1">
	    <ul class="tabs">

			<li custom:index="0" id="tabs-4-link" name="tabs-4"><a href="#tabs-4">Tab 4</a></li>
	        <li custom:index="1" id="tabs-5-link" name="tabs-5"><a href="#tabs-5">Tab 5</a></li>
	        <li custom:index="2" id="tabs-6-link" name="tabs-6"><a href="#tabs-6">Tab 6</a></li>

	    <!-- add wrapper that Layout will auto-size to 'fill space' -->
		    <div class="ui-layout-content" id="tabs-layout1">

		     <div id="tabs-4" >
		      <button id="addButton">Click to add a tab</button>
		      </div>

		     <div id="tabs-5" >
		        	<p>Morbi tincidunt, dui sit amet facilisis feugiat, </p>
		        </div>
		        <div id="tabs-6" >
		        <p>Mauris eleifend est et turpis. Duis id erat. Suspendisse potenti. </p>
		        </div>
		    </div>
	  </ul>
    </div>
</div>


<div class="ui-layout-west">
 					<div id="columns">

					        <div id="column1" class="column">
					            <div class="widget color-red" id="intro">
					                <div class="widget-head">
					                    <h3>Introduction Widget</h3>
					                </div>
					                <div class="widget-content">
					                    <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aliquam magna sem, fringilla in, commodo a, rutrum ut, massa. Donec id nibh eu dui auctor tempor. Morbi laoreet eleifend dolor. Suspendisse pede odio, accumsan vitae, auctor non, suscipit at, ipsum. Cras varius sapien vel lectus.</p>
					                </div>
					            </div>
					        </div>

					    </div>
</div>
</div>


<div class="ui-layout-north">


<div class="ui-layout-south">South</div>

<script type="text/javascript">
						//alert('in here');
						//Need some way to pause between these
						var patientId= <%= patientId %>
						
						$(window).bind("load",{},
						
						function(e){

								//var patientId=1;
								
								$("#aaa").load("http://127.0.0.1:8080/medcafe/c/patient/" + patientId);
					
								/*$(this).delay(80,function()
								{
									$("#example").dataTable( {
										"aaSorting": [[ 1, "desc" ]]
									} );
								} );*/
						} );
		</script>
</body>
		<script type="text/javascript" src="js/widgets/inettuts.js"></script>
     	<link href="css/inettuts.css" rel="stylesheet" type="text/css" />
     	
     	<script type="text/javascript">
	     	$(document).ready( function() {
				
			});
			
			$('#addButton').bind("click",{},
				
				function(e)
				{
					var tab_num = 15;
					var hrefBase = "tabs-" + tab_num;
					var label = "Tab " + tab_num;
					var patientId = 2;
					//Add a new Tab
					$('#tabs').tabs("add","#" + hrefBase,label);
					
					//Load the widget template
					$("#tabs-" + tab_num ).load("tabs-template.jsp?tab_num=" + tab_num);
					
					//Delay to let the DOM refresh
					$(this).delay(500,function()
					{
						iNettuts.refresh("yellow-widget" + tab_num);
					
						//Add the patient data
						$("#aaa" + tab_num).load("http://127.0.0.1:8080/medcafe/c/patient/" + patientId);
						
						//Delay to let DOM refresh before adding table styling
						$(this).delay(500,function()
						{
							alert( $("#example" + patientId).text());
							$("#example" + patientId).dataTable( {
								"aaSorting": [[ 1, "desc" ]]
							} );
						} );
						
					} );
				} );
			
			
			//iNettuts.addWidgetControls();
     	</script>
     	
</html>