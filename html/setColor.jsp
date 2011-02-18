<%@ page import="org.mitre.medcafe.util.*, java.sql.*" %><%@
    taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %><%

//Retrieve the current color in use

//set the css-theme parameter
//Refresh

	String action="save";
	action = request.getParameter("action");
	
	if (action == null)
		action = "save";
		
    DbConnection conn = new DbConnection();
    PreparedStatement prep=null;
    String userName =  request.getRemoteUser();	
     
    if (action.equals("select"))
    {
	    String query = "select value from preferences where key=? and username=?";
	    String themeValue = "css/ui-darkness/jquery-ui-1.8.9.custom.css";
	    prep = conn.prepareStatement(query);
	    prep.setString(1, "theme");
	    prep.setString(2, userName);
	   
	    ResultSet rs = prep.executeQuery();
		
		if (rs.next())
		{
			themeValue = rs.getString("value");
		}
		String webApp =  (String)getServletContext().getAttribute("base");
		  
	 	session.setAttribute(Constants.CSS_THEME,  webApp + "/" + themeValue);  
	    String newTheme = (String)getServletContext().getAttribute("css_theme");
	  	DatabaseUtility.close(rs);
	}
	else
	{
		String themeValue = request.getParameter("theme_value");
		if (themeValue == null)
			return;
			
	 	String query = "select value from preferences where key=? and username=?";
	    prep = conn.prepareStatement(query);
	    prep.setString(1, "theme");
	    prep.setString(2, userName);
	    
	    ResultSet rs = prep.executeQuery();
		
		if (rs.next()) //Do update
		{

			query = "update preferences set value = ? where username=? and key=?";
			
	   	}
	   	else
	   	{
	   	//insert into preferences (key, username, value) values ('theme', 'guest','css/
	   	
	   			query = "insert into preferences ( value, username, key) values (?,?,?)";

	   	}
	   	prep = conn.prepareStatement(query);
	   	prep.setString(1, themeValue);
		prep.setString(2, userName);
		prep.setString(3, "theme");
		int rtn = prep.executeUpdate();
		String webApp =  (String)getServletContext().getAttribute("base");	
		session.setAttribute(Constants.CSS_THEME,  webApp + "/" + themeValue);  
	}
    
    conn.close();
%>
