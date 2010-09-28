package edu.wustl.migrator;

/**
 * This enum defines the possible states of migration a user can be in.
 *
 * @author niharika_sharma
 *
 */
public enum MigrationState
{

    /** State for MIGRATED user. */
    MIGRATED("MIGRATED"),
    /** State for To be migrated user. */
    TO_BE_MIGRATED("TO_BE_MIGRATED"),
    /** State for Do not migrate user. */
    DO_NOT_MIGRATE("DO_NOT_MIGRATE"),
    /** State for New wustl user. */
    NEW_WUSTL_USER("NEW_WUSTL_USER");

    String state;

    MigrationState(final String state)
    {
        this.state = state;
    }

    public String getState()
    {
        return state;
    }

    public static MigrationState get(final String state)
    {
        MigrationState migrationState = null;
        for (final MigrationState optStatus : MigrationState.values())
        {
            if (optStatus.getState().equals(state))
            {
                migrationState = optStatus;
                break;
            }
        }
        return migrationState;
    }
}
