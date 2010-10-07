<%@ page import="org.mitre.medcafe.util.*" %><%
String message = WebUtils.getOptionalParameter(request, "message", null);
%><html>
<head>
    <link type="text/css" href="${css}/custom-theme/jquery-ui-1.7.2.custom.css" rel="stylesheet" />
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.min.js"></script>
    <script type="text/javascript" src="http://dev.jquery.com/view/trunk/plugins/validate/jquery.validate.js"></script>
    <style type="text/css">
        label { width: 10em; float: left; }
        label.error { float: none; color: red; padding-left: .5em; vertical-align: top; }
        p { clear: both; }
        em { font-weight: bold; padding-right: 1em; vertical-align: top; }
        .submit {padding: .4em 1em .4em 20px;text-decoration: none;position: relative;}
        #registration_link {padding: .4em 1em .4em 20px;text-decoration: none;position: relative;}
        #registration_link span.ui-icon {margin: 0 5px 0 0;position: absolute;left: .2em;top: 50%;margin-top: -8px;}

    </style>
    <script>
        $(document).ready(function(){
          $("#loginForm").validate();
          // Dialog
          $('#registration').dialog({
              autoOpen: false,
              width: 625,
              buttons: {
                  "Submit": function() {
                      $("#registrationForm").submit();
                  },
                  "Cancel": function() {
                      $(this).dialog("close");
                  }
              }
          });
          // Dialog Link
          $('#registration_link').click(function(){
              $('#registration').dialog('open');

          });
          //hover states on the static widgets
          $('#registration_link, ul#icons li').hover(
              function() { $(this).addClass('ui-state-hover'); },
              function() { $(this).removeClass('ui-state-hover'); }
          );
        });
    </script>
</head>
<body>
    <center><img style="margin-bottom:1.5em" alt="logo" src="${images}/medCafe_logo.png"/></center>
    <div style="float:left;">
        <% if(message!=null)
        {%>
            <center><div class="ui-state-error ui-corner-all" style="padding:1em;width:350px;text-align:center;">
                <%=message%>
            </div></center>
        <%}%>
        <!-- registration -->
        <div id="registration" class="ui-dialog-content ui-widget-content" style="background: none; border: 0;display:none">
             <form class="cmxform" id="registrationForm" method="post" action="registrationProcess.jsp" enctype="x-www-form-urlencoded" >
                 <fieldset>
                   <legend>Register a new user</legend>
                   <p>
                     <label for="cname">User Name</label>
                     <input id="cname" name="name" size="15" remote="checkUsernameAvail.jsp" class="required" minlength="2" />
                   </p>             <p>
                     <label for="cemail">E-Mail</label>
                     <input id="cemail" name="email" size="15"  class="required email" />
                   </p>
                   <p>
                     <label for="passwd1">Password</label>
                     <input id="passwd1" name="passwd" size="15" class="required" type="password" />
                   </p>
                   <p>
                     <label for="passwd2">Confirm Password</label>
                     <input id="passwd2" name="verifyPassword" equalTo="#passwd1" size="15"  type="password" />
                   </p>
                 </fieldset>
             </form>
        </div>

        <!-- login -->
        <div id="login" style="width:400px">
         <form class="cmxform" id="loginForm" method="post" action="${base}/j_security_check" >
         <fieldset>
           <legend>User Login</legend>
           <p>
             <label for="cname">User Name</label>
             <input id="cname" name="j_username" size="20" class="required" minlength="2" />
           </p>
           <p>
             <label for="passwd">Password</label>
             <input id="passwd" name="j_password" size="20" class="required" type="password" />
           </p>
           <p style="text-align:center">
             <input class="submit ui-state-default ui-corner-all" type="submit" value="Submit"/><br/><br/>
             <a href="#" id="registration_link" class="ui-state-default ui-corner-all"><span class="ui-icon ui-icon-newwin"></span>Register a New Account</a>
           </p>

         </fieldset>
         </form>
         </div>
        <!--center><img style="height:33%" alt="tired doc" src="${images}/doctor_logo.png"/></center><--->
    </div>

    <div style="margin-left:420px;padding:1.5em;border:1px solid black;">
        MedCafe is a MITRE Innovation Program initiative to explore new ways for clinicians to interact with medical health records.
        <br/><br/>The architecture that we describe as the 'iGoogle' approach is currently being successfully applied within MITRE to non-medical domains, however the goals within these domains are identical: to provide access to just the right amount of information at the right time to the right people.
        <br/><br/>This design facilitates this goal, by providing the user a completely customized view of the data that they wish to see, using a set of user selected components.  This allows a clinician to tailor his patient view as required by historical and immediate concerns.  By allowing him to reconfigure and recombine components clinicians are provided the flexibility to adjust the view of the patient data as their hypothesis on how best to provide treatment evolves and will ensure pertinent data  is displayed at any given time.
        <br/><br/>Each component provides a simple use functionality, e.g. Displaying of an image, or a visual timeline of patient events such as hospital visits and XRAYs. As components will operate independently of each other, they can be added or removed from interface at will without impacting functionality of the system as a whole. All communication between these components occurs loosely through an identified data standard.  In this case a potential HL7 data standard, called hData. hData, is an approach to simplifying the representation and access of continuity of care data, which is currently being developed within MITRE. (http://projecthdata.org)
        <br/><br/>The system design is based on the following principles:
        <ul>
        <li>Each component provides a simple to use interface for some set of patient data.</li>
        <li>Each component operates independently of other components.</li>
        <li>All communication to/ from these components takes places through a defined data standard.</li>
        <li>A pre-defined set of use case templates can be developed and deployed to allow for clinician to quickly access exactly the information he needs for a particular patient circumstance.</li>
        <li>Templates can be used as a starting point, or a new template can be formed from scratch.  New components can be added or removed at will and the resulting view can be saved at any time, allowing for quick reaquisition of data.  A new template can be created from an existing view at any time. And this new template can then be shared with other users for use in building their own patient views.</li></ul>
        As the components are dependent only upon the chosen data standard they can display information from any data source, provided an adapter exists to translate the data source to the standard chosen.   The system is currently connecting to an open source version of VISTA, called OpenVISTA. Adapters to this system are already in place to translate between VISTA and hData. Adaptors to translate between the hData standard and  HL7 C32 standard will be implemented.
        <br/><br/>To encourage adaption and experimentation with this approach, our prototype system will be released under a business-friendly, open source license and will be available to the medical community.  An end goal of this design is to allow for any technically skilled person to build a component and integrate with the existing system, with little effort. This model would allow for a rich user interface to be available almost immediately with little set up time and allow customizations for different specialties and facilities.     </div>

</body>
</html>
