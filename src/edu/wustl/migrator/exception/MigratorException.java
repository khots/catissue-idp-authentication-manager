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
