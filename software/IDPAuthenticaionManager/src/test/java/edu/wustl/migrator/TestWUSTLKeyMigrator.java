/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-idp-authentication-manager/LICENSE.txt for details.
 */

package edu.wustl.migrator;

import edu.wustl.BaseTestCase;
import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.authmanager.IDPAuthManager;
import edu.wustl.authmanager.factory.AuthManagerFactory;
import edu.wustl.dao.exception.DAOException;
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
        } catch (DAOException e) {
            fail("test_Migrate_User failed due to " + e.getMessage());
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
