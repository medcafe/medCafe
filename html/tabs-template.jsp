<%
	String tabNum = request.getParameter("tab_num");
	if (tabNum == null)
		tabNum = "2";
		
	String title = request.getParameter("title");
	if (title == null)
		title = "Title";
%>
<!--  div id="tabs-<%=tabNum%>" class="tabContent ui-tabs-panel ui-widget-content ui-corner-bottom"-->
			        
					
					<div id="columns">

							<div id="column<%=tabNum%>" class="column">
					            <div class="widget color-yellow" id="yellow-widget<%=tabNum%>">
					                <div style="cursor: move;" class="widget-head"><a href="#" class="collapse">COLLAPSE</a>
					                    <h3><%=title%></h3>
					                <a href="#" class="remove">CLOSE</a><a href="#" class="edit">EDIT</a></div>
					                <div class="edit-box" style="display: none;">
					                
					                <ul><li class="item">
					                	<label>Change the title?</label>
					                	<input value="<%=title%>"></li></ul>
					                	
					                <li class="item"><label>Available colors:</label>
					                <ul class="colors"><li class="color-yellow"></li>
					                <li class="color-red"></li><li class="color-blue"></li>
					                <li class="color-white"></li><li class="color-orange"></li>
					                <li class="color-green"></li></ul></li></div>
					                
					                <div class="widget-content">

					                    <p>Morbi tincidunt, dui sit amet facilisis feugiat</p>
					                </div>
					            </div>
				            </div>
			        </div>

					
<!--  /div-->

