<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr-ch" lang="fr-ch">
<head>
<title>A Light weight RTE jQuery Plugin</title>
<link type="text/css" rel="stylesheet" href="css/editor/jquery.rte.css" />
<style type="text/css">
body, textarea {
    font-family:sans-serif;
    font-size:12px;
}
</style>
</head>
<body>

<div id="main" style="width:800px;">
<form action="saveText.jsp">
    <p>
        <textarea name="form[info1]" cols="100" rows="10"  class="rte1" method="post" action="#">
		</textarea>
    </p>
    <input type="submit" value="Save"></input>
</form>
<div id="test"></div>
    
<script type="text/javascript" src="js/jquery-1.3.2.js"></script>
<script type="text/javascript" src="js/editor/jquery.rte.js"></script>
<script type="text/javascript" src="js/editor/jquery.rte.tb.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	var arr = $('.rte1').rte({
		css: ['default.css'],
		controls_rte: rte_toolbar,
		controls_html: html_toolbar
	});

		
});
</script>

<hr>
</body>
</html>
