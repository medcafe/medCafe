<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ page import="org.mitre.medcafe.util.*, org.mitre.medcafe.model.*"  %>
<%@ page import = "java.util.logging.Logger, java.util.logging.Level"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %><%!
    public final static String KEY = "/addAllergy.jsp";
    public final static Logger log = Logger.getLogger( KEY );
   	static{log.setLevel(Level.FINER);}%>
<%

	log.finer("addAllergy: url start");

	 String cacheKey = (String) request.getParameter("cacheKey");
	 log.finer("cacheKey = " + cacheKey);
    PatientCache cache = (PatientCache) session.getAttribute(PatientCache.KEY);
    if( cache == null )
    {  //nobody is logged in

        response.sendRedirect("introPage.jsp");
        return;
    }
	String repoName = cache.getPrimaryRepos();
	String restlet = cache.getDataSourceRestlet(cacheKey);
	String id = cache.getPrimaryReposId();
	restlet = restlet.replaceAll("\\{repository\\}", repoName);
	restlet = restlet.replaceAll("\\{id\\}", id);




%>
<html>

<meta http-equiv="Content-type" content="text/html; charset=utf-8" />
<meta charset="utf-8">
	
	
	
	
	
	
	
	
<style>
	.ui-autocomplete {
		max-height: 100px;
		overflow-y: auto;
		/* prevent horizontal scrollbar */
		overflow-x: hidden;
		/* add padding to account for vertical scrollbar */
		padding-right: 20px;
	}
	/* IE 6 doesn't support max-height
	 * we use height instead, but this forces the menu to always be this tall
	 */
	* html .ui-autocomplete {
		height: 100px;
	}
		.ui-autocomplete-loading { background: white url('images/ui-anim_basic_16x16.gif') right center no-repeat; }
	</style>




	<div class = "ui-widget">
<form name = "restletForm" id = "restletForm" >
</form>
	<input name = "restlet" id = "restlet" type = "hidden" value="c<%=restlet%>?method=PUT">
<form name = "insertAllergyForm" id = "insertAllergyForm" >
	<label for= "allergens">Allergic to: </label> <input id = "allergens" name="allergens" type="text" size = "40" style = "position:relative"/>
	<br/>
	<label for ="reactions">Reactions:   </label> <input id = "reactions" name="reactions" type="text" size = "40" style = "position:relative"/>
	<br/>
	Date of Reaction: <input id="reactiondate" name="reactiondate" type="text" style = "position:relative"/>
	<br/>
	<label for ="allergyType">Type of Allergy: </label><br> <input type="radio" name="allergyType" value="food allergy"> Food Allergy<br><input type="radio" name="allergyType" value="drug allergy"> Drug Allergy<br><input type="radio" name="allergyType" value="allergy to substance"> Allergy to Substance<br>
	<br/>
	Comments: <input id="comments" name="comments" style = "position:relative" type ="text" size = "80"/>
	<br/>

	</form>
		<ul class="ui-autocomplete"></ul>
		</div>


</html>

