// Delay Plugin for jQuery
// - http://www.evanbot.com
// - � 2008 Evan Byrne

jQuery.fn.delay = function(time,func){
	this.each(function(){
		setTimeout(func,time);
	});

	return this;
};