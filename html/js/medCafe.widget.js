var medCafeWidget = 
{ 
			medcafeWidgets : $,
			extSettings : 
			{ 
					/*for convenience*/
					columns : '.column',
			        widgetSelector: '.widget',
        			handleSelector: '.widget-head',
        			contentSelector: '.widget-content',
					widgetDefault : 
					{
							id : 1,
							tab_num : 1, 
							repository : 'OurVista',
							type: 'images',
							location : 'center',
							clickUrl :'http://127.0.0.1:8080',
							name : 'widget name',
							server : 'http://127.0.0.1',
							patient_id :1
				    },
	        		widgetIndSettings : 
					{
			             intro : 
			             {
				            id : 1,
							tab_num : 1, 
							repository : 'OurVista',
							type: 'images',
							location : 'center',
							clickUrl :'http://127.0.0.1:8080',
							name : 'widget name',
							server : 'http://127.0.0.1',
							patient_id :1
						}
					}	
			},
			test: function(txt)
		    {
		    	alert("txt " + txt);
		    },
		    getAllWidgetSettings : function ( id, origSettings) {
    
		        var $ = this.medcafeWidgets,
		            extSettings = this.extSettings;
		       
		       	var rtnSettings =  (id&&extSettings.widgetIndSettings[id]) ? $.extend({},extSettings.widgetDefault,extSettings.widgetIndSettings[id]) : settings.widgetDefault;
    			//Get all the settings
    			return $.extend({},origSettings,rtnSettings);
    			
    		}
    		,
		    getExtWidgetSettings : function ( id) {
    
		        var $ = this.medcafeWidgets,
		            extSettings = this.extSettings;
		       
		       	var rtnSettings =  (id&&extSettings.widgetIndSettings[id]) ? $.extend({},extSettings.widgetDefault,extSettings.widgetIndSettings[id]) : extSettings.widgetDefault;
    			
    			return rtnSettings;
    			
    		}
    		,
		    getAllIds : function () {
    
    			
		        var $ = this.medcafeWidgets,
		            extSettings = this.extSettings;
		        var ids = [];
		        var i=0;
		        $.each (extSettings.widgetIndSettings, function(id, val)
		        {
		        	ids[i++] = id;
		        });
		        return ids;
    			
    		}
    		,
		    setExtWidgetSettings : function ( id, newSettings) {
		    
		        this.extSettings.widgetIndSettings[id] = newSettings;
    			//alert("got settings tab_num " + this.extSettings.widgetIndSettings[id].tab_num  );
    		
    		}
    		,
    		populateExtWidgetSettings : function ( patientId,link, label, type ,tab_num, params, repId )
    		{   			
    			var id = "yellow-widget" + tab_num;
		
    			newSettings = this.getExtWidgetSettings(id);
		
    			if (!newSettings)
    			{
    				alert("Could not set values ");
    			}
    			newSettings.id = id;
    			newSettings.tab_num = tab_num;
    			newSettings.repository = repId;
    			newSettings.type = type;
    			//extSettings.location = location;
    			//extSettings.clickUrl = clickUrl;
    			newSettings.name = label;
    			newSettings.server = link;
    			newSettings.patient_id = patientId;
    			
    			this.setExtWidgetSettings(id,newSettings );   			
    			/*
							id : 1,
							tab_num : 1, 
							repository : 'OurVista',
							type: 'images',
							location : 'center',
							clickUrl :'http://127.0.0.1:8080',
							name : 'widget name',
							server : 'http://127.0.0.1',
							patient_id :1    			 * 
    			 */
    		}
    		,
		    loadExtWidgetSettings : function () 
		    {
  
    			 var medWidgets = this,
            	 $ = this.medcafeWidgets,
            	 extSettings = this.extSettings;
        		 $(extSettings.widgetSelector, $(extSettings.columns)).each(function () 
        		 {
      
           			var thisWidgetSettings = medWidgets.getExtWidgetSettings(this.id);
           				 
           			//Cycle through the returned values and 
           			$('.settings').each(function ()
           			{
           				   //Goal is to get the following info
           			});
            	 });
        
    		},
    		saveWidget : function (url, id)
    		{
    			var widgetSettings = this.getExtWidgetSettings(id);
    			$.ajax({
	                url: url,
	                type: 'POST',
	                data: widgetSettings,
	                dataType: 'json',
	                contentType: "application/json; charset=utf-8",
	                beforeSend: function() { alert("saving");$("#saveStatus").html("Saving").show(); },
	                success: function(result) {
	                    alert(result.Result);
	                    $("#saveStatus").html(result.Result).show();
	                }
            	});
    		},
    		saveAll : function (url)
    		{
    			
    		}
};

function extendWidgets(id){
	
	//$.extend($.fn.inettuts, {/*your methods/properties*/});
	
	var test = $.extend( {}, medCafeWidget , iNettuts);	
	var settings = test.getSettings();	
	//var allSettings = $.extend( {}, medCafeWidget.getExtWidgetSettings('intro') , settings);	
	var allSettings = test.getExtWidgetSettings( id, test.getWidgetSettings(id));
	
}