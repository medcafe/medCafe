<jsp:include page="header.html"/>
<jsp:include page="nav.html"/>
<head>
	<script type="text/javascript" src="js/jquery-1.4.4.js"></script>
	<script type="text/javascript" src="js/jquery-ui-1.8.6.custom.min.js"></script>
    <script type="text/javascript" src="js/vel2jstools.js"></script>
	<script type="text/javascript" src="js/vel2js.js"></script>

	<script type="text/javascript">
		$(function(){
			var url = "ccr/patients";

			$.getJSON(url, function(data)
			{
				var patientHtml = v2js_listPatients( data );
	  			var patientList = data.patients;

	 			$("#patientList").html("");

				var len = patientList.length;
	  			$("#patientList").append(patientHtml);
	  		});
	  	});

	  	function showMeds( pid )
	  	{
            $("#kibbe").html("");
			var url = "ccr/patients/" + pid + "/medications";
			$.getJSON(url, function(data)
			{
				var patientHtml = v2js_listPatientMeds( data );
	  			$("#kibbe").append(patientHtml);
	  		});
	  	}

	  	function showAlerts( pid )
	  	{
            $("#kibbe").html("");
			var url = "ccr/patients/" + pid + "/alerts";
			$.getJSON(url, function(data)
			{
				var patientHtml = v2js_listPatientAlerts( data );
	  			$("#kibbe").append(patientHtml);
	  		});
	  	}
	  	function showResults(pid)
	  		{
            $("#kibbe").html("");
			var url = "ccr/patients/" + pid + "/results";
			$.getJSON(url, function(data)
			{
				var patientHtml = v2js_listPatientResults( data );
	  			$("#kibbe").append(patientHtml);
	  		});
	  	}

    </script>

</head>
    <div id="main">
		<div id="fullcontent">

			<div id="patientList"></div>

			<div id="kibbe"></div>
        </div><!-- end content -->
    	<div class="clear"></div>
    </div><!-- end main -->
<jsp:include page="footer.html"/>

