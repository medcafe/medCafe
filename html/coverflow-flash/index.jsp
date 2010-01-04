<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<%
	String coverflowFile = "http://127.0.0.1:8080/medcafe/coverflow-flash/coverFeed.jsp";
	//coverflowFile = "coverFeed.xml";
	//coverflowFile = "http://127.0.0.1:8080/medcafe/c/treenode?relurl=/repositories/medcafe/patients/1/images&type=link";
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title>Cover Flow by YoFLA.com</title>
<meta name="author" content="YoFLA.com" />
<meta name="copyright" content="YoFLA.com" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="robots" content="all" />
<style>
body {background: #F0F0FF;}
#envelope{width: 780px;  margin-right: auto; 	margin-top: 0px;	margin-bottom: 40px;	padding: 0px;	background: white;	font-family: Verdana, Arial, sans-serif;	border-left: 1px solid #DDDDDD;	border-top: 1px solid #DDDDDD;	border-right: 2px solid #AAAAAA;	border-bottom: 2px solid #AAAAAA;}
#flashContent{ background-color: silver; border: 1px solid gray; width: 780px; height:450px;}
#flashWrapper{margin-left: auto; margin-right: auto; width: 780px; height:450px; }
h2{	font-size: 16px;	background: #EEEEFF;	padding: 2px; padding-left: 10px;}
h1{	font-size: 18px;	background: #333333;	marigin: 0;	padding: 10px;	color: white;}
p{	font-size: 12px;	margin-left: 5px;	margin-top: 20px;}
li{ font-size: 14px; line-height: 22px;}
.caption{	font-family: arial,sans-serif;	font-size: 10px;	text-align:center;	margin-top: 20px;}
.caption a{	color: black;}
#footer{margin-left: auto; margin-right: auto; font-size: 10px; text-align:center; width: 100%; padding: 10px;}
</style>
<script type="text/javascript" src="swfobject.js"></script>
<script type="text/javascript" src="custom.js"></script>

<script type="text/javascript">
	swfobject.registerObject("flashContent", "9.0.0", "expressInstall.swf");
	
	
	
</script>

</head>

<body> 

<div id="envelope">
   <div id="flashWrapper">

	 <div id="flashContent">

      <object id="myId" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="590" height="450">

        <param name="movie" value="coverflow.swf?coverConfig=coverConfig.xml&coverFeed=<%=coverflowFile%>&current=3" />
        <!--[if !IE]>-->
        <object type="application/x-shockwave-flash" data="coverflow.swf?coverConfig=coverConfig.xml&coverFeed=<%=coverflowFile%>&current=3" width="590" height="450">
        <!--<![endif]-->
          <p>Detecting Flash Player...</p>
        <!--[if !IE]>-->
        </object>
        <!--<![endif]-->
      </object>
    </div>	
    
   </div>

	<h2>About</h2>
	
	<p>
	The coverflow script was orginaly developed for <a href="http://www.krumedia.com/" target="_blank">http://www.krumedia.com/</a> company who gave me (Matus Laco) the right to resell the tool. I decided to provide the tool as open-source for non-comercial use under the Creative Commons Attribution-Noncommercial-Share Alike 3.0 Unported License. For commercial use please purchase the PRO or ENTERPRISE license at <a href="http://www.yofla.com/flash/cover-flow/" target="_blank">www.yofla.com/flash/cover-flow/</a>. 
	</p>
	
<p>

<a style="display: block; float: left; margin: 0 5px 0 0;" rel="license" href="http://creativecommons.org/licenses/by-nc-sa/3.0/"><img alt="Creative Commons License" style="border-width:0" src="http://i.creativecommons.org/l/by-nc-sa/3.0/88x31.png" /></a>


<span xmlns:dc="http://purl.org/dc/elements/1.1/" href="http://purl.org/dc/dcmitype/InteractiveResource" property="dc:title" rel="dc:type">Cover Flow</span> by <a xmlns:cc="http://creativecommons.org/ns#" href="http://www.yofla.com/flash/cover-flow/" property="cc:attributionName" rel="cc:attributionURL">YoFLA - Matus Laco</a> is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/3.0/">Creative Commons Attribution-Noncommercial-Share Alike 3.0 Unported License</a>.

Based on a work at <a xmlns:dc="http://purl.org/dc/elements/1.1/" href="http://www.yofla.com/flash/cover-flow/" rel="dc:source">www.yofla.com/flash/cover-flow/</a>.
<br />
<br />
Permissions beyond the scope of this license may be available at <a xmlns:cc="http://creativecommons.org/ns#" href="http://www.yofla.com/flash/cover-flow/" rel="cc:morePermissions">http://www.yofla.com/flash/cover-flow/</a>.
</p>

<p>
<b>How to attribute:</b> <br /> Simply do not remove or modify the "About Cover Flow script..." link in the contex menu and the popup which it opens.
</p>

<h2>Other YoFLA Software</h2>

<p>
 &raquo; <a href="http://www.yofla.com/flash/3d-rotate/?ref=doc" title="3d object rotation">3D Rotate</a> - Rotate objects in 3D :: Best Product Presenation ever! <br />
 &raquo; <a href="http://www.yofla.com/flash/multi-zoomer/?ref=doc" title="zoom multiple images">Multiple Zoomer</a> - Zoom multiple images<br />
 &raquo; <a href="http://www.yofla.com/flash/image-zoomer/?ref=doc" title="zoom images">Image Zoomer</a> - Zoom images in a different way <br />
 &raquo; <a href="http://www.yofla.com/flash/flash-zoomer/?ref=doc" title="zoom images">Flash Zoomer</a> - Image zoomer with navigator
</p>

<div id="footer">
&copy; 2008 YoFLA.com
</div>


</div> <!-- envelope -->

</body>
</html>