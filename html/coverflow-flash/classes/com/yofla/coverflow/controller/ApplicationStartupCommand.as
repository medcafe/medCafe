package com.yofla.coverflow.controller {
	import com.yofla.coverflow.view.CopyrightMediator;	
	import com.yofla.coverflow.view.AppMediator;	
	import com.yofla.coverflow.model.XmlProxy;	
	import com.yofla.coverflow.model.AppProxy;	
	import com.yofla.coverflow.model.ParametersProxy;	
	import com.yofla.utils.ErrorReporter;	
	import com.yofla.coverflow.CoverFlowApp;	
	
	import org.puremvc.as3.multicore.interfaces.INotification;	
	import org.puremvc.as3.multicore.patterns.command.SimpleCommand;	
	
	/**
	 * @author Matus Laco, www.yofla.com
	 */
	public class ApplicationStartupCommand extends SimpleCommand {
		

		override public function execute( note : INotification ) : void{

			// get reference to app
			var app : CoverFlowApp = note.getBody( ) as CoverFlowApp;
		
			// init error reporter
			ErrorReporter.init( app.stage);
			
			// parameters proxy
			facade.registerProxy( new ParametersProxy( ParametersProxy.NAME, app) );
			
			// app proxy
			var appProxy : AppProxy = new AppProxy( );
			facade.registerProxy( appProxy );
						
			// load xml files
			var xmlProxy : XmlProxy = new XmlProxy( );
			facade.registerProxy( xmlProxy );
			
			// mediators
			var appMediator : AppMediator = new AppMediator( app );	
			facade.registerMediator( appMediator );
			appMediator.init();
			
			var copyrightMediator : CopyrightMediator = new CopyrightMediator( app );
			facade.registerMediator( copyrightMediator );
			copyrightMediator.init();
			
			
			// load xml files			
			xmlProxy.init();
		}		
		
	}//class
}//package