package edu.wustl.processor;

import edu.wustl.BaseTestCase;
import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.domain.LoginResult;
import edu.wustl.migrator.MigrationState;

/**
 *
 * @author niharika_sharma
 *
 */
public class TestLoginProcessor extends BaseTestCase
{
//    public void test_authenticate()
//    {
//        try
//        {
//            final LoginCredentials credentials=new LoginCredentials();
//            credentials.setLoginName("admin@admin.com");
//            credentials.setPassword("Login123");
//            final boolean success=LoginProcessor.authenticate(credentials);
//            assertTrue(success);
//
//        }
//        catch (final AuthenticationException authenticationException)
//        {
//           fail("test_authenticate failed due to "+authenticationException);
//        }
//
//    }
//
//    public void test_authenticate_negative()
//    {
//        try
//        {
//            final LoginCredentials credentials=new LoginCredentials();
//            credentials.setLoginName("admin@admin.com");
//            credentials.setPassword("xcxcx");
//            final boolean success=LoginProcessor.authenticate(credentials);
//            assertFalse(success);
//        }
//        catch (final AuthenticationException authenticationException)
//        {
//           fail("test_authenticate failed due to "+authenticationException);
//        }
//
//    }

    public void test_Process_User_Login()
    {
        try
        {
            final LoginCredentials credentials=new LoginCredentials();
            credentials.setLoginName("admin@admin.com");
            credentials.setPassword("Login123");
            final LoginResult loginResult=LoginProcessor.processUserLogin(credentials);
            assertNotNull(loginResult);
            assertNotNull(loginResult.getAppLoginName());
            assertEquals("admin@admin.com",loginResult.getAppLoginName());
            assertNotNull(loginResult.getMigrationState());
            assertEquals(MigrationState.DO_NOT_MIGRATE,loginResult.getMigrationState());
        }
        catch (final AuthenticationException authenticationException)
        {
            fail("test_Process_User_Login failed due to "+authenticationException);
        }

    }

    public void test_Process_User_Login_WashU_User()
    {
        try
        {
            final LoginCredentials credentials=new LoginCredentials();
            credentials.setLoginName("user3@wustl.edu");
            credentials.setPassword("Login123");
            final LoginResult loginResult=LoginProcessor.processUserLogin(credentials);
            assertNotNull(loginResult);
            assertNotNull(loginResult.getAppLoginName());
            assertEquals("user3@wustl.edu",loginResult.getAppLoginName());
            assertNotNull(loginResult.getMigrationState());
            assertEquals(MigrationState.NEW_IDP_USER,loginResult.getMigrationState());
        }
        catch (final AuthenticationException authenticationException)
        {
            fail("test_Process_User_Login failed due to "+authenticationException);
        }

    }

}
