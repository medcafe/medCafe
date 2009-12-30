/**
 * Flash Zoomer
 * Copyright (C) 2007-2008 www.yofla.com
 */

package com.yofla.coverflow.controller {
	import org.puremvc.as3.multicore.interfaces.INotification;	
	import org.puremvc.as3.multicore.patterns.command.SimpleCommand;	

	import com.yofla.utils.ErrorReporter;	

	/**
	 * Passes an error message to the ErrorReporter calss, which displays
	 * it on the screen.
	 * 
	 * @author Matus Laco, www.yofla.com
	 */
	public class ErrorCommand extends SimpleCommand {
		override public function execute( note : INotification ) : void{
			var errorString : String = note.getBody() as String;
			//add error
			ErrorReporter.addError(errorString);
		}
	}
}