package edu.wustl.migrator.actionform;

import org.apache.struts.action.ActionForm;

/**
 * This class contains migration information provided by the user on the
 * migration page.
 *
 * @author niharika_sharma
 *
 */
public class MigrationForm extends ActionForm
{

    /** The migrated login name. */
    private String migratedLoginName;

    /** The migrated password. */
    private String migratedPassword;

    /** The target idp. */
    private String targetIdp;

    /**
     * Gets the migrated password.
     *
     * @return the migrated password
     */
    public String getMigratedPassword()
    {
        return migratedPassword;
    }

    /**
     * Sets the migrated password.
     *
     * @param migratedPassword
     *            the new migrated password
     */
    public void setMigratedPassword(final String migratedPassword)
    {
        this.migratedPassword = migratedPassword;
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
     * @param migratedLoginName
     *            the new migrated login name
     */
    public void setMigratedLoginName(final String migratedLoginName)
    {
        this.migratedLoginName = migratedLoginName;
    }

    /**
     * Gets the target idp.
     *
     * @return the target idp
     */
    public String getTargetIdp()
    {
        return targetIdp;
    }

    /**
     * Sets the target idp.
     *
     * @param targetIdp
     *            the new target idp
     */
    public void setTargetIdp(final String targetIdp)
    {
        this.targetIdp = targetIdp;
    }
}
