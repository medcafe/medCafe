package com.yofla.coverflow.model {
	import flash.events.ErrorEvent;
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.events.SecurityErrorEvent;
	import flash.net.URLLoader;
	import flash.net.URLRequest;
	
	import org.puremvc.as3.multicore.interfaces.IProxy;
	import org.puremvc.as3.multicore.patterns.proxy.Proxy;
	
	import com.yofla.coverflow.ApplicationFacade;		

	/**
	 * 
	 * @author Matus Laco, www.yofla.com
	 */
	public class XmlProxy extends Proxy implements IProxy {
		
		
		//--------------------------------------------------------------------------------
		// PRIVATE VARS
		//--------------------------------------------------------------------------------
		
		private var _preventCache : Boolean = false;
		
		// proxies
		private var paramProxy : ParametersProxy;
		
		
		// runtime vars
		private var _configXml : XML;		private var _feedXml : XML;

		//--------------------------------------------------------------------------------
		// PUBLIC VARS
		//--------------------------------------------------------------------------------

		//--------------------------------------------------------------------------------
		// CONSTS
		//--------------------------------------------------------------------------------
		
		// proxy name
		public static const NAME:String = 'XmlProxy';
		

		//--------------------------------------------------------------------------------
		// GETTERS AND SETTERS
		//--------------------------------------------------------------------------------
		
		public function get feedXml() : XML {
			return _feedXml;
		}

		public function get configXml() : XML {
			return _configXml;
		}

		//--------------------------------------------------------------------------------
		// CONSTRUCTOR
		//--------------------------------------------------------------------------------
		
		public function XmlProxy(proxyName : String = null, data : Object = null) {
			super( NAME);
		}

		//--------------------------------------------------------------------------------
		// PUBLIC FUNCTIONS
		//--------------------------------------------------------------------------------
		
		public function init() : void {
			// get param proxy
			paramProxy = facade.retrieveProxy( ParametersProxy.NAME ) as ParametersProxy;

			var loader : URLLoader = new URLLoader();
			loader.addEventListener( Event.COMPLETE, onConfigLoaded, false, 0, true);
			loader.addEventListener( IOErrorEvent.IO_ERROR, onXmlError, false, 0, true);
			loader.addEventListener( SecurityErrorEvent.SECURITY_ERROR, onXmlError, false, 0, true);
			loader.load( new URLRequest( paramProxy.coverConfigFile ) );		
		}

		//--------------------------------------------------------------------------------
		// PRIVATE FUNCTIONS
		//--------------------------------------------------------------------------------

		//---------------------------------------------------------------
		// CALLBASKS
        //---------------------------------------------------------------
		private function onConfigLoaded( e : Event ) : void{
			
			// get data 
			try{
				_configXml = XML(e.target.data);
			}
			catch(er:*){
				sendNotification( ApplicationFacade.ERROR_COMMAND, er.message);
				return;
			}

			// get feed url prefix			
			var feedUrlPrefix : String = _configXml.feedURLprefix;
			
			// get path
			var feedXmlPath : String = feedUrlPrefix + paramProxy.coverFeedFile;
			 			
			// load feed xml
			var loader : URLLoader = new URLLoader();
			loader.addEventListener( Event.COMPLETE, onFeedLoaded, false, 0, true);
			loader.addEventListener( IOErrorEvent.IO_ERROR, onXmlError, false, 0, true);
			loader.addEventListener( SecurityErrorEvent.SECURITY_ERROR, onXmlError, false, 0, true);
			loader.load( new URLRequest( feedXmlPath ) );		
		}

		
		private function onFeedLoaded( e : Event ) : void{
			
			// get data 
			try{
				_feedXml = XML(e.target.data);
			}
			catch(er:*){
				sendNotification( ApplicationFacade.ERROR_COMMAND, er.message);
				return;
			}
			
			
			// both of the xmls are now loaded, proceeed
			sendNotification( ApplicationFacade.XMLLOADED_COMMAND);
		}
		
		private function onXmlError(evt : ErrorEvent) : void{
			sendNotification( ApplicationFacade.ERROR_COMMAND, "Error loading xml file : " + evt.text + "\n" );
		}
		
		
	}//class
}//package
