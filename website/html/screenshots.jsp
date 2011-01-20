<jsp:include page="header.html"/>
<jsp:include page="nav.html"/>

    <div id="main">
		<div id="fullcontent">
			<h2>Screenshots</h2>
            <!-- include jQuery library -->
            <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
            <!-- include Cycle plugin -->
            <script type="text/javascript" src="js/galleria.js"></script>
            <script type="text/javascript">

                Galleria.loadTheme('js/themes/classic/galleria.classic.js');
                $(document).ready(function() {
                    // crop images:
                    $('#gallery').galleria({
                        height: 600,
                        transition: "fade"

                    });
                });
            </script>
            <div id="gallery">
                <img src="images/overview.png" alt="Overivew of the medCafe web application." />
                <img src="images/coverflow.png" alt="Coverflow-like facility for dealing with imagery and outside records (such as PDFs)"/>
                <img src="images/timeline2.png" alt="Time line shows all chosen events at one glance.  Click on any event for details."/>
                <img src="images/patient_info.png" alt="Information widgets can be grouped by types of information.  Here, all the contact and family information is on one tab, separate from the medical informatoin."/>
                <img src="images/loading_patient.png" alt="Loading a patient into the data cache (for prototyping purposes)"  />
                <img src="images/editScreen.png" alt="Widget title, color, and other attributes can be changed." />
                <img src="images/login.png" alt="Greeting for a user." />
                <img src="images/viewer.png" alt="Embedded image viewer allows pan and zoom." />
                <img src="images/annotations.png" alt="Images can also be annotated with a marking layer." />
                <img src="images/patientbanner.png" alt="The patient banner across the top shows summary information important for all interactions." />
                <img src="images/Templates.png" alt="Templates represent pre-set views of a patient's record.  You can use these directly, or just preview them as in this picture." />
                <img src="images/Add Schedule.png" alt="Adding a new schedule for a patient" />
            </div><!-- end slideshow -->
        </div><!-- end content -->
    	<div class="clear"></div>
    </div><!-- end main -->
<jsp:include page="footer.html"/>

