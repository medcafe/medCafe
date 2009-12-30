package com.yofla.coverflow.model {
	import org.puremvc.as3.multicore.interfaces.IProxy;	
	import org.puremvc.as3.multicore.patterns.proxy.Proxy;	
	
	/**
	 * FeedProxy is reponsible for storing and providing external
	 * parameters
	 * 
	 * @author Matus Laco, www.yofla.com
	 */
	public class FeedProxy extends Proxy implements IProxy {
		
		
		//--------------------------------------------------------------------------------
		// PRIVATE VARS
		//--------------------------------------------------------------------------------
		
		private var paramProxy : ParametersProxy;
		
		
		//--------------------------------------------------------------------------------
		// CONSTS
		//--------------------------------------------------------------------------------
		
		// proxy name
		public static const NAME:String = 'FeedProxy';
	
		//--------------------------------------------------------------------------------
		// CONSTRUCTOR
		//--------------------------------------------------------------------------------
		
		public function FeedProxy(proxyName : String = null, data : Object = null) {
			super( proxyName, data );
		}
		
		//--------------------------------------------------------------------------------
		// PUBLIC FUNCTIONS
		//--------------------------------------------------------------------------------

		//--------------------------------------------------------------------------------
		// GETTERS AND SETTERS
		//--------------------------------------------------------------------------------
		
		public function get xmldata() : XML {
			return data as XML;
		}
		
		public function get current() : uint{
			var value : uint = uint(xmldata..@current[0]);
			return ( value ) ? value : null;	
		}
		

	}//class
}//package
