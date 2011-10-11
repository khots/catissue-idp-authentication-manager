package edu.wustl.domain;

import edu.wustl.migrator.MigrationState;

public class UserDetails
{
    private String loginName;
    
    private String password;
    
    private String identity;

    public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private String migratedLoginName;

    private MigrationState migrationState;

    private String targetIDP;

    public String getTargetIDP()
    {
        return targetIDP;
    }

    public void setTargetIDP(final String targetIDP)
    {
        this.targetIDP = targetIDP;
    }

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
