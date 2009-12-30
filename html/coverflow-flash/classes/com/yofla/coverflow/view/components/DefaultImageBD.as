package com.yofla.coverflow.view.components {
	import flash.display.BitmapData;
	
	/**
	 * @author Matus
	 */
	public class DefaultImageBD extends BitmapData {
		public function DefaultImageBD(width : int, height : int, transparent : Boolean = true, fillColor : uint = 4.294967295E9) {
			super( width, height, transparent, fillColor );
		}
	}
}
