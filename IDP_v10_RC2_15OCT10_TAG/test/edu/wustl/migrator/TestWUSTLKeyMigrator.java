package edu.wustl.migrator;

import edu.wustl.BaseTestCase;
import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.authmanager.IDPAuthManager;
import edu.wustl.authmanager.factory.AuthManagerFactory;
import edu.wustl.domain.UserDetails;
import edu.wustl.migrator.exception.MigratorException;

/**
 *
 * @author niharika_sharma
 *
 */
public class TestWUSTLKeyMigrator extends BaseTestCase
{
    public void test_Migrate_User()
    {
        try
        {
            final IDPAuthManager targetAuthManager = AuthManagerFactory.getInstance().getAuthManagerInstance(
            "WUSTLKEY_IDP");
            final MigratorInterface migrator=getWUSTLKeyMigrator();
            final UserDetails userDetails=new UserDetails();
            userDetails.setLoginName("user10@wustl.edu");
            userDetails.setMigratedLoginName("TestUser-10");
            userDetails.setMigrationState(MigrationState.MIGRATED);
            userDetails.setTargetIDP(targetAuthManager.getIDP().getName());
            migrator.migrate(userDetails);
        }
        catch (final AuthenticationException authenticationException)
        {
            fail("test_Migrate_User failed due to " + authenticationException.getMessage());
        }
        catch (final MigratorException migratorException)
        {
            fail("test_Migrate_User failed due to " + migratorException.getMessage());
        }

    }

    public void test_Do_Not_Migrate_User()
    {
        try
        {
            final IDPAuthManager targetAuthManager = AuthManagerFactory.getInstance().getAuthManagerInstance(
            "WUSTLKEY_IDP");
            final MigratorInterface migrator=getWUSTLKeyMigrator();
            migrator.neverMigrate("user20@wustl.edu");
        }
        catch (final AuthenticationException authenticationException)
        {
            fail("test_Migrate_User failed due to " + authenticationException.getMessage());
        }
        catch (final MigratorException migratorException)
        {
            fail("test_Migrate_User failed due to " + migratorException.getMessage());
        }

    }
}
