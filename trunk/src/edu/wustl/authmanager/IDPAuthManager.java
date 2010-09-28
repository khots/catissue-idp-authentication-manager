/**
 *
 */
package edu.wustl.authmanager;

import edu.wustl.auth.exception.AuthenticationException;

/**
 * @author supriya_dankh
 *
 */
public interface IDPAuthManager {

	public boolean authenticate(String loginName,String password)throws AuthenticationException;


}
