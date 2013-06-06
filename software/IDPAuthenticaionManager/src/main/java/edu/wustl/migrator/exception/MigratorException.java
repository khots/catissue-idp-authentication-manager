/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-idp-authentication-manager/LICENSE.txt for details.
 */

package edu.wustl.migrator.exception;

/**
 * This class defines the exception to be thrown by the migrator classes.
 *
 * @author niharika_sharma
 *
 */
public class MigratorException extends Exception
{

    /**
     * Instantiates a new migrator exception.
     *
     * @param message
     *            the message
     */
    public MigratorException(final String message)
    {
        super(message);
    }

    /**
     * Instantiates a new migrator exception.
     *
     * @param exception
     *            the exception
     */
    public MigratorException(final Exception exception)
    {
        super(exception);
    }
}
