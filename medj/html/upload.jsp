<jsp:include page="header.html"/>
<jsp:include page="nav.html"/>
                <div id="main">
                    <div id="fullcontent">
                        <h2>
                            Welcome to the website
                        </h2>
                        Welcome CCR founders! This site exists to show the possibilities to converting CCR XML to JSON.
                        <br/>
                        <h3>
                            Convert your CCR to JSON format
                        </h3>
                         <fieldset>
					       <legend>Upload File</legend>
					        <form action="convert" method="post" enctype="multipart/form-data">
					           <label for="filename_1">File: </label>
					           <input id="filename_1" type="file" name="filename_1" size="50"/><br/>
					            <br/>
					            <input type="submit" value="Upload CCR"/>
					        </form>
					    </fieldset>
                    </div>
                    <!-- end content -->
                    <div class="clear">
                    </div>
                </div>
                <!-- end main -->
<jsp:include page="footer.html"/>

