package edu.wustl.migration.rules;

import edu.wustl.BaseTestCase;
import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.domain.LoginCredentials;

/**
 *
 * @author niharika_sharma
 *
 */
public class TestWustlKeyMigrationRule extends BaseTestCase
{


    public void test_Check_Migration_Rule()
    {
        try
        {
            final RuleInterface ruleInterface=getWUSTLKeyRule();
            final LoginCredentials credentials=new LoginCredentials();
            credentials.setLoginName("user10@wustl.edu");
            ruleInterface.checkMigrationRules(credentials);
        }
        catch (final AuthenticationException authenticationException)
        {
            fail("test_Migrate_User failed due to " + authenticationException.getMessage());
        }
    }
}