package com.yofla.coverflow.model {
	import org.puremvc.as3.multicore.interfaces.IProxy;	
	import org.puremvc.as3.multicore.patterns.proxy.Proxy;	

	/**
	 * ConfigProxy is reponsible for storing and providing external
	 * parameters
	 * 
	 * @author Matus Laco, www.yofla.com
	 */
	public class ConfigProxy extends Proxy implements IProxy {
		//--------------------------------------------------------------------------------
		// PRIVATE VARS
		//--------------------------------------------------------------------------------
		
		private var paramProxy : ParametersProxy;
		
		
		//--------------------------------------------------------------------------------
		// CONSTS
		//--------------------------------------------------------------------------------
		
		// proxy name
		public static const NAME:String = 'ConfigProxy';
	
		//--------------------------------------------------------------------------------
		// CONSTRUCTOR
		//--------------------------------------------------------------------------------
		
		public function ConfigProxy(proxyName : String = null, data : Object = null) {
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

		public function get showImageError() : Boolean {
			var value : String = xmldata.showImageError; 
			return ( value == "true" ) ? true : false;	
		}
		
		public function get frontCDWidth() : uint{
			var value : uint = xmldata.frontCDWidth; 
			return ( value ) ? value : 200;	
		}

		public function get frontCDHeight() : uint{
			var value : uint = xmldata.frontCDHeight; 
			return ( value ) ? value : 275;	
		}

		public function get shelveCDHeight() : uint{
			var value : uint = xmldata.shelveCDHeight; 
			return ( value ) ? value : 180;	
		}

		public function get shelveCDWidth() : uint{
			var value : uint = xmldata.shelveCDWidth; 
			return ( value ) ? value : 50;	
		}

		public function get shelveCDSpacing() : uint{
			var value : uint = xmldata.shelveCDSpacing; 
			return ( value ) ? value : 60;	
		}

		public function get centerDistance() : uint{
			var value : uint = xmldata.centerDistance; 
			return ( value ) ? value : 100;	
		}

		public function get angle() : uint{
			var value : uint = xmldata.angle; 
			return ( value ) ? value : 40;	
		}
		
		public function get clickDelay() : uint{
			var value : uint = xmldata.clickDelay; 
			return ( value ) ? value : 500;	
		}
		
		public function get current() : uint{
			var value : uint = xmldata.current; 
			return ( value ) ? value : 0;	
		}

		public function get fontColor() : uint{
			var value : uint = xmldata.fontColor; 
			return ( value ) ? value : 0xFFFFFF;	
		}

		public function get fontBold() : Boolean{
			var value : String = xmldata.fontBold; 
			return ( value == "true" ) ? true : false;	
		}

		public function get fadeDist() : uint{
			var value : uint = xmldata.fadeDist; 
			return ( value ) ? value : 50;	
		}

		public function get fadeSpan() : uint{
			var value : uint = xmldata.fadeSpan; 
			return ( value ) ? value : 40;	
		}


		public function get ease() : Number {
			var value : Number  = xmldata.duration; 
			return ( value ) ? value : 0.8;	
		}

		public function get controlsOffsetBottom() : Number {
			var value : Number  = xmldata.controlsOffsetBottom; 
			return ( value ) ? value : 50;	
		}

		public function get coversOffsetCenter() : Number {
			var value : Number  = xmldata.coversOffsetCenter; 
			return ( value ) ? value : -60;	
		}

		public function get copyOffsetCenter() : Number {
			var value : Number  = xmldata.copyOffsetCenter; 
			return ( value ) ? value : 60;	
		}


		public function get gradientAlpha() : Number {
			var value : Number  = xmldata.gradientAlpha; 
			return ( value ) ? value : 0;	
		}

		public function get gradientHeight() : Number {
			var value : Number  = xmldata.gradientHeight; 
			return ( value ) ? value : 100;	
		}

		public function get easing() : String {
			var value : String  = xmldata.easing; 
			return ( value ) ? value : null;	
		}

		public function get reflectionBackgroundColor() : uint {
			var value : uint  = xmldata.fadeColor; 
			return ( value ) ? value : 0x000000;	
		}


		
	}//class
}//package
