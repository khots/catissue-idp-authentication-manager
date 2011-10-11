/**
 *
 */
package edu.wustl.authmanager;

import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.idp.IDPInterface;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

/**
 * @author supriya_dankh
 *
 */
public interface IDPAuthManager {

	public boolean authenticate(LoginCredentials credentials)throws AuthenticationException;
	
	public String getIdentity(LoginCredentials credentials)throws AuthenticationException;
	
	public SAMLAssertion getSAMLAssertion(LoginCredentials credentials)throws AuthenticationException;

	IDPInterface getIDP();


}
