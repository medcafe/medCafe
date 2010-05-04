<%@ page import="org.mitre.medcafe.util.*,org.mitre.medcafe.model.*, org.json.*, java.util.*,java.text.*,java.sql.*,java.util.logging.*" %><%@
    taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %><%

    String apptId = WebUtils.getRequiredParameter(request, "id");
    //this is for preventing sql-injection attacks only
    Integer.parseInt( minuteDelta );
    JSONObject ret = new JSONObject();
    try
    {
        int id = Integer.parseInt(apptId);
        DbConnection conn = new DbConnection();
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
        out.write(String.valueOf(ret));
    }
%>
