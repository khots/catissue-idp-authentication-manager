package edu.wustl.migration.rules;

import edu.wustl.domain.LoginCredentials;

public class CAGridMigrationRule implements RuleInterface
{

    public CAGridMigrationRule()
    {
    }

    public boolean checkMigrationRules(LoginCredentials loginCredentials)
    {

        return true;
    }

}
