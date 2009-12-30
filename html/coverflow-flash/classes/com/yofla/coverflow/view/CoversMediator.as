package com.yofla.coverflow.view {
	import org.puremvc.as3.multicore.interfaces.INotification;	
	import org.puremvc.as3.multicore.patterns.mediator.Mediator;	

	import flash.display.Sprite;
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import com.yofla.coverflow.ApplicationFacade;
	import com.yofla.coverflow.model.AppProxy;
	import com.yofla.coverflow.model.ConfigProxy;
	import com.yofla.coverflow.model.FeedProxy;		

	/**
	 * CoversMediator is ... 
	 * 
	 * @author Matus Laco, www.yofla.com
	 * @see    AppProxy
	 */
	public class CoversMediator extends Mediator {
		
		//--------------------------------------------------------
		// PRIVATE VARS
		//--------------------------------------------------------

		private var appProxy : AppProxy;		private var configProxy : ConfigProxy;		private var feedProxy : FeedProxy;
		
		private var aItems : Array;
		private var checkLoadedTimer : Timer;
		private var aItemsNotLoaded : Array;
		private var _loadedInBatch : Number;
		private var _loadedTotal : Number; 

		// defines the count of images which should start to load after all 
		// visible images are loaded
		private const BATCHSIZE : uint = 20;

		//--------------------------------------------------------
		// PUBLIC CONSTS
		//--------------------------------------------------------

		public static const NAME : String = "CoversMediator";


		//--------------------------------------------------------
		// CONSTURCTOR
		//--------------------------------------------------------
		/**
		 * The viewComponent of this mediator is an empty sprite
		 * created by 
		 * @param viewComponent
		 */
		
		public function CoversMediator(viewComponent : Object = null) {
			super( NAME, viewComponent );
		}
		
		public function init () : void {
			// retrieve proxies
			appProxy     = facade.retrieveProxy( AppProxy.NAME ) as AppProxy;
		}

		//--------------------------------------------------------
		// OVERRIDES
		//--------------------------------------------------------
		
		/**
		 * List of notification Interests
		 */
		override public function listNotificationInterests() : Array {
			return [ApplicationFacade.XML_LOADED,
					ApplicationFacade.IMAGE_LOADED,
					ApplicationFacade.NEW_CURRENT];
		}

		override public function handleNotification( note : INotification ) : void {
			switch ( note.getName() ) {
				case ApplicationFacade.XML_LOADED:

					configProxy  = facade.retrieveProxy( ConfigProxy.NAME ) as ConfigProxy;

					component.x = appProxy.appWidth / 2 - configProxy.frontCDWidth /2;
					component.y = appProxy.appHeight / 2  - configProxy.frontCDHeight /2 + configProxy.coversOffsetCenter;
					
					aItems = new Array;
					
					// create items					
					for (var i : Number = 0; i < appProxy.itemsCount ; i++) {
						var itemMediator : ItemMediator = new ItemMediator( component );
						facade.registerMediator( itemMediator );
						itemMediator.init(i);
						
						aItems.push( itemMediator );
					}
					
					// check loading status
					checkLoadedTimer = new Timer(500);
					checkLoadedTimer.addEventListener( TimerEvent.TIMER, checkLoadedTimerlistener);
					checkLoadedTimer.start();
					
				break;
				case ApplicationFacade.IMAGE_LOADED :
					_loadedInBatch++;
					_loadedTotal++;
					if(_loadedInBatch >= BATCHSIZE){
						//restart timer
						if(!checkLoadedTimer.running) checkLoadedTimer.start();
					}				
				break;
				case ApplicationFacade.NEW_CURRENT :
					if(_loadedTotal == appProxy.itemsCount) return;
					// restart the the timer to check loaded images
					if(!checkLoadedTimer.running)	checkLoadedTimer.start();
				break;
			}
		}


		//--------------------------------------------------------
		// PRIVATE FUNCTIONS
		//--------------------------------------------------------
		
		
		/**
		 * Check on regular basis (timer). If there is no motion. If there is no 
		 * motion - no tweening and all visible images are loaded, a batch of BATCHSIZE
		 * imates is loaded.
		 */
		private function checkLoadedTimerlistener ( t : TimerEvent) : void {
			
			// dont perform loading if tweening - itemMediators will load themselfes
			if(appProxy.isTweening()) return;
			
			var visibleItems : uint = 0;
			var loadedVisible : uint = 0;
			
			// reset the batch counter. increased only if all visible are loaded and timer is not active
			_loadedInBatch = 0;
			
			// for storing not loaded items
			aItemsNotLoaded = new Array();
			
			for (var i : Number = 0; i < aItems.length; i++) {
				if((aItems[i] as ItemMediator).isVisible()){
					
					// count how many visible items are loaded
					 visibleItems++;
					 if((aItems[i] as ItemMediator).isLoaded() ) loadedVisible++;
				}
				else{
					 if(!(aItems[i] as ItemMediator).isLoaded() ){
					 	// not visible not loaded - store to array
						aItemsNotLoaded.push(aItems[i]);
					}
				}
			}
			
			// if all visible are loaded, load batch of unloaded images			
			if(visibleItems == loadedVisible) {
				if(checkLoadedTimer.running) checkLoadedTimer.stop();
				
				_loadedInBatch = 0;
				
				var toLoad : uint = Math.min( BATCHSIZE, aItemsNotLoaded.length );
				
				for (var j : Number = 0; j < toLoad ; j++) {
					(aItemsNotLoaded[j] as ItemMediator).load();
				}	
			}
		}
		
		//--------------------------------------------------------
		// GETTERS AND SETTERS
		//--------------------------------------------------------

		private function get component() : Sprite {
			return viewComponent as Sprite;	
		}
	}
}
