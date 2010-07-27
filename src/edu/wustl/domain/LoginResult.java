/**
 *
 */
package edu.wustl.domain;

import edu.wustl.migrator.MigrationState;

/**
 * The Class LoginResult.
 *
 * @author supriya_dankh
 */
public class LoginResult
{

    /** The authentication success. */
    private boolean authenticationSuccess;

    /** The app login name. */
    private String appLoginName;

    /** The migrated login name. */
    private String migratedLoginName;

    /** The migration state. */
    private MigrationState migrationState;

    /**
     * Checks if is authentication success.
     *
     * @return true, if is authentication success
     */
    public boolean isAuthenticationSuccess()
    {
        return authenticationSuccess;
    }

    /**
     * Sets the authentication success.
     *
     * @param authenticationSuccess the new authentication success
     */
    public void setAuthenticationSuccess(final boolean authenticationSuccess)
    {
        this.authenticationSuccess = authenticationSuccess;
    }

    /**
     * Gets the app login name.
     *
     * @return the app login name
     */
    public String getAppLoginName()
    {
        return appLoginName;
    }

    /**
     * Sets the app login name.
     *
     * @param appLoginName the new app login name
     */
    public void setAppLoginName(final String appLoginName)
    {
        this.appLoginName = appLoginName;
    }

    /**
     * Gets the migrated login name.
     *
     * @return the migrated login name
     */
    public String getMigratedLoginName()
    {
        return migratedLoginName;
    }

    /**
     * Sets the migrated login name.
     *
     * @param migratedLoginName the new migrated login name
     */
    public void setMigratedLoginName(final String migratedLoginName)
    {
        this.migratedLoginName = migratedLoginName;
    }

    /**
     * Gets the migration state.
     *
     * @return the migration state
     */
    public MigrationState getMigrationState()
    {
        return migrationState;
    }

    /**
     * Sets the migration state.
     *
     * @param migrationState the new migration state
     */
    public void setMigrationState(final MigrationState migrationState)
    {
        this.migrationState = migrationState;
    }
}
