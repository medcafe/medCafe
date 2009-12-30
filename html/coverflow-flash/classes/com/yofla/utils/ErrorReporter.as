package com.yofla.utils {
	
	import flash.display.Stage;
	import flash.display.Sprite;
	import flash.display.Graphics;
	import flash.text.TextField;
	import flash.text.TextFieldAutoSize;
	import flash.text.StyleSheet;

	/**
	 * Last edit: 21/01/2008
	 * 
	 * @author Matus Laco, www.yofla.com
	 * @crated 11/2007
	 * 
	 */
	public class ErrorReporter {
		
		private static var instance : ErrorReporter;
		private var stage : Stage;
		private var container : Sprite;
		private var tf : TextField;

		/**
		 * Empty constructor
		 */
		public function ErrorReporter() {			
		}
		
		
		public static function init(s : Stage) : void {			
			if(instance == null) instance = new ErrorReporter();			
			instance.stage = s;						
		}
		
		public static function addError(errorString : String) : void {			
			if(instance.container == null) instance.drawInterface();
			instance.tf.htmlText += "<li class='error'>" + errorString + "</li>\n";			
		}
		
		
		private  function drawInterface() : void {
			
			instance.container = new Sprite();

			//get dims
			var width : uint = instance.stage.stageWidth;			var height : uint = instance.stage.stageHeight;

			// background
			var background : Sprite = new Sprite();
			var bg : Graphics = background.graphics;
			instance.container.addChild(background);
			bg.clear();
			bg.lineStyle(1, 0xff0000);
			bg.beginFill(0xFFFF00, 1);
			bg.drawRect(5, 5, width - 10, height - 10);
			
		
			// create text styles
			var styles : StyleSheet = new StyleSheet();

			var heading:Object = new Object();
            heading.fontWeight = "bold";
            heading.fontFamily = "Arial";
            heading.color = "#FF0000";

			var error:Object = new Object();
            error.fontFamily = "Arial";
            
            styles.setStyle("h1", heading);            styles.setStyle(".error", error);

			// textfield
			tf = new TextField();			
			tf.x = 10;
			tf.y = 10;
			tf.width = width - 20;
			tf.styleSheet = styles;
			tf.wordWrap = true;
			tf.autoSize = TextFieldAutoSize.LEFT;			
			
			// add text
			tf.htmlText = '<h1>Error Reporting:</h1>\n';

			// add tf to container
			instance.container.addChild(tf);
		 
			 						
			//add container to stage			
			instance.stage.addChild(container);
		}
	}
}
