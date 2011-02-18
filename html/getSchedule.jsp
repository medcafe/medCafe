<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ page import="org.mitre.medcafe.util.*,org.mitre.medcafe.model.*, org.json.*, java.util.*,java.text.*,java.sql.*" %><%@
    taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %><%

    DbConnection conn = new DbConnection();
    String query = "select id,  last_name || ', ' || first_name as name, appoint_time, end_time, appoint_date from schedule order by appoint_date, appoint_time";
    ResultSet rs = conn.executeQuery( query );

    //Cheating to make sure there's always someone scheduled for today
    //In a real sysetm we'd have to modify this to look at the next 2 months or somethings.
    SimpleDateFormat format = new SimpleDateFormat(Schedule.DATE_FORMAT);

    JSONArray ret = new JSONArray();
    while(rs.next())
    {
        JSONObject o = new JSONObject();
        o.put( "id", rs.getString("id") );
        o.put( "title", rs.getString("name") );
        String date = rs.getString("appoint_date");

        o.put( "start", date + " "  + rs.getString( "appoint_time" ) );
        o.put( "end",  date + " "  + rs.getString("end_time") );
        o.put( "allDay", false );
        ret.put(o);
    }
    DatabaseUtility.close(rs);
    conn.close();
    out.write(String.valueOf(ret));
%>
