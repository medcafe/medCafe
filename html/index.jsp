<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />

	<title>Droppable Between Panes</title>

	<link type="text/css" href="${css}/ui.tabs.css" rel="stylesheet" />
  	<link type="text/css" href="${css}/ui.theme.css" rel="stylesheet" />
 	<link type="text/css" href="${css}/droppable-tabs.css" rel="stylesheet" />

	<script type="text/javascript" src="${js}/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="${js}/jquery.layout.js"></script>
	<script type="text/javascript" src="${js}/custom.js"></script>
	<script type="text/javascript" src="${js}/ui.all-1.7.1.js"></script>

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

	    <!-- add wrapper that Layout will auto-size to 'fill space' -->
		    <div class="ui-layout-content" id="tabs-layout">

		        <div id="tabs-1" >
		        	<p>

					<div id="columns">

					        <div id="column1" class="column">
					            <div class="widget color-green" id="intro">
					                <div class="widget-head">
					                    <h3>Introduction Widget</h3>
					                </div>
					                <div class="widget-content">
					                    <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aliquam magna sem, fringilla in, commodo a, rutrum ut, massa. Donec id nibh eu dui auctor tempor. Morbi laoreet eleifend dolor. Suspendisse pede odio, accumsan vitae, auctor non, suscipit at, ipsum. Cras varius sapien vel lectus.</p>
					                </div>
					            </div>
					        </div>
					        Should be a frame here
					        <%-- <c:import url="http://127.0.0.1:8080/medcafe/c/patient/1"/> --%>
					        <div id="aaa"></div>
					        <script type="text/javascript">
					            $("#aaa").load("http://127.0.0.1:8080/medcafe/c/patient/1");
					        </script>
					        <%--
					        <c:catch var="exception">
                              <c:import url="ftp://ftp.example.com/package/README"/>
                            </c:catch>
                            <c:if test="${not empty exception}">
                              Sorry, the remote content is not currently available.
                            </c:if>
                            --%>
					    </div>

					</p>
		        </div>
		        <div id="tabs-2" >
		        	<p>
		        	<div id="columns">
			        	<div id="column2" class="column">
					        <div class="widget color-red">
				                <div class="widget-head">
				                    <h3>Widget title</h3>
				                </div>
				                <div class="widget-content">
				                    <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aliquam magna sem, fringilla in, commodo a, rutrum ut, massa. Donec id nibh eu dui auctor tempor. Morbi laoreet eleifend dolor. Suspendisse pede odio, accumsan vitae, auctor non, suscipit at, ipsum. Cras varius sapien vel lectus.</p>
				                </div>
		            		</div>
	            		 </div>

					</div>
		        	</p>
		        </div>
		        <div id="tabs-3" >
		        <p>

				<div id="columns">
					<div id="column3" class="column">
			            <div class="widget color-blue">
			                <div class="widget-head">
			                    <h3>Widget title</h3>
			                </div>
			                <div class="widget-content">
			                    <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aliquam magna sem, fringilla in, commodo a, rutrum ut, massa. Donec id nibh eu dui auctor tempor. Morbi laoreet eleifend dolor. Suspendisse pede odio, accumsan vitae, auctor non, suscipit at, ipsum. Cras varius sapien vel lectus.</p>
			                </div>
			            </div>
		            </div>
	        	</div>

				</p>
		        </div>
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
		       <p>Proin elit arcu, rutrum commodo, vehicula tempus,</p>
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

</body>
		<script type="text/javascript" src="../js/widgets/inettuts.js"></script>
     	<link href="css/inettuts.css" rel="stylesheet" type="text/css" />
</html>