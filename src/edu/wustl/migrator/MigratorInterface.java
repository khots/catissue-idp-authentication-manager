/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-idp-authentication-manager/LICENSE.txt for details.
 */

package edu.wustl.migrator;

import edu.wustl.domain.UserDetails;
import edu.wustl.migrator.exception.MigratorException;

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