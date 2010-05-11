<%@ page import="org.mitre.medcafe.util.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
 "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<link type="text/css" href="${css}/custom-theme/jquery-ui-1.7.2.custom.css" rel="stylesheet" />
<link type="text/css" href="${css}/custom.css" rel="stylesheet" />
<link href="css/inettuts.css" rel="stylesheet" type="text/css" />	
<script type="text/javascript" src="${js}/jquery-1.3.2.js"></script>
<script type="text/javascript" src="${js}/medCafe.repository.js"></script>
<script type="text/javascript" src="${js}/vel2jstools.js"></script>
	<script type="text/javascript" src="${js}/vel2js.js"></script>
<%
	String repositoryUrl ="/repositories";
%>

<script>
$(function(){
	
	listRepositories("true");
	listRecentPatients();
	
});

function updateAnnouncements(data)
{
	
    if(data.announce)
    {
    	/*$.each(data.announce, function(i, item){
            alert("item " + i + " value " + item);
        });*/
    	var html = v2js_announcements(data);
        $('#announcements').html(html);
    }
    else
    {
        $('#announcements').html("");
    }
}

function listRecentPatients()
{
	 
	 	var serverLink ="recentPatientsJSON.jsp";
	 	
	 	$.getJSON(serverLink,
		      function(data)
		      {
		      	  //Check to see if any error message
				  
				  updateAnnouncements(data);
				  if (data.announce)
				  {
					return;
				  }
				  var html = v2js_listSearchPatientsSelect( data );  	 

			      $("#recent_patients").html( html);
			      $("#recent_patients").change(function() 
				  {
		    		var src = $("option:selected", this).val();
		    		//Get details for this patient
		    		window.location.replace("http://${server}/index.jsp?patient_id=" + src);	
	    		  });
		      });
		      
		      
}
</script>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
	
</head>
<body>
<div class="ui-corner-all" >
 <center><img style="margin-bottom:1.5em" alt="logo" class="ui-corner-all" src="${images}/medCafe_logo.png"/></center>
   </div>
<div class="ui-corner-all" >
        
        <div class="widget color-5" id="intro"  >
        	<div class="ui-widget-header ui-corner-all">
         	<center><h2 id="patient-search">Repository List</h6></center>
         	</h2>
        	</div>
            <div class="ui-widget-content ui-corner-all color-5">
                <p>
                	<div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
                	
                	<center>
                	<p><div id="listRepositories"></div>
                	</center>
                	</div>
                </p>
            </div>
        </div>
       
        <div class="widget color-5" id="intro"  >
        	<div class="ui-widget-header ui-corner-all">
         	<center><h2 id="patient-search">Patient Search</h6></center>
         	</h2>
        	</div>
            <div class="ui-widget-content ui-corner-all color-5">
                <p>
                	<div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
                	<center>
                    <iframe frameborder="0" height="200" width="1000" name="patientSearchframe" id="patient_searchFrame" src="http://${server}/searchPatients.jsp?intro=true"></iframe>
                	</center>
                	</div>
                </p>
            </div>
        </div>

		<div class="widget color-5" id="recent"  >
        	<div class="ui-widget-header ui-corner-all">
         	<center><h2>Recent Patients</h6></center>
         	</h2>
        	</div>
        	
            <div class="ui-widget-content ui-corner-all color-5">
                <p>
                	<div class="ui-state-highlight ui-corner-all" style="padding: .7em;">
                	<center>
                		<p><div id="recent_patients"></div></p>
                    </center>
                    </div>
                </p>
            </div>
        </div>
        
</div>

<noscript>
This page is the introductory page to medcafe. Please enable Javascript in your browser to see the full page. Thank you.
</noscript>
</body>
</html>