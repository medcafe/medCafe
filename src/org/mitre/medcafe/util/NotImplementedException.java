package org.mitre.medcafe.util;





/**
 *  This class is used to return the information that a particular method inherited from an abstract class is not implemented.
 */
public class NotImplementedException extends Exception
{
	public NotImplementedException()
	{
		super();
	}
	public NotImplementedException(String message)
	{
		super(message);
	}
}
