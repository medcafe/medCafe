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

	<link type="text/css" href="${css}/custom-theme/jquery-ui-1.8.6.custom.css" rel="stylesheet" />
  	<link type="text/css" href="${css}/custom.css" rel="stylesheet" />
	<link type="text/css" href="${css}/demo_page.css" rel="stylesheet" />
	<link type="text/css" href="${css}/demo_table.css" rel="stylesheet" />
  	<link type="text/css" rel="stylesheet" href="${css}/treeview/jquery.treeview.css" />
	<link type="text/css" rel="stylesheet" href="${css}/treeview/screen.css" />
	<link type="text/css" rel="stylesheet" href="${css}/jqzoom.css" >
	<link type="text/css" rel="stylesheet" href="${css}/jquery.iviewer.css" />

	<script type="text/javascript" src="${js}/jquery-1.4.4.js"></script>
	<script type="text/javascript" src="${js}/jquery.layout.js"></script>
	<script type="text/javascript" src="${js}/jquery-ui-1.8.6.custom.min.js"></script>
	<script type="text/javascript" src="${js}/custom.js"></script>
	<script type="text/javascript" src="${js}/jquery.highlight.js"></script>
	<script type="text/javascript" language="javascript" src="${js}/jquery.dataTables.js"></script>
	<script type="text/javascript" language="javascript" src="${js}/jquery.delay.js"></script>
	<script src="${js}/treeview/jquery.treeview.js" type="text/javascript"></script>
	<script language="javascript" type="text/javascript" src="${js}/jquery.flot.js"></script>
 	<script language="javascript" type="text/javascript" src="${js}/jquery.flot.selection.js"></script>
 	<script type="text/javascript" src="${js}/jquery.iviewer.js" ></script>
 	<script type="text/javascript" src="${js}/jquery.mousewheel.js" ></script>
 	<script src="${js}/jqzoom.pack.1.0.1.js" type="text/javascript"></script>


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
    <div id="dialog" >Are you sure you want to close?</div>


<div class="ui-layout-center ui-corner-all">
	<div id="tabs" >
	    <ul class="tabs" id ="test">

			<li custom:index="0" id="tabs-1-link" name="tabs-1" class="tabHeader"><a href="#tabs-1">Tab 1</a><div class="close"></div></li>
	        <li custom:index="1" id="tabs-2-link" name="tabs-2" class="tabHeader"><a href="#tabs-2">Tab 2</a><div class="close"></div></li>
	        <li custom:index="2" id="tabs-3-link" name="tabs-3" class="tabHeader"><a href="#tabs-3">Tab 3</a><div class="close"></div></li>

	    <!-- add wrapper that Layout will auto-size to 'fill space' -->

    	</ul>

    	 <div class="ui-layout-content" id="tabs-layout">

		        <div id="tabs-1" class="tabContent" >
		        	<p>

					<div id="columns">

					      <div id="column1" class="column">
					            <div class="widget color-1" id="green-widget">
					                <div class="widget-head">
					                    <h3>Coverflow Widget</h3>
					                </div>
					                <div class="widget-content">
					                    <iframe height="400" width="680" name="tempframe" id="tempFrame" src="zoom.html"></iframe>

					                    </div>
					            </div>
					        </div>

					    </div>

					</p>
		        </div>


		        <div id="tabs-2" class="tabContent">
			        <div id="columns">
						<div id="column2" class="column">
				            <div class="widget color-2" id="blue-widget">
				                <div class="widget-head">
				                    <h3>Patient Summary</h3>
				                </div>
				                <div class="widget-content">
							       	<div class="summary"><a href="#" class="details">11</a></div>
									<div class="summary"><a href="#" class="details">12</a></div>
									<div class="summary"><a href="#" class="details">13</a></div>
									<div class="summary"><a href="#" class="details">14</a></div>
									<div class="summary"><a href="#" class="details">15</a></div>

								</div>
				            </div>
			            </div>
		        	</div>
		        </div>

		        <div id="tabs-3" class="tabContent">
		        <p>

				<div id="columns">
					<div id="column3" class="column">
			            <div class="widget color-3" id="blue-widget">
			                <div class="widget-head">
			                    <h3>Widget title</h3>
			                </div>
			                <div class="widget-content">

						     	   <p>
					                    <iframe height="400" width="680" name="framename" id="myframe" src="http://127.0.0.1:8080/medcafe/coverflow-flash/index.jsp"></iframe>
					               </p>

							</div>
			            </div>
		            </div>
	        	</div>

				</p>
		        </div>


		    </div>
    </div>


</div>



