package com.yofla.coverflow {
	import flash.display.MovieClip;	
	
	/**
	 * @author Matus
	 */
	public class CoverFlowApp extends MovieClip {
		
		//--------------------------------------------------------
		// PRIVATE VARS
		//--------------------------------------------------------
		
		private var IS_STANDALONE : Boolean;

		
		// parameters
		private var _coverConfig : String;
		private var _coverFeed : String;
		private var _current : Number;
		
		private var _width : Number;
		private var _height : Number;


		//--------------------------------------------------------
		// PROTECTED VARS
		//--------------------------------------------------------

		// purmeMVC multitokn key
		protected var appKey : String;
		
		// facade
		protected var facade: ApplicationFacade;


		//--------------------------------------------------------
		// GETTERS AND SETTERS
		//--------------------------------------------------------

		// coverConfig
		public function set coverConfig ( s : String) : void {
			_coverConfig = s; 
		}

		public function get coverConfig ( ) : String {
			return _coverConfig; 
		}

		// coverFeed
		public function set coverFeed ( s : String) : void {
			_coverFeed = s; 
		}

		public function get coverFeed ( ) : String {
			return _coverFeed; 
		}

		// coverCurrent
		public function set current ( n : Number) : void {
			_current = n; 
		}

		public function get current ( ) : Number {
			return _current; 
		}

		// width
		override public function set width ( n : Number) : void {
			_width = n; 
		}

		override public function get width ( ) : Number {
			return _width; 
		}

		// height
		override public function set height ( n : Number) : void {
			_height = n; 
		}

		override public function get height ( ) : Number {
			return _height; 
		}



		/**
		 * Constructor.
		 */		
		public function CoverFlowApp() {
			
			// loaded as stand-alone
			if(stage) {
				
				IS_STANDALONE = true;
				
				_coverConfig = root.loaderInfo.parameters.coverConfig;				_coverFeed   = root.loaderInfo.parameters.coverFeed;				_current     = root.loaderInfo.parameters.current;
				
				_width  = stage.stageWidth;				_height = stage.stageHeight;
				
				start();
			}
			// loaded as swf / component
			else{
				IS_STANDALONE = false;
				throw new Error(" ONLY STANDALONE VERSION AVAIABLE NOW ");
				return;
			}
		}		
		
		//--------------------------------------------------------
		// PUBLIC FUNCTIONS
		//--------------------------------------------------------
		
		public function start() : void {
			
			appKey = "CoverFlow" + String(Math.round(Math.random()*1000));
			facade = ApplicationFacade.getInstance( appKey );
			
			// start application
			facade.sendNotification(ApplicationFacade.APPSTARTUP_COMMAND, this);			
			
		}
		
	}
}
