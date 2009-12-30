package com.yofla.coverflow.view {
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.display.GradientType;
	import flash.display.Loader;
	import flash.display.Shape;
	import flash.display.Sprite;
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.events.MouseEvent;
	import flash.events.SecurityErrorEvent;
	import flash.external.ExternalInterface;
	import flash.geom.Matrix;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.net.URLRequest;
	import flash.system.LoaderContext;
	
	import org.flashsandy.display.DistortImage;
	import org.puremvc.as3.multicore.interfaces.INotification;
	import org.puremvc.as3.multicore.patterns.mediator.Mediator;
	
	import com.yofla.coverflow.ApplicationFacade;
	import com.yofla.coverflow.model.AppProxy;
	import com.yofla.coverflow.model.ConfigProxy;
	import com.yofla.coverflow.model.FeedProxy;
	import com.yofla.coverflow.view.components.DefaultImageBD;
	import com.yofla.coverflow.view.components.LoadingImageBD;import flash.net.navigateToURL;		

	/**
	 * ItemMediator is ... 
	 * 
	 * @author Matus Laco, www.yofla.com
	 * @see    AppProxy
	 */
	public class ItemMediator extends Mediator {

		//--------------------------------------------------------
		// PRIVATE VARS
		//--------------------------------------------------------

		private var appProxy : AppProxy;
		private var configProxy : ConfigProxy;
		private var feedProxy : FeedProxy;
		
		private var _id : uint;
		private var _content : Sprite;           // content Holder		private var _reflection : Sprite;        // reflection Holder		private var _image : Bitmap;             // content Bitmap
		private var _reflectionImage : Bitmap;   // reflection Bitmap
		private var _distortion : DistortImage;           // distortion of image
		private var _reflectionDistortion : DistortImage;  // distortion of reflection
				
		private var _imageFile : String;
		private var _param : String;
		private var _target : String;
		private var _type : String;
		private var _isLoaded : Boolean;
		private var _isLoading : Boolean;
		private var _loader : Loader;

		private var _isOut : Boolean;

		private var _coverWidth : Number;
		private var _coverHeight : Number;
		private var _coverHeightShelve : Number;
		private var _coverWidthShelve : Number;		private var _centerDistance : Number;		private var _shelveCDspacing : Number;
		private var _loaderContex : LoaderContext;
		private var _xpos : Number;
		private var _backgroundColor : uint = 0x000000;
				
		private var _angle : Number = 40;

		//---------------------------------------------------------------------------------------
		// CONSTS
		//---------------------------------------------------------------------------------------

		private static const kFalloff:Number = .4;
		public static const NAME : String = "itemMediator";
		

		//--------------------------------------------------------
		// CONSTURCTOR
		//--------------------------------------------------------
		/**
		 * The viewComponent of this mediator is an empty sprite
		 * created by CoversMediator
		 * @param viewComponent
		 */
		
		public function ItemMediator(viewComponent : Object = null) {
			super( NAME, viewComponent );
		}

		//--------------------------------------------------------
		// OVERRIDES
		//--------------------------------------------------------
		
		/**
		 * List of notification Interests
		 */
		override public function listNotificationInterests() : Array {
			return [ApplicationFacade.NEW_CURRENT_TWEENED];
		}

		override public function handleNotification( note : INotification ) : void {
			switch ( note.getName() ) {
				case ApplicationFacade.NEW_CURRENT_TWEENED:
					var currentTweened : Number = note.getBody() as Number;
					redrawItem( currentTweened, true );
				break;
			}
		}


		//--------------------------------------------------------
		// PUBLIC FUNCTIONS
		//--------------------------------------------------------
		
		public function init ( id  : uint ) : void {
			_id = id;			

			// retrieve proxies
			appProxy = facade.retrieveProxy( AppProxy.NAME ) as AppProxy;
			configProxy  = facade.retrieveProxy( ConfigProxy.NAME ) as ConfigProxy;
			feedProxy    = facade.retrieveProxy( FeedProxy.NAME ) as FeedProxy;			
			
			
			var imageUrlPrefix : String = configProxy.xmldata.imageURLprefix;
			
			// init image Data
			_imageFile = imageUrlPrefix + feedProxy.xmldata.children()[id].img;
			_param  =  feedProxy.xmldata.children()[id].param;			_target =  feedProxy.xmldata.children()[id].param.@target;			_type   =  feedProxy.xmldata.children()[id].param.@type;
			
			_coverHeight = configProxy.frontCDHeight;			_coverWidth = configProxy.frontCDWidth;
			_coverHeightShelve = configProxy.shelveCDHeight;
			_coverWidthShelve  = configProxy.shelveCDWidth ;			_centerDistance = configProxy.centerDistance;			_shelveCDspacing = configProxy.shelveCDSpacing;
			_angle = configProxy.angle;
						
			_backgroundColor = configProxy.reflectionBackgroundColor;
						
			// add loading image
			var loadingImage : Bitmap = new Bitmap( new LoadingImageBD(100,100) );
			_image = loadingImage;
			
			checkImageSize();
			
			// init content
			_content = new Sprite();
			component.addChild(_content);
			
			// init disortion
			_distortion = new DistortImage(_coverWidth, _coverHeight, 3, 3 );
			
			// create reflection
			_reflection = new Sprite;
			component.addChild( _reflection );
			
			// init disortion
			_reflectionDistortion = new DistortImage(_coverWidth, _coverHeight, 1, 1 );
			
			// draw reflection
			drawReflection();
			
			
			// init content mouse actions
			_content.addEventListener( MouseEvent.CLICK, contentClickListener);
			_content.mouseEnabled = true;
			_content.buttonMode = true;
			
			// as bitmap
			_content.cacheAsBitmap = true;
			_reflection.cacheAsBitmap = true;
		}

		public function isVisible() : Boolean {
			return _content.visible;
		}

		public function isLoaded() : Boolean {
			return _isLoaded;
		}

		
		/**
		 * Resumes loading the image
		 */
		public function load() : void {
			if(!_isLoading){
				loadImage();
			}
			else{
				performLoad();
			}	
		}
		

		//--------------------------------------------------------
		// PRIVATE FUNCTIONS
		//--------------------------------------------------------
		
		/**
		 * Draws tilted item based on current position
		 */
		private function redrawItem( currentTweened : Number, checkZindex : Boolean = false ) : void{
				
				
			var itemPosition : Number = _id - currentTweened;
			
			var diff : Number; 
			var currentAngle : Number = 0;
			var currentHeight : Number;
			var currentHeightRear : Number;
			var currentWidth : Number;
			var _xpos : Number = 0;
			var heightDiff : Number = 0;
			var heightDiffRear : Number = 0;
			var xDiff : Number =0;

			diff = Math.abs( itemPosition );
			
			if(diff <= 1){
				currentHeight = _coverHeightShelve + (1 - diff) * (_coverHeight - _coverHeightShelve);
				_xpos = itemPosition * _centerDistance;
				currentAngle = (diff * _angle);
				currentWidth  = _coverWidthShelve  + (1 - diff) * (_coverWidth  - _coverWidthShelve);
			}
			else{
				currentHeight = _coverHeightShelve;						
				currentAngle = _angle;
				currentWidth = _coverWidthShelve;
				
				if(itemPosition > 0){
					_xpos = _centerDistance + (itemPosition - 1 ) * _shelveCDspacing;
				}
				else{
					_xpos = -_centerDistance + (itemPosition + 1 ) * _shelveCDspacing;
				}
				
			}
			
			//check load status only only when current "is integer"
			var checkLoadStatus : Boolean = (currentTweened - Math.floor( currentTweened )  == 0);
			
			
					
			// check if xposition is not out of viewfield
			if(Math.abs(_xpos ) > appProxy.appWidth / 2){
				_isOut = true;
				_content.visible = false;
				_reflection.visible = false;
				
				
				if(checkLoadStatus){
					if(!_isLoaded){
						// pause loading
						try{
							_loader.close();
						}
						catch(error:Error) {}
					}
					else{
						// clear disorted graphics conent
						_content.graphics.clear();
						_reflection.graphics.clear();
					}
				}
				//----------------------
				// EXIT
				//----------------------
				return;
			}
			else{						
				_isOut = false;
				_content.visible = true;
				_reflection.visible = true;
				
				if(checkLoadStatus){
					// resume loading				
					if(!_isLoaded){
						if(!_isLoading){
							// loading not started yet
							loadImage();
						}
						else{
							// resume loading
							performLoad();
						}													
					}
				}
			}
			
			
			var tlXoffset : Number = 0;
			var tlYoffset : Number = 0;
			
			var trXoffset : Number = 0;
			var trYoffset : Number = 0;

			var blXoffset : Number = 0;
			var blYoffset : Number = 0;
			
			var brXoffset : Number = 0;
			var brYoffset : Number = 0;
			
			heightDiff = (_coverHeight - currentHeight) / 2;
			heightDiffRear = _coverWidthShelve * Math.sin( currentAngle * (Math.PI / 180) );
			
			currentHeightRear = currentHeight - 2 * (heightDiffRear);
			
			
			xDiff = _coverWidth - currentWidth;
			
			if(itemPosition <= 0){
			
				tlYoffset += heightDiff;
				blYoffset -= heightDiff;
				
				trYoffset += heightDiff + heightDiffRear;
				brYoffset -= heightDiff + heightDiffRear;
				
				trXoffset -= xDiff;
				brXoffset -= xDiff;
			}
			else{

				trYoffset += heightDiff;
				brYoffset -= heightDiff;
				
				tlYoffset += heightDiff + heightDiffRear;
				blYoffset -= heightDiff + heightDiffRear;
				
				tlXoffset += xDiff;
				blXoffset += xDiff;
				
			}
			
			
			var currentAlpha : Number = 1;

			//---------------------
			// FADING
			//---------------------
			
			if(Math.abs( _xpos ) > configProxy.fadeDist){
				var dist : Number = Math.abs( _xpos ) - configProxy.fadeDist;
				var newAlpha : Number = configProxy.fadeSpan / dist;
				newAlpha = Math.min( 1, newAlpha );
				currentAlpha = newAlpha; 			
			}
			else{
				currentAlpha = 1;
			}

			var alphaImageBitmapData:BitmapData = new BitmapData(_coverWidth, _coverHeight, false);									
			var alphaReflectionBitmapData : BitmapData = new BitmapData(_coverWidth, _coverHeight, false);									
			
			if(currentAlpha == 1) {
				alphaImageBitmapData = _image.bitmapData;				alphaReflectionBitmapData = _reflectionImage.bitmapData;
										
			}
			else{
				// CREATE "FADED" BITAMPDATE - FADED TO defined background color
				var rect : Rectangle = new Rectangle(0, 0, _coverWidth, _coverHeight );
				var alphaShape : Shape = new Shape( );
				alphaShape.graphics.beginFill( 0x000000, currentAlpha );
				alphaShape.graphics.drawRect( 0, 0, _coverWidth, _coverHeight );
				alphaShape.graphics.endFill();
				var alphaBitmapData : BitmapData = new BitmapData(_coverWidth, _coverHeight, true, 0x000000FF );
				alphaBitmapData.draw( alphaShape, new Matrix());
	
				alphaImageBitmapData.fillRect( rect, _backgroundColor );
				alphaImageBitmapData.copyPixels( _image.bitmapData, rect, new Point( ), alphaBitmapData, new Point(), true);			
	
				alphaReflectionBitmapData.fillRect( rect, _backgroundColor );
				alphaReflectionBitmapData.copyPixels( _reflectionImage.bitmapData, rect, new Point( ), alphaBitmapData, new Point(), true);			
			}

			//---------------------
			// IMAGE DISTORTION
			//---------------------

			
			var tl : Point = new Point( 0 + tlXoffset, 0 + tlYoffset );
			var tr : Point = new Point( _coverWidth + trXoffset, 0 + trYoffset );
			var bl : Point = new Point( 0 + blXoffset, _coverHeight + blYoffset );
			var br : Point = new Point( _coverWidth + brXoffset, _coverHeight + brYoffset );
			
			// draw grphic
			_content.graphics.clear();
	//		_distortion.setTransform(_content.graphics, _image.bitmapData, tl, tr, br, bl);
			_distortion.setTransform(_content.graphics, alphaImageBitmapData, tl, tr, br, bl);
			_content.x = _xpos;
			
			//---------------------
			// REFLECTION
			//---------------------
			
			_reflection.x = _xpos;
			_reflection.graphics.clear();

			var tl_r : Point = new Point( 0 + tlXoffset, bl.y  + currentHeightRear );
			var tr_r : Point = new Point( _coverWidth + trXoffset, br.y + currentHeightRear );
			var bl_r : Point = new Point( 0 + blXoffset, _coverHeight + blYoffset );
			var br_r : Point = new Point( _coverWidth + brXoffset, _coverHeight + brYoffset );

	//		_reflectionDistortion.setTransform( _reflection.graphics, _reflectionImage.bitmapData, tl_r, tr_r, br_r, bl_r);			_reflectionDistortion.setTransform( _reflection.graphics, alphaReflectionBitmapData, tl_r, tr_r, br_r, bl_r);

	
	 		//---------------------
			// Z INDEX
			//---------------------
			if(checkZindex){ 
				if(_xpos > 0){
					// send the items on the right to the bottom
					component.setChildIndex( _content, 0);
					component.setChildIndex( _reflection, 0);
				}
				else{
					// send the items on the left to the top
					component.setChildIndex( _content, component.numChildren - 1);
					component.setChildIndex( _reflection, component.numChildren - 1);
				}
				
				if ( Math.abs(_xpos) <= _centerDistance && ( _id == appProxy.current )){
					component.setChildIndex( _content, component.numChildren - 1);
					component.setChildIndex( _reflection, component.numChildren - 1);
				}
				
			}
		}
		
		private function drawReflection() : void {
			
			// get image 			
			var imageBitmapData : BitmapData = _image.bitmapData.clone();
	
	
			// rect used in various cases
			var rect : Rectangle = new Rectangle(0, 0, _coverWidth, _coverHeight );

			// gradient bigmapData
			var alphaGradientBitmapData : BitmapData = new BitmapData(_coverWidth, _coverHeight, true, 0x00000000);
			var gradientMatrix: Matrix = new Matrix( );
			var gradientShape: Shape = new Shape( );
			gradientMatrix.createGradientBox(_coverWidth, _coverHeight , Math.PI/2,	0, 0);
			
			var gradientAlpha : Number = configProxy.gradientAlpha;
			var gradientHeight : uint = 255 - Math.floor((configProxy.gradientHeight/100) * 255);
			
			gradientShape.graphics.beginGradientFill( GradientType.LINEAR, [0xFFFFFF, 0xFFFFFF],[0, gradientAlpha], [gradientHeight, 255], gradientMatrix);
			gradientShape.graphics.drawRect(0, 0, 	_coverWidth, _coverHeight);
			gradientShape.graphics.endFill();
			alphaGradientBitmapData.draw(gradientShape, new Matrix());

			
			// create reflection
			var reflectionData:BitmapData = new BitmapData(_coverWidth, _coverHeight, false);									
			reflectionData.fillRect( rect, _backgroundColor );
			// copy in the bits from our content, and merge it with the gradient bitmap as an alpha channel.
			reflectionData.copyPixels( imageBitmapData, rect, new Point( ), alphaGradientBitmapData , new Point(), true);
			
			_reflectionImage = new Bitmap( reflectionData );
		}

		private function loadImage() : void {
			
			if(_isLoading) return;
			
			// init loader			
			_loader = new Loader();
			
			// init loaderContex
			_loaderContex = new LoaderContext( );
			_loaderContex.checkPolicyFile = true;
			
			// add listeners
			_loader.contentLoaderInfo.addEventListener( Event.COMPLETE, completeListener, false, 0, true);
			_loader.contentLoaderInfo.addEventListener( IOErrorEvent.IO_ERROR, ioErrorListener, false, 0, true);
			_loader.contentLoaderInfo.addEventListener( SecurityErrorEvent.SECURITY_ERROR, secerrorListener );
			
			// start loading
			performLoad();			
		}
		
		private function performLoad() : void {
			_loader.load( new URLRequest( _imageFile ), _loaderContex );
			_isLoading = true;
		}

		/**
		 * Checks if the loaded bitmaps bitmapData has the required width
		 * and height, rescales if necessary. The image bitamp to rescale
		 * is store in in _image.bitamData
		 */
		private function checkImageSize() : void {
			
			var w:Number = _image.bitmapData.width;
			var h:Number = _image.bitmapData.height;
			
			if(w != _coverWidth || h != _coverHeight) {
			
				var newBitmapData : BitmapData = new BitmapData(_coverWidth, _coverHeight, true, 0x00000000);
				
				var sizeMultiplierX : Number = _coverWidth / w; 				var sizeMultiplierY : Number = _coverHeight / h; 
				
				var matrix : Matrix = new Matrix();
				matrix.scale( sizeMultiplierX, sizeMultiplierY );
				
				newBitmapData.draw(_image.bitmapData, matrix, null, null, null, true);
				
				// replace bitamData
				_image.bitmapData = newBitmapData;
			}
			
		}

		//--------------------------------------------------------
		// CALLBACKS
		//--------------------------------------------------------
		
		/**
		 * Image Loaded
		 */
		private function completeListener(e : Event) :void {
			try{
				_image = _loader.contentLoaderInfo.content as Bitmap;
				checkImageSize();
			}
			catch (e2 : SecurityError){
				sendNotification( ApplicationFacade.ERROR_COMMAND, "Cross domain file missing for: " + configProxy.xmldata.imageURLprefix);
			}
			
			// redraw reflection
			drawReflection();
			
			_isLoaded  = true;
			_isLoading = false;
			redrawItem( appProxy.current );
			
			sendNotification( ApplicationFacade.IMAGE_LOADED);	
		}

		private function secerrorListener( e : SecurityErrorEvent) : void {
			sendNotification( ApplicationFacade.ERROR_COMMAND, e.text);
		}

		private function ioErrorListener( e : IOErrorEvent ) : void {
			
			if (configProxy.showImageError){
				sendNotification( ApplicationFacade.ERROR_COMMAND, e.text);
			}
			
			sendNotification( ApplicationFacade.IMAGE_LOADED );
			_isLoaded = true;
			_isLoading = false;
			// try to load the notfound image specified in config.xml
			if(configProxy.xmldata.notFoundImage){
				
				//load config specified notFoundImage				
				// init loader			
				_loader = new Loader();
				
				// init loaderContex
				_loaderContex = new LoaderContext( );
				_loaderContex.checkPolicyFile = true;
				
				// add listeners
				_loader.contentLoaderInfo.addEventListener( Event.COMPLETE, completeListenerNotFoundConfigImage, false, 0, true);
				_loader.contentLoaderInfo.addEventListener( IOErrorEvent.IO_ERROR, ioErrorListenerNotFoundConfigImage, false, 0, true);
				_loader.contentLoaderInfo.addEventListener( SecurityErrorEvent.SECURITY_ERROR, secerrorListener );
				
				_loader.load( new URLRequest( configProxy.xmldata.notFoundImage ), _loaderContex );
			}
			else{
				displayEmbeddedNotFoundImage();	
			}
		}
		
		/**
		 * Sets the embedded not found image for source image bitmap data
		 */
		private function displayEmbeddedNotFoundImage() : void {
			// add loading image
			var notFoundImage : Bitmap = new Bitmap( new DefaultImageBD(100,100 ) );
			_image = notFoundImage;			
			checkImageSize();
			drawReflection();
			redrawItem( appProxy.current );
		}


		/**
		 * The in the config.xml specified not found image is loaded
		 */
		private function completeListenerNotFoundConfigImage(e : Event) :void {
			try{
				_image = _loader.contentLoaderInfo.content as Bitmap;
				checkImageSize();
				drawReflection();
				redrawItem( appProxy.current );
			}
			catch (e2 : SecurityError){
				sendNotification( ApplicationFacade.ERROR_COMMAND, "Cross domain file missing for: " + configProxy.xmldata.imageURLprefix);
			}
		}

		/**
		 * Error loading the in the config.xml specified image
		 */
		private function ioErrorListenerNotFoundConfigImage( e : IOErrorEvent ) : void {
			displayEmbeddedNotFoundImage();
		}


		/**
		 * MOUSE content click
		 */
		private function contentClickListener( e : MouseEvent) : void {
						
			if(appProxy.current == _id){
				
				if(_param.substr(0,4) == "http" || _type == "url"){
					if(!_target) _target = "_blank";
					navigateToURL( new URLRequest( _param ), _target);
				}
				else{
					if (ExternalInterface.available) {
	    	            ExternalInterface.call("selected", _param);
		            }
				}
			}
			else{
				// prepare for tweening
				appProxy.setNewPos( _id );
			}
		}

		//--------------------------------------------------------
		// GETTERS AND SETTERS
		//--------------------------------------------------------

		private function get component() : Sprite {
			return viewComponent as Sprite;	
		}		
		
		public function get id() : uint {
			return _id;
		}
	}
}
