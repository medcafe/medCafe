package com.yofla.coverflow.view {
	import com.dynamicflash.util.Base64;	
	
	import flash.display.Loader;
	import flash.display.Sprite;
	import flash.events.ContextMenuEvent;
	import flash.events.MouseEvent;
	import flash.text.TextField;
	import flash.text.TextFieldAutoSize;
	import flash.text.TextFormat;
	import flash.ui.ContextMenu;
	import flash.ui.ContextMenuItem;
	import flash.utils.ByteArray;
	
	import org.puremvc.as3.multicore.interfaces.INotification;
	import org.puremvc.as3.multicore.patterns.mediator.Mediator;
	
	import com.yofla.coverflow.ApplicationFacade;
	import com.yofla.coverflow.CoverFlowApp;
	import com.yofla.coverflow.model.AppProxy;
	import com.yofla.coverflow.view.components.InfoData;
	
	import gs.TweenLite;		

	/**
	 * Copyright mediator
	 * 
	 * @author Matus Laco, www.yofla.com
	 */
	public class CopyrightMediator extends Mediator {

		//--------------------------------------------------------
		// PRIVATE VARS
		//-------------------------------------------------------

		private var appProxy : AppProxy;
		
		private var myTextField : TextField;
		private var textFormatNormal : TextFormat;
		private var textFormatOver : TextFormat;
		private var infoBox : Sprite;
						
		private var copyRightUrl : String = "http://www.yofla.com/flash/cover-flow/?ref=player";
		
		
		//--------------------------------------------------------
		// PUBLIC VARS
		//-------------------------------------------------------

        // Cannonical name of the Mediator
        public static const NAME:String = 'CopyrightMediator';
		
		//--------------------------------------------------------
		// CONSTRUCTOR
		//-------------------------------------------------------
		
		/**
		 * The viewComponent of hte mediator is the FlashZoomerApp 
		 */
		public function CopyrightMediator(viewComponent : Object = null) {
			super( NAME, viewComponent);
		}

		//--------------------------------------------------------
		// OVERRIDES
		//-------------------------------------------------------

 		override public function listNotificationInterests():Array 
        {
            return [ApplicationFacade.NEW_CURRENT];
		}
		
	/**
	 * Handling of notifications
	 */		
        override public function handleNotification( note:INotification ):void 
        {
            switch ( note.getName() ) {
				case ApplicationFacade.NEW_CURRENT:
					hideInfo();
				break;
			}
		}


		//--------------------------------------------------------
		// PUBLIC FUNCTIONS
		//-------------------------------------------------------


		public function init() : void {
			// reference proxies
			appProxy = facade.retrieveProxy(AppProxy.NAME) as AppProxy;
			
			// attach info box
			attachInfoBox();

			// CUSTOMIZE CONTEX MENU
			var myContextMenu : ContextMenu = new ContextMenu();
			myContextMenu.hideBuiltInItems();
			var item:ContextMenuItem = new ContextMenuItem("About Cover Flow script...");
        	myContextMenu.customItems.push(item);
        	item.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, menuItemSelectHandler);
        	app.contextMenu = myContextMenu;
		}

		//--------------------------------------------------------
		// PRIVATE FUNCTIONS
		//-------------------------------------------------------
		
		/**
		 * Initializes the info box
		 */
		private function attachInfoBox() : void {
			infoBox = new Sprite;
			
			// init infobox image
			var info : InfoData = new InfoData( );
			var info64 : String = info.info64;
			var infoBA : ByteArray = Base64.decodeToByteArray(info64);
			var loader : Loader = new Loader( );
			loader.loadBytes(infoBA);
			
			infoBox.addChild(loader);			
			
			// center info box - we dont use and onLoad listener so we define image size here
			var infoWidth  : uint = 280;
			var infoHeight : uint = 165;
			
			// link			
			var format : TextFormat = new TextFormat();
            format.font = "Verdana";
            format.color = 0x0000FF;
            format.size = 12;
            format.underline = true;

			
			// textfield
			var tf : TextField = new TextField();
			tf.autoSize = TextFieldAutoSize.LEFT;
			tf.htmlText = '<font color="#000000" face="Arial, Verdana, Sans">Homepage:</a> <a href="' + copyRightUrl + '" target="_blank"><u><font color="#0000FF" face="Arial, Verdana, Sans">www.yofla.com/flash/cover-flow/</font></u></a>';
			tf.defaultTextFormat = format;
			tf.x = 10;
			tf.y = 140;
			
			infoBox.addChild(tf); 
			
			infoBox.visible = false;
			infoBox.alpha = 0;
						
			app.addChild(infoBox);			
			infoBox.x = app.width / 2 - infoWidth / 2; 
			infoBox.y = app.height / 2  - infoHeight / 2 - 50;
			
			// mouse actions
			infoBox.addEventListener(MouseEvent.CLICK, infoClickListener);
			 
		}


		private function showInfo() : void {
			if (infoBox.alpha == 0){				
				infoBox.visible = true;
				TweenLite.to( infoBox, 0.2, {alpha : 1});
			}
			else{
				hideInfo();
			}			
		}
		
		private function hideInfo() : void {
			if (infoBox){
				infoBox.alpha = 0;
				infoBox.visible = false;
			}
		}

		//--------------------------------------------------------
		// PRIVATE FUNCTIONS - CALLBACKS
		//--------------------------------------------------------


		private function menuItemSelectHandler( e : ContextMenuEvent ) : void {
			showInfo();			
		}

		private function infoClickListener(e : MouseEvent) : void {
			hideInfo();
		}


		//--------------------------------------------------------
		// GETTERS AND SETTERS
		//--------------------------------------------------------

		private function get app() : CoverFlowApp {
			return viewComponent as CoverFlowApp;	
		}
		
		
	}//class
}//package