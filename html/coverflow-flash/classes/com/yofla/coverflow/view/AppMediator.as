package com.yofla.coverflow.view {
	import flash.display.MovieClip;
	import flash.display.Sprite;
	import flash.display.Stage;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.text.TextField;
	import flash.utils.Timer;
	
	import org.puremvc.as3.multicore.interfaces.INotification;
	import org.puremvc.as3.multicore.patterns.mediator.Mediator;
	
	import com.yofla.coverflow.ApplicationFacade;
	import com.yofla.coverflow.CoverFlowApp;
	import com.yofla.coverflow.model.AppProxy;
	import com.yofla.coverflow.view.components.Preloader;
	import com.yofla.coverflow.view.components.Scrollbar;		

	/**
	 * AppMediator main feature is to initialize and register the other 
	 * mediators (like controls, cursors and others). It also initializes
	 * the application movie parameters (width, height, position) and
	 * stores it in the application model - AppProxy . It also sets mask
	 * for the application.
	 * 
	 * @author Matus Laco, www.yofla.com
	 * @see    AppProxy
	 */
	public class AppMediator extends Mediator {

		//--------------------------------------------------------
		// PRIVATE VARS
		//--------------------------------------------------------
		
		public static var NAME : String = "AppMediator";

		//--------------------------------------------------------
		// PRIVATE VARS
		//--------------------------------------------------------
		
		// proxies
		private var appProxy : AppProxy;
		
		// debug
		private var tf : TextField;
		private var tm : Timer;
		private var _preloader : MovieClip;

		
		//--------------------------------------------------------
		// CONSTURCTOR
		//--------------------------------------------------------
		/**
		 * The viewComponent of this mediator is the FlashZoomerApp.
		 * The AppMediator registers MouseUp listener with the stage and
		 * when event is triggered, notification is sent to other mediators.
		 * 
		 * @param viewComponent
		 */
		public function AppMediator(viewComponent : Object = null) {

			super( NAME, viewComponent );
			
		}
		
		public function init() : void {
			
			// retrieve proxies
			appProxy = facade.retrieveProxy( AppProxy.NAME ) as AppProxy;
			
			//add global mouse actions
			app.stage.addEventListener(MouseEvent.MOUSE_UP, stageMouseUpListener );
						
			// init dims
			initApp();
			
			// init mediators
			initMediators();
			
			// add preloader
			showPreloader();
			
		}
		
		
		private function showPreloader() : void {
			// TODO
			_preloader = new Preloader( );
			_preloader.x = appProxy.appWidth / 2;			_preloader.y = appProxy.appHeight / 2;
			
			app.addChild( _preloader );
		}

		//--------------------------------------------------------
		// OVERRIDES
		//--------------------------------------------------------
		
		/**
		 * List of notification Interests
		 */
		override public function listNotificationInterests() : Array {
			return [ApplicationFacade.XML_LOADED];
		}

		override public function handleNotification( note : INotification ) : void {
			switch ( note.getName() ) {
				case ApplicationFacade.XML_LOADED:
					app.removeChild( _preloader );
				break;
			}
		}
		
		//--------------------------------------------------------
		// PRIVATE FUNCTIONS - NOTIFICATION HANDLING
		//--------------------------------------------------------

		/**
		 * Triggered when assets are loaded stores the application movie parameters. 
		 * Sets application mask.
		 */
		private function initApp() : void {

			var appPos : Point = new Point(0,0);

			// STORE DIMS	
			appProxy.appWidth = app.width;			appProxy.appHeight = app.height;
			appProxy.appPos = appPos;

		}
		
		private function initMediators() : void {

			
			// COVERS MEDIATOR
			var covers : Sprite = new Sprite();
			app.addChild( covers );
			var coversMediator : CoversMediator = new CoversMediator( covers );
			facade.registerMediator( coversMediator );
			coversMediator.init();
			
		 	// CONTROLS MEDIATOR
			var scrollbar : Sprite = new Scrollbar( );
			app.addChild( scrollbar );
			var scrollbarMediator : ScrollbarMediator = new ScrollbarMediator( scrollbar );
			facade.registerMediator( scrollbarMediator );
			scrollbarMediator.init();
			
			// COPY MEDIATOR		
			var copy : Sprite = new Sprite();
			app.addChild( copy );
			var copyMediator : CopyMediator = new CopyMediator( copy );
			facade.registerMediator( copyMediator );
			copyMediator.init();
		}

		//--------------------------------------------------------
		// PRIVATE FUNCTIONS  
		//--------------------------------------------------------


		//--------------------------------------------------------
		// PRIVATE FUNCTIONS - callbacks 
		//--------------------------------------------------------

		private function stageMouseUpListener(e : MouseEvent) : void {
			sendNotification( ApplicationFacade.STAGE_MOUSEUP, e);
		}
	
		//--------------------------------------------------------
		// GETTERS AND SETTERS
		//--------------------------------------------------------

		private function get app() : CoverFlowApp {
			return viewComponent as CoverFlowApp;	
		}
		
	}//class
}//package