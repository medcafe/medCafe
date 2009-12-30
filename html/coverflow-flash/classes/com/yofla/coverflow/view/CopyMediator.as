package com.yofla.coverflow.view {
	import org.puremvc.as3.multicore.interfaces.INotification;	
	import org.puremvc.as3.multicore.patterns.mediator.Mediator;	
	
	import com.yofla.coverflow.model.ConfigProxy;	
	
	import flash.display.Sprite;
	import flash.text.AntiAliasType;
	import flash.text.TextField;
	import flash.text.TextFieldAutoSize;
	import flash.text.TextFormat;
	import flash.text.TextFormatAlign;
	
	
	import com.yofla.coverflow.ApplicationFacade;
	import com.yofla.coverflow.model.AppProxy;
	import com.yofla.coverflow.model.FeedProxy;		

	/**
	 * CopyMediator is ... 
	 * 
	 * @author Matus Laco, www.yofla.com
	 * @see    AppProxy
	 */
	public class CopyMediator extends Mediator {
		
		//--------------------------------------------------------
		// CONST
		//--------------------------------------------------------
		
		public const NAME : String  = "copyMediator";
		
		
		//--------------------------------------------------------
		// PRIVATE VARS
		//--------------------------------------------------------
		
		private var _tf : TextField;
		
		private var appProxy : AppProxy;		private var feedProxy : FeedProxy;

		//--------------------------------------------------------
		// CONSTURCTOR
		//--------------------------------------------------------
		/**
		 * The viewComponent of this mediator is an empty sprite
		 * created by 
		 * @param viewComponent
		 */
		
		public function CopyMediator(viewComponent : Object = null) {
			super( NAME, viewComponent );
		}

		public function init () : void {
			// retrieve proxies
			appProxy = facade.retrieveProxy( AppProxy.NAME ) as AppProxy; 
		}

		//--------------------------------------------------------
		// OVERRIDES
		//--------------------------------------------------------
		
		/**
		 * List of notification Interests
		 */
		override public function listNotificationInterests() : Array {
			return [ApplicationFacade.XML_LOADED,
					ApplicationFacade.NEW_CURRENT];
		}

		override public function handleNotification( note : INotification ) : void {
			switch ( note.getName() ) {
				case ApplicationFacade.XML_LOADED:
					
					var configProxy : ConfigProxy = facade.retrieveProxy( ConfigProxy.NAME ) as ConfigProxy;
					
					_tf = new TextField();
					_tf.defaultTextFormat = new TextFormat( "Arial", 14, configProxy.fontColor, configProxy.fontBold, null, null, null, null, TextFormatAlign.CENTER );
	            	_tf.antiAliasType = AntiAliasType.NORMAL;
					_tf.autoSize = TextFieldAutoSize.CENTER;
					_tf.multiline = true;
					_tf.border = false;
										
					_tf.x = appProxy.appWidth / 2;
					_tf.y = appProxy.appHeight / 2 + configProxy.copyOffsetCenter;
					
					component.addChild(_tf); 		
					
					feedProxy = facade.retrieveProxy( FeedProxy.NAME ) as FeedProxy;		
					
				break;
				case ApplicationFacade.NEW_CURRENT:
					var current : uint = note.getBody() as uint;
					var item : XML = feedProxy.xmldata.children()[current];
					// if item is not formated well 
					try{
						_tf.text = item.desc + "\n";
					}
					catch(e:*){}
				break;
			}
		}


		//--------------------------------------------------------
		// PRIVATE FUNCTIONS
		//--------------------------------------------------------

		
		//--------------------------------------------------------
		// GETTERS AND SETTERS
		//--------------------------------------------------------

		private function get component() : Sprite {
			return viewComponent as Sprite;	
		}
	}
}
