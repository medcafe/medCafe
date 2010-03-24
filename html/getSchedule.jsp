<%@ page import="org.mitre.medcafe.util.*, org.json.*, java.util.*,java.text.*,java.sql.*" %><%@
    taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %><%

    DbConnection conn = new DbConnection();
    String query = "select id,  last_name || ', ' || first_name as name, appoint_time, appoint_time+interval '30 minutes' as end_time from schedule order by appoint_date, appoint_time";
    ResultSet rs = conn.executeQuery( query );

    //Cheating to make sure there's always someone scheduled for today
    //In a real sysetm we'd have to modify this to look at the next 2 months or somethings.
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String today = format.format(new java.util.Date());

    JSONArray ret = new JSONArray();
    while(rs.next())
    {
        JSONObject o = new JSONObject();
        o.put( "id", rs.getString("id") );
        o.put( "title", rs.getString("name") );
        o.put( "start", today + " "  + rs.getString( "appoint_time" ) );
        o.put( "end",  today + " "  + rs.getString("end_time") );
        o.put( "allDay", false );
        ret.put(o);
    }
    out.write(String.valueOf(ret));
%>
