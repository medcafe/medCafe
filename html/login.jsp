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
    <center><img style="margin-bottom:1.5em" alt="logo" src="${images}/banner.jpg"/></center>
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
        <img alt="tired doc" src="${images}/doctired.jpg"/>
    </div>

    <div style="margin-left:420px;padding:1.5em;border:1px solid black;">
        Explore the impact of CCOD approaches on healthcare professionals<br/><br/>
        Nice stuff here about what we are trying to accomplish
    </div>

</body>
</html>
