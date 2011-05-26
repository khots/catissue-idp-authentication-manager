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
public class TestLDAPAuthManager extends BaseTestCase
{

    public void test_Authenticate_User()
    {
        try
        {
            final AuthManagerFactory authManagerFactory = AuthManagerFactory.getInstance();
            final IDPAuthManager defaultAuthManager = authManagerFactory.getAuthManagerInstance("WUSTLKEY_IDP");
            Assert.assertNotNull(defaultAuthManager);
            final LoginCredentials credentials = new LoginCredentials();
            credentials.setLoginName("john.jones");
            credentials.setPassword("BjcNT!@#");
            final boolean isAuthentic = defaultAuthManager.authenticate(credentials);
            Assert.assertTrue(isAuthentic);
        }
        catch (final AuthenticationException authenticationException)
        {
            fail("test_Authenticate_User failed due to " + authenticationException);
        }
        catch (final Exception exception)
        {
            fail("test_Authenticate_User failed due to " + exception);
        }
    }

    public void test_Get_Idp()
    {
        try
        {
            final AuthManagerFactory authManagerFactory = AuthManagerFactory.getInstance();
            final IDPAuthManager authManager = authManagerFactory.getAuthManagerInstance("WUSTLKEY_IDP");
            Assert.assertNotNull(authManager);
            Assert.assertNotNull(authManager.getIDP());
            Assert.assertEquals("WUSTLKEY_IDP", authManager.getIDP().getName());
        }
        catch (final AuthenticationException authenticationException)
        {
            fail("test_Get_Idp failed due to " + authenticationException);
        }
        catch (final Exception exception)
        {
            fail("test_Get_Idp failed due to " + exception);
        }
    }
}
