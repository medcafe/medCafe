package com.yofla.coverflow.model {
	import flash.geom.Point;
	
	import org.puremvc.as3.multicore.interfaces.IProxy;
	import org.puremvc.as3.multicore.patterns.proxy.Proxy;
	
	import com.yofla.coverflow.ApplicationFacade;
	
	import gs.TweenLite;		

	/**
	 * 
	 * @author Matus Laco, www.yofla.com
	 */
	public class AppProxy extends Proxy implements IProxy {
		
		
		//--------------------------------------------------------------------------------
		// PRIVATE VARS
		//--------------------------------------------------------------------------------
		
		
		// proxies
		private var paramProxy : ParametersProxy;		private var configProxy : ConfigProxy;
		
		
		// app vars
		private var _appWidth : Number;
		private var _appHeight : Number;
		private var _appPos : Point;
		private var _easingFunction : Function;
		
		// runtime vars
		private var _current    : Number;		private var _itemsCount : uint;
		private var _tweenedCurrent : Number;
		private var _isTweening : Boolean;
		

		//--------------------------------------------------------------------------------
		// PUBLIC VARS
		//--------------------------------------------------------------------------------

		//--------------------------------------------------------------------------------
		// CONSTS
		//--------------------------------------------------------------------------------
		
		// proxy name
		public static const NAME:String = 'AppProxy';

		//--------------------------------------------------------------------------------
		// CONSTRUCTOR
		//--------------------------------------------------------------------------------
		
		public function AppProxy(proxyName : String = null, data : Object = null) {
			super( NAME);
		}

		//--------------------------------------------------------------------------------
		// PUBLIC FUNCTIONS
		//--------------------------------------------------------------------------------

		public function init() : void {
			paramProxy = facade.retrieveProxy( ParametersProxy.NAME ) as ParametersProxy;
			configProxy = facade.retrieveProxy( ConfigProxy.NAME ) as ConfigProxy;
			_isTweening = false;
		}


		public function setNewPos(newCurrent : Number) : void {
			
			// return if not change
			if(newCurrent == _current) return;

			// notify change			
			sendNotification( ApplicationFacade.NEW_CURRENT, newCurrent );
						
			// tween
			TweenLite.to( this, configProxy.ease, {tweenedCurrent: newCurrent, ease : _easingFunction, onUpdate : tweenedCurrentOnUpdate, onComplete : tweenedCurrentOnComplete} );
			_isTweening = true;			
			
			// store change
			_current = newCurrent;
		}

		
		public function isTweening() : Boolean {
			return _isTweening;	
		}

		//--------------------------------------------------------------------------------
		// PRIVATE FUNCTIONS
		//--------------------------------------------------------------------------------

		private function tweenedCurrentOnUpdate() : void {			
			sendNotification( ApplicationFacade.NEW_CURRENT_TWEENED, tweenedCurrent );
		}

		private function tweenedCurrentOnComplete() : void {
			// notify once again so the covers are redrawn (expecially alphas)
			// this fixes the issue if center distance is to small and
			// the tween is accros a longer distance 
			
			sendNotification( ApplicationFacade.NEW_CURRENT_TWEENED, _current);			_isTweening = false;
		}


		//--------------------------------------------------------------------------------
		// GETTERS AND SETTERS
		//--------------------------------------------------------------------------------

		public function set appWidth ( n : Number) : void{
			_appWidth = n;	
		}
		public function set appHeight ( n : Number) : void {
			_appHeight  = n;	
		}
		public function get appWidth() : Number {
			return _appWidth;
		}
		public function get appHeight() : Number {
			return _appHeight;
		}

		public function set appPos (p : Point) : void {
			_appPos = p;
		}

		public function get appPos () : Point {
			return _appPos;
		}
		
		public function get appCenter() : Point {
			return new Point( _appPos.x + _appWidth / 2, _appPos.y + _appHeight / 2);	
		}
		
				
		public function set easingFunction( f : Function) : void {
			_easingFunction = f;
		} 
		
		public function set current (n : Number) : void {
			if(n >= itemsCount) n = itemsCount -1;
			if(n < 0) n = 0;
			_current = n;	
		}
		
		public function get current () : Number {
			return _current;	
		}

		public function set tweenedCurrent (n : Number) : void {
			_tweenedCurrent = n;	
		}
		
		public function get tweenedCurrent () : Number {
			return _tweenedCurrent;	
		}


		public function set itemsCount (n : uint) : void {
			_itemsCount = n;	
		}
		
		public function get itemsCount () : uint {
			return _itemsCount;	
		}
		
	}//class
}//package
