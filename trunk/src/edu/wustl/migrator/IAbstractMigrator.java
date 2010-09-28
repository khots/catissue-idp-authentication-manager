package edu.wustl.migrator;

import edu.wustl.domain.UserDetails;
import edu.wustl.migrator.exception.MigratorException;

/**
 * This interface defines the behaviour of the migrators to be defined in the
 * system.
 *
 * @author niharika_sharma
 */
public interface IAbstractMigrator
{

    /**
     * Gets the csm name.
     *
     * @param key
     *            the key
     *
     * @return the CSM name
     *
     * @throws MigratorException
     *             the migrator exception
     */
    public String getCSMName(String key) throws MigratorException;

    /**
     * Migrate.
     *
     * @param userDetails
     *            the user details
     *
     * @throws MigratorException
     *             the migrator exception
     */
    public void migrate(UserDetails userDetails) throws MigratorException;

    /**
     * Never migrate.
     *
     * @param loginName
     *            the login name
     *
     * @throws MigratorException
     *             the migrator exception
     */
    public void neverMigrate(String loginName) throws MigratorException;

}