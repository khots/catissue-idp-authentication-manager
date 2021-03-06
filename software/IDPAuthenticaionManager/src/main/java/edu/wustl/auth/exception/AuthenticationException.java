/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-idp-authentication-manager/LICENSE.txt for details.
 */

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
