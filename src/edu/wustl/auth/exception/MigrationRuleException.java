/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-idp-authentication-manager/LICENSE.txt for details.
 */

package edu.wustl.auth.exception;

/**
 * This class represnts the exception to be thrown in case some exception occurs
 * while validating the user against the idp's migration rule.
 *
 * @author niharika_sharma
 *
 */
public class MigrationRuleException extends Exception
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public MigrationRuleException(final Exception exception)
    {
        super(exception);
    }

    public MigrationRuleException(final String message)
    {
        super(message);
    }

}
