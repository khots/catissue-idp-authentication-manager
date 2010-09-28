
package edu.wustl.authmanager;

import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.manager.SecurityManagerFactory;

/**
 * @author supriya_dankh
 *
 */
public class CSMAuthManager extends IDPAuthManagerImpl {
	protected void initialize() {

	}

	public boolean authenticate(String loginName, String password)
			throws AuthenticationException {
		boolean loginSuccess = false;

		try {
			loginSuccess = SecurityManagerFactory.getSecurityManager().login(
					loginName, password);
		} catch (SMException e) {
			throw new AuthenticationException(e);
		}

		return loginSuccess;
	}

}
