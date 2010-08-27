var alreadyFetched = {};
var data = [];
function processChart(widgetInfo, data)
{	
	// fetch one series, adding to what we got

   	// find the URL in the link right next to us
    var dataurl =  "chartJSON.jsp?patient_id=" + widgetInfo.patient_id;

    // then fetch the data with jQuery

	$.getJSON(dataurl, function(data)
	{
		onDataReceived(data);
	});

     //var myJSONObject =<tags:IncludeRestlet relurl=dataUrl mediatype="json"/>;
     //var myJSONObject ={"label":"Temperature","data":[["1261380000000","100.1"],["1261385000000","101.7"],["12614250000001261480000000","101.7101.5"],["1261485000000","100.2"],["1261490000000","100.2"],["1261584970731","98.7"]]};
    //var myJSONObject ={ "label": 'Temperature (Patient 1)', "data": [[1261380000000, 100.1], [1261385000000, 101.7],[1261425000000, 101.7],[1261480000000, 101.5], [1261485000000, 100.2], [1261490000000, 100.2], [1261584970731, 98.7]]};
  
		
}

function onDataReceived(series) 
{

            // extract the first coordinate pair so you can see that
            // data is now an ordinary Javascript object
            var firstcoordinate = '(' + series.data[0][0] + ', ' + series.data[0][1] + ')';

            // let's add it to our current data
            if (!alreadyFetched[series.label]) {
                alreadyFetched[series.label] = true;
                data.push(series);
            }
			
			var options = {
		        lines: { show: true },
		        points: { show: true },
		        xaxis: { mode: "time"},
		        selection: { mode: "xy" }
		    };
			
			var placeholder = $("#placeholder");
    		var overview = $("#overview");
            // and plot all we got
            $("#placeholder").delay(100,function()
			{

            	$.plot(placeholder, data, options);


	            $.plot(overview, data, {
			        legend: { show: true, container: $("#overviewLegend") },
			        points: { show: true },
			        xaxis: { mode: "time" },
			        grid: { color: "#999" },
			        selection: { mode: "xy" }
	    		});

	    		$("#placeholder").bind("plotselected", function (event, ranges)
	    		{
			        // clamp the zooming to prevent eternal zoom
			        if (ranges.xaxis.to - ranges.xaxis.from < 0.00001)
			            ranges.xaxis.to = ranges.xaxis.from + 0.00001;
			        if (ranges.yaxis.to - ranges.yaxis.from < 0.00001)
			            ranges.yaxis.to = ranges.yaxis.from + 0.00001;

			        // do the zooming
			        plot = $.plot($("#placeholder"),data,
			                      $.extend(true, {}, options, {
			                          xaxis: { min: ranges.xaxis.from, max: ranges.xaxis.to },
			                          yaxis: { min: ranges.yaxis.from, max: ranges.yaxis.to }
			                      }));

			        // don't fire event on the overview to prevent eternal loop
			        overview.setSelection(ranges, true);
			    });

			    $("#overview").bind("plotselected", function (event, ranges) {
	        		plot.setSelection(ranges);
	   			 });

   			  });
}	
