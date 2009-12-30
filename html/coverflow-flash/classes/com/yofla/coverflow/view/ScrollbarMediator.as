package com.yofla.coverflow.view {
	import flash.display.SimpleButton;
	import flash.events.MouseEvent;
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import org.puremvc.as3.multicore.interfaces.INotification;
	import org.puremvc.as3.multicore.patterns.mediator.Mediator;
	
	import com.yofla.coverflow.ApplicationFacade;
	import com.yofla.coverflow.model.AppProxy;
	import com.yofla.coverflow.model.ConfigProxy;
	import com.yofla.coverflow.view.components.Scrollbar;		

	/**
	 * Scrollbar Mediator 
	 * 
	 * @author Matus Laco, www.yofla.com
	 * @see    AppProxy
	 */
	public class ScrollbarMediator extends Mediator {
		
		//--------------------------------------------------------
		// PRIVATE CONSTS
		//--------------------------------------------------------

		private const MOUSE_DOWN_LEFT  : uint = 1;		private const MOUSE_DOWN_RIGHT : uint = 2;		private const MOUSE_UP         : uint = 0;

		//--------------------------------------------------------
		// PUBLIC CONSTS
		//--------------------------------------------------------
	
		public const NAME : String = "scrollbarMediator";
		
		//--------------------------------------------------------
		// PRIVATE VARS
		//--------------------------------------------------------

		private var appProxy : AppProxy;
		
		// assets
		private var btnLeft   : SimpleButton;		private var btnRight  : SimpleButton;		private var btnHandle : SimpleButton;
		
		private var handleMaxX : Number;		
		private var isHandleDragging : Boolean;
		private var mouseTimer : Timer;
		private var prevCurrent : Number;
		
		private var mouseDownPositionX : Number;
		
		private var handlePixelStep : Number;
		private var mouseDownCurrent : uint;
		
		private var _clickDelay : uint;
		private var _mouseState : uint;
		private var _checkDownTimer : Timer;
		private var _scrollTimer : Timer;
		private var _scrollDelta : Number;

		//--------------------------------------------------------
		// CONSTURCTOR
		//--------------------------------------------------------
		/**
		 * The viewComponent of this mediator is an empty sprite
		 * created by 
		 * @param viewComponent
		 */
		
		public function ScrollbarMediator(viewComponent : Object = null) {
			super( NAME, viewComponent );
		}

		public function init() : void {

			// retrieve proxies
			appProxy = facade.retrieveProxy( AppProxy.NAME ) as AppProxy;
			
			// init assets
			btnHandle = component.handle;
			btnRight  = component.right;
			btnLeft   = component.left;
			
			component.visible = false;			
						
			btnHandle.x = 0;
			
			// init vars
			handleMaxX = btnRight.x - btnHandle.width;
		}


		//--------------------------------------------------------
		// OVERRIDES
		//--------------------------------------------------------
		
		/**
		 * List of notification Interests
		 */
		override public function listNotificationInterests() : Array {
			return [ApplicationFacade.XML_LOADED,
					ApplicationFacade.STAGE_MOUSEUP,
					ApplicationFacade.NEW_CURRENT];
		}

		override public function handleNotification( note : INotification ) : void {
			switch ( note.getName() ) {
				case ApplicationFacade.XML_LOADED:
					
					var configProxy : ConfigProxy = facade.retrieveProxy( ConfigProxy.NAME ) as ConfigProxy;
					
					// show controls
					component.visible = true;					
					
					// position assets
					component.y = appProxy.appHeight - component.height - configProxy.controlsOffsetBottom;
					component.x = (appProxy.appWidth - component.width) / 2 + 25;
					
					// add mouse events
					btnHandle.addEventListener( MouseEvent.MOUSE_DOWN, handleMouseDownListener);
					
					
					//down
					btnLeft.addEventListener( MouseEvent.MOUSE_DOWN, leftDownListener );
					btnRight.addEventListener( MouseEvent.MOUSE_DOWN, rightDownListener );
					
					// init vars
					_checkDownTimer = new Timer(configProxy.clickDelay);
					_checkDownTimer.addEventListener( TimerEvent.TIMER, checkDownTimerListener);
					
					// compute handlePixelSteip
					handlePixelStep = handleMaxX / (appProxy.itemsCount - 1);
				break;

				case ApplicationFacade.STAGE_MOUSEUP:
					if(mouseTimer) {
						if (mouseTimer.running) mouseTimer.stop();
						mouseTimer.removeEventListener(TimerEvent.TIMER, mcHandleTimer);
						mouseTimer = null;
					}
					if(_scrollTimer) {
						if (_scrollTimer.running) _scrollTimer.stop();
						_scrollTimer.removeEventListener(TimerEvent.TIMER, scrollTimerListener);
						_scrollTimer = null;
					}
					if(_checkDownTimer){
						
					}
					_mouseState = MOUSE_UP;						
				break;
				case ApplicationFacade.NEW_CURRENT:
					var current : Number = note.getBody() as Number;
					btnHandle.x = Math.round(current * handlePixelStep);
					
					
				break;
			}
		}
		

		//--------------------------------------------------------
		// PRIVATE FUNCTIONS
		//--------------------------------------------------------
		
		/**
		 * Updates the tweenedCurrent property before any position change is started.
		 * Necessary because the tweening function needs a startpoint...
		 */
		private function storeCurrentTween() : void {
			appProxy.tweenedCurrent = appProxy.current;
		}
		
		private function updatePosition( delta : int ) : void {
			
			// gett new pos
			var newCurrent : int = appProxy.current + delta;
			
			// check pos
			if(newCurrent < 0) newCurrent = 0;
			if(newCurrent >= appProxy.itemsCount) newCurrent = appProxy.itemsCount - 1; 
			
			// tween
			appProxy.setNewPos( newCurrent );
		}

		//--------------------------------------------------------
		// PRIVATE FUNCTIONS - CALLBACKS 
		//--------------------------------------------------------

		/**
		 * MOUSE DOWN LISTENER - SLIDER HANDLE
		 * After the scale slider is pressed...
		 */
		private function handleMouseDownListener( e : MouseEvent ) : void {
			//xx var myRect : Rectangle = new Rectangle( 0, btnHandle.y, handleMaxX, 0 );
			//xx btnHandle.startDrag(false,myRect);
			// start monitoring handle movement
			
			mouseDownPositionX = component.mouseX;
			mouseDownCurrent = appProxy.current;
			
			mouseTimer = new Timer( 20 );
			mouseTimer.addEventListener( TimerEvent.TIMER, mcHandleTimer);
			mouseTimer.start();
			isHandleDragging = true;
			
			// store start tween
			storeCurrentTween();
		}
		

		/**
		 * MOUSE MOVE TIMER - handle
		 */
		private function mcHandleTimer( e : TimerEvent) : void {
			
			
			var currentMousueX : Number = component.mouseX;
			var diff : Number = currentMousueX - mouseDownPositionX;
			var steps : Number = Math.ceil(diff / handlePixelStep);
			var newCurrent : int = mouseDownCurrent + steps; 
			if (newCurrent < 0) newCurrent = 0;
			if (newCurrent > appProxy.itemsCount-1) newCurrent = appProxy.itemsCount - 1;  
			
			if(prevCurrent != newCurrent){
				appProxy.setNewPos ( newCurrent );
			}
					
			prevCurrent = newCurrent;
		}

		

		/**
		 * MOUSE DOWN - left
		 */
		private function leftDownListener( e : MouseEvent) : void {
			if(!appProxy.isTweening()) storeCurrentTween();
			// save state
			_mouseState = MOUSE_DOWN_LEFT;
			// tween
			updatePosition(-1);
			// check if mouse down	
			_checkDownTimer.start();
		}


		/**
		 * MOUSE DOWN - right
		 */
		private function rightDownListener(e : MouseEvent) : void {
			if(!appProxy.isTweening()) storeCurrentTween();
			// save state
			_mouseState = MOUSE_DOWN_RIGHT;
			// tween
			updatePosition(1);
			// check if mouse down	
			_checkDownTimer.start();
		}

		/**
		 * TIMER - check down
		 */
		private function checkDownTimerListener(t : TimerEvent) : void {
			_checkDownTimer.stop();
			
			if(_mouseState == MOUSE_DOWN_LEFT || _mouseState == MOUSE_DOWN_RIGHT){
				_scrollTimer = new Timer(200);
				_scrollTimer.addEventListener( TimerEvent.TIMER, scrollTimerListener);
				_scrollDelta = 1;
				_scrollTimer.start();
			}
			
		}

		/**
		 * TIMER - scroll
		 * 
		 * called when L or R button is pressed for longer than clickDelay
		 */
		private function scrollTimerListener(t : TimerEvent) : void {
		
			
			if(_mouseState == MOUSE_DOWN_RIGHT){
				updatePosition( _scrollDelta );
			}
			else if(_mouseState == MOUSE_DOWN_LEFT){
				updatePosition( -_scrollDelta );
			}
			_scrollDelta++;
			if(_scrollDelta > 5 )_scrollDelta = 5;
		}

		//--------------------------------------------------------
		// GETTERS AND SETTERS
		//--------------------------------------------------------

		private function get component() : Scrollbar {
			return viewComponent as Scrollbar;	
		}
	}
}
