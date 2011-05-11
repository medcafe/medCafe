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
                        <form action="convert" method="post" enctype="x-www-form-urlencoded" >
                            <p>
                                <textarea cols="70" rows="15" name="source_xml">Paste your CCR XML into this box and hit submit...</textarea>
                            </p>
                            We suggest you take the resut and run it through <a href="http://jsonlint.com">JSONLint</a> to verify and beautify the result.
                            <input type="submit"/>
                        </form>
                    </div>
                    <!-- end content -->
                    <div class="clear">
                    </div>
                </div>
                <!-- end main -->
<%-- <jsp:include page="news.html"/> --%>
    	<div class="clear"></div>
    </div><!-- end main -->
<jsp:include page="footer.html"/>

