 (function($) {
 $.widget("ui.tabs", {
   
   // do something here
	add: function() {
		alert("in here");
	}
	
 });
 $.extend($.ui.tabs, {
   getter: "value length",
   defaults: {
     option1: "defaultValue",
     hidden: true
   }
 });
 
})
