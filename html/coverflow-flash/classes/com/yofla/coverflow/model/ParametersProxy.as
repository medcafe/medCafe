package com.yofla.coverflow.model {
	import org.puremvc.as3.multicore.interfaces.IProxy;
	import org.puremvc.as3.multicore.patterns.proxy.Proxy;
	
	import com.yofla.coverflow.CoverFlowApp;		

	/**
	 * ParametersProxy is reponsible for storing and providing external
	 * parameters
	 * 
	 * @author Matus Laco, www.yofla.com
	 */
	public class ParametersProxy extends Proxy implements IProxy {
		
		
		//--------------------------------------------------------------------------------
		// PRIVATE VARS
		//--------------------------------------------------------------------------------
		
		private var app : CoverFlowApp;
		
		private var _coverConfig : String;
		private var _coverFeed : String;
		private var _current : Number;
		
		//--------------------------------------------------------------------------------
		// CONSTS
		//--------------------------------------------------------------------------------
		
		// proxy name
		public static const NAME:String = 'ParametersProxy';
	
		//--------------------------------------------------------------------------------
		// CONSTRUCTOR
		//--------------------------------------------------------------------------------
		
		public function ParametersProxy(proxyName : String = null, data : Object = null) {
			super( proxyName, data );
			app = data as CoverFlowApp;

			_coverConfig  = app.coverConfig;
			_coverFeed = app.coverFeed;
			_current = app.current;
			
			if(!_coverConfig) {
				_coverConfig = "coverConfig.xml";
			}

			if(!_coverFeed) {
				_coverFeed = "coverFeed.xml";
			}

			if(!_current) {
				_current = 0;
			}
		}
		
		//--------------------------------------------------------------------------------
		// PUBLIC FUNCTIONS
		//--------------------------------------------------------------------------------

		
		//--------------------------------------------------------------------------------
		// GETTERS & SETTERS
		//--------------------------------------------------------------------------------
		
		public function get coverConfigFile () : String {
			return _coverConfig;
		}

		public function get coverFeedFile () : String {
			return _coverFeed;
		}

		public function get current () : Number {
			return _current;
		}
		
	}//class
}//package
