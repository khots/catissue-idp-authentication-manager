/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-idp-authentication-manager/LICENSE.txt for details.
 */

package edu.wustl.authmanager;

import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.idp.IDPInterface;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.manager.SecurityManagerFactory;

/**
 * @author supriya_dankh
 *
 */
public class CSMAuthManager extends IDPAuthManagerImpl
{

    private static final Logger LOGGER = Logger.getCommonLogger(CSMAuthManager.class);

    public CSMAuthManager(final IDPInterface abstratIdp)
    {
        super(abstratIdp);
        // TODO Auto-generated constructor stub
    }

    public CSMAuthManager()
    {

    }

    protected void initialize()
    {

    }

    public boolean authenticate(final LoginCredentials loginCredentials) throws AuthenticationException
    {
        boolean loginSuccess = false;

        try
        {
            loginSuccess = SecurityManagerFactory.getSecurityManager().login(loginCredentials.getLoginName(),
                    loginCredentials.getPassword());
        }
        catch (final SMException e)
        {
            LOGGER.debug(e);
            throw new AuthenticationException(e);
        }

        return loginSuccess;
    }

}
