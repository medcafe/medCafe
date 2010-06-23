<%@ page import="org.mitre.medcafe.util.*, org.mitre.medcafe.model.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%
	String server = "" ;
	MedCafeFilter filter = null;
	Object filterObj = session.getAttribute("filter");

	String patientId = request.getParameter(Constants.PATIENT_ID);
	if (patientId == null)
		patientId = "1";
	String append = "~";
	String delim = "=";
	//coverFeed.jsp?filter=dates=<start_date>_<end_date>~filter=filter1,filter2
	String coverflowFile = "contentflow/coverFeed.jsp?filter=patient_id"  + delim + patientId;
	String url = coverflowFile;
	String startDate = request.getParameter("start_date");
	String endDate = request.getParameter("end_date");
	String filterCat = request.getParameter("filterCat");
	System.out.println("coverflow flash: index.jsp filterCat from params " +  filterCat );

	if (filterObj != null)
	{
		filter = (MedCafeFilter)filterObj;
		startDate = filter.getStartDate();
		endDate = filter.getEndDate();
		filterCat = filter.catToString();
		System.out.println("contentflow/index.jsp filter " + filter.toJSON());
	}

	if (startDate != null)
			coverflowFile += append + "dates" +  delim  + startDate;
	if (endDate != null)
			coverflowFile +=  "_" + endDate;
	if ( (filterCat != null) && (!filterCat.equals("")) )
			coverflowFile +=  "~filterCat"+  delim  + filterCat;

	System.out.println("contentflow flash: index.jsp startDate " +  startDate + " endDate " + endDate );
    System.out.println("contentflow flash: index.jsp url " +  coverflowFile );

	//coverflowFile = "coverFeedGen.xml";
	//coverflowFile = "http://127.0.0.1:8080/medcafe/c/repositories/medcafe/patients/1/images";
%>
<html>
    <head>
        <script tyle="text/javascript">
           // var cf = new ContentFlow('contentFlow', {reflectionColor: "#000000"});
        </script>
    </head>
    <body>
        <div id="contentFlow" class="ContentFlow">
            <!-- should be place before flow so that contained images will be loaded first -->
            <div class="loadIndicator"><div class="indicator"></div></div>

            <div class="flow">
                <jsp:include page="<%=coverflowFile%>"/>
            </div>
            <div class="globalCaption"></div>
            <div class="scrollbar">
                <div class="slider"><div class="position"></div></div>
            </div>

        </div>
    </body>
</html>