<div class="ui-layout-east  ui-corner-all">


	<div id="columns">

		<div id="column21" class="column">

			<div class="widget color-1">
					<div class="widget-head">
						   <h3>Menu</h3>
					</div>
					<div class="widget-content">

						 <p>
						 	<iframe height="400" width="155" name="widgetframe" id="widgetFrame" src="http://127.0.0.1:8080/medcafe/widgets-list.jsp"></iframe>
					     </p>


					</div>
				</div>
			</div>
	</div>
</div>


<div class="ui-layout-west  ui-corner-all">

	<!-- div id="tabs2">
	    <ul class="tabs">

			<li custom:index="0" id="tabs-9-link" name="tabs-9"><a href="#tabs-9">Tab 9</a></li>

	        <div class="ui-layout-content" id="tabs-layout1">

		     	<div id="tabs-9" class="tabContent"-->
		      <div id="columns">

					        <div id="column10" class="column">
					            <div class="widget color-6" id="intro">
					                <div class="widget-head">
					                    <h3>Repositories</h3>
					                </div>
					                <div class="widget-content">
						                 <p>

											<div id="main">
											<a href=".">Main Demo</a>

											<ul id="browser" class="filetree treeview-famfamfam">
												<li>

													<span class="folder repository">Repositories</span>
													<div id="listRepository"></div>

													<span class="file summary">Add Coverflow
														<div class="images" custom:url="http://127.0.0.1:8080/medcafe/coverflow-flash/index.jsp">Images</div>
													</span>

													<ul>
														<li>
															<span class="folder">OurVista</span>

																<ul>
																<span class="repository">
																<a href="#" class="repList" custom:server="127.0.0.1:8080/medcafe/c">OurVista</a>
																<li>
																</li>
																</span>
															</ul>

														</li>

														<li><span class="folder">MedsphereVista</span>
															<ul>
																<li><span class="file">Patient
																	<span class="summary"><a href="#" class="details">11</a><a href="#" class="images">11</a></span>
																	</span>
																</li>
															</ul>
														</li>
														<li><span class="folder">medCafe</span>
															<ul>
																	<li><span class="file">Patient
																	<span class="summary"><a href="#" class="details">12</a></span>
																	</span></li>
																	<li><span class="file">
																	Patient
																	<span class="summary"><a href="#" class="details">13</a></span>

																	</span></li>
															</ul>
														</li>
														<li><span class="folder">Charts</span>
															<ul>
																	<li>
																		<span class="file">Temp Chart
																			<a href="#" class="chart" custom:url="http://127.0.0.1:8080/medcafe/chart.jsp"/>12</a>
																		</span>
																	</li>
																	<li><span class="file">
																	Patient
																	<span class="summary"><a href="#" class="details">13</a></span>

																	</span></li>
															</ul>
														</li>
														<li class="closed"><span class="folder">JeffVista</span>
															<ul>
																<li><span class="file">Patient
																<span class="summary"><a href="#" class="details">15</a></span>


																</span></li>
															</ul>
														</li>
													</ul>
												</li>
											</ul>

											<button id="add">Add!</button>

											<p>+/- Icons from <a href="http://www.famfamfam.com/lab/icons/">famfamfam</a></p>

										</div>
					                    </p>


					                </div>
					            </div>
					        </div>

					    </div>
</div>
		     <!--	</div>
			</div>

	  </ul>
    </div>
</div-->

<div class="ui-layout-north">

</div>





<div class="ui-layout-south">
    <div id="south-tabs">
        <ul>
            <li><a href="#south-tabs-1">Email</a></li>
            <li><a href="#south-tabs-2">PubMed</a></li>
            <li><a href="#south-tabs-3">Journal of Medical Internet Research</a></li>
        </ul>
        <div id="south-tabs-1">
            <iframe src ="samplecontent/Inbox - Outlook Web Access Light.html" width="100%" height="700">
                <p>Your browser does not support iframes.</p>
            </iframe>
        </div>
        <div id="south-tabs-2">
            <iframe src ="samplecontent/PubMed home.html" width="100%" height="700">
                <p>Your browser does not support iframes.</p>
            </iframe>
        </div>
        <div id="south-tabs-3">
            <iframe src ="samplecontent/JMIR Home.html" width="100%" height="700">
                <p>Your browser does not support iframes.</p>
            </iframe>
        </div>
    </div>

</div>


</body>
	<link href="css/inettuts.js.css" rel="stylesheet" type-"text/css"/>
		<script type="text/javascript" src="js/widgets/inettuts.js"></script>
     	<link href="css/inettuts.css" rel="stylesheet" type="text/css" />


</html>