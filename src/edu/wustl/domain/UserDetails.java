package edu.wustl.domain;

import edu.wustl.authmanager.MigrationState;

public class UserDetails
{
    private String loginName;

    private String migratedLoginName;

    private MigrationState migrationState;

    public String getMigratedLoginName()
    {
        return migratedLoginName;
    }

    public void setMigratedLoginName(final String migratedLoginName)
    {
        this.migratedLoginName = migratedLoginName;
    }

    public MigrationState getMigrationState()
    {
        return migrationState;
    }

    public void setMigrationState(final MigrationState migrationState)
    {
        this.migrationState = migrationState;
    }

    public String getLoginName()
    {
        return loginName;
    }

    public void setLoginName(final String loginName)
    {
        this.loginName = loginName;
    }
}
