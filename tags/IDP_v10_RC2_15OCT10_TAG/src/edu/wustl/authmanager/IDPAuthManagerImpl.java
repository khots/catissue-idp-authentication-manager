/**
 *
 */
package edu.wustl.authmanager;

import edu.wustl.idp.IDPInterface;

/**
 * @author supriya_dankh
 *
 */
public abstract class IDPAuthManagerImpl implements IDPAuthManager {


	protected IDPInterface idp;


	public IDPAuthManagerImpl(final IDPInterface idp)
	{
         this.idp= idp;
	}

	public IDPAuthManagerImpl()
	{

	}
	public IDPInterface getIDP()
	{
	    return idp;
	}

}