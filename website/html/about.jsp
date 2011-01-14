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
            <p>There are several design principles which reinforce each other to produce a system that is expandable, modular, and agile.
            <h4>User designed, adaptable interface</h4>
            <p>This allows the end user to configure their interface on a per patient basis so that the information most pertinent to the current patient is what the clinician will see.  This also has the benefit of allowing the physician to quickly remember what was discussed during the last encounter because that information is what will be seen first upon re-engaging them.  Of course, generic templates for the user interfaces can be provided ensuring a quick start for new patients.</p>
            <h4>Small pieces</h4>
            <p>The user interface is comprised of an overarching framework and a set of small, independent pieces called components or widgets.  The components can access any available data and can provide any service to the user.
            <h4>Consistent access to data</h4>
            <p>For each data source format, a translator is provided to convert that data source into a standard format.  While this takes extra initial work, the result is that every front end component can be used for every data source format and every data source has immediate access to multiple ways of publishing that data.</p>
            <p><a href="#top">Top</a></p>
			<a name="advantages"></a><h3>Advantages</h3>
			<p>Added together building a system in this manner provides the following benefits:</p>
            <h4>TODO</h4>
            <p>Stuff for UDOP</p>
            <h4>Best of breed components</h4>
            <p>Because the components are independent of each other, they can be replaced on a case by case basis, or even deployed side by side.  This prevents vendor lock-in to a certain user interface.  Consider: if a UI is purchased and part of it is not suitable for your needs, you can replace just part of the entire thing.  Or, UI's dedicated to certain specialties can be added to a generic interface to server a subset of users.</p>
            <h4>Easy to specialize</h4>
            <p>If you have a need to set up access for just a certain specialty, just the components that are needed can be deployed – no more need to field a one-size-fits-all solution, the vast majority of which you don't need.</p>
            <h4>Smooth transition</h4>
            <p>The ability to start small and add additional capability as time goes on.  Since all components are independent of the others, there's no need to purchase a huge system right away.  Only the basic pieces can be acquired initially and more components added on as the needs of the organization grow.</p>
            <h4>Freedom from vendor lock-in</h4>
            <p>Using open standards and having a lower barrier to entry (from the independent nature of the components) allows smaller, specialized vendors to provide components for a system and allows for vendors other than the original one to provide support and system updates.</p>
            <p><a href="#top">Top</a></p>
			<a name="ideas"></a><h3>Ideas Explored</h3>
			<h4>Dealing With Imagery</h4>
            <p>Good stuff</p>
			<h4>Timeline</h4>
            <p>Good stuff</p>
            <p><a href="#top">Top</a></p>
        </div><!-- end content -->
    	<div class="clear"></div>
    </div><!-- end main -->
<jsp:include page="footer.html"/>

