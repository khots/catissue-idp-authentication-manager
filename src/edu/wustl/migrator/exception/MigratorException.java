package edu.wustl.migrator.exception;

public class MigratorException extends Exception
{
    public MigratorException(final String message)
    {
        super(message);
    }

    public MigratorException(final Exception exception)
    {
        super(exception);
    }
}
