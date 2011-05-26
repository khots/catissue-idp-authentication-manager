package edu.wustl.authmanager;

import org.junit.Assert;

import edu.wustl.BaseTestCase;
import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.authmanager.factory.AuthManagerFactory;
import edu.wustl.domain.LoginCredentials;

/**
 *
 * @author niharika_sharma
 *
 */
public class TestCSMAuthManager extends BaseTestCase
{

    public void test_Authenticate_Default_Idp_Admin_User()
    {
        try
        {
            final AuthManagerFactory authManagerFactory = AuthManagerFactory.getInstance();
            final IDPAuthManager defaultAuthManager = authManagerFactory.getAuthManagerInstance();
            Assert.assertNotNull(defaultAuthManager);
            final LoginCredentials credentials = new LoginCredentials();
            credentials.setLoginName("admin@admin.com");
            credentials.setPassword("Login123");
            defaultAuthManager.authenticate(credentials);
        }
        catch (final AuthenticationException authenticationException)
        {
            fail("test_Authenticate_Default_Idp_Admin_User failed due to "+authenticationException);
        }
    }
}
