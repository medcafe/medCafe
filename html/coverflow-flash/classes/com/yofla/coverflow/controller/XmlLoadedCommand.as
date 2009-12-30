package com.yofla.coverflow.controller {
	import org.puremvc.as3.multicore.interfaces.INotification;
	import org.puremvc.as3.multicore.patterns.command.SimpleCommand;
	
	import com.yofla.coverflow.ApplicationFacade;
	import com.yofla.coverflow.model.AppProxy;
	import com.yofla.coverflow.model.ConfigProxy;
	import com.yofla.coverflow.model.FeedProxy;
	import com.yofla.coverflow.model.ParametersProxy;
	import com.yofla.coverflow.model.XmlProxy;
	
	import fl.motion.easing.Cubic;
	import fl.motion.easing.Elastic;
	import fl.motion.easing.Exponential;
	import fl.motion.easing.Linear;
	import fl.motion.easing.Quadratic;	

	/**
	 * @author Matus Laco, www.yofla.com
	 */
	public class XmlLoadedCommand extends SimpleCommand {
		
		override public function execute( note : INotification ) : void{
			
			var xmlProxy : XmlProxy = facade.retrieveProxy( XmlProxy.NAME ) as XmlProxy;
			
			// get data
			var configXml : XML = xmlProxy.configXml; 			var feedXml   : XML = xmlProxy.feedXml;
			
			// register new proxies			
			facade.registerProxy( new ConfigProxy( ConfigProxy.NAME, configXml ) );
			facade.registerProxy( new FeedProxy( FeedProxy.NAME, feedXml ) );
			
			// retrieve proxies
			var configProxy : ConfigProxy = facade.retrieveProxy( ConfigProxy.NAME ) as ConfigProxy;			var feedProxy   : FeedProxy   = facade.retrieveProxy( FeedProxy.NAME ) as FeedProxy;			var appProxy    : AppProxy    = facade.retrieveProxy( AppProxy.NAME ) as AppProxy;			var paramProxy  : ParametersProxy  = facade.retrieveProxy( ParametersProxy.NAME ) as ParametersProxy;
			
			// init appProxy
			appProxy.init();
			
			// set vals
			appProxy.itemsCount =  feedProxy.xmldata.children().length();
			
			if(appProxy.itemsCount < 1) {
				sendNotification( ApplicationFacade.ERROR_COMMAND, "No Items to display");
				return;	
			}
			
			// set current if in parameter			
			var current : uint = 0;
			current = (paramProxy.current) ? paramProxy.current : configProxy.current;

			// override current if in feedProxy
			if(feedProxy.current){
				current = feedProxy.current;	
			}
			
			// set current
			appProxy.current = current;
			appProxy.tweenedCurrent = current;

			
			// set tweening function for appProxy
			switch (configProxy.easing){
				
				case "Quadratic":
					appProxy.easingFunction = Quadratic.easeOut;
				break;
				case "Cubic":
					appProxy.easingFunction = Cubic.easeOut;
				break;
				case "Elastic":
					appProxy.easingFunction = Elastic.easeOut;
				break;
				case "Linear":
					appProxy.easingFunction = Linear.easeOut;
				break;
				case "Exponential":
					appProxy.easingFunction = Exponential.easeOut;
				break;
				default :
					appProxy.easingFunction = Quadratic.easeOut;
				break;
			}
			
						
			sendNotification( ApplicationFacade.XML_LOADED);
			
			// for setting controls to the right position
			sendNotification( ApplicationFacade.NEW_CURRENT, appProxy.current );

			// for positioning items
			sendNotification( ApplicationFacade.NEW_CURRENT_TWEENED, appProxy.current );

			
		}
	}
}