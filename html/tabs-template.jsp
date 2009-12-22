<%
	String tabNum = request.getParameter("tab_num");
	if (tabNum == null)
		tabNum = "2";
		
	String title = request.getParameter("title");
	if (title == null)
		title = "Title";
%>
					
<div id="columns">

	<div id="column<%=tabNum%>" class="column">
			<div class="widget color-<%=tabNum%>" id="yellow-widget<%=tabNum%>">
				<div style="cursor: move;" class="widget-head">
					   <a href="#" class="collapse">COLLAPSE</a>
					   <h3><%=title%></h3>
					   <a href="#" class="remove">CLOSE</a><a href="#" class="edit">EDIT</a>
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
					                
				<div class="widget-content">

					    	<p><div id="aaa<%=tabNum%>"></div></p>
					                    
				</div>
	       </div>
    </div>
</div>



