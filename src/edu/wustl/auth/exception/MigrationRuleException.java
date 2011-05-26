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
