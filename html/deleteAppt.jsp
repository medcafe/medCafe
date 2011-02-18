<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ page import="org.mitre.medcafe.util.*,org.mitre.medcafe.model.*, org.json.*, java.util.*,java.text.*,java.sql.*,java.util.logging.*" %><%@
    taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %><%

    String apptId = WebUtils.getRequiredParameter(request, "id");
    //this is for preventing sql-injection attacks only
    JSONObject ret = new JSONObject();
    DbConnection conn = null;
    try
    {
        int id = Integer.parseInt(apptId);
        conn = new DbConnection();
        String query = Schedule.DELETE_APPOINTMENT_BY_ID;
        int count = conn.psExecuteUpdate( query, "Could not delete appointment", id );
        if( count == -1 )
            ret.put("announce", WebUtils.createErrorAlert("Error while saving your change."));


    }catch(Exception e)
    {
        Logger log = Logger.getLogger( "moveAppt.jsp" );
        log.log( Level.WARNING, "Error saving appt change to the database.", e );
    }
    finally
    {
    	conn.close();
        out.write(String.valueOf(ret));
    }
%>
