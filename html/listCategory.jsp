<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%
	
	
%>    
    <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
	<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<title>Category Filter</title>
	<script type="text/javascript" src="js/jquery-1.3.2.js"></script>
	
	<link type="text/css" href="css/custom-theme/jquery-ui-1.7.2.custom.css" rel="stylesheet" />	
	
	<script type="text/javascript">
		$(function(){
		
			
				$('#filter_button').click(function()
				 {
 	
 					var category ="";
 					var comma="";
						   
				    $('.filter_checkbox').each(
						  
						  function() 
						  {
						  	
						  	if ( $(this).attr('checked') )
						  	{
						   		var id = $(this).attr('id');
								category = category + comma +  $('#' + id).val();
								comma=",";
							}
						  }
					);
					//alert("listCategory.jsp: category filter " + category);
					parent.triggerFilterCategory(category);
				 
				});
					
				$('#unfilter_button').click(function()
				 {
				 	//for some reason cannot call trigger('FILTER_DATE') directly
				 	var category = "";
				 	parent.triggerFilterCategory(category);
				});
			});

		
	</script>
</head>

<body>
	
	<form>
	<input type="checkbox" class="filter_checkbox" value="Smoking" id="smoking">Smoking</input>
	<input type="checkbox" class="filter_checkbox" value="NonSmoking" id="nonsmoking">Non Smoking</input>
	<button value="Filter" id="filter_button">Filter</button>
	<button value="Remove Filter" id="unfilter_button">Remove Filter</button>
	</form>
</body>
</html>