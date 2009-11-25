<%@ page import="org.mitre.medcafe.util.*" %><%@
    taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %><%
    String repo = WebUtils.getRequiredParameter(request, "repo");
    pageContext.setAttribute("repo", repo);
%><html>
    <head>
        <meta http-equiv="Content-type" content="text/html; charset=utf-8" />
        <title>Browse Repository</title>
        <link type="text/css" href="${css}/ui.tabs.css" rel="stylesheet" />
        <link type="text/css" href="${css}/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="${css}/droppable-tabs.css" rel="stylesheet" />

        <script type="text/javascript" src="${js}/jquery-1.3.2.js"></script>
        <script type="text/javascript" src="${js}/jquery.layout.js"></script>
        <script type="text/javascript" src="${js}/custom.js"></script>
        <script type="text/javascript" src="${js}/ui.all-1.7.1.js"></script>
    </head>
    <body>
        <div class="ui-widget"><a href="browse.jsp">Browse</a></div>
        <h2>Repository: <%=repo%></h2>
        <div class="ui-widget"><h1>Patients</h1><tags:IncludeRestlet relurl="c/repositories/${repo}/patients"/></div>
    </body>
</html>
