package edu.wustl.migration.rules;

import edu.wustl.domain.LoginCredentials;
import edu.wustl.wustlkey.util.global.Constants;

/**
 * This class defines the migration rule for WUSTLKey idp.
 *
 * @author niharika_sharma
 *
 */
public class WustlKeyMigrationRule implements RuleInterface
{

    public boolean checkMigrationRules(final LoginCredentials loginCredentials)
    {
        boolean isRuleApplicable;
        if (loginCredentials.getLoginName().endsWith(Constants.WUSTL_EDU)
                || loginCredentials.getLoginName().endsWith(Constants.WUSTL_EDU_CAPS))
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
