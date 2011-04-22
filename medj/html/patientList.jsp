<jsp:include page="header.html"/>
<jsp:include page="nav.html"/>
<head>
	<script type="text/javascript" src="${js}/jquery-1.4.4.js"></script>
	<script type="text/javascript" src="${js}/jquery.layout.js"></script>
	<script type="text/javascript" src="${js}/jquery-ui-1.8.6.custom.min.js"></script>
<script type="text/javascript" src="${js}/vel2jstools.js"></script>
	<script type="text/javascript" src="${js}/vel2js.js"></script>

	<script type="text/javascript">
		$(function(){
			var url = "ccr/patients";
			
			$.getJSON(url, function(data)
			{	
				//var patientHtml = v2js_listPatients( data );  
	  			var patientList = data.patients;
	 
	 			$("#patientList").html("");
				var patientHtml;
				var len = patientList.length;
				for (i=0;i<len;i++)
	  			{
	  			   patientHtml = "<a href=\"ccr/patients/" + patientList[i] + "\">" + patientList[i] + "</a><br/>";
	  				$("#patientList").append(patientHtml);
	  			}
	  			
	  		});
	  	});
	 </script>
	  		 
</head>
    <div id="main">
		<div id="content">
			
			<div id="patientList"></div>
			
        </div><!-- end content -->
<jsp:include page="news.html"/>
    	<div class="clear"></div>
    </div><!-- end main -->
<jsp:include page="footer.html"/>

