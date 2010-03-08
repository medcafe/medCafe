<%
	String tabNum = request.getParameter("tab_num");
	if (tabNum == null)
		tabNum = "2";
		
	String title = request.getParameter("title");
	if (title == null)
		title = "Title";
%>
<script>


$(function(){

	var srcName = "js/filterDate<%=title%>.js"; 
	$.getScript(srcName, function(){

		$(document).bind('FILTER_DATE', function(event, startDate, endDate) 
		{	 		
			var tabNum = "<%=tabNum%>";
			filterDate<%=title%>(startDate, endDate,tabNum );
		});	
		
	});
	
	$(".widget-content").droppable({
      drop: function(event, ui) 
      {
      
      		var dragObj = $(ui.draggable)
       		var widgetId = $(ui.draggable).html();
       		
       		var imgHtml = $(dragObj).find('img').html();
       		var label = $(dragObj).find('img').attr("src");
			var link = $(dragObj).find('img').attr("custom:url");
			var type = $(dragObj).find('img').attr("custom:type");
			var html = $(dragObj).find('img').attr("custom:html");
			var method = $(dragObj).find('img').attr("custom:method");
			var patientId = $(dragObj).find('img').attr("custom:Id");
			
			addChart(this, link, "<%=tabNum%>");
			//alert("object label  " + label);
      }
    });
	
});


	
</script>
		
<div class="id" id="<%=title%>"></div>	
<div id="columns">

	<div id="column<%=tabNum%>" class="column">
			<div class="widget color-<%=tabNum%>" id="yellow-widget<%=tabNum%>">
				<div style="cursor: move;" class="widget-head">
					   <a href="#" class="collapse">COLLAPSE</a>
					   <h3><%=title%></h3>
					   <a href="#" class="remove">CLOSE</a><a href="#" class="edit">EDIT</a>
					   <a href="#" class="maximize">MAXIMIZE</a>
				</div>
				<div class="edit-box" style="display: none;">
					                
					                <ul><li class="item">
					                	<label>Change the title?</label>
					                	<input value="<%=title%>"></li></ul>
					                	
					                <li class="item"><label>Available colors:</label>
					                <ul class="colors"><li class="color-1></li>
					                <li class="color-2"></li><li class="color-3"></li>
					                <li class="color-4"></li><li class="color-5"></li>
					                <li class="color-6"></li></ul></li>
				</div>
					                
				<div class="widget-content" id="widget-content<%=tabNum%>">

						
					    	<p><div id="aaa<%=tabNum%>">
					    		</div>
					    	</p>
					    
					    	<div id="dialog<%=tabNum%>">
					    		<div id="modalaaa<%=tabNum%>"></div>
					    	</div>
				</div>
	       </div>
    </div>
</div>



