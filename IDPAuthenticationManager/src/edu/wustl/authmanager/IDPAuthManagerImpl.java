/**
 *
 */
package edu.wustl.authmanager;

import edu.wustl.abstractidp.IAbstractIDP;
import edu.wustl.auth.exception.AuthenticationException;

/**
 * @author supriya_dankh
 *
 */
public abstract class IDPAuthManagerImpl implements IDPAuthManager {


	protected IAbstractIDP abstratIdp;
	/* (non-Javadoc)
	 * @see edu.wustl.authmanager.IDPAuthManager#authenticate(java.lang.String, java.lang.String)
	 */
	public abstract boolean authenticate(String loginName, String password)throws AuthenticationException;
}