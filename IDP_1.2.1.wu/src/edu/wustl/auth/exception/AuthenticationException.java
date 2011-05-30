package edu.wustl.auth.exception;

public class AuthenticationException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public AuthenticationException(Exception exception)
	{
		super(exception);
	}

	public AuthenticationException(String message)
    {
    	super(message);
    }



}
