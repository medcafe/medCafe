<jsp:include page="header.html"/>
<jsp:include page="nav.html"/>

    <div id="main">
		<div id="fullcontent">
			<h2>About medCafe</h2>
			<div class="menu">
                <ul style="margin-bottom:0px;">
                    <li><a href="#design">Design Principles</a></li>
                    <li><a href="#advantages">Advantages</a></li>
                    <li><a href="#ideas">Ideas Explored</a></li>
                </ul>
			</div>
            <a name="design"></a><h3>Design Principles</h3>
            <p>There are several design principles which reinforce each other to produce a system that is expandable, modular, and agile.</p>
            <img src="images/patientScreen.png" width="300" height="150" alt="screenshot" style="float:left;"/>
            <h4>User designed, adaptable interface</h4>
            <p>The interface allows the user to choose how the health record is displayed.  How many tabs, what is displayed on each tab, and even the component decoration is editable by the user.  This allows the user to set up the patient view that is optimized for <i>them</i> not for some generic user.  Editing the user interface is simple and intuitive.  Simply drag the component you want to view onto the tab you want it one and it instantly appears.  Components can be re-ordered via drag and drop as well with constant visual feedback on where the new component will reside.  When the user is done setting up the view, it can be saved as a template that can be used over and over again for other patients, or saved as the default view for just that patient. </p>
            <p>Total flexibility is not always the best thing and other concerns, such as patient safety, will likely require restrictions.  The infrastructure provided allows that sort of control as well, however that is a governance issue the implementing organization will have to put into place depending on the needs specific to them.</p>
            <h4>Small pieces</h4>
            <p>The user interface is comprised of an overarching framework and a set of small, independent pieces called components or widgets.  The components can access any available data and can provide any service to the user.</p>
            <img src="images/architecture.png" width="300" height="165" alt="medCafe architecture" style="float:right"/>
            <h4>Consistent access to data</h4>
            <p>For each data source format, a translator is provided to convert that data source into a standard format.  While this takes extra initial work, the result is that every front end component can be used for every data source format and every data source has immediate access to multiple ways of publishing that data.</p>
            <p><a href="#top">Top</a></p>
			<a name="advantages"></a><h3>Benefits</h3>
			<p>Added together building a system in this manner provides the following benefits:</p>
            <h4>Patient-specific view of the health record</h4>
            <p>This allows the end user to configure their interface on a per patient basis so that the information most pertinent to the current patient is what the clinician will see.  This also has the benefit of allowing the physician to quickly remember what was discussed during the last encounter because that information is what will be seen first upon re-engaging them.  Of course, generic templates for the user interfaces can be provided ensuring a quick start for new patients. </p>
            <p>Because the user can save the view for the current patient, the next time she sees that patient, the same view of the healt record will show up again, instantly letting the user know what was important the last time that patient was seen.  This can greatly reduce the time needed for a physician to re-acquire the specific health issues of that patient.
            <img src="images/puzzle.png" alt="Components as independent puzzle pieces" style="float:left;"/>
            <h4>Best of breed components</h4>
            <p>Because the components are independent of each other, they can be replaced on a case by case basis, or even deployed side by side.  This prevents vendor lock-in to a certain user interface.  Consider: if a UI is purchased and part of it is not suitable for your needs, you can replace just part of the entire thing.  Or, UI's dedicated to certain specialties can be added to a generic interface to serve a subset of users.</p>
            <h4>Easy to specialize</h4>
            <p>If you have a need to set up access for just a certain specialty, just the components that are needed can be deployed – no more need to field a one-size-fits-all solution, the vast majority of which you don't need.</p>
            <h4>Smooth transition</h4>
            <p>The ability to start small and add additional capability as time goes on.  Since all components are independent of the others, there's no need to purchase a huge system right away.  Only the basic pieces can be acquired initially and more components added on as the needs of the organization grow.</p>
            <h4>Freedom from vendor lock-in</h4>
            <p>Using open standards and having a lower barrier to entry (from the independent nature of the components) allows smaller, specialized vendors to provide components for a system and allows for vendors other than the original one to provide support and system updates.</p>
            <p><a href="#top">Top</a></p>
			<a name="ideas"></a><h3>Ideas Explored</h3>
			<h4>Dealing With Imagery</h4>
            <p><a href="images/coverflow.png"><img src="images/coverflow.png" style="float:left;width:246px;height:107px;" alt="picture of coverflow widget"/></a>For some physicians, imagery and external records are a problem.  The files themselves are accessable, but it takes 30+ seconds to retrieve one.  To compound this, the imagery isn't labelled usefully, so many images have to be checked.  We show a way to pre-filter these images, then display only those in a way that let's the user "flip through" the thumbnails simliar to flipping through a stack of papers.  Only when something looks promising does the full image come up, saving time and resources.</p>
			<h4>Timeline</h4>
            <p><a href="images/timeline.png"><img src="images/timeline.png" style="float:right;width:200px;height:117px;" alt="picture of coverflow widget"/></a>The ability to see a patient's entire history at a glance can be very powerful.  With the timeline widget, one can see a list of all the events in a patient's history.  Immunizations, Out Patient visits, X-Rays, lab results...whatever has a date field and is chosen to appear.  To make this more useful, the user can click on any event and get a summary of the informatoin in the patient's record.</p>
            <p><a href="#top">Top</a></p>
        </div><!-- end content -->
    	<div class="clear"></div>
    </div><!-- end main -->
<jsp:include page="footer.html"/>

