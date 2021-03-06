<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ page import="org.mitre.medcafe.util.*, org.json.*, java.util.*,java.text.*,java.sql.*,java.util.logging.*" %><%@
    taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %><%

    String apptId = WebUtils.getRequiredParameter(request, "id");
    String minuteDelta = WebUtils.getRequiredParameter(request, "minutes");
    //this is for preventing sql-injection attacks only
    Integer.parseInt( minuteDelta );
    JSONObject ret = new JSONObject();
    DbConnection conn = null;
    try
    {
        int id = Integer.parseInt(apptId);
        conn = new DbConnection();
        String query = "update schedule set appoint_time = appoint_time+interval '" + minuteDelta + " minutes', end_time = end_time+interval '" + minuteDelta + " minutes' where id=?";
        int count = conn.psExecuteUpdate( query, "", id );
        if( count == -1 )
            ret.put("announce", WebUtils.createErrorAlert("Error while saving your change."));
        else
            ret.put("announce", WebUtils.createInfoAlert("Appointment successfully resized."));

    }catch(Exception e)
    {
        Logger log = Logger.getLogger( "moveAppt.jsp" );
        log.log( Level.WARNING, "Error saving appt change to the database.", e );
    }
    finally
    {
    	if (conn != null)
    		conn.close();
        out.write(String.valueOf(ret));
    }
%>
