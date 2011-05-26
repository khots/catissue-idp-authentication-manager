package edu.wustl.authmanager.factory;

import org.junit.Assert;

import edu.wustl.BaseTestCase;
import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.authmanager.IDPAuthManager;

/**
 *
 * @author niharika_sharma
 *
 */
public class TestAuthManagerFactory extends BaseTestCase
{
    /**
     * Negative - To ensure AuthenticationException occurs if the properties
     * file is not configured.
     */
//    public void test_Get_AuthManager_Instance_Default_No_Auth_File_Negative()
//    {
//        try
//        {
//            System.setProperty("app.domainAuthFilePath", "");
//            final AuthManagerFactory authManagerFactory = AuthManagerFactory.getInstance();
//            final IDPAuthManager defaultAuthManager = authManagerFactory.getAuthManagerInstance();
//            fail("test_Get_AuthManager_Instance_Default_No_Auth_File_Negative failed because '"
//                    + defaultAuthManager + "' auth manager instance was returned.");
//        }
//        catch (final AuthenticationException authenticationException)
//        {
//            Assert.assertTrue(true);
//        }
//        catch (final Exception exception)
//        {
//            fail("test_Get_AuthManager_Instance_Default_No_Auth_File_Negative failed due to "
//                    + exception.getMessage());
//        }
//    }

    /**
     * To ensure the class is singleton and returns the same instance always.
     */
    public void test_Get_Same_Instance()
    {
        final AuthManagerFactory authManagerFactory = AuthManagerFactory.getInstance();
        Assert.assertNotNull(authManagerFactory);
        final AuthManagerFactory newAuthManagerFactory = AuthManagerFactory.getInstance();
        Assert.assertNotNull(newAuthManagerFactory);
        Assert.assertEquals(authManagerFactory, newAuthManagerFactory);
    }

    /**
     * To ensure the same authentication manager instance is returned always.
     */
    public void test_Get_AuthManager_Instance_Default()
    {
        try
        {
            final AuthManagerFactory authManagerFactory = AuthManagerFactory.getInstance();
            final IDPAuthManager defaultAuthManager = authManagerFactory.getAuthManagerInstance();
            Assert.assertNotNull(defaultAuthManager);
            final IDPAuthManager defaultAuthManager2 = authManagerFactory.getAuthManagerInstance();
            Assert.assertNotNull(defaultAuthManager2);
            Assert.assertEquals(defaultAuthManager.getIDP(), defaultAuthManager2.getIDP());
        }
        catch (final AuthenticationException authenticationException)
        {
            fail("test_Get_AuthManager_Instance_Default failed due to " + authenticationException.getMessage());
        }
    }

    /**
     * To ensure the same authentication manager instance is returned always.
     */
    public void test_Get_AuthManager_Instance_WUSTLKEY_IDP()
    {
        try
        {
            final AuthManagerFactory authManagerFactory = AuthManagerFactory.getInstance();
            final IDPAuthManager authManager = authManagerFactory.getAuthManagerInstance("WUSTLKEY_IDP");
            Assert.assertNotNull(authManager);
            final IDPAuthManager wustlKeyAuthManager = authManagerFactory.getAuthManagerInstance("WUSTLKEY_IDP");
            Assert.assertNotNull(wustlKeyAuthManager);
            Assert.assertEquals(authManager.getIDP(), wustlKeyAuthManager.getIDP());
            Assert.assertEquals("WUSTLKEY_IDP", wustlKeyAuthManager.getIDP().getName());
            Assert.assertEquals(authManager.getIDP().getName(), wustlKeyAuthManager.getIDP().getName());
        }
        catch (final AuthenticationException authenticationException)
        {
            fail("test_Get_AuthManager_Instance_WUSTLKEY_IDP failed due to " + authenticationException.getMessage());
        }
    }
}
