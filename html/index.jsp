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
	
	<script type="text/javascript" src="${js}/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="${js}/jquery.layout.js"></script>
	<script type="text/javascript" src="${js}/ui.all-1.7.1.js"></script>
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
 	<script type="text/javascript" src="${js}/jquery.annotate.js"></script>

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
	      	
	    <!-- add wrapper that Layout will auto-size to 'fill space' -->
		   
    	</ul>
    	
    	
		        <div id="tabs-1" class="tabContent">
		        <p>

				<div id="columns">
					<div id="column1" class="column">
			            <div class="widget color-1" id="blue-widget">
			                <div class="widget-head">
			                    <h3>CoverFlow</h3>
			                </div>
			                <div class="widget-content">
						     	   
						     	   <p>
					                    <iframe height="400" width="680" name="framename" id="myframe" src="http://${server}/coverflow-flash/index.jsp"></iframe>
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

	<div id="accordion">
    	<h3><a href="#">General Widgets</a></h3>
    	<div>
			<p>
				<iframe height="400" width="155" name="widgetframe" id="widgetFrame" src="http://${server}/widgets-list.jsp"></iframe>
			</p> 
					
		</div>
		<h3><a href="#">Patient Specific</a></h3>
    	<div>
    		<p>
				<iframe height="400" width="155" name="patientWidgetframe" id="patientWidgetframe" src="http://${server}/widgets-list.jsp?type=patient_widgets"></iframe>
			</p>
    	</div>
	
	</div>
</div>


<div class="ui-layout-west  ui-corner-all">

	
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
													
													<ul>
														<li>
															<span class="folder">OurVista</span>
															
																<ul>
																<span class="repository">
																<a href="#" class="repList" custom:server="${server}/c">OurVista</a>			
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
		   
<div class="ui-layout-north">
 						
</div>





<div class="ui-layout-south"></div>


</body>
		<script type="text/javascript" src="js/widgets/inettuts.js"></script>
     	<link href="css/inettuts.css" rel="stylesheet" type="text/css" />
     	
     	
</html>