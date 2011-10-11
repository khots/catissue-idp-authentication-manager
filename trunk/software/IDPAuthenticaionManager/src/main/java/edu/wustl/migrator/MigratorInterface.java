package edu.wustl.migrator;

import edu.wustl.domain.UserDetails;
import edu.wustl.migrator.exception.MigratorException;
import edu.wustl.dao.exception.DAOException;

/**
 * This interface defines the behaviour of the migrators to be defined in the
 * system.
 *
 * @author niharika_sharma
 */
public interface MigratorInterface
{
    /**
     * Migrate.
     *
     * @param userDetails
     *            the user details
     *
     * @throws MigratorException
     *             the migrator exception
     */
    public void migrate(UserDetails userDetails) throws MigratorException, DAOException;

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