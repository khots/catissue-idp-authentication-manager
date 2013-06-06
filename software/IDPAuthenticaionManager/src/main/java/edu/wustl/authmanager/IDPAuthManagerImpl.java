/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-idp-authentication-manager/LICENSE.txt for details.
 */

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