package com.yofla.coverflow {
	import com.yofla.coverflow.controller.XmlLoadedCommand;	
	import com.yofla.coverflow.controller.ErrorCommand;	
	import com.yofla.coverflow.controller.ApplicationStartupCommand;	
	
	import org.puremvc.as3.multicore.patterns.facade.Facade;	
	
	/**
	 * Application facade stores the comand and notification names,
	 * registers command with the controller
	 * 
	 * @author Matus Laco, www.yofla.com
	 * @author PureMVC, www.puremvc.org
	 */
	public class ApplicationFacade extends Facade {
		
		// COMMANDS		
		public static const APPSTARTUP_COMMAND : String       = "appStartupCommand";
		public static const ERROR_COMMAND : String             = "errorCommand";		
		public static const XMLLOADED_COMMAND : String         = "xmlLoadedCommand";		


		//NOTIFICATIONS
		public static const NEW_CURRENT : String               = "newCurrent";		
		public static const NEW_CURRENT_TWEENED : String       = "newCurrentTweened";		
		public static const XML_LOADED : String                = "xmlLoaded";		public static const IMAGE_LOADED : String              = "imageLoaded";		public static const STAGE_MOUSEUP : String             = "stageMouseUp";

				
		/**
		 * Constructor
		 */
		public function ApplicationFacade( key:String )
		{
			super(key);	
		}

	   /**
        * Singleton ApplicationFacade Factory Method
        */
        public static function getInstance( key:String ) : ApplicationFacade 
        {
            if ( instanceMap[ key ] == null ) instanceMap[ key ] = new ApplicationFacade( key );
            return instanceMap[ key ] as ApplicationFacade;
        }


		/**
		 * Register Commands with the Controller 
		 */
		override protected function initializeController() : void 
		{
			super.initializeController();			
			registerCommand( APPSTARTUP_COMMAND, ApplicationStartupCommand );
			registerCommand( ERROR_COMMAND, ErrorCommand );
			registerCommand( XMLLOADED_COMMAND, XmlLoadedCommand );
		}				
		
		
	}
}
