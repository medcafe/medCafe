<%
	String tabNum = request.getParameter("tab_num");
	if (tabNum == null)
		tabNum = "2";
		
	String title = request.getParameter("title");
	if (title == null)
		title = "Title";
	
	String type = request.getParameter("type");
	if (type == null)
		type = "Chart";
		
	String patientId ="1";
	Object patientIdObj = session.getAttribute("patient");
	if (patientIdObj != null)
		patientId = patientIdObj.toString();
		
	System.out.println("tabs-template.jsp type " + type + " patient id " + patientId );
	
%>
<script>

var hasContent = false;

$(function(){

	filterType();
	
	$(".widget-content").droppable({
      drop: function(event, ui) 
      {
      
      		var hasContentObj = $(this).find("#hasContent");
      		var hasContent = $(hasContentObj).attr("custom:hasContent");
      		
      		var dragObj = $(ui.draggable)
       		var widgetId = $(ui.draggable).html();
       		
       		var text = $(dragObj).find('p').text();
								
       		var imgHtml = $(dragObj).find('img').html();
       		var label = $(dragObj).find('img').attr("src");
			var link = $(dragObj).find('img').attr("custom:url");
			var type = $(dragObj).find('img').attr("custom:type");
			var html = $(dragObj).find('img').attr("custom:html");
			var method = $(dragObj).find('img').attr("custom:method");
			var patientId = "<%=patientId%>";
			var params = $(dragObj).find('img').attr("custom:params");
			var repository = $(dragObj).find('img').attr("custom:repository");
			if (hasContent == "false")
			{
				//No content : Use the current Tab
				//addChart(this, link, "<%=tabNum%>");
				
				createWidgetContent(patientId,link, text, type ,"<%=tabNum%>",params, repository);
				$(hasContentObj).attr("custom:hasContent",true);
				
				renameTab("<%=tabNum%>",text);
			}
			else
			{
				//Tab already has content Create a new Tab
				createLink(patientId,link, text, type ,params, repository);
			}
      }
    });
	
});

function filterType()
{
	var srcName = "js/filterDate<%=type%>.js"; 
	
	$.getScript(srcName, function(){

		$(document).bind('FILTER_DATE', function(event, startDate, endDate) 
		{	 		
			var tabNum = "<%=tabNum%>";
			filterDate<%=type%>(startDate, endDate,tabNum );
			
		});	
		
	});
}
	
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
					    	<div id="hasContent" custom:hasContent="false"></div>
					    	
				</div>
	       </div>
    </div>
</div>



