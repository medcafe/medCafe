function addBookmarks(callObj, widgetInfo, data)
{
        var html = v2js_inettutsHead(widgetInfo) +window["v2js_" + widgetInfo.template](data) + v2js_inettutsTail(widgetInfo);
        var selectedRow=0;
        if (!widgetInfo.tab_num)
            widgetInfo.tab_num = "2";
        if (!widgetInfo.column)
            widgetInfo.column = "1";

        $("#tabs-" + widgetInfo.tab_num + " #column" + widgetInfo.column).append(html);

        //TODO make bookmarks editable
       // setHasContent(widgetInfo.order);

        //set up tooltips
        $('.qtip a[href][title]').qtip({
            content: {
                text: false // Use each elements title attribute
            },
            style: 'cream' // Give it some style
        });
}
