<%-- Copyright 2010 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.  Licensed under the Apache License, Version 2.0 (the "License").  --%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %><%@
    taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<html>
    <head>
        <meta http-equiv="Content-type" content="text/html; charset=utf-8" />
        <title>Repository List</title>
        <link type="text/css" href="${css}/ui.tabs.css" rel="stylesheet" />
        <link type="text/css" href="${css}/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="${css}/droppable-tabs.css" rel="stylesheet" />

        <script type="text/javascript" src="${js}/jquery-1.4.4.js"></script>
        <script type="text/javascript" src="${js}/jquery.layout-latest.js"></script>
        <script type="text/javascript" src="${js}/custom.js"></script>
        <script type="text/javascript" src="${js}/jquery-ui-1.8.6.custom.min.js"></script>
    </head>
    <body>
        <div class="ui-widget"><tags:IncludeRestlet relurl="/repositories"/></div>

    </body>
</html>
