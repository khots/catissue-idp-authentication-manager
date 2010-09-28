package edu.wustl.migration.rules;

import edu.wustl.domain.LoginCredentials;
import edu.wustl.wustlkey.util.global.Constants;

/**
 * This class defines the migration rule for BJC idp.
 *
 * @author niharika_sharma
 *
 */
public class BJCMigrationRule implements RuleInterface
{

    public boolean checkMigrationRules(final LoginCredentials loginCredentials)
    {
        boolean isRuleApplicable;
        if (loginCredentials.getLoginName().endsWith(Constants.BJC_FOO)
                || loginCredentials.getLoginName().endsWith(Constants.BJC_FOO_CAPS))
        {
            isRuleApplicable = true;
        }
        else
        {
            isRuleApplicable = false;
        }
        return isRuleApplicable;
    }

}
